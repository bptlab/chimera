package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.DataAttributeWriter;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class EventOutgoingBehavior extends AbstractParallelOutgoingBehavior {

    public EventOutgoingBehavior(int controlNodeId, ScenarioInstance scenarioInstance,
                                 int fragmentInstanceId) {
        this.setControlNodeId(controlNodeId);
        this.setFragmentInstanceId(fragmentInstanceId);
        this.setScenarioInstance(scenarioInstance);
    }

    @Override public void terminate() {
        ScenarioInstance scenarioInstance = this.getScenarioInstance();
        scenarioInstance.updateDataFlow();
        scenarioInstance.checkXorGatewaysForTermination(this.getControlNodeId());

        this.enableFollowing();
        this.runAutomaticTasks();
    }

    public void terminate(String json) {
        this.writeDataObjects(json);
        this.terminate();
    }

    public void writeDataObjects(String eventJson) {
        Map<Integer, DataAttributeInstance> attributeInstanceMap =
                this.getScenarioInstance().getDataAttributeInstances();
        logDataAttributeTransitions(attributeInstanceMap);
        DataAttributeWriter writer = new DataAttributeWriter(
                this.getControlNodeId());
        List<DataAttributeInstance> attributeInstances =
                new ArrayList<>(attributeInstanceMap.values());
        writer.writeDataAttributesFromJson(eventJson, attributeInstances);
    }

    private void logDataAttributeTransitions(
            Map<Integer, DataAttributeInstance> attributeInstanceMap) {
        HistoryLogger logger = new HistoryLogger();
        for (Map.Entry<Integer, DataAttributeInstance> entry : attributeInstanceMap.entrySet()) {
            Integer dataattributeInstanceId = entry.getKey();
            String value = entry.getValue().getName();
            Integer nodeInstanceId = getScenarioInstance()
                    .getControlNodeInstanceForControlNodeId(
                            this.getControlNodeId()).getControlNodeInstanceId();
            logger.logDataAttributeTransition(dataattributeInstanceId, value, nodeInstanceId);

        }

    }
}
