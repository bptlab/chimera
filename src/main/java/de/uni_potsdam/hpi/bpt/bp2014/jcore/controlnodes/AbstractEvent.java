package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events.DbEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.SseNotifier;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.EventOutgoingBehavior;

/**
 *
 */
public abstract class AbstractEvent extends AbstractControlNodeInstance {
    private int controlNodeId;
    private String queryString;
    private EventOutgoingBehavior outgoingBehavior;

    /**
     * Builds a new instance of an AbstractEvent.
     * @param controlNodeId the id of the event control node.
     * @param fragmentInstanceId the ud of the instance the event belongs to.
     * @param scenarioInstance the scenarioInstance object.
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
            this.setControlNodeInstanceId(databaseNodeInstance.getControlNodeInstanceId(
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
                this.getScenarioInstance().getId(),
                this.getScenarioInstance().getScenarioId());
    }

    public String getQueryString() {
        return queryString;
    }

    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public boolean terminate() {
        return terminate("");
    }

    /**
     * Terminate the event and set the data attributes accordingly. Send refresh to the
     * Chimera frontend.
     *
     * @param eventJson the json string containing the values.
     * @return Returns true
     */
    public boolean terminate(String eventJson) {
        outgoingBehavior.terminate(eventJson);
        SseNotifier.notifyRefresh();
        return true;
    }
}
