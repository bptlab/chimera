package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 *
 */
public class StartEvent extends AbstractEvent {
    private final ScenarioInstance scenarioInstance;

    /**
     * @param controlNodeId id of the abstract control node which represents the event.
     * @param fragmentInstanceId databaseId of the Fragment where start event belongs to.
     * @param scenarioInstance ScenarioInstance object which holds control nodes.
     */
    public StartEvent(int controlNodeId, int fragmentInstanceId,
                      ScenarioInstance scenarioInstance) {
        super(controlNodeId);
        this.scenarioInstance = scenarioInstance;
        this.setFragmentInstanceId(fragmentInstanceId);
    }

    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public boolean terminate() {
        return false;
    }

}
