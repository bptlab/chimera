package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventQueryQueue;

/**
 *
 */
public abstract class AbstractEvent extends AbstractControlNodeInstance {
    private static final String MQ_HOST = "bpt.hpi.uni-potsdam.de";
    private static final String MQ_PORT = "61616";
    private static final String REST_PATH = "webapi/REST/EventQuery";
    private static final String REST_URL = "http://172.16.64.105:8080/Unicorn-unicorn_BP15_dev/";
    private int controlNodeId;

    /**
     *
     * @param controlNodeId id of the abstract control node which represents the event.
     */
    public AbstractEvent(int controlNodeId) {
        this.controlNodeId = controlNodeId;
    }

    @Override
    public void enableControlFlow() {
        DbEvent eventDao = new DbEvent();
        try {
            String query = eventDao.getQueryForControlNode(this.controlNodeId);
            EventQueryQueue queryQueue = new EventQueryQueue(MQ_HOST, MQ_PORT, REST_PATH, REST_URL);
            String queueId = queryQueue.registerQuery("Automatic", query,
                    "test@mail.de");
            queryQueue.listenToEvent(this, queueId);
        } catch (IllegalArgumentException e) {
            this.terminate();
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
}
