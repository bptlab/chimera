package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


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


@Path("interface/v1/en/") //defining also version and language
public class RestConnection {
    private ExecutionService executionService = new ExecutionService();
    private HistoryService historyService = new HistoryService();

    // GET open / closed activities
    @GET
    @Path("scenario/{scenarioID}/interface/{instanceID}/{Status}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showEnabledActivities(@PathParam("Scenarioname") int scenarioID, @PathParam("Instance") int scenarioInstanceID, @PathParam("Status") String status) {

        if (status.equals("enabled")) {//open activities

            if (!executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID))) {
                return Response.serverError().entity("Error: not a correct scenario instance").build();

            }
            LinkedList<Integer> enabledActivitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(scenarioInstanceID);
            if (enabledActivitiesIDs.size() == 0)
                return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//no open activities present
            Gson gson = new Gson();
            JsonHashMapIntegerString json = new JsonHashMapIntegerString(enabledActivitiesIDs, labels);
            String jsonRepresentation = gson.toJson(json);
            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();

        } else if (status.equals("terminated")) {//closed activities

            if (!executionService.existScenarioInstance(scenarioID, scenarioInstanceID))
                return Response.serverError().entity("Error: not a correct scenario instance").build();

            LinkedList<Integer> terminatedActivities = historyService.getTerminatedActivitysForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = historyService.getTerminatedActivityLabelsForScenarioInstance(scenarioInstanceID);

            if (terminatedActivities.size() == 0)
                return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//no closed activities present

            Gson gson = new Gson();
            JsonHashMapIntegerString json = new JsonHashMapIntegerString(terminatedActivities, labels);
            String jsonRepresentation = gson.toJson(json);

            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
        }

        return Response.serverError().entity("Error: status not clear").build();//status != {enabled,terminated}
    }

    // GET Dataobjects & States
    @GET
    @Path("DataObjects/{Scenarioname}/{Instance}")
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

    // GET scenarioIDs for a scenarioInstance
    @GET
    @Path("Get/ScenarioID/{ScenarioInstance}")  //scenarioInstanceID = (int) ScenarioInstance
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenarioID(@PathParam("ScenarioInstance") int scenarioInstanceID) {

        if (!executionService.existScenarioInstance(scenarioInstanceID))
            return Response.serverError().entity("Error: not a correct scenario instance").build();

        int scenarioID = executionService.getScenarioIDForScenarioInstance(scenarioInstanceID);
        Gson gson = new Gson();
        JsonInteger json = new JsonInteger(scenarioID);
        String jsonRepresentation = gson.toJson(json);

        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    // GET all scenarioInstanceIDs for all scenarioInstances of a scenario
    @GET
    @Path("Instances/{Scenarioname}")   //scenarioID = (int) Scenarioname
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScenarioInstances(@PathParam("Scenarioname") int scenarioID) {

        if (!executionService.existScenario(scenarioID))
            return Response.serverError().entity("Error: not a correct scenario").build();

        LinkedList<Integer> scenarioIDs = executionService.listAllScenarioInstancesForScenario(scenarioID);

        if (scenarioIDs.size() == 0)
            return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//no instances present

        Gson gson = new Gson();
        JsonIntegerList json = new JsonIntegerList(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);

        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    //GET  details for an activityID
    @GET
    @Path("ActivityID/{Activity}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showLabelForActivity(@PathParam("Activity") int activityInstanceID) {
        String label = executionService.getLabelForControlNodeID(activityInstanceID);

        if (label.equals(""))
            return Response.serverError().entity("Error: not correct Activity ID").build();//no activity with this id present

        return Response.ok(new String("{\"" + label + "\"}"), MediaType.APPLICATION_JSON).build();
    }

    //GET  information about an activityID
    @GET
    @Path("scenario/{scenarioID}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showLabelForScenarios(@PathParam("scenarioID") int scenarioID) {
        //if 0 as scenarioID is provided, list all available scenarioIDs
        if(scenarioID == 0) {
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
                return Response.serverError().entity("Error: not correct Activity ID").build();//no activity with this id present

            return Response.ok(new String("{\"" + label + "\"}"), MediaType.APPLICATION_JSON).build();
        }
    }

    // POST to start/terminate an activity + comment
    @POST
    @Path("{Scenarioname}/{Instance}/{Activity}/{Status}/{Comment}")
    public Boolean doActivity(@PathParam("Scenarioname") String scenarioID, @PathParam("Instance") int scenarioInstanceID, @PathParam("Activity") int activityInstanceID, @PathParam("Status") String status, @PathParam("Comment") String comment) {
        executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID));

        if (status.equals("begin")) {//start activity
            executionService.beginActivity(scenarioInstanceID, activityInstanceID);
            return true;
        } else if (status.equals("terminate")) {//terminate activity
            executionService.terminateActivity(scenarioInstanceID, activityInstanceID);
            return true;
        }
        return false;
    }

    // POST to start an instance of a scenario
    @POST
    @Path("Start/{Scenarioname}")   //scenarioID = (int) Scenarioname
    public int startNewActivity(@PathParam("Scenarioname") int scenarioID) {
        if (executionService.existScenario(scenarioID)) {//scenario exists
            return executionService.startNewScenarioInstance(scenarioID);
        } else {//scenario does not exist
            return -1;
        }
    }



    /***************************************************************
     * HELPER classes
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

    /*
    // Credits to http://stackoverflow.com/questions/13592236/parse-the-uri-string-into-name-value-collection-in-java
    public static Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }
    */
}
