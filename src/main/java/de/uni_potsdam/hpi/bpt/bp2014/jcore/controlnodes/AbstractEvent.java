package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events.DbEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.EventOutgoingBehavior;

/**
 *
 */
public abstract class AbstractEvent extends AbstractControlNodeInstance {
    private int controlNodeId;
    private String queryString;
    private EventOutgoingBehavior outgoingBehavior;
    /**
     *
     * @param controlNodeId id of the abstract control node which represents the event.
     */
    public AbstractEvent(
            int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance) {
        this.controlNodeId = controlNodeId;
        this.scenarioInstance = scenarioInstance;
        this.setFragmentInstanceId(fragmentInstanceId);

        DbControlNodeInstance databaseNodeInstance = new DbControlNodeInstance();
        if (!databaseNodeInstance.existControlNodeInstance(controlNodeId, fragmentInstanceId)) {
            int controlNodeInstanceId = databaseNodeInstance.createNewControlNodeInstance(
                    controlNodeId, this.getType(), fragmentInstanceId);
            this.setControlNodeInstanceId(controlNodeInstanceId);
        } else {
            this.setControlNodeInstanceId(databaseNodeInstance.getControlNodeInstanceID(
                    controlNodeId, fragmentInstanceId));
        }
        outgoingBehavior = this.createOutgoingBehavior();
    }

    protected EventOutgoingBehavior createOutgoingBehavior() {
        return new EventOutgoingBehavior(controlNodeId,
                scenarioInstance, getFragmentInstanceId(), getControlNodeInstanceId());
    }

    public abstract String getType();

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

    @Override
    public EventOutgoingBehavior getOutgoingBehavior() {
        return this.outgoingBehavior;
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
        return terminate("");
    }

    public boolean terminate(String eventJson) {
        outgoingBehavior.terminate(eventJson);
        return true;
    }
}
