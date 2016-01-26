package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventQueryQueue;

/**
 *
 */
public abstract class AbstractEvent extends AbstractControlNodeInstance {
    private static final String MQ_HOST = "bpt.hpi.uni-potsdam.de";
    private static final String MQ_PORT = "61616";
    private static final String REST_PATH = "Unicorn/webapi/REST/EventQuery";

    @Override
    public void enableControlFlow() {
        EventQueryQueue queryQueue = new EventQueryQueue(MQ_HOST, MQ_PORT, REST_PATH);
        queryQueue.listenToEvent(this, "");
    }
}
