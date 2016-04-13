package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.DataAttributeWriter;

import java.util.ArrayList;
import java.util.List;

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
        DataAttributeWriter writer = new DataAttributeWriter(
                this.getControlNodeId());
        List<DataAttributeInstance> attributeInstances = new ArrayList<>(
                this.getScenarioInstance().getDataAttributeInstances().values());
        writer.writeDataAttributesFromJson(eventJson, attributeInstances);
    }
}
