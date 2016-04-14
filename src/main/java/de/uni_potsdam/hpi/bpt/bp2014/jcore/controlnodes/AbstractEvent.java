package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.DataAttributeWriter;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.EventOutgoingBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class AbstractEvent extends AbstractControlNodeInstance {
    private int controlNodeId;
    private String queryString;

    /**
     *
     * @param controlNodeId id of the abstract control node which represents the event.
     */
    public AbstractEvent(int controlNodeId, ScenarioInstance scenarioInstance) {
        this.controlNodeId = controlNodeId;
        this.scenarioInstance = scenarioInstance;
    }

    @Override
    public void enableControlFlow() {
        DbEvent eventDao = new DbEvent();

        this.queryString = eventDao.getQueryForControlNode(this.controlNodeId);
        if (queryString.trim().isEmpty()) {
            this.terminate();
        } else {
            this.registerEvent();
        }
    }

    @Override
    public int getControlNodeId() {
        return controlNodeId;
    }

    @Override
    public void setControlNodeId(int controlNodeId) {
        this.controlNodeId = controlNodeId;
    }

    protected void registerEvent() {
        EventDispatcher.registerEvent(this, this.getFragmentInstanceId(),
                this.getScenarioInstance().getScenarioInstanceId(),
                this.getScenarioInstance().getScenarioId());
    }

    public String getQueryString() {
        return queryString;
    }

    public ScenarioInstance getScenarioInstance() {
        return scenarioInstance;
    }

    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public boolean terminate() {
        EventOutgoingBehavior outgoingBehavior = new EventOutgoingBehavior(this.getControlNodeId(),
                this.scenarioInstance, this.getFragmentInstanceId(),  this.getControlNodeInstanceId());
        outgoingBehavior.terminate();
        return true;
    }

    public boolean terminate(String eventJson) {
        EventOutgoingBehavior outgoingBehavior = new EventOutgoingBehavior(this.getControlNodeId(),
                this.scenarioInstance, this.getFragmentInstanceId(), this.getControlNodeInstanceId());
        outgoingBehavior.terminate(eventJson);
        return true;
    }


}
