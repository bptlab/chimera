package de.hpi.bpt.chimera.execution.controlnodes.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.AbstractDataControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.BoundaryEventInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;

@Entity
public abstract class AbstractActivityInstance extends AbstractDataControlNodeInstance {
	private static final Logger log = Logger.getLogger(AbstractActivityInstance.class);

	/**
	 * Decide whether the instance will begin automatically.
	 */
	private boolean hasAutomaticBegin;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "attachedToActivity")
	private List<BoundaryEventInstance> attachedBoundaryEventInstances;


	/**
	 * for JPA only
	 */
	public AbstractActivityInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	/**
	 * Create a new AbstractActivityInstance.
	 * 
	 * @param activity
	 * @param fragmentInstance
	 */
	public AbstractActivityInstance(AbstractActivity activity, FragmentInstance fragmentInstance) {
		super(activity, fragmentInstance);
		allowAutomaticExecution();
		this.attachedBoundaryEventInstances = new ArrayList<>();
	}

	/**
	 * Enables the incoming control flow of the ActivityInstance. Any previously
	 * selected DataObjects are cleared. If state is INIT, it is set to
	 * CONTROLFLOW_ENABLED. If data preconditions are fulfilled (or state is
	 * already DATAFLOW_ENABLED), state is set to READY. If an automatic tasks
	 * is READY, it is started. However, if it is the first ActivityInstance in
	 * the FragmentInstance, which means the FragmentInstance has not started
	 * yet, it can not begin automatically.
	 */
	@Override
	public void enableControlFlow() {
		getSelectedDataObjects().clear();
		if (getState().equals(State.INIT)) {
			setState(State.CONTROLFLOW_ENABLED);
		}
		if (getState().equals(State.DATAFLOW_ENABLED) || 
				getControlNode().getPreCondition().isFulfilled(getDataManager().getDataStateConditions())) {
			setState(State.READY);
		}
		if (canBegin() && hasAutomaticBegin() && getFragmentInstance().isStarted()) {
			// automatically select data objects, input set must be unique
			assert getControlNode().hasUniquePreCondition() : "For automatic execution tasks need an unique pre-condition";
			List<ConditionSet> conditionSets = getControlNode().getPreCondition().getConditionSets();
			ConditionSet inputSet = new ConditionSet();
			if (!conditionSets.isEmpty()) {
				inputSet = conditionSets.get(0);
			}
			Set<DataObject> fulfillingDataObjects = getDataManager().getFulfillingDataObjects(inputSet);
			if (fulfillingDataObjects.isEmpty() && !conditionSets.isEmpty()) {
				// this should not happen, someone changed a DO state we needed or locked a DO
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
		if (getControlNode().getPreCondition().isFulfilled(getDataManager().getDataStateConditions())) {
			enableDataFlow();
		} else {
			disableDataFlow();
		}
	}

	private void enableDataFlow() {
		if (getState().equals(State.INIT)) {
			setState(State.DATAFLOW_ENABLED);
		} else if (getState().equals(State.CONTROLFLOW_ENABLED)) {
			setState(State.READY);
		}
	}

	private void disableDataFlow() {
		if (getState().equals(State.DATAFLOW_ENABLED)) {
			setState(State.INIT);
		} else if (getState().equals(State.READY)) {
			setState(State.CONTROLFLOW_ENABLED);
		}
	}

	/**
	 * Begin the Activity Instance. Inform the {@link FragmentInstance} that has
	 * started now. The selected DataObjects were set by the CaseExecutioner. If
	 * the ActivityInstance is an automatic task and has one or none condition
	 * set in the output the task will be terminated automatically and if there
	 * is one condition set it will be used for the termination of the task.
	 */
	@Override
	public void begin() {
		if (!canBegin()) {
			log.info(String.format("The activity instance of %s can not begin", this.getControlNode().getName()));
			return;
		}
		getFragmentInstance().setStarted(true);
		createAttachedBoundaryEvents();

		setState(State.RUNNING);
		execute();

		if (hasAutomaticBegin()) {
			Map<DataClass, ObjectLifecycleState> dataObjectToObjectLifecycleTransition = new HashMap<>();
			if (getControlNode().hasPostCondition()) {
				dataObjectToObjectLifecycleTransition = getControlNode().getPostCondition().getConditionSets().get(0).getDataClassToObjectLifecycleState();
			}
			
			getCaseExecutioner().terminateDataControlNodeInstance(this, dataObjectToObjectLifecycleTransition);
		}
	}

	/**
	 * Create instances for the attached boundary events and enable them.
	 */
	private void createAttachedBoundaryEvents() {
		for (BoundaryEvent boundaryEvent : getControlNode().getAttachedBoundaryEvents()) {
			BoundaryEventInstance boundaryEventInstance = (BoundaryEventInstance) getFragmentInstance().createControlNodeInstance(boundaryEvent);
			boundaryEventInstance.setAttachedToActivity(this);
			attachedBoundaryEventInstances.add(boundaryEventInstance);
			boundaryEventInstance.enableControlFlow();
		}
	}

	/**
	 * Terminate the Activity. The transition of the States of the DataClasses
	 * is handled by the CaseExecutioner.
	 */
	@Override
	public void terminate() {
		if (!canTerminate()) {
			log.info(String.format("The activity instance of %s can not terminate", this.getControlNode().getName()));
			return;
		}
		attachedBoundaryEventInstances.forEach(BoundaryEventInstance::skip);

		setState(State.TERMINATED);
		getCaseExecutioner().updateDataFlow();
		getFragmentInstance().createFollowing(getControlNode());
	}


	@Override
	public void skip() {
		for (BoundaryEventInstance boundaryEvent : attachedBoundaryEventInstances) {
			boundaryEvent.skip();
		}
		setState(State.SKIPPED);
	}

	/**
	 * Cancel the activity instance. Therefore the instance need to be running.
	 * The data objects locked by the activity instance will be unlocked.
	 */
	public void cancel() {
		if (!getState().equals(State.RUNNING)) {
			String errorMsg = "Tried cancelling an activity instance, which is not running";
			log.warn(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		setState(State.CANCEL);

		List<DataObject> workingItems = getSelectedDataObjects();
		this.getCaseExecutioner().getDataManager().unlockDataObjects(workingItems);
	}

	public abstract void execute();

	// GETTER & SETTER
	@Override
	public void setState(State state) {
		getCaseExecutioner().logActivityTransition(this, state);
		super.setState(state);
	}

	@Override
	public AbstractActivity getControlNode() {
		return (AbstractActivity) super.getControlNode();
	}

	public boolean hasAutomaticBegin() {
		return hasAutomaticBegin;
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
		if (getControlNode().hasUniquePostCondition() &&
				getControlNode().hasUniquePreCondition() &&
					getControlNode().isAutomaticTask()) {
			hasAutomaticBegin = true;
		} else {
			log.warn("Tasks with more than one input or output set cannot be executed automatically.");
			hasAutomaticBegin = false;
		}
	}

	/**
	 * Sets the flag for automatic execution of this activity to {@literal false}. 
	 * This is used by exclusive gateways to prevent that the branch starting with the automatic
	 * activity is always taken. In this case, the automatic activity has to be started manually
	 * by the user.  
	 */
	public void forbidAutomaticStart() {
		hasAutomaticBegin = false;
	}

	/**
	 * An activity instance can only begin if it is in State READY and the
	 * FragmentPreCondition for the fragment instance is fulfilled.
	 */
	@Override
	public boolean canBegin() {
		return getState().equals(State.READY) && getFragmentInstance().isDataFlowEnabled();
	}

	/**
	 * An activity instance can only terminate if it is in State RUNNING.
	 */
	@Override
	public boolean canTerminate() {
		return getState().equals(State.RUNNING);
	}

	public List<BoundaryEventInstance> getAttachedBoundaryEventInstances() {
		return attachedBoundaryEventInstances;
	}

	public void setAttachedBoundaryEventInstances(List<BoundaryEventInstance> attachedBoundaryEventInstances) {
		this.attachedBoundaryEventInstances = attachedBoundaryEventInstances;
	}
}
