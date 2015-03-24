package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * This class implements the REST interface of the JEngine core.
 * The core module provides methods to execute PCM instances
 * and to access the date inside the engine.
 * This REST interface provides methods to access this information
 * and to control the instances.
 * Methods which are necessary for the controlling can be found
 * inside the {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService}.
 * This class will use {@link de.uni_potsdam.hpi.bpt.bp2014.database.Connection}
 * to access the database directly.
 */
@Path("interface/v2")
public class RestInterface {

    /**
     * This method allows to give an overview of all scenarios.
     * The response will return a JSON-Array containing the basic
     * information of all scenarios currently inside the database.
     * If different versions of an scenarios exist only the latest
     * ones will be added to the json.
     *
     * @param filterString Specifies a search. Only scenarios which
     *                     name contain the specified string will be
     *                     returned.
     * @return Returns a JSON-Object with an Array with entries for
     * every Scenario.
     * Each Entry is a JSON-Object with a label and id of a scenario.
     */
    @GET
    @Path("scenario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenarios(@QueryParam("filter") String filterString) {
        DbScenario scenario = new DbScenario();
        Map<Integer, String> scenarios;
        if (filterString == null || filterString.equals("")) {
            scenarios = scenario.getScenarios();
        } else {
            scenarios = scenario.getScenariosLike(filterString);
        }
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(mapToKeysAndResults(scenarios, "ids", "labels").toString())
                .build();
    }

    /**
     * This method provides information about one scenario.
     * The scenario is specified by an given id.
     * The response of this request will contain a valid JSON-Object
     * containing detailed information about the scenario.
     * If there is no scenario with the specific id a 404 will be returned,
     * with a meaningful error message.
     *
     * @param scenarioID The Id of the scenario used inside the database.
     * @return Returns a JSON-Object with detailed information about one scenario.
     * The Information contain the id, label, number of instances, latest version
     * and more.
     */
    @GET
    @Path("scenario/{scenarioID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenario(@PathParam("scenarioID") int scenarioID) {
        DbScenario dbScenario = new DbScenario();
        Map<String, Object> data = dbScenario.getScenarioDetails(scenarioID);
        if (data.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject(data).toString())
                .build();
    }

