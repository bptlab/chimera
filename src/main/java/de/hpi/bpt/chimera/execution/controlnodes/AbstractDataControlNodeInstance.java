package de.hpi.bpt.chimera.execution.controlnodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.IntermediateCatchEventInstance;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

@Entity
public abstract class AbstractDataControlNodeInstance extends ControlNodeInstance {
	private static final Logger log = Logger.getLogger(AbstractDataControlNodeInstance.class);

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "JoinTableSelectedDataControlNode_DataObject", joinColumns = @JoinColumn(name = "ControlNodeInstance_Id"), inverseJoinColumns = @JoinColumn(name = "DataObject_Id"))
	private List<DataObject> selectedDataObjects;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "JoinTableOutputDataControlNode_DataObject", joinColumns = @JoinColumn(name = "ControlNodeInstance_Id"), inverseJoinColumns = @JoinColumn(name = "DataObject_Id"))
	private List<DataObject> outputDataObjects;

	/**
	 * Decide whether the instance will begin automatically.
	 */
	private boolean hasAutomaticBegin;

	/**
	 * for JPA only
	 */
	public AbstractDataControlNodeInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}

	public AbstractDataControlNodeInstance(AbstractControlNode controlNode, FragmentInstance fragmentInstance) {
		super(controlNode, fragmentInstance);
		this.selectedDataObjects = new ArrayList<>();
		this.outputDataObjects = new ArrayList<>();
		allowAutomaticExecution();
	}

	/**
	 * Enables the incoming control flow of the DataControlNodeInstance. Any
	 * previously selected DataObjects are cleared. If state is INIT, it is set
	 * to CONTROLFLOW_ENABLED. If data preconditions are fulfilled (or state is
	 * already DATAFLOW_ENABLED), state is set to READY. If an
	 * DataControlNodeInstance that is allow to begin automatically is READY, it
	 * is started. However, if it is the first ActivityInstance in the
	 * FragmentInstance, which means the FragmentInstance has not started yet,
	 * it can not begin automatically.
	 */
	@Override
	public void enableControlFlow() {
		getSelectedDataObjects().clear();
		if (getState().equals(State.INIT)) {
			setState(State.CONTROLFLOW_ENABLED);
		}
		if (getState().equals(State.DATAFLOW_ENABLED) || getControlNode().getPreCondition().isFulfilled(getDataManager().getDataStateConditions())) {
			setState(State.READY);
		}
		if (canBeginAutomatically()) {
			// automatically select data objects, input set must be unique
			assert getControlNode().hasUniquePreCondition() : "For automatic execution tasks need an unique pre-condition";
			List<ConditionSet> conditionSets = getControlNode().getPreCondition().getConditionSets();
			ConditionSet inputSet = new ConditionSet();
			if (!conditionSets.isEmpty()) {
				inputSet = conditionSets.get(0);
			}
			Set<DataObject> fulfillingDataObjects = getDataManager().getFulfillingDataObjects(inputSet);
			if (fulfillingDataObjects.isEmpty() && !conditionSets.isEmpty()) {
				// this should not happen, someone changed a DO state we needed
				// or locked a DO
				checkDataFlow();
			} else {
				getCaseExecutioner().beginDataControlNodeInstance(this, new ArrayList<>(fulfillingDataObjects));
			}
		}
	}

	/**
	 * Used for updating the DataFlow of the ActivityInstance.
	 */
	public void checkDataFlow() {
		if (getFragmentInstance().isExecutable() && getControlNode().getPreCondition().isFulfilled(getDataManager().getDataStateConditions())) {
			enableDataFlow();
		} else {
			disableDataFlow();
		}
	}

	public void enableDataFlow() {
		if (getState().equals(State.INIT)) {
			setState(State.DATAFLOW_ENABLED);
		} else if (getState().equals(State.CONTROLFLOW_ENABLED)) {
			setState(State.READY);
		}
	}

	public void disableDataFlow() {
		if (getState().equals(State.DATAFLOW_ENABLED)) {
			setState(State.INIT);
		} else if (getState().equals(State.READY)) {
			setState(State.CONTROLFLOW_ENABLED);
		}
	}

	/**
	 * Tries to set the flag for automatic execution of this activity instance
	 * to {@literal true}. This fails if the activity has multiple input or
	 * output sets which would require user choice. Gateways themselves take
	 * care to forbid automatic execution of their successor activities,
	 * 
	 * @see {@link ExclusiveGatewayInstance}.
	 */
	public void allowAutomaticExecution() {
		if (getControlNode().hasUniquePostCondition() && getControlNode().hasUniquePreCondition() && getControlNode().isAutomatic()) {
			hasAutomaticBegin = true;
		} else {
			log.warn("Tasks with more than one input or output set cannot be executed automatically.");
			hasAutomaticBegin = false;
		}
	}

	/**
	 * Sets the flag for automatic execution of this activity to
	 * {@literal false}. This is used by exclusive gateways to prevent that the
	 * branch starting with the automatic activity is always taken. In this
	 * case, the automatic activity has to be started manually by the user.
	 */
	public void forbidAutomaticStart() {
		hasAutomaticBegin = false;
	}

	/**
	 * @return true if the DataControlNodeInstance is in the correct state, is
	 *         allowed to begin automatically and the corresponding
	 *         FragmentInstance is already started. However,
	 *         {@link IntermediateCatchEventInstance} can also begin
	 *         automatically if the FragmentInstance has not started yet because
	 *         they need to be registered.
	 */
	public boolean canBeginAutomatically() {
		return canBegin() && hasAutomaticBegin() && getFragmentInstance().isActive();
	}

	/**
	 * A data control node instance can only begin if it is in State READY and
	 * the fragment instance allows execution.
	 */
	public boolean canBegin() {
		return getState().equals(State.READY) && getFragmentInstance().isExecutable();
	}

	/**
	 * 
	 * @return whether the data control node instance should be able to
	 *         terminate.
	 */
	public abstract boolean canTerminate();

	/**
	 * If the input string contains a variable expression, i.e.
	 * {@code #DataClass} or {@code #DataClass.attributeName}, this method tries
	 * to find a {@link DataObject} of this data class among the selected data
	 * objects. In the first case ({@code #DataClass}) the variable expression
	 * is replaced with the current state of the found data object (or "<not
	 * found>" if no such data object was found). In the second case
	 * ({@code #DataClass.attributeName}) the attribute instances of the found
	 * data object are searched for one that matches {@code attributeName}. If
	 * such attribute instance is found, its value is used to replace the
	 * variable expression in the input string. Otherwise, it is replaced with
	 * "<not found>".
	 * 
	 * If the input string contains multiple variable expression the first one
	 * is replaced and the method calls itself recursively with the resulting
	 * string. The recursion ends, when the input no longer contains any
	 * variable expressions.
	 * 
	 * If the input string does not contain a variable expression, it is
	 * returned unchanged.
	 * 
	 * @param toReplace
	 *            - the input string which might contain variable expressions
	 *            {@code #DataClass} or {@code #DataClass.attributeName}
	 * @return the input string with the variable expression replaced by the
	 *         referenced data object state or data attribute value
	 */
	public String replaceVariableExpressions(String toReplace) {
		Pattern p = Pattern.compile("#(\\w+)(?:\\.(\\w+))?\\b");
		Matcher m = p.matcher(toReplace);
		if (!m.find()) { // no variable used in input, end recursion
			return toReplace;
		}
		final int attributeNameGroup = 2;
		final int dataClassNameGroup = 1;
		String dataClassName = m.group(dataClassNameGroup);
		Optional<String> attrName = Optional.ofNullable(m.group(attributeNameGroup));
		Optional<DataObject> foundDO = getSelectedDataObjects().stream().filter(d -> dataClassName.equals(d.getDataClass().getName())).findFirst();
		if (!foundDO.isPresent()) { // no DO found for data class referenced in
			// variable expression
			log.error(String.format("None of the selected data objects of the task '%s' matches the data class '%s' referenced in the variable expression %s.", getControlNode().getName(), dataClassName, m.group()));
			// replace first match and recursive call to replace other potential
			// variable expressions
			String replacedFirstOccurrence = m.replaceFirst("<not found>");
			return replaceVariableExpressions(replacedFirstOccurrence);
		}
		if (!attrName.isPresent()) { // no attribute referenced -> replace
			// "#DataClass" with its state
			// replace first match and recursive call to replace other potential
			// variable expressions
			String replacedFirstOccurrence = m.replaceFirst(foundDO.get().getObjectLifecycleState().getName());
			return replaceVariableExpressions(replacedFirstOccurrence);
		}
		Optional<DataAttributeInstance> foundDAI = foundDO.get().getDataAttributeInstances().stream().filter(dai -> attrName.get().equals(dai.getDataAttribute().getName())).findFirst();
		if (!foundDAI.isPresent()) { // no DAI found for attribute referenced in
			// variable expression
			log.error(String.format("The found data object of class '%s' does not have a attribute with name '%s' specified in the variable expression %s.", dataClassName, attrName.get(), m.group()));
			// replace first match and recursive call to replace other potential
			// variable expressions
			String replacedFirstOccurrence = m.replaceFirst("<not found>");
			return replaceVariableExpressions(replacedFirstOccurrence);
		}
		Object value = foundDAI.get().getValue();
		if (value == null) { // attribute value is null
			log.error(String.format("The attribute value of the variable expression '%s' is 'null'.", m.group()));
			// replace first match and recursive call to replace other potential
			// variable expressions
			String replacedFirstOccurrence = m.replaceFirst("<value is 'null'>");
			return replaceVariableExpressions(replacedFirstOccurrence);
		}
		// replace first match and recursive call to replace other potential
		// variable expressions
		String replacedFirstOccurrence = m.replaceFirst(value.toString());
		return replaceVariableExpressions(replacedFirstOccurrence);
	}

	@Override
	public AbstractDataControlNode getControlNode() {
		return (AbstractDataControlNode) super.getControlNode();
	}

	public boolean hasAutomaticBegin() {
		return hasAutomaticBegin;
	}

	public List<DataObject> getSelectedDataObjects() {
		return selectedDataObjects;
	}

	public void setSelectedDataObjects(List<DataObject> selectedDataObjects) {
		this.selectedDataObjects = selectedDataObjects;
	}

	public List<DataObject> getOutputDataObjects() {
		return outputDataObjects;
	}

	public void setOutputDataObjects(List<DataObject> outputDataObjects) {
		this.outputDataObjects = outputDataObjects;
	}

}
