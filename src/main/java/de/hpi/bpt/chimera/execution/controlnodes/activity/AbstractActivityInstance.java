package de.hpi.bpt.chimera.execution.controlnodes.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.AbstractDataControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.BoundaryEventInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;

@Entity
public abstract class AbstractActivityInstance extends AbstractDataControlNodeInstance {
	private static final Logger log = Logger.getLogger(AbstractActivityInstance.class);

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
		this.attachedBoundaryEventInstances = new ArrayList<>();
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
		getFragmentInstance().activate();
		getFragmentInstance().skipAlternativeControlNodes(this);
		
		createAttachedBoundaryEvents();

		setState(State.RUNNING);
		execute();

		if (hasAutomaticBegin()) {
			log.info(this);
			getCaseExecutioner().terminateDataControlNodeInstance(this);
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
		log.info("Super terminate");
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

		// It is possible that an attached boundary event already unlocked some
		// of the selected data objects
		List<DataObject> workingItems = getSelectedDataObjects().stream().filter(d -> d.isLocked()).collect(Collectors.toList());
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
