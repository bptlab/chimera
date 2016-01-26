package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 *
 */
public class StartEvent extends AbstractEvent {
    private final ScenarioInstance scenarioInstance;

    /**
     * @param fragmentInstanceId databaseId of the Fragment where start event belongs to.
     * @param scenarioInstance ScenarioInstance object which holds control nodes.
     */
    public StartEvent(int fragmentInstanceId, ScenarioInstance scenarioInstance) {
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
