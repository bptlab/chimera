package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;

/**
 *
 */
public class TimerEventInstance extends AbstractEvent {

    public TimerEventInstance(int controlNodeId, int fragmentInstanceId,
                              ScenarioInstance scenarioInstance) {
        super(controlNodeId, scenarioInstance);
        this.setFragmentInstanceId(fragmentInstanceId);
    }

    @Override
    public void registerEvent() {
        EventDispatcher.registerTimerEvent(this, this.getFragmentInstanceId(),
                this.getScenarioInstance().getScenarioInstanceId(),
                this.getScenarioInstance().getScenarioId());
    }
}
