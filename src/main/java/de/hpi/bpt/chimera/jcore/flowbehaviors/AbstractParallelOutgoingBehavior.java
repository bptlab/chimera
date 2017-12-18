package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ControlNodeFactory;

import java.util.List;

/**
 * This class represents parallel outgoing behavior.
 */
public abstract class AbstractParallelOutgoingBehavior extends AbstractOutgoingBehavior {
	/**
	 * Set all following control nodes to control flow enabled and initializes them.
	 */
	public void enableFollowing() {
		List<Integer> followingControlNodeIds = this.getDbControlFlow().getFollowingControlNodes(this.getControlNodeId());
		for (int followingControlNodeId : followingControlNodeIds) {
			AbstractControlNodeInstance followingControlNodeInstance = createFollowingNodeInstance(followingControlNodeId);
			//enable following instances
			followingControlNodeInstance.enableControlFlow();
		}
	}

	/**
	 * Scans for the passed controlnode id in the currently registered instances
	 * in the scenario instance. If it is present the controlNodeInstance will be returned
	 * if it's not an activity instance or terminated. Else a new control node will created.
	 *
	 * @param controlNodeId This is the database id from the control node instance.
	 * @return the created control node instance.
	 */
	protected AbstractControlNodeInstance createFollowingNodeInstance(int controlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance : this.getScenarioInstance().getControlNodeInstances()) {
			if (controlNodeId == controlNodeInstance.getControlNodeId() && !controlNodeInstance.getClass().equals(ActivityInstance.class) && !controlNodeInstance.getState().equals(State.TERMINATED)) {
				return controlNodeInstance;
			}
		}
		ControlNodeFactory controlNodeFactory = new ControlNodeFactory();
		AbstractControlNodeInstance controlNode = controlNodeFactory.createControlNodeInstance(controlNodeId, getFragmentInstanceId(), getScenarioInstance());
		getScenarioInstance().getControlNodeInstances().add(controlNode);
		return controlNode;
	}
}
