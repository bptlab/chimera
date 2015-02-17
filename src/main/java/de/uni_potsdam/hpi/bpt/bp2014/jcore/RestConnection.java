package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * ********************************************************************************
 *
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 *
 * ******************************************************************
 *
 * Copyright © All Rights Reserved 2014 - 2015
 *
 * Please be aware of the License. You may found it in the root directory.
 *
 * **********************************************************************************
 */

/**
 *
 */
@Path("interface/v1/en/") //defining also version and language
public class RestConnection {
    private ExecutionService executionService = new ExecutionService();
    private HistoryService historyService = new HistoryService();

    /* ######################################################
     *
     * HTTP GET REQUESTS
     *
     * #######################################################
     */

    /**
     * GET  information about an activityID
     *
     * @param scenarioID the ID of the related scenario
     * @return returns JSON containing details for scenarios
     */

    @GET
    @Path("scenario/{scenarioID}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showDetailsForScenarios(@PathParam("scenarioID") int scenarioID) {
        //if 0 as scenarioID is provided, list all available scenarioIDs
        if (scenarioID == 0) {
            LinkedList<Integer> scenarioIDs = executionService.getAllScenarioIDs();

            if (scenarioIDs.size() == 0)
                return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//no scenarios present

            Gson gson = new Gson();
            JsonIntegerList json = new JsonIntegerList(scenarioIDs);
            String jsonRepresentation = gson.toJson(json);

            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
            //otherwise display the label for the scenarioID
        } else {
            String label = executionService.getScenarioName(scenarioID);

            if (label.equals(""))
                return Response.serverError().entity("Error: not correct scenarioID").build();//no activity with this id present

            return Response.ok(new String("{\"" + label + "\"}"), MediaType.APPLICATION_JSON).build();
        }
    }

    /**
     * GET all scenarioInstanceIDs  of a scenario
     *
     * @param scenarioID the ID of the related scenario
     * @param instanceID the ID of the related scenario instance
     * @return details for a scenario instance or a error code
     */

    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScenarioInstances(@PathParam("scenarioID") int scenarioID, @PathParam("instanceID") int instanceID) {
        //if instanceID is null, display all available instances for the mentioned scenarioID
        if (instanceID == 0) {
            if (!executionService.existScenario(scenarioID))
                return Response.serverError().entity("Error: not a correct scenario").build();

            LinkedList<Integer> scenarioIDs = executionService.listAllScenarioInstancesForScenario(scenarioID);

            if (scenarioIDs.size() == 0)
                return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build(); //no instances present

            Gson gson = new Gson();
            JsonIntegerList json = new JsonIntegerList(scenarioIDs);
            String jsonRepresentation = gson.toJson(json);

            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();

            //otherwise display details for this instanceID
            //TODO: implement returning of instance details (label, timestamp..)
        } else {
            String label = executionService.getScenarioNameForScenarioInstance(instanceID);

            if (label.equals(""))
                return Response.serverError().entity("Error: not correct instanceID").build();//no activity with this id present

            return Response.ok(new String("{\"" + label + "\"}"), MediaType.APPLICATION_JSON).build();
        }


    }

    /**
     * GET  details for an activityID
     *
     * @param scenarioID the ID of the related scenario
     * @param instanceID the ID of the related scenario instance
     * @param activityinstanceID the ID of the related activity instance
     * @param status the new status of the activity which is supposed to be updated
     * @param limit a limit which is not used yet but defined through API specs
     * @return details for an activity
     */

    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activityinstance/{activityinstanceID}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showDetailsForActivity(@PathParam("scenarioID") int scenarioID, @PathParam("instanceID") int instanceID, @PathParam("activityinstanceID") int activityinstanceID, @QueryParam("status") String status, @QueryParam("limit") String limit) {

        //display all open activities
        if (activityinstanceID == 0) {

            //TODO: Limit has to be implemented

            if (status == null) {
                status = "enabled"; //set status enabled for default value
            }

            if (status.equals("enabled")) { //open activities;

                if (!executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(instanceID))) {
                    return Response.serverError().entity("Error: not a correct scenario instance").build();
                }
                LinkedList<Integer> enabledActivitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(instanceID);
                HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(instanceID);
                if (enabledActivitiesIDs.size() == 0)
                    return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//no open activities present
                Gson gson = new Gson();
                JsonHashMapIntegerString json = new JsonHashMapIntegerString(enabledActivitiesIDs, labels);
                String jsonRepresentation = gson.toJson(json);
                return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();

            } else if (status.equals("terminated")) {//closed activities

                if (!executionService.existScenarioInstance(scenarioID, instanceID))
                    return Response.serverError().entity("Error: not a correct scenario instance").build();

                LinkedList<Integer> terminatedActivities = historyService.getTerminatedActivitiesForScenarioInstance(instanceID);
                HashMap<Integer, String> labels = historyService.getTerminatedActivityLabelsForScenarioInstance(instanceID);

                if (terminatedActivities.size() == 0)
                    return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//no closed activities present

                Gson gson = new Gson();
                JsonHashMapIntegerString json = new JsonHashMapIntegerString(terminatedActivities, labels);
                String jsonRepresentation = gson.toJson(json);

                return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
            } else if (status.equals("running")) { //running activities;

                if (!executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(instanceID))) {
                    return Response.serverError().entity("Error: not a correct scenario instance").build();
                }
                LinkedList<Integer> enabledActivitiesIDs = executionService.getRunningActivitiesIDsForScenarioInstance(instanceID);
                HashMap<Integer, String> labels = executionService.getRunningActivityLabelsForScenarioInstance(instanceID);
                if (enabledActivitiesIDs.size() == 0)
                    return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//no running activities present
                Gson gson = new Gson();
                JsonHashMapIntegerString json = new JsonHashMapIntegerString(enabledActivitiesIDs, labels);
                String jsonRepresentation = gson.toJson(json);
                return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();

            }

