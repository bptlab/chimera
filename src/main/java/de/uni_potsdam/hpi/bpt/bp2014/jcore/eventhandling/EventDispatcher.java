package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbEventMapping;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.UUID;

/**
 * The event dispatcher class is responsible for manage registrations from Events to RestQueries.
 */
public class EventDispatcher {
    private final String restPath;
    private final String restUrl;
    private final int fragmentInstanceId;


    public EventDispatcher(String restPath, String restUrl, int fragmentInstanceId) {
        this.restPath = restPath;
        this.restUrl = restUrl;
        this.fragmentInstanceId = fragmentInstanceId;
    }

    public void receiveEvent(String requestId) {
        DbEventMapping eventMapping = new DbEventMapping();
        int eventControlNodeId = eventMapping.getEventControlNodeId(fragmentInstanceId, requestId);
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
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            StringEntity postString = new StringEntity(gson.toJson(queryRequest));
            post.setEntity(postString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Query could not be registered");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}
