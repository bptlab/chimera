package de.uni_potsdam.hpi.bpt.bp2014.jcore;


/**
 * Represents the incoming behavior of end events.
 */
public class EventIncomingControlFlowBehavior extends IncomingBehavior {
    private final String type;

    /**
     * Creates and initializes an event incoming control flow behavior.
     * This behavior is only for an end event.
     *
     * @param controlNodeInstance This is an instance of the class ControlNodeInstance.
     * @param scenarioInstance    This is an instance of the class ScenarioInstance.
     * @param type                This is the type of the event.
     */
    public EventIncomingControlFlowBehavior(ControlNodeInstance controlNodeInstance, ScenarioInstance scenarioInstance, String type) {
        this.controlNodeInstance = controlNodeInstance;
        this.scenarioInstance = scenarioInstance;
        this.type = type;
    }

    @Override
    public void enableControlFlow() {
        scenarioInstance.restartFragment(this.controlNodeInstance.fragmentInstance_id);
    }

    /*
     * Getter
     */

    /**
     * @return
     */
    public String getType() {
        return type;
    }
}
