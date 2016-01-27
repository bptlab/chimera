package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventQueryQueue;

/**
 *
 */
public abstract class AbstractEvent extends AbstractControlNodeInstance {
    private static final String MQ_HOST = "bpt.hpi.uni-potsdam.de";
    private static final String MQ_PORT = "61616";
    private static final String REST_PATH = "Unicorn/webapi/REST/EventQuery";

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
        String query = eventDao.getQueryForControlNode(this.controlNodeId);
        EventQueryQueue queryQueue = new EventQueryQueue(MQ_HOST, MQ_PORT, REST_PATH);
        String queueId = queryQueue.registerQuery("Automatic", query,
                "test@mail.de", "localhost:8080");
        queryQueue.listenToEvent(this, queueId);
    }
}
