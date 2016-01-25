package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.EventIncomingControlFlowBehavior;

/**
 *
 */
public class StartEvent extends AbstractControlNodeInstance{
    private final ScenarioInstance scenarioInstance;
    private final String type;

    /**
     *
     * @param fragmentInstanceId
     * @param scenarioInstance
     * @param type
     */
    public StartEvent(int fragmentInstanceId, ScenarioInstance scenarioInstance, String type) {
        this.scenarioInstance = scenarioInstance;
        this.setFragmentInstanceId(fragmentInstanceId);
        this.type = type;

    }


    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public boolean terminate() {
        return false;
    }

    @Override
    public void enableControlFlow() {

    }
}
