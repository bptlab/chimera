package de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest;

import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryService;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * This class implements the REST interface of the JEngine history.
 */
@Path("history/v2/")
public class RestInterface {
    private HistoryService historyService = new HistoryService();

    /**
     * This method gives the log entries for all activities for a specific scenario instance.
     *
     * @param scenarioInstanceID The id of the scenario instance.
     * @return a JSON-Object with the log entries.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{scenarioInstanceID}/activities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivityLog(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("scenarioInstanceID") int scenarioInstanceID,
            @QueryParam("state") String state) {
        Map<Integer, Map<String, Object>> activityLog;
        if(state == null) {
            state = "";
        }
        switch (state) {
            case "terminated":
                activityLog = historyService.getActivityInstanceLogEntriesForScenarioInstance(scenarioInstanceID);
                break;
            default:
                activityLog = historyService.getSelectedActivityInstanceLogEntriesForScenarioInstance(scenarioInstanceID);
                break;
        }
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject(activityLog).toString())
                .build();
    }

    /**
     * This method gives the log entries for all dataObjects for a specific scenario instance.
     *
     * @param scenarioInstanceID The id of the scenario instance.
     * @return a JSON-Object with the log entries.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{scenarioInstanceID}/dataobjects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataObjectLog(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("scenarioInstanceID") int scenarioInstanceID) {
        Map<Integer, Map<String, Object>> dataObjectLog;
        dataObjectLog = historyService.getDataObjectLogEntriesForScenarioInstance(scenarioInstanceID);
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject(dataObjectLog).toString())
                .build();
    }
}
