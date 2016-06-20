package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;


import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventSpawner;

public class SendEventOutgoingBehavior extends AbstractParallelOutgoingBehavior{

    private EventSpawner eventSpawner;

    public SendEventOutgoingBehavior(ScenarioInstance scenarioInstance,
                                     int controlNodeId) {
        this.setControlNodeId(controlNodeId);
        this.setScenarioInstance(scenarioInstance);
        eventSpawner = new EventSpawner(scenarioInstance);
    }

    @Override
    public void terminate() {
        eventSpawner.spawnEvent(getControlNodeId());

        ScenarioInstance scenarioInstance = this.getScenarioInstance();
        scenarioInstance.updateDataFlow();
        scenarioInstance.checkXorGatewaysForTermination(this.getControlNodeId());

        this.enableFollowing();
        this.runAutomaticTasks();
    }
}
