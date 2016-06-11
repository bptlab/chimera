package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events.DbTimerEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.TimeCalculator;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;

import java.util.Date;

/**
 *
 */
public class TimerEventInstance extends AbstractEvent {

    public TimerEventInstance(int controlNodeId, int fragmentInstanceId,
                              ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.setFragmentInstanceId(fragmentInstanceId);
    }

    @Override
    public String getType() {
        return "TimerEvent";
    }

    @Override
    public void registerEvent() {
        EventDispatcher.registerTimerEvent(this, this.getFragmentInstanceId(),
                this.getScenarioInstance().getScenarioInstanceId(),
                this.getScenarioInstance().getScenarioId());
    }

    public Date getTerminationDate() {
        DbTimerEvent dbTimerEvent = new DbTimerEvent();
        String timerDefinition = dbTimerEvent.retrieveTimerDefinition(this.getControlNodeId());
        TimeCalculator calculator = new TimeCalculator();
        Date now = new Date();
        return calculator.getDatePlusInterval(now, timerDefinition);
    }
}
