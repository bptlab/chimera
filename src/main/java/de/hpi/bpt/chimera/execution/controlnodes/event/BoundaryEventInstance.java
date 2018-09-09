package de.hpi.bpt.chimera.execution.controlnodes.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;

@Entity
public class BoundaryEventInstance extends AbstractEventInstance {
	@ManyToOne
	@JoinColumn(name = "BoundaryEventInstance_ID")
	private AbstractActivityInstance attachedToActivity;

	/**
	 * for JPA only
	 */
	public BoundaryEventInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public BoundaryEventInstance(BoundaryEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
	}

	@Override
	public void enableControlFlow() {
		getSelectedDataObjects().clear();
		if (getState().equals(State.INIT)) {
			setState(State.CONTROLFLOW_ENABLED);
		}
		if (isDataFlowEnabled()) {
			setState(State.READY);
		}
		if (canBeginAutomatically()) {
			List<ConditionSet> conditionSets = getControlNode().getPreCondition().getConditionSets();
			ConditionSet inputSet = new ConditionSet();
			List<DataObject> dataObjectsWithSameNodeAsAttachedActivity = new ArrayList<>();
			if (!conditionSets.isEmpty()) {
				inputSet = conditionSets.get(0);

				// Check if boundary event has same DataNode in the PreCondition
				// in order to use those
				for (AtomicDataStateCondition condition : inputSet.getConditions()) {
					Optional<DataObject> dataObject = attachedToActivity.getSelectedDataObjects().stream()
														.filter(d -> d.getCondition().equals(condition))
														.findAny();
					if (dataObject.isPresent()) {
						dataObjectsWithSameNodeAsAttachedActivity.add(dataObject.get());
					}
				}
			}

			// Remove those data object that have a condition that is already
			// provided by the selected data objects of the attached activity
			Set<DataObject> fulfillingDataObjects = getDataManager().getFulfillingDataObjects(inputSet);
			for (DataObject fulfullingDataObject : fulfillingDataObjects) {
				for (DataObject dataObject : dataObjectsWithSameNodeAsAttachedActivity) {
					if (fulfullingDataObject.getCondition().equals(dataObject.getCondition())) {
						fulfillingDataObjects.remove(fulfullingDataObject);
					}
				}
			}

			getCaseExecutioner().beginDataControlNodeInstance(this, new ArrayList<>(fulfillingDataObjects));
			// For termination the boundary event needs to selected the same
			// data objects as the attached activity although it is not the who
			// locked it
			getSelectedDataObjects().addAll(dataObjectsWithSameNodeAsAttachedActivity);
		}
	}

	/**
	 * Boundary Events need to consider the Data Objects locked by the activity
	 * they are attached to because they have when this activity is canceled the
	 * bounded event has access to these DataObjects, too.
	 */
	@Override
	public boolean isDataFlowEnabled() {
		if (getState().equals(State.DATAFLOW_ENABLED)) {
			return true;
		}

		Set<DataObject> availableDataObjects = getDataManager().getAvailableDataObjects();
		availableDataObjects.addAll(attachedToActivity.getSelectedDataObjects());
		
		List<AtomicDataStateCondition> availableDataStateConditions = availableDataObjects.stream()
																		.map(DataObject::getCondition)
																		.collect(Collectors.toList());
		return getControlNode().getPreCondition().isFulfilled(availableDataStateConditions);
	}

	@Override
	public void terminate() {
		attachedToActivity.cancel();
		super.terminate();
	}

	public AbstractActivityInstance getAttachedToActivity() {
		return attachedToActivity;
	}

	public void setAttachedToActivity(AbstractActivityInstance attachedToActivity) {
		this.attachedToActivity = attachedToActivity;
	}

	@Override
	public BoundaryEvent getControlNode() {
		return (BoundaryEvent) super.getControlNode();
	}
}
