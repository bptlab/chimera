package de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryService;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * This class implements the REST interface of the JEngine history.
 */
@Path("v2/history/")
public class RestInterface {
    private HistoryService historyService = new HistoryService();

    @GET
    @Path("scenario/{scenarioID}/instance/{scenarioInstanceID}/activities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivityLog(@PathParam("scenarioInstanceID") int scenarioInstanceID) {
        DbScenario scenario = new DbScenario();
        Map<Integer, Map<String, Object>> activityLog;
        activityLog = historyService.getActivityInstanceLogEntriesForScenarioInstance(scenarioInstanceID);
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject(activityLog).toString())
                .build();
    }

    @GET
    @Path("scenario/{scenarioID}/instance/{scenarioInstanceID}/dataobjects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataObjectLog(@PathParam("scenarioInstanceID") int scenarioInstanceID) {
        DbScenario scenario = new DbScenario();
        Map<Integer, Map<String, Object>> dataObjectLog;
        dataObjectLog = historyService.getDataObjectLogEntriesForScenarioInstance(scenarioInstanceID);
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject(dataObjectLog).toString())
                .build();
    }
}
