package de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest;

import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryService;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */


/**
 * This class implements the REST interface of the JEngine history.
 */
@Path("history/v2/")
public class RestInterface {
    private HistoryService historyService = new HistoryService();

    /**
     * This method gives the log entries for all activities for a specific scenario instance.
     *
     * @param instanceID The id of the scenario instance.
     * @return a JSON-Object with the log entries.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivityLog(
            @DefaultValue("0") @PathParam("scenarioID") int scenarioID,
            @DefaultValue("0") @PathParam("instanceID") int instanceID,
            @DefaultValue(" ") @QueryParam("state") String state) {
        if (instanceID == 0 || scenarioID == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The instance or scenario ID is incorrect\"}")
                    .build();
        }

        Map<Integer, Map<String, Object>> activityLog;
        if (state == null) {
            state = "";
        }
        switch (state) {
            case "terminated":
                activityLog = historyService.getSelectedActivityInstanceLogEntriesForScenarioInstance(instanceID);
                break;
            default:
                activityLog = historyService.getActivityInstanceLogEntriesForScenarioInstance(instanceID);
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
            @DefaultValue("0") @PathParam("scenarioID") int scenarioID,
            @DefaultValue("0") @PathParam("scenarioInstanceID") int scenarioInstanceID) {
        if (scenarioInstanceID == 0 || scenarioID == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The instance or scenario ID is incorrect\"}")
                    .build();
        }
        Map<Integer, Map<String, Object>> dataObjectLog;
        dataObjectLog = historyService.getDataObjectLogEntriesForScenarioInstance(scenarioInstanceID);
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject(dataObjectLog).toString())
                .build();
    }

    /**
     * This method gives the log entries for all DataAttributeInstances for a specific scenario instance.
     *
     * @param scenarioInstanceID The id of the scenario instance.
     * @return a JSON-Object with the log entries.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{scenarioInstanceID}/attributes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataAttributeLog(
            @DefaultValue("0") @PathParam("scenarioID") int scenarioID,
            @DefaultValue("0") @PathParam("scenarioInstanceID") int scenarioInstanceID) {
        if (scenarioInstanceID == 0 || scenarioID == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The instance or scenario ID is incorrect\"}")
                    .build();
        }

        Map<Integer, Map<String, Object>> attributeLog;
        attributeLog = historyService.getDataAttributeInstanceLogEntriesForScenarioInstance(scenarioInstanceID);
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject(attributeLog).toString())
                .build();
    }
}
