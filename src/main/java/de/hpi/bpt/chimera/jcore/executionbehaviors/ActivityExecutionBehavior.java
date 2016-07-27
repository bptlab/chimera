package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.database.DbSelectedDataObjects;
import de.hpi.bpt.chimera.database.controlnodes.DbBoundaryEvent;
import de.hpi.bpt.chimera.database.history.DbLogEntry;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.BoundaryEvent;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.flowbehaviors.TaskIncomingControlFlowBehavior;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class ActivityExecutionBehavior extends AbstractExecutionBehavior {
    private static Logger log = Logger.getLogger(ActivityExecutionBehavior.class);

    private final ScenarioInstance scenarioInstance;

    ActivityInstance activityInstance;
    public ActivityExecutionBehavior(ActivityInstance instance) {
        this.activityInstance = instance;
        this.scenarioInstance = activityInstance.getScenarioInstance();
    }

    /**
     * Begins an ActivityInstance without specifying the data objects used.
     * <p>
     * This is possible when either, the input set of the ActivityInstance is empty,
     * or there is only one possible data object configuration for the input set.
     * Internally determines the data objects and calls {@link #begin(List)}.
     * <p>
     *
     * @throws IllegalArgumentException when there is more than one possible input selection.
     *                                  181
     */
    public void begin() {
        if (!activityInstance.getState().equals(State.READY)) {
            return;
        }
        DataManager dataManager = activityInstance.getScenarioInstance().getDataManager();
        List<DataObject> dataObjects = dataManager.getAvailableInput(
                activityInstance.getControlNodeId());
        long distinctDataclasses = dataObjects.stream().map(DataObject::getDataClassId)
                .distinct().count();
        if (!(dataObjects.size() == distinctDataclasses)) {
            String errorMsg = "Trying to start an activity instance with multiple possible" +
                    " input data objects, without specifying selected data object.";
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        List<Integer> dataobjectids = dataObjects.stream().map(DataObject::getId)
                .collect(Collectors.toList());
        this.begin(dataobjectids);
    }

    /**
     * Begins the activity instance. This locks all data objects, which are used by this activity.
     * Beginning an activity also begins all events attached to it.
     *
     * @param workingItems Ids of the data objects used by this activity
     *                     208
     * 209
     */
    public void begin(List<Integer> workingItems) {
        if (!activityInstance.getState().equals(State.READY)) {
            return;
        }
        ((TaskIncomingControlFlowBehavior) activityInstance.getIncomingBehavior())
                .lockDataObjects(workingItems);
        DbSelectedDataObjects dbDataObjectSelection = new DbSelectedDataObjects();
        int scenarioInstanceId = activityInstance.getScenarioInstance().getId();
        dbDataObjectSelection.saveDataObjectSelection(scenarioInstanceId,
                activityInstance.getControlNodeInstanceId(), workingItems);
        beginExecution();
    }

    private void beginExecution() {
        ScenarioInstance scenarioInstance = activityInstance.getScenarioInstance();
        int scenarioInstanceId = scenarioInstance.getId();
        new DbLogEntry().logActivity(activityInstance.getControlNodeInstanceId(),
                "running", scenarioInstanceId);
        scenarioInstance.updateDataFlow();
        scenarioInstance.checkXorGatewaysForTermination(activityInstance.getControlNodeId());
        registerAttachedEvents();
        if (activityInstance.isAutomaticTask()) {
            activityInstance.terminate();
        }
    }

    private void registerAttachedEvents() {
        DbBoundaryEvent boundaryEventDao = new DbBoundaryEvent();
        int boundaryEventId = boundaryEventDao.getBoundaryEventForActivity(
                activityInstance.getControlNodeId());
        if (boundaryEventId != -1) {
            BoundaryEvent event = new BoundaryEvent(boundaryEventId,
                    activityInstance.getFragmentInstanceId(),
                    activityInstance.getScenarioInstance());
            event.enableControlFlow();
        }
    }

    public ScenarioInstance getScenarioInstance() {
        return scenarioInstance;
    }
}