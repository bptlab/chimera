package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class specifies the human task execution behavior.
 * TODO is this needed
 */
public class HumanTaskExecutionBehavior extends TaskExecutionBehavior {
	/**
	 *
	 * @param activityInstanceId This is an activty instance id.
	 * @param scenarioInstance	  This is a scenario instance.
	 * @param controlNodeInstance This is a control node instance.
	 */
	public HumanTaskExecutionBehavior(int activityInstanceId, ScenarioInstance scenarioInstance,
			AbstractControlNodeInstance controlNodeInstance) {
		super(activityInstanceId, scenarioInstance, controlNodeInstance);
	}

	@Override public void execute() {
        DbDataFlow dbDataFlow = new DbDataFlow();
		//allow an activity to terminate if it has no data attributes in output.
        int controlNodeId = this.getControlNodeInstance().getControlNodeId();
        List<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(controlNodeId);
        if (outputSets.isEmpty()) {
			this.setCanTerminate(true);
		} else if (getScenarioInstance().getDataAttributeInstances().isEmpty()) {
			this.setCanTerminate(true);
		} else {
			int outputSet = outputSets.get(0);
			DbDataFlow dataFlow = new DbDataFlow();
            List<Integer> dataClassIds = dataFlow.getFollowingDataClassIds(controlNodeId);
            if (attributeInOutputSet(dataClassIds)) {
                this.setCanTerminate(true);
            }
        }
 	}

    /**
     * Checks whether one of the currently initialized data attributes
     * references a data class id from the passed id's
     *
     * @param dataClassIds The dataclass ids from the output set to check
     * @return
     */
    private boolean attributeInOutputSet(List<Integer> dataClassIds) {
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        // get all data class ids from the currently initialized attributes
        Set<Integer> attributeClassIds = dataManager.getDataObjects()
                .stream().map(DataObject::getDataAttributeInstances).flatMap(Collection::stream)
                .map(x -> x.getDataObject().getDataClassId()).collect(Collectors.toSet());
        attributeClassIds.retainAll(dataClassIds);
        return !attributeClassIds.isEmpty();
    }
}
