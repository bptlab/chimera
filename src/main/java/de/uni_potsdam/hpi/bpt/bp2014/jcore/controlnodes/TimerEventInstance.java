package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events.DbTimerEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.TimeCalculator;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;

import java.util.Date;

/**
 * Represent timer events. Currently only time spans are supported (e.g. one day).
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

    /**
     * Since timer events are not registered at a Event platform, but are handled internally,
     * calls specific method for timer events.
     */
    @Override
    public void registerEvent() {
        EventDispatcher.registerTimerEvent(this, this.getFragmentInstanceId(),
                this.getScenarioInstance().getScenarioInstanceId(),
                this.getScenarioInstance().getScenarioId());
    }

    /**
     * Calculate the termination of the timer from the current time and the
     * specified time span.
     *
     * @return the Date when the timer should be triggered.
     */
    public Date getTerminationDate() {
        DbTimerEvent dbTimerEvent = new DbTimerEvent();
        String timerDefinition = dbTimerEvent.retrieveTimerDefinition(this.getControlNodeId());
        TimeCalculator calculator = new TimeCalculator();
        Date now = new Date();
        return calculator.getDatePlusInterval(now, timerDefinition);
    }
}