            return Response.serverError().entity("Error: status not clear").build();//status != {enabled,terminated}

            // display details for this activityID
            //TODO: implement returning of timestamp and additional details
        } else {
            String label = executionService.getLabelForControlNodeID(activityinstanceID);

            if (label.equals(""))
                return Response.serverError().entity("Error: not correct Activity ID").build();//no activity with this id present

            return Response.ok(new String("{\"" + label + "\"}"), MediaType.APPLICATION_JSON).build();
        }
    }

    /**
     * GET Dataobjects & States
     *
     * @param scenarioID the ID of the related scenario
     * @param scenarioInstanceID the ID of the related scenario instance
     * @param status the new status of the activity which is supposed to be updated
     * @return unkown
     */
 /*
    @GET
    @Path("DataObjects/{Scenarioname}/{Instance}")
    //TODO: adapt to new API Specification
    @Produces(MediaType.APPLICATION_JSON)
    public Response showDataObjects(@PathParam("Scenarioname") int scenarioID, @PathParam("Instance") int scenarioInstanceID, @PathParam("Status") String status) {

        if (!executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID)))
            return Response.serverError().entity("Error: not a correct scenario instance").build();

        LinkedList<Integer> dataObjects = executionService.getAllDataObjectIDs(scenarioInstanceID);
        HashMap<Integer, String> labels = executionService.getAllDataObjectStates(scenarioInstanceID);

        if (dataObjects.size() == 0)
            return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//no dataobjects present

        Gson gson = new Gson();
        JsonHashMapIntegerString json = new JsonHashMapIntegerString(dataObjects, labels);
        String jsonRepresentation = gson.toJson(json);

        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }
*/




    //TODO: not sure why we would need this

    /**
     * GET scenarioIDs for a scenarioInstance
     *
     * @param scenarioInstanceID the ID of the related scenario instance
     * @return unkown
     */
/*
    @GET
    @Path("Get/ScenarioID/{ScenarioInstance}")
    //TODO: adapt to new API Specification
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenarioID(@PathParam("scenarioID") int scenarioInstanceID) {

        if (!executionService.existScenarioInstance(scenarioInstanceID))
            return Response.serverError().entity("Error: not a correct scenario instance").build();

        int scenarioID = executionService.getScenarioIDForScenarioInstance(scenarioInstanceID);
        Gson gson = new Gson();
        JsonInteger json = new JsonInteger(scenarioID);
        String jsonRepresentation = gson.toJson(json);

        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }
*/

    /* #############################################################################
     *
     * HTTP POST REQUEST
     *
     * #############################################################################
     */

     /**
     * POST to start a new instance
     *
     * @param scenarioID defines the ID of the scenario
     * @return error status or id of new instance
     */

    // POST to start an instance of a scenario
    @POST
    @Path("scenario/{scenarioID}/")
    public int startNewScenarioInstance(@PathParam("scenarioID") int scenarioID) {
        // if scenario exists
        if (executionService.existScenario(scenarioID)) {
            //return the ID of new instanceID
            return executionService.startNewScenarioInstance(scenarioID);
        // else scenario does not exist
        } else {
            return -1;
        }
    }

    /**
     * POST to change status of an activity
     *
     * @param scenarioID the ID of the related scenario
     * @param scenarioInstanceID the ID of the related scenario instance
     * @param activityInstanceID the ID of the related activity instance
     * @param status the new status of the activity which is supposed to be updated
     * @return
     */
    // POST to start/terminate an activity + comment
    @POST
    @Path("scenario/{scenarioID}/instance/{instanceID}/activityinstance/{activityinstanceID}/")
    public Boolean doActivity(@PathParam("scenarioID") String scenarioID, @PathParam("instanceID") int scenarioInstanceID, @PathParam("activityinstanceID") int activityInstanceID, @QueryParam("status") String status) {
        executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID));

        if (status.equals("begin")) {//start activity
            return executionService.beginActivity(scenarioInstanceID, activityInstanceID);
        } else if (status.equals("terminate")) {//terminate activity
            return executionService.terminateActivity(scenarioInstanceID, activityInstanceID);
        }
        //return Response.serverError().entity("Error: status not clear").build();//status != {begin,begin}
        System.err.print("ERROR: no status defined " + status);
        return false;
    }


    /*
     *
     * HELPER CLASSES
     */

    class JsonHashMapIntegerString {
        private LinkedList<Integer> ids;
        private HashMap<Integer, String> label;

        public JsonHashMapIntegerString(LinkedList<Integer> ids, HashMap<Integer, String> labels) {
            this.ids = ids;
            this.label = labels;
        }
    }

    class JsonIntegerList {
        private LinkedList<Integer> ids;

        public JsonIntegerList(LinkedList<Integer> ids) {
            this.ids = ids;
        }
    }

    class JsonInteger {
        private Integer id;

        public JsonInteger(Integer id) {
            this.id = id;
        }
    }
}