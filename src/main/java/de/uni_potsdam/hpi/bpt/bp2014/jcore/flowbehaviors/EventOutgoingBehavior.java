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

    private final int controlNodeInstanceId;

    public EventOutgoingBehavior(int controlNodeId, ScenarioInstance scenarioInstance,
                                 int fragmentInstanceId, int controlNodeInstanceId) {
        this.setControlNodeId(controlNodeId);
        this.controlNodeInstanceId = controlNodeInstanceId;
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
        DataAttributeWriter writer = new DataAttributeWriter(
                this.getControlNodeId(), controlNodeInstanceId);
        List<DataAttributeInstance> attributeInstances = new ArrayList<>(
                this.getScenarioInstance().getDataAttributeInstances().values());
        writer.writeDataAttributesFromJson(eventJson, attributeInstances);
    }
}
