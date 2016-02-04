package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEventMapping;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.glassfish.grizzly.http.server.Response;

import javax.ws.rs.client.InvocationCallback;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * The event dispatcher class is responsible for manage registrations from Events to RestQueries.
 */
public class EventDispatcher {
    private final String restPath;
    private final String restUrl;
    private final int scenarioInstanceId;


    public EventDispatcher(String restPath, String restUrl, int scenarioInstanceId) {
        this.restPath = restPath;
        this.restUrl = restUrl;
        this.scenarioInstanceId = scenarioInstanceId;
    }

    public void receiveEvent(String requestId) {
        DbEventMapping eventMapping = new DbEventMapping();
        int eventControlNodeId = eventMapping.getEventControlNodeId(scenarioInstanceId, requestId);
    }

    public void registerEvent(AbstractEvent event) {
        final String requestId = UUID.randomUUID().toString().replaceAll("\\-", "");
        sendQueryToEventService(event.getQueryString(), requestId);
        DbEventMapping mapping = new DbEventMapping();
        mapping.saveMappingToDatabase(this.scenarioInstanceId, requestId, event.getControlNodeId());
    }

    private void sendQueryToEventService(String query, String requestId) {
        // TODO add here path which is used to receive events
        String dummyPath = "";
        String requestPath = dummyPath + requestId;

    }





}
