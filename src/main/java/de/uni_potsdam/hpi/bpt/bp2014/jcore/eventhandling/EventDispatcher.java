package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbEventMapping;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.EventFactory;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.UUID;

/**
 * The event dispatcher class is responsible for manage registrations from Events to RestQueries.
 */
@Path("scenario/{scenarioID}/instance/{instanceID}/events")
public class EventDispatcher {
    private final String restPath;
    private final String restUrl;
    private final int fragmentInstanceId;


    public EventDispatcher(String restPath, String restUrl, int fragmentInstanceId) {
        this.restPath = restPath;
        this.restUrl = restUrl;
        this.fragmentInstanceId = fragmentInstanceId;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{requestKey}")
    public Response receiveEvent(
            @PathParam("scenarioID") int scenarioId,
            @PathParam("instanceID") int scenarioInstanceId,
            @PathParam("requestKey") String requestId) {
        DbEventMapping eventMapping = new DbEventMapping();
        ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId, scenarioInstanceId);
        int eventControlNodeId = eventMapping.getEventControlNodeId(fragmentInstanceId, requestId);
        EventFactory factory = new EventFactory(scenarioInstance);
        AbstractEvent event = factory.getEventForInstanceId(eventControlNodeId);
        event.terminate();
        return Response.status(Response.Status.ACCEPTED).build();
    }

    public void registerEvent(AbstractEvent event) {
        final String requestId = UUID.randomUUID().toString().replaceAll("\\-", "");
        sendQueryToEventService(event.getQueryString(), requestId);
        DbEventMapping mapping = new DbEventMapping();
        mapping.saveMappingToDatabase(this.fragmentInstanceId, requestId, event.getControlNodeId());
    }

    private void sendQueryToEventService(String query, String requestId) {
        // TODO add here path which is used to receive events
        String dummyPath = "";
        String notificationPath = dummyPath + requestId;

        JsonObject queryRequest = new JsonObject();
        queryRequest.addProperty("queryString", query);
        queryRequest.addProperty("notificationPath", notificationPath);
        Gson gson = new Gson();
        HttpPost post = new HttpPost(restUrl);
        try {
            StringEntity postString = new StringEntity(gson.toJson(queryRequest));
            post.setEntity(postString);
            post.setHeader("Content-type", "application/json");
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
            HttpClient httpClient1 = new DefaultHttpClient(httpParams);
            HttpResponse response = httpClient1.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Query could not be registered");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