    /**
     * This method provides information about all instances of one scenario.
     * The scenario is specified by an given id.
     * If there is no scenario with the specific id a 404 response with a meaningful
     * error message will be returned.
     * If the Scenario exists a JSON-Array containing JSON-Objects with
     * important information about an instance of the scenario will be returned.
     *
     * @param scenarioID   The id of the scenario which instances should be returned.
     * @param filterString Specifies a search. Only scenarios which
     *                     name contain the specified string will be
     *                     returned.
     * @return A JSON-Object with an array of information about all instances of
     * one specified scenario. The information contains the id and name.
     */
    @GET
    @Path("scenario/{scenarioID}/instance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenarioInstances(
            @PathParam("scenarioID") int scenarioID,
            @QueryParam("filter") String filterString) {
        ExecutionService executionService = new ExecutionService();
        if (!executionService.existScenario(scenarioID)) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"Scenario not found!\"}")
                    .build();
        }
        DbScenarioInstance instance = new DbScenarioInstance();
        JSONObject result = new JSONObject();
        Map<Integer, String> data = instance.getScenarioInstancesLike(scenarioID, filterString);
        result.put("ids", new JSONArray(data.keySet()));
        result.put("labels", new JSONObject(data));
        return Response
                .ok(result.toString(), MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * This method provides information about the termination condition.
     * Of the specified Scenario.
     * The termination condition is a set of sets of conditions.
     * Only if all conditions of one set are true the scenario will
     * terminate.
     * If the scenario does not exists a 404 with an error will be returned.
     * If the scenario exists the JSON representation of the condition set
     * will be returned.
     *
     * @param scenarioID This id specifies the scenario. The id is the
     *                   primary key inside the database.
     * @return Returns a response object. It will either  be a 200 or
     * 404. The content will be either the JSON representation of the termination
     * condition or an JSON object with the error message.
     */
    @GET
    @Path("scenario/{scenarioId}/terminationcondition")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTerminationCondition(@PathParam("scenarioId") int scenarioID) {
        DbScenario dbScenario = new DbScenario();
        if (!dbScenario.existScenario(scenarioID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no scenario with the id " + scenarioID + "\"}")
                    .build();
        }
        DbTerminationCondition terminationCondition = new DbTerminationCondition();
        Map<Integer, List<Map<String, Object>>> conditionSets = terminationCondition
                .getDetailedConditionsForScenario(scenarioID);
        JSONObject result = new JSONObject(conditionSets);
        result.put("setIDs", new JSONArray(conditionSets.keySet()));
        return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
    }

    /**
     * Creates a new instance of a specified scenario.
     * This method assumes that the name of then new instance will be the same
     * as the name of the scenario.
     * Hence no additional information should be transmitted.
     * The response will imply if the post was successful.
     *
     * @param scenarioID the id of the scenario.
     * @return The Response of the POST. The Response code will be
     * either a 201 (CREATED) if the post was successful or 400 (BAD_REQUEST)
     * if the scenarioID was invalid.
     * The content of the Response will be a JSON-Object containing information
     * about the new instance.
     */
    public Response startNewInstance(@PathParam("scenarioID") int scenarioID) {
        ExecutionService executionService = new ExecutionService();
        if (executionService.existScenario(scenarioID)) {
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"id\":" + executionService.startNewScenarioInstance(scenarioID) + "}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The Scenario could not be found!\"}")
                    .build();
        }
    }

    /**
     * Creates a new instance of a specified scenario.
     * This method assumes that the new instance will be named.
     * The name will be received as a JSON-Object inside the request
     * Body.
     * The JSON should have the format
     * {@code {"name": <nameOfInstance>}}.
     * The response will imply if the post was successful.
     *
     * @param scenarioID the id of the scenario.
     * @param name       The name, which will be used for the new instance.
     * @return The Response of the POST. The Response code will be
     * either a 201 (CREATED) if the post was successful or 400 (BAD_REQUEST)
     * if the scenarioID was invalid.
     * The content of the Response will be a JSON-Object containing information
     * about the new instance.
     */
    @POST
    @Path("scenario/{scenarioID}/instance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startNewNamedInstance(@PathParam("scenarioID") int scenarioID, NamedJaxBean name) {
        if (name == null) {
            return startNewInstance(scenarioID);
        }
        ExecutionService executionService = new ExecutionService();
        if (executionService.existScenario(scenarioID)) {
            DbScenarioInstance instance = new DbScenarioInstance();
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"id\":" + instance.createNewScenarioInstance(scenarioID, name.name) + "}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The Scenario could not be found!\"}")
                    .build();
        }
    }

    @POST
    @Path("scenario/{scenarioID}/instance/{instanceID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response terminateScenarioInstance(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID) {
        ExecutionService executionService = new ExecutionService();
        if (executionService.existScenario(scenarioID)) {
            executionService.terminateScenarioInstance(instanceID);
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The Scenario could not be found!\"}")
                    .build();
        }
    }

    // TODO: Change the state of an instance via POST

    /**
     * This method provides detailed information about a scenario instance.
     * The information will contain the id, name, parent scenario and the
     * number of activities in the different states.
     * The Response is JSON-Object.
     *
     * @param scenarioID The ID of the scenario.
     * @param instanceID The ID of the instance.
     * @return Will return a Response with a JSON-Object body, containing
     * the information about the instance.
     * If the instance ID or both are incorrect 404 (NOT_FOUND) will be
     * returned.
     * If the scenario ID is wrong but the instance ID is correct a 301
     * (REDIRECT) will be returned.
     * If both IDs are correct a 200 (OK) with the expected JSON-Content
     * will be returned.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenarioInstance(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID) {
        ExecutionService executionService = new ExecutionService();
        DbScenarioInstance instance = new DbScenarioInstance();
        if (!executionService.existScenarioInstance(instanceID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"There is no instance with the id " + instanceID + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else if (!executionService.existScenario(scenarioID)) {
            scenarioID = instance.getScenarioID(instanceID);
            try {
                return Response
                        .seeOther(new URI("interface/v2/scenario/" + scenarioID + "/instance/" + instanceID))
                        .build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return Response
                .ok((new JSONObject(instance.getInstanceMap(instanceID))).toString(),
                        MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * Returns a JSON-Object containing information about all activity
     * instances of a specified scenario instance.
     * The JSON-Object will group the activities regarding their state.
     * If the scenario instance does not exist, the response code will
     * specify the error which occurred.
     *
     * @param scenarioID   The id of the scenario
     * @param instanceID   The id of the instance.
     * @param filterString Defines a search strings. Only activities
     *                     with a label containing this String will be
     *                     shown.
     * @return A Response with the status and content of the request.
     * A 200 (OK) implies that the instance was found and the
     * result contains the JSON-Object.
     * If only the scenario ID is incorrect a 301 (REDIRECT)
     * will point to the correct URL.
     * If the instance ID is incorrect a 404 (NOT_FOUND) will
     * be returned.
     * TODO: Use the ExecutionService instead - be aware that using the E.S. leads to state changes.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivitiesOfInstance(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID,
            @QueryParam("filter") String filterString,
            @QueryParam("status") String state) {
        ExecutionService executionService = new ExecutionService();
        if (!executionService.existScenarioInstance(instanceID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"There is no instance with id" + instanceID + "\"}")
                    .build();
        } else if (!executionService.existScenario(scenarioID)) {
            try {
                return Response.seeOther(new URI("interface/v2/scenario/" +
                        executionService.getScenarioIDForScenarioInstance(instanceID) +
                        "/instance/" + instanceID + "/activity")).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if ((filterString == null || filterString.isEmpty()) && (state == null || state.isEmpty())) {
            return getAllActivitiesOfInstance(instanceID);
        } else if ((filterString == null || filterString.isEmpty())) {
            return getAllActivitiesOfInstanceWithState(instanceID, state);
        } else if ((state == null || state.isEmpty())) {
            return getAllActivitiesOfInstanceWithFilter(instanceID, filterString);
        } else {
            return getAllActivitiesWithFilterAndState(instanceID, filterString, state);
        }
    }

    /**
     * Returns a Response object.
     * The Object will be either a 200 with the activities in an JSON-Object
     * or an 400 with an error message if the state is invalid
     *
     * @param instanceID   The id of the scenario instance
     * @param filterString the filter string to be applied
     * @param state        the state of the activity
     * @return The Response object as described above.
     */
    private Response getAllActivitiesWithFilterAndState(int instanceID, String filterString, String state) {
        String states[] = {"enabled", "terminated", "running"};
        if ((new LinkedList<>(Arrays.asList(states))).contains(state)) {
            DbActivityInstance activityInstance = new DbActivityInstance();
            Map<Integer, Map<String, Object>> instances =
                    activityInstance.getMapForActivityInstancesWithFilterAndState(instanceID, filterString, state);
            JSONObject result = buildJSONObjectForActivities(instances);
            return Response
                    .ok(result.toString(), MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\":\"The state is not allowed " + state + " \"}")
                .build();
    }

    /**
     * Returns a Response Object.
     * The Response Object will be a 200 with JSON content.
     * The Content will be a JSON Object, containing information about activities.
     * The Label of the activities mus correspond to the filter String and be
     * part of the scenario instance specified by the instanceID.
     *
     * @param instanceID   The id of the scenario instance.
     * @param filterString The string which will be the filter condition for the activity ids.
     * @return The created Response object with a 200 and a JSON.
     */
    private Response getAllActivitiesOfInstanceWithFilter(int instanceID, String filterString) {
        DbActivityInstance activityInstance = new DbActivityInstance();
        Map<Integer, Map<String, Object>> instances;
        instances = activityInstance.getMapForActivityInstancesWithFilter(instanceID, filterString);
        JSONObject result = buildJSONObjectForActivities(instances);
        return Response
                .ok(result.toString(), MediaType.APPLICATION_JSON)
                .build();
    }

    private Response getAllActivitiesOfInstanceWithState(int instanceID, String state) {
        String states[] = {"enabled", "terminated", "running"};
        if ((new LinkedList<>(Arrays.asList(states))).contains(state)) {
            DbActivityInstance activityInstance = new DbActivityInstance();
            Map<Integer, Map<String, Object>> instances;
            instances = activityInstance.getMapForActivityInstancesWithState(instanceID, state);
            JSONObject result = buildJSONObjectForActivities(instances);
            return Response
                    .ok(result.toString(), MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\":\"The state is not allowed " + state + " \"}")
                .build();
    }

    /**
     * Builds a JSON Object for a Map with data
     * corresponding to a set of activities.
     *
     * @param instances The Map containing information about the activity instances.
     *                  We Assume that the key is a the id and the value is a Map
     *                  from String to Object with the properties of the instance.
     * @return The newly created JSON Object with the activity data.
     */
    private JSONObject buildJSONObjectForActivities(Map<Integer, Map<String, Object>> instances) {
        JSONObject result = new JSONObject();
        result.put("ids", instances.keySet());
        JSONArray activities = new JSONArray();
        for (Map<String, Object> value : instances.values()) {
            activities.put(new JSONObject(value));
        }
        result.put("activities", activities);
        return result;
    }

    /**
     * Returns a Response Object for all activities with the instance Id.
     * We assume that the instanceId is correct.
     * The Response will be a 200 with json content.
     * The Content will be a json object with information about each activity.
     *
     * @param instanceID the instance id of the scenario instance.
     * @return The Response Object, with 200 and JSON Content.
     */
    private Response getAllActivitiesOfInstance(int instanceID) {
        DbActivityInstance activityInstance = new DbActivityInstance();
        Map<Integer, Map<String, Object>> instances = activityInstance.getMapForAllActivityInstances(instanceID);
        JSONObject result = new JSONObject();
        result.put("ids", instances.keySet());
        JSONArray activities = new JSONArray();
        for (Map<String, Object> value : instances.values()) {
            activities.put(new JSONObject(value));
        }
        result.put("activities", activities);
        return Response
                .ok(result.toString(), MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * Updates the state of an activity instance.
     * The state will be changed to the specified one.
     * The activity Instance is specified by:
     *
     * @param scenarioID         The id of a scenario model.
     * @param scenarioInstanceID the id of an scenario instance.
     * @param activityID         the control node id of the activity.
     * @param state              the new status of the activity.
     * @return Returns a Response, the response code implies the
     * outcome of the POST-Request.
     * A 202 (ACCEPTED) means that the POST was successful.
     * A 400 (BAD_REQUEST) if the transition was not allowed.
     */
    @POST
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity/{activityID}/")
    public Response updateActivityStatus(@PathParam("scenarioID") String scenarioID,
                                         @PathParam("instanceID") int scenarioInstanceID,
                                         @PathParam("activityID") int activityID,
                                         @QueryParam("status") String state) {

        boolean result;
        ExecutionService executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(new Integer(scenarioID), scenarioInstanceID);
        switch (state) {
            case "begin":
                result = executionService.beginActivity(scenarioInstanceID, activityID);
                break;
            case "terminate":
                result = executionService.terminateActivity(scenarioInstanceID, activityID);
                break;
            default:
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"error\":\"The state transition " + state + "is unknown\"}")
                        .build();
        }
        if (result) {
            return Response.status(Response.Status.ACCEPTED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"activity state changed.\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"impsossible to " + (state.equals("begin") ? "start" : "terminate") +
                            "activity with id" + activityID + "\"}")
                    .build();
        }
    }

    /**
     * Returns a JSON-Object, which contains information about all
     * data objects of a specified scenario instance.
     * The data contains the id, label and state.
     *
     * @param scenarioID   The ID of the scenario model.
     * @param instanceID   The ID of the scenario instance.
     * @param filterString A String which specifies a filter. Only Data
     *                     Objects with a label containing this string
     *                     will be returned.
     * @return A Response with the outcome of the GET-Request. The Response
     * will be a 200 (OK) if the specified instance was found. Hence
     * the JSON-Object will be returned.
     * It will be a 301 (REDIRECT) if the scenarioID is wrong.
     * And a 404 if the instance id is wrong.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/dataobject")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataObjects(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID,
            @QueryParam("filter") String filterString) {
        ExecutionService executionService = new ExecutionService();
        if (!executionService.existScenarioInstance(instanceID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no instance with the id " + instanceID + "\"}")
                    .build();
        } else if (!executionService.existScenario(scenarioID)) {
            try {
                return Response.seeOther(new URI("interface/v2/scenario/" +
                        executionService.getScenarioIDForScenarioInstance(instanceID) +
                        "/instance/" + instanceID + "/dataobject")).build();
            } catch (URISyntaxException e) {
                return Response.serverError().build();
            }
        }

        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        LinkedList<Integer> dataObjects = executionService.getAllDataObjectIDs(instanceID);
        HashMap<Integer, String> states = executionService.getAllDataObjectStates(instanceID);
        HashMap<Integer, String> labels = executionService.getAllDataObjectNames(instanceID);
        if (filterString != null && !filterString.isEmpty()) {
            for (Map.Entry<Integer, String> labelEntry : labels.entrySet()) {
                if (!labelEntry.getValue().contains(filterString)) {
                    dataObjects.remove(labelEntry.getKey());
                    states.remove(labelEntry.getKey());
                    labels.remove(labelEntry.getKey());
                }
            }
        }
        JSONObject result = buildListForDataObjects(dataObjects, states, labels);
        return Response.ok(result.toString(),
                MediaType.APPLICATION_JSON).build();
    }


    /**
     * This method provides detailed information about an data Object.
     * The information contain the id, parent scenario instance, label
     * and the current state,
     * This information will be provided as a JSON-Object.
     * A Data object is specified by:
     *
     * @param scenarioID   The scenario Model ID.
     * @param instanceID   The scenario Instance ID.
     * @param dataObjectID The Data Object ID.
     * @return Returns a JSON-Object with information about the dataObject,
     * the response code will be a 200 (OK).
     * If the data object does not exist a 404 (NOT_FOUND) will not be
     * returned.
     * If the instance does exist but some params are wrong a 301
     * (REDIRECT) will be returned.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/dataobject/{dataObjectID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataObject(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID,
            @PathParam("dataObjectID") int dataObjectID) {
        ExecutionService executionService = new ExecutionService();
        if (!executionService.existScenarioInstance(instanceID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no instance with the id " + instanceID + "\"}")
                    .build();
        } else if (!executionService.existScenario(scenarioID)) {
            try {
                return Response.seeOther(new URI("interface/v2/scenario/" +
                        executionService.getScenarioIDForScenarioInstance(instanceID) +
                        "/instance/" + instanceID + "/dataobject/" + dataObjectID)).build();
            } catch (URISyntaxException e) {
                return Response.serverError().build();
            }
        }
        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        LinkedList<Integer> dataObjects = executionService.getAllDataObjectIDs(instanceID);
        HashMap<Integer, String> states = executionService.getAllDataObjectStates(instanceID);
        HashMap<Integer, String> labels = executionService.getAllDataObjectNames(instanceID);
        if (!dataObjects.contains(new Integer(dataObjectID))) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no dataobject with the id " + dataObjectID +
                            " for the scenario instance " + instanceID + "\"}")
                    .build();
        }
        DataObjectJaxBean dataObject = new DataObjectJaxBean();
        dataObject.id = dataObjectID;
        dataObject.label = labels.get(new Integer(dataObjectID));
        dataObject.state = states.get(new Integer(dataObjectID));
        return Response.ok(dataObject, MediaType.APPLICATION_JSON).build();
    }


    /*
     * Helper
     */


    /**
     * Creates an array of DataObjects.
     * The data objects will be created out of the information received from the execution Service.
     * The array elements will be of type {@link RestInterface.DataObjectJaxBean), hence JSON and
     * XML can be generated automatically.
     *
     * @param dataObjectIds an Arraqy of IDs used for the dataobjects inside the database.
     * @param states        The states, mapped from dataobject database id to state (String)
     * @param labels        The labels, mapped from dataobject database id to label (String)
     * @return A array with a DataObject for each entry in dataObjectIds
     */
    private JSONObject buildListForDataObjects(
            LinkedList<Integer> dataObjectIds,
            HashMap<Integer, String> states,
            HashMap<Integer, String> labels) {
        JSONObject result = new JSONObject();
        result.put("ids", dataObjectIds);
        JSONObject results = new JSONObject();
        for (Integer id : dataObjectIds) {
            JSONObject dataObject = new JSONObject();
            dataObject.put("id", id);
            dataObject.put("label", labels.get(id));
            dataObject.put("state", states.get(id));
            results.put("" + id, dataObject);
        }
        result.put("results", results);
        return result;
    }

    /**
     * Creates a JSON object from an HashMap.
     * The keys will be listed seperatly.
     *
     * @param data        The HashMap which contains the data of the Object
     * @param keyLabel    The name which will be used
     * @param resultLabel The label of the results.
     * @return The newly created JSON Object.
     */
    public JSONObject mapToKeysAndResults(Map data, String keyLabel, String resultLabel) {
        JSONObject result = new JSONObject();
        result.put(keyLabel, new JSONArray(data.keySet()));
        result.put(resultLabel, data);
        return result;
    }

    /**
     * A JAX bean which is used for a naming an entity.
     * Therefor a name can be transmitted.
     */
    @XmlRootElement
    public static class NamedJaxBean {
        /**
         * The name which should be assigned to the entity.
         */
        public String name;
    }

    /**
     * A JAX bean which is used for dataobject data.
     * It contains the data of one dataobject.
     * It can be used to create a JSON Object
     */
    @XmlRootElement
    public static class DataObjectJaxBean {
        /**
         * The label of the data object.
         */
        public String label;
        /**
         * The id the dataobject (not the instance) has inside
         * the database
         */
        public int id;
        /**
         * The state inside the database of the dataobject
         * which is stored in the table.
         * The label not the id will be holded.
         */
        public String state;
    }
}
