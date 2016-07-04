package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataConditions;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.DataAttributeWriter;
import org.apache.log4j.Logger;

import java.util.*;

/**
 *
 */
public class EventOutgoingBehavior extends AbstractParallelOutgoingBehavior {

    static final Logger LOGGER = Logger.getLogger(EventOutgoingBehavior.class);

    private final int controlNodeInstanceId;

    public EventOutgoingBehavior(int controlNodeId, ScenarioInstance scenarioInstance,
                                 int fragmentInstanceId, int controlNodeInstanceId) {
        this.setControlNodeId(controlNodeId);
        this.controlNodeInstanceId = controlNodeInstanceId;
        this.setFragmentInstanceId(fragmentInstanceId);
        this.setScenarioInstance(scenarioInstance);
    }

    @Override
    public void terminate() {
        ScenarioInstance scenarioInstance = this.getScenarioInstance();
        scenarioInstance.updateDataFlow();
        scenarioInstance.checkXorGatewaysForTermination(this.getControlNodeId());

        this.enableFollowing();
        this.runAutomaticTasks();
    }

    public void terminate(String json) {
        DbDataFlow dataFlow = new DbDataFlow();
        List<Integer> inputClassIds = dataFlow.getPrecedingDataClassIds(this.getControlNodeId());
        List<Integer> outputClassIds = dataFlow.getFollowingDataClassIds(this.getControlNodeId());
        Set<Integer> toCreate = new HashSet<>(outputClassIds);
        toCreate.removeAll(inputClassIds);

        Map<String, String> dataClassNameToStateName = loadOnlyOutputSet();
        createDataObjects(toCreate, dataClassNameToStateName);

        if (json.isEmpty()) {
            LOGGER.info("No event json present to write data attributes from.");
        } else {
            this.writeDataObjects(json);
        }
        this.terminate();
    }

    private void createDataObjects(Set<Integer> toCreate, Map<String, String> dataClassNameToStateName) {
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        Map<Integer, Integer> dataClassIdToStateId =
                dataManager.translate(dataClassNameToStateName);
        for (int classId : toCreate) {
            dataManager.initializeDataObject(classId, dataClassIdToStateId.get(classId));
        }
    }

    public void writeDataObjects(String eventJson) {
        DataAttributeWriter writer = new DataAttributeWriter(
                this.getControlNodeId(), controlNodeInstanceId, this.getScenarioInstance());
        List<DataAttributeInstance> attributeInstances = new ArrayList<>(
                this.getScenarioInstance().getDataManager().getDataAttributeInstances());

        writer.writeDataAttributesFromJson(eventJson, attributeInstances);
    }

    /**
     * This method is used to load the default output of an activity.
     * If there is more than one possible output set an IllegalArgumentException
     * is thrown.
     * @return Map from data class id to state id, if the dataobject has only one possible output set
     */
    private Map<String, String> loadOnlyOutputSet() {
        DbDataFlow dbDataFlow = new DbDataFlow();
        List<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(
                this.getControlNodeId());
        if (outputSets.size() > 1) {
            throw new IllegalArgumentException("Should only be used when there are no "
                    + "alternative output sets.");
        }
        DbDataConditions dataConditions = new DbDataConditions();
        Map<String, Set<String>> outputMapWithSet = dataConditions.loadOutputSets(this.getControlNodeId());
        Map<String, String> outputMapForOnlyOutput = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : outputMapWithSet.entrySet()) {
            assert 1 == entry.getValue().size();
            outputMapForOnlyOutput.put(entry.getKey(), entry.getValue().iterator().next());
        }
        return outputMapForOnlyOutput;
    }
}
