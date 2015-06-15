package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbTerminationCondition;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataObjectInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
    static Logger log = Logger.getLogger(RestInterface.class.getName());

    /**
     * This method allows to give an overview of all scenarios.
     * The response will return a JSON-Array containing the basic
     * information of all scenarios currently inside the database.
     * If different versions of an scenarios exist only the latest
     * ones will be added to the json.
     *
     * @param uriInfo      Specifies the context. For example the uri
     *                     of the request.
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
    public Response getScenarios(
            @Context UriInfo uriInfo,
            @QueryParam("filter") String filterString) {
        DbScenario scenario = new DbScenario();
        Map<Integer, String> scenarios;
        if (filterString == null || filterString.equals("")) {
            scenarios = scenario.getScenarios();
        } else {
            scenarios = scenario.getScenariosLike(filterString);
        }
        JSONObject jsonResult = mapToKeysAndResults(scenarios, "ids", "labels");
        JSONObject refs = new JSONObject();
        for (int id : scenarios.keySet()) {
            refs.put("" + id, uriInfo.getAbsolutePath() + "/" + id);
        }
        jsonResult.put("links", refs);
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(jsonResult.toString())
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
    public Response getScenario(@Context UriInfo uri,
                                @PathParam("scenarioID") int scenarioID) {
        DbScenario dbScenario = new DbScenario();
        Map<String, Object> data = dbScenario.getScenarioDetails(scenarioID);

        if (data.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        data.put("instances",
                uri.getAbsolutePath() + "/instance");
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject(data).toString())
                .build();
    }

    /**
     * This method provides information about all email Tasks inside
     * a given scenario.
     * The information consists of the id and the label.
     * A Json Object will be returned with an array of ids and a Map
     * from ids to labels.
     *
     * @param scenarioID   The ID of the scenario, its mail tasks will be returned.
     * @param filterString A Filter String, only mail tasks with a label containing
     *                     this filter String will be returned.
     * @return The JSON Object with ids and labels.
     */
    @GET
    @Path("scenario/{scenarioID}/emailtask")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEmailTasks(
            @PathParam("scenarioID") int scenarioID,
            @QueryParam("filter") String filterString) {
        DbScenario scenario = new DbScenario();
        DbEmailConfiguration mail = new DbEmailConfiguration();
        if (!scenario.existScenario(scenarioID)) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        String jsonRepresentation = JsonUtil.JsonWrapperLinkedList(mail.getAllEmailTasksForScenario(scenarioID));
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    /**
     * This method provides information about an email Task.
     * It will return a JSON-Object with information about the mail
     * configuration.
     * A Configuration contains a receiver, a subject and a content.
     * A Mail task is specified by:
     *
     * @param scenarioID The ID of the scenario model.
     * @param mailTaskID The control node ID of the mail Task.
     * @return Returns a 404 if the mail Task or scenario does not exist
     * and a 200 (OK) with a JSON-Object if the emailTask was found.
     */
    @GET
    @Path("scenario/{scenarioID}/emailtask/{emailTaskID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmailTaskConfiguration(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("emailTaskID") int mailTaskID) {
        DbScenario scenario = new DbScenario();
        DbEmailConfiguration mail = new DbEmailConfiguration();
        EmailConfigJaxBean mailConfig = new EmailConfigJaxBean();
        mailConfig.receiver = mail.getReceiverEmailAddress(mailTaskID);
        if (!scenario.existScenario(scenarioID) || mailConfig.receiver.equals("")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        mailConfig.content = mail.getMessage(mailTaskID);
        mailConfig.subject = mail.getSubject(mailTaskID);
        return Response.ok(mailConfig, MediaType.APPLICATION_JSON).build();
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
            @Context UriInfo uri,
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
        JSONObject links = new JSONObject();
        for (int id : data.keySet()) {
            links.put("" + id, uri.getAbsolutePath() + "/" + id);
        }
        result.put("ids", new JSONArray(data.keySet()));
        result.put("labels", new JSONObject(data));
        result.put("links", links);
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
        JSONObject conditions = new JSONObject(conditionSets);
        JSONObject result = new JSONObject();
        result.put("conditions", conditions);
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
     * @param uri        a context, which holds information about the server
     * @param scenarioID the id of the scenario.
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
    public Response startNewInstance(
            @Context UriInfo uri,
            @PathParam("scenarioID") int scenarioID) {
        ExecutionService executionService = new ExecutionService();
        if (executionService.existScenario(scenarioID)) {
            int instanceId = executionService.startNewScenarioInstance(scenarioID);
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"id\":" + instanceId +
                            ",\"link\":\"" + uri.getAbsolutePath() + "/" + instanceId + "\"}")
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
     * @param uriInfo    The context of the server, used to receive the url.
     * @param scenarioID the id of the scenario.
     * @param name       The name, which will be used for the new instance.
     * @return The Response of the PUT. The Response code will be
     * either a 201 (CREATED) if the post was successful or 400 (BAD_REQUEST)
     * if the scenarioID was invalid.
     * The content of the Response will be a JSON-Object containing information
     * about the new instance.
     */
    @PUT
    @Path("scenario/{scenarioID}/instance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startNewNamedInstance(
            @Context UriInfo uriInfo,
            @PathParam("scenarioID") int scenarioID, NamedJaxBean name) {
        if (name == null) {
            return startNewInstance(uriInfo, scenarioID);
        }
        ExecutionService executionService = new ExecutionService();
        if (executionService.existScenario(scenarioID)) {
            DbScenarioInstance instance = new DbScenarioInstance();
            int instanceId = instance.createNewScenarioInstance(scenarioID, name.name);
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"id\":" + instanceId +
                            ",\"link\":\"" + uriInfo.getAbsolutePath() + "/" + instanceId + "\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The Scenario could not be found!\"}")
                    .build();
        }
    }

    /**
     * This post can be used to terminate an existing scenario instance.
     * The scenario instance is specified by an instance id and a scenario id.
     * The Response will be either a JSON Object with an error message (400)
     * or an 201 without an content.
     *
     * @param scenarioID The Id of the scenario.
     * @param instanceID The Id of the instance to be terminated.
     * @return A Response object with json Object.
     * A Created Response will be returned if the POST has been successful
     * and a BAD_REQUEST else.
     */
    @PUT
    @Path("scenario/{scenarioID}/instance/{instanceID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response terminateScenarioInstance(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID) {
        ExecutionService executionService = new ExecutionService();
        if (executionService.existScenario(scenarioID) &&
                executionService.existScenarioInstance(instanceID)) {
            executionService.terminateScenarioInstance(instanceID);
            return Response.status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"The is instance has been terminated.\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The Scenario instance could not be found!\"}")
                    .build();
        }
    }

    /**
     * This method provides detailed information about a scenario instance.
     * The information will contain the id, name, parent scenario and the
     * number of activities in the different states.
     * The Response is JSON-Object.
     *
     * @param uriInfo    Contains the context information, is used to build
     *                   links to other resources.
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
            @Context UriInfo uriInfo,
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
                log.error("Error:", e);
            }
        }
        JSONObject result = new JSONObject(instance.getInstanceMap(instanceID));
        result.put("activities", uriInfo.getAbsolutePath() + "/activity");
        return Response
                .ok(result.toString(),
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
     * @param uriInfo      The context object. It provides information
     *                     the server context.
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
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivitiesOfInstance(
            @Context UriInfo uriInfo,
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID,
            @QueryParam("filter") String filterString,
            @QueryParam("state") String state) {
        ExecutionService executionService = new ExecutionService();
        if (!executionService.existScenarioInstance(instanceID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"There is no instance with id " + instanceID + "\"}")
                    .build();
        } else if (!executionService.existScenario(scenarioID)) {
            try {
                return Response.seeOther(new URI("interface/v2/scenario/" +
                        executionService.getScenarioIDForScenarioInstance(instanceID) +
                        "/instance/" + instanceID + "/activity")).build();
            } catch (URISyntaxException e) {
                log.error("Error:", e);
            }
        }
        if ((filterString == null || filterString.isEmpty()) && (state == null || state.isEmpty())) {
            return getAllActivitiesOfInstance(scenarioID, instanceID, uriInfo);
        } else if ((filterString == null || filterString.isEmpty())) {
            return getAllActivitiesOfInstanceWithState(scenarioID, instanceID, state, uriInfo);
        } else if ((state == null || state.isEmpty())) {
            return getAllActivitiesOfInstanceWithFilter(scenarioID, instanceID, filterString, uriInfo);
        } else {
            return getAllActivitiesWithFilterAndState(scenarioID, instanceID, filterString, state, uriInfo);
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
    private Response getAllActivitiesWithFilterAndState(
            int scenarioID, int instanceID, String filterString,
            String state, UriInfo uriInfo) {
        ExecutionService executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        Collection<ActivityInstance> instances;
        switch (state) {
            case "ready":
                instances = executionService.getEnabledActivities(instanceID);
                break;
            case "terminated":
                instances = executionService.getTerminatedActivities(instanceID);
                break;
            case "running":
                instances = executionService.getRunningActivities(instanceID);
                break;
            default:
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"error\":\"The state is not allowed " + state + "\"}")
                        .build();
        }
        Collection<ActivityInstance> selection = new LinkedList<>();
        for (ActivityInstance instance : instances) {
            if (instance.getLabel().contains(filterString)) {
                selection.add(instance);
            }
        }
        JSONObject result = buildJSONObjectForActivities(selection, state, uriInfo);
        return Response
                .ok(result.toString(), MediaType.APPLICATION_JSON)
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
    private Response getAllActivitiesOfInstanceWithFilter(
            int scenarioID, int instanceID, String filterString, UriInfo uriInfo) {
        ExecutionService executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        Map<String, Collection<ActivityInstance>> instances = new HashMap<>();
        instances.put("ready", executionService.getEnabledActivities(instanceID));
        instances.put("running", executionService.getRunningActivities(instanceID));
        instances.put("terminated", executionService.getTerminatedActivities(instanceID));
        JSONArray ids = new JSONArray();
        JSONObject activities = new JSONObject();
        for (Map.Entry<String, Collection<ActivityInstance>> entry : instances.entrySet()) {
            for (ActivityInstance instance : entry.getValue()) {
                if (instance.getLabel().contains(filterString)) {
                    ids.put(instance.getControlNodeInstance_id());
                    JSONObject activityJSON = new JSONObject();
                    activityJSON.put("id", instance.getControlNodeInstance_id());
                    activityJSON.put("label", instance.getLabel());
                    activityJSON.put("state", entry.getKey());
                    activityJSON.put("link", uriInfo.getAbsolutePath() + "/" +
                            instance.getControlNodeInstance_id());
                    activities.put("" + instance.getControlNodeInstance_id(), activityJSON);
                }
            }
        }
        JSONObject result = new JSONObject();
        result.put("ids", ids);
        result.put("activities", activities);
        return Response
                .ok(result.toString(), MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * This method creates a Response object for all specified activities.
     * The activities are specified by an scenario instance and a state.
     * In addition UriInfo object is needed in order to create the links
     * to the activity instances.
     *
     * @param scenarioID The ID of the scenario (model).
     * @param instanceID The ID of the scenario instance.
     * @param state      A String identifying the state.
     * @param uriInfo    A UriInfo object, which holds the server context.
     * @return A Response object, which is either a 404 if the state is invalid,
     * or a 200 if with json content.
     */
    private Response getAllActivitiesOfInstanceWithState(
            int scenarioID, int instanceID, String state, UriInfo uriInfo) {
        ExecutionService executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        Collection<ActivityInstance> instances;
        switch (state) {
            case "ready":
                instances = executionService.getEnabledActivities(instanceID);
                break;
            case "terminated":
                instances = executionService.getTerminatedActivities(instanceID);
                break;
            case "running":
                instances = executionService.getRunningActivities(instanceID);
                break;
            default:
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"error\":\"The state is not allowed " + state + "\"}")
                        .build();
        }
        JSONObject result = buildJSONObjectForActivities(instances, state, uriInfo);
        return Response
                .ok(result.toString(), MediaType.APPLICATION_JSON)
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
    private JSONObject buildJSONObjectForActivities(
            Collection<ActivityInstance> instances, String state, UriInfo uriInfo) {
        List<Integer> ids = new ArrayList<>(instances.size());
        JSONArray activities = new JSONArray();
        for (ActivityInstance instance : instances) {
            JSONObject activityJSON = new JSONObject();
            ids.add(instance.getControlNodeInstance_id());
            activityJSON.put("id", instance.getControlNodeInstance_id());
            activityJSON.put("label", instance.getLabel());
            activityJSON.put("state", state);
            activityJSON.put("link", uriInfo.getAbsolutePath() + "/" +
                    instance.getControlNodeInstance_id());
            activities.put(activityJSON);
        }
        JSONObject result = new JSONObject();
        result.put("ids", new JSONArray(ids));
        result.put("activities", activities);
        return result;
    }

    /**
     * @param instances The Map containing information about the activity instances.
     *                  We Assume that the key is a the id and the value is a Map
     *                  from String to Object with the properties of the instance.
     * @param uriInfo   Specifies the context. For example the uri
     *                  of the request.
     * @return
     */
    private JSONObject buildJSONObjectForReferencedActivities(
            Collection<ActivityInstance> instances, UriInfo uriInfo) {
        List<Integer> ids = new ArrayList<>(instances.size());
        JSONArray activities = new JSONArray();
        for (ActivityInstance instance : instances) {
            JSONObject activityJSON = new JSONObject();
            ids.add(instance.getControlNodeInstance_id());
            activityJSON.put("id", instance.getControlNodeInstance_id());
            activityJSON.put("label", instance.getLabel());
            activityJSON.put("link", uriInfo.getAbsolutePath() + "/" +
                    instance.getControlNodeInstance_id());
            activities.put(activityJSON);
        }
        JSONObject result = new JSONObject();
        result.put("ids", new JSONArray(ids));
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
    private Response getAllActivitiesOfInstance(
            int scenarioID, int instanceID, UriInfo uriInfo) {
        ExecutionService executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        Map<String, Collection<ActivityInstance>> instances = new HashMap<>();
        instances.put("ready", executionService.getEnabledActivities(instanceID));
        instances.put("running", executionService.getRunningActivities(instanceID));
        instances.put("terminated", executionService.getTerminatedActivities(instanceID));
        JSONArray ids = new JSONArray();
        JSONObject activities = new JSONObject();
        for (Map.Entry<String, Collection<ActivityInstance>> entry : instances.entrySet()) {
            for (ActivityInstance instance : entry.getValue()) {
                ids.put(instance.getControlNodeInstance_id());
                JSONObject activityJSON = new JSONObject();
                activityJSON.put("id", instance.getControlNodeInstance_id());
                activityJSON.put("label", instance.getLabel());
                activityJSON.put("state", entry.getKey());
                activityJSON.put("link", uriInfo.getAbsolutePath() + "/" +
                        instance.getControlNodeInstance_id());
                activities.put("" + instance.getControlNodeInstance_id(), activityJSON);
            }
        }
        JSONObject result = new JSONObject();
        result.put("ids", ids);
        result.put("activities", activities);
        return Response
                .ok(result.toString(), MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * This method is used to get all the information for an activity.
     * This means the label, id and a link for the input-/outputSets.
     *
     * @param uriInfo            A UriInfo object, which holds the server context.
     * @param scenarioID         The databaseID of a scenario.
     * @param scenarioInstanceID The databaseID of a scenarioInstance.
     * @param activityID         The databaseID of an activityInstance.
     * @return a response Object with the status code:
     * 200 if everything was correct and holds the information about the activityInstance.
     * A 404 Not Found is returned if the scenario/scenarioInstance/activityInstanceID is wrong.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity/{activityID}")
    public Response getActivity(@Context UriInfo uriInfo,
                                @PathParam("scenarioID") int scenarioID,
                                @PathParam("instanceID") int scenarioInstanceID,
                                @PathParam("activityID") int activityID) {

        ExecutionService executionService = new ExecutionService();
        if (!executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID)) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such scenario instance.\"}").build();
        }
        if (!executionService.testActivityInstanceExists(activityID)) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such activity instance.\"}").build();
        }
        ActivityJaxBean activity = new ActivityJaxBean();
        activity.id = activityID;
        LinkedList<ControlNodeInstance> controlNodeInstances = executionService.getScenarioInstance(scenarioInstanceID).getControlNodeInstances();
        for (ControlNodeInstance controlNodeInstance : controlNodeInstances) {
            if (controlNodeInstance.getControlNodeInstance_id() == activityID) {
                activity.label = executionService.getLabelForControlNodeID(controlNodeInstance.getControlNode_id());
            }
        }
        activity.inputSetLink = uriInfo.getAbsolutePath() + "/input";
        activity.outputSetLink = uriInfo.getAbsolutePath() + "/output";
        return Response.ok(activity, MediaType.APPLICATION_JSON).build();

    }

    /**
     * This method implements the REST call for retrieving reference information for a specific activity
     *
     * @param uriInfo            A UriInfo object, which holds the server context.
     * @param scenarioID         The databaseID of a scenario.
     * @param scenarioInstanceID The databaseID of a scenarioInstance.
     * @param activityID         The databaseID of an activityInstance.
     * @return a json object containing the referenced activities
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity/{activityID}/references")
    public Response getReferencesForActivity(@Context UriInfo uriInfo,
                                             @PathParam("scenarioID") int scenarioID,
                                             @PathParam("instanceID") int scenarioInstanceID,
                                             @PathParam("activityID") int activityID) {
        ExecutionService executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID);

        Collection<ActivityInstance> referencedActivities = executionService.getReferentialEnabledActivities(scenarioInstanceID, activityID);

        JSONObject result = buildJSONObjectForReferencedActivities(referencedActivities, uriInfo);
        return Response
                .ok(result.toString(), MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * This method responds to a GET request by returning an array of inputSets.
     * Each contains the inputSetDatabaseID, the name of the dataObject and their state as a Map &
     * a link to get the dataObjectInstances with their dataAttributesInstances.
     * The result is determined by:
     *
     * @param uriInfo            A UriInfo object, which holds the server context used for the link.
     * @param scenarioID         The databaseID of the scenario.
     * @param scenarioInstanceID The databaseID of the scenarioInstance belonging to the aforementioned scenario.
     * @param activityID         The databaseID of the activityInstance belonging to this scenarioInstance.
     * @return a response consisting of:
     * array of inputSets containing the inputSetDatabaseID, the name of the dataObject and their state as a Map &
     * a link to get the dataObjectInstances with their dataAttributesInstances.
     * a response status code:
     * <p/>
     * A 200 if everything was correct.
     * A 404 Not Found is returned if the scenario/scenarioInstance/activityInstance is non-existing or
     * if the activity has no inputSet & with an error message instead of the array.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity/{activityID}/input")
    public Response getInputDataObjects(@Context UriInfo uriInfo,
                                        @PathParam("scenarioID") int scenarioID,
                                        @PathParam("instanceID") int scenarioInstanceID,
                                        @PathParam("activityID") int activityID) {

        ExecutionService executionService = new ExecutionService();
        if (!executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID)) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such scenario instance.\"}").build();
        }
        if (!executionService.testActivityInstanceExists(activityID)) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such activity instance.\"}").build();
        }
        if (executionService.getInputSetsForActivityInstance(activityID) == null || executionService.getInputSetsForActivityInstance(activityID).size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no inputSet for this activity instance.\"}").build();
        }
        Map<Integer, Map<String, String>> inputSetMap = executionService.getInputSetsForActivityInstance(activityID);
        int j = 0;
        DataObjectSetsJaxBean[] inputSets = new DataObjectSetsJaxBean[inputSetMap.keySet().size()];
        for (Integer i : inputSetMap.keySet()) {
            DataObjectSetsJaxBean inputSet = new DataObjectSetsJaxBean();
            inputSet.id = i;
            inputSet.dataObjects = inputSetMap.get(i);
            String[] path = uriInfo.getAbsolutePath().toString().split("/");
            inputSet.linkDataObject = "";
            for (int k = 0; k < path.length - 3; k++) {
                inputSet.linkDataObject += path[k] + "/";
            }
            inputSet.linkDataObject += "inputset/" + inputSet.id;
            inputSets[j] = inputSet;
            j++;
        }
        return Response.ok(inputSets, MediaType.APPLICATION_JSON).build();
    }

    /**
     * This method responds to a GET request by returning an array of outputSets.
     * Each contains the outputSetDatabaseID, the name of the dataObject and their state as a Map &
     * a link to get the dataObjectInstances with their dataAttributesInstances.
     * The result is determined by:
     *
     * @param uriInfo            A UriInfo object, which holds the server context used for the link.
     * @param scenarioID         The databaseID of the scenario.
     * @param scenarioInstanceID The databaseID of the scenarioInstance belonging to the aforementioned scenario.
     * @param activityID         The databaseID of the activityInstance belonging to this scenarioInstance.
     * @return a response consisting of:
     * array of outputSets containing the outputSetDatabaseID, the name of the dataObject and their state as a Map &
     * a link to get the dataObjectInstances with their dataAttributesInstances.
     * a response status code:
     * <p/>
     * A 200 if everything was correct.
     * A 404 Not Found is returned if the scenario/scenarioInstance/activityInstance is non-existing or
     * if the activity has no outputSet & with an error message instead of the array.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity/{activityID}/output")
    public Response getOutputDataObjects(@Context UriInfo uriInfo,
                                         @PathParam("scenarioID") int scenarioID,
                                         @PathParam("instanceID") int scenarioInstanceID,
                                         @PathParam("activityID") int activityID) {

        ExecutionService executionService = new ExecutionService();
        if (!executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID)) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such scenario instance.\"}").build();
        }
        if (!executionService.testActivityInstanceExists(activityID)) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such activity instance.\"}").build();
        }
        if (executionService.getOutputSetsForActivityInstance(activityID) == null || executionService.getOutputSetsForActivityInstance(activityID).size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no outputSet for this activity instance.\"}").build();
        }
        Map<Integer, Map<String, String>> outputSetMap = executionService.getOutputSetsForActivityInstance(activityID);
        int j = 0;
        DataObjectSetsJaxBean[] outputSets = new DataObjectSetsJaxBean[outputSetMap.keySet().size()];
        for (Integer i : outputSetMap.keySet()) {
            DataObjectSetsJaxBean outputSet = new DataObjectSetsJaxBean();
            outputSet.id = i;
            outputSet.dataObjects = outputSetMap.get(i);
            String[] path = uriInfo.getAbsolutePath().toString().split("/");
            outputSet.linkDataObject = "";
            for (int k = 0; k < path.length - 3; k++) {
                outputSet.linkDataObject += path[k] + "/";
            }
            outputSet.linkDataObject += "outputset/" + outputSet.id;
            outputSets[j] = outputSet;
            j++;
        }
        return Response.ok(outputSets, MediaType.APPLICATION_JSON).build();
    }

    /**
     * This method responds to a GET request
     * by returning an array of dataObjectsInstances with their dataAttributeInstances belonging to an inputSet.
     * The outcome is specified by:
     *
     * @param scenarioID         This is the databaseID of the scenario.
     * @param scenarioInstanceID This is the databaseID of the scenarioInstance of the aforementioned scenario.
     * @param inputsetID         This is the databaseID of an inputSet belonging to this scenarioInstance.
     * @return a response consisting of:
     * an array of dataObjectsInstances with their dataAttributeInstances [also as an array].
     * a response status code:
     * A 200 if everything is correct.
     * A 404 Not Found is returned if the scenario/scenarioInstance/inputSetInstance is non-existing &
     * with an error message instead of the array.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/inputset/{inputsetID}")
    public Response getInputDataObjectsAndAttributes(@PathParam("scenarioID") int scenarioID,
                                                     @PathParam("instanceID") int scenarioInstanceID,
                                                     @PathParam("inputsetID") int inputsetID) {

        ExecutionService executionService = new ExecutionService();
        if (!executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID)) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such scenario instance.\"}").build();
        }
        if (executionService.getDataObjectInstancesForDataSetId(inputsetID, scenarioInstanceID) == null ||
                executionService.getDataObjectInstancesForDataSetId(inputsetID, scenarioInstanceID).length == 0) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such inputSet instance.\"}").build();
        }
        DataObjectInstance[] dataObjectInstances = executionService
                .getDataObjectInstancesForDataSetId(inputsetID, scenarioInstanceID);
        DataObjectJaxBean[] dataObjects = new DataObjectJaxBean[dataObjectInstances.length];
        for (int i = 0; i < dataObjectInstances.length; i++) {
            DataObjectJaxBean dataObject = new DataObjectJaxBean();
            dataObject.set_id = inputsetID;
            dataObject.id = dataObjectInstances[i].getDataObjectInstance_id();
            dataObject.label = dataObjectInstances[i].getName();
            dataObject.state = executionService.getStateNameForDataObjectInstanceInput(dataObjectInstances[i]);
            dataObject.attributeConfiguration = executionService.getDataAttributesForDataObjectInstance(dataObjectInstances[i]);
            dataObjects[i] = dataObject;
        }
        return Response.ok(dataObjects, MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * This method responds to a GET request
     * by returning an array of dataObjectsInstances with their dataAttributeInstances belonging to an outputSet.
     * The outcome is specified by:
     *
     * @param scenarioID         This is the databaseID of the scenario.
     * @param scenarioInstanceID This is the databaseID of the scenarioInstance of the aforementioned scenario.
     * @param outputsetID        This is the databaseID of an outputSet belonging to this scenarioInstance.
     * @return a response consisting of:
     * an array of dataObjectsInstances with their dataAttributeInstances also as an array.
     * a response status code:
     * A 200 if everything is correct.
     * A 404 Not Found is returned if the scenario/scenarioInstance/outputSetInstance is non-existing
     * with an error message instead of the array.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/outputset/{outputsetID}")
    public Response getOutputDataObjectsAndAttributes(@PathParam("scenarioID") int scenarioID,
                                                      @PathParam("instanceID") int scenarioInstanceID,
                                                      @PathParam("outputsetID") int outputsetID) {

        ExecutionService executionService = new ExecutionService();
        if (!executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID)) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such scenario instance.\"}").build();
        }
        if (executionService.getDataObjectInstancesForDataSetId(outputsetID, scenarioInstanceID) == null ||
                executionService.getDataObjectInstancesForDataSetId(outputsetID, scenarioInstanceID).length == 0) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such outputSet instance.\"}").build();
        }
        DataObjectInstance[] dataObjectInstances = executionService
                .getDataObjectInstancesForDataSetId(outputsetID, scenarioInstanceID);
        DataObjectJaxBean[] dataObjects = new DataObjectJaxBean[dataObjectInstances.length];
        for (int i = 0; i < dataObjectInstances.length; i++) {
            DataObjectJaxBean dataObject = new DataObjectJaxBean();
            dataObject.set_id = outputsetID;
            dataObject.id = dataObjectInstances[i].getDataObjectInstance_id();
            dataObject.label = dataObjectInstances[i].getName();
            dataObject.state = executionService.getStateNameForDataObjectInstanceOutput(dataObjectInstances[i], outputsetID);
            dataObject.attributeConfiguration = executionService.getDataAttributesForDataObjectInstance(dataObjectInstances[i]);
            dataObjects[i] = dataObject;
        }
        return Response.ok(dataObjects, MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * Updates the state of an activity instance.
     * The state will be changed to the specified one.
     * The activity Instance is specified by:
     *
     * @param scenarioID         The id of a scenario model.
     * @param scenarioInstanceID the id of an scenario instance.
     * @param activityID         the control node id of the activity.
     * @param state              the new state of the activity.
     * @return Returns a Response, the response code implies the
     * outcome of the PUT-Request.
     * A 202 (ACCEPTED) means that the POST was successful.
     * A 400 (BAD_REQUEST) if the transition was not allowed.
     */
    @POST
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity/{activityID}")
    public Response updateActivityState(@PathParam("scenarioID") int scenarioID,
                                        @PathParam("instanceID") int scenarioInstanceID,
                                        @PathParam("activityID") int activityID,
                                        @QueryParam("state") String state,
                                        @DefaultValue("-1") @QueryParam("outputset") int outputset) {

        boolean result;
        if (state == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The state is not set\"}")
                    .build();
        }
        ExecutionService executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID);
        switch (state) {
            case "begin":
                result = executionService.beginActivityInstance(scenarioInstanceID, activityID);
                break;
            case "terminate":
                if (outputset != -1) {
                    result = executionService.terminateActivityInstance(scenarioInstanceID, activityID, outputset);
                } else {
                    result = executionService.terminateActivityInstance(scenarioInstanceID, activityID);
                }
                break;
            default:
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"error\":\"The state transition " + state + " is unknown\"}")
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
                    .entity("{\"error\":\"impossible to " + (state.equals("begin") ? "start" : "terminate") +
                            " activity with id " + activityID + "\"}")
                    .build();
        }
    }

    /**
     * This method updates the data attributes of a specific activity defined via its activityID
     *
     * @param scenarioID         The id of a scenario model.
     * @param scenarioInstanceID the id of an scenario instance.
     * @param activityID         the control node id of the activity.
     * @return Status code with regard to its success / failure
     */
    @PUT
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity/{activityID}")
    public Response setDataAttribute(@PathParam("scenarioID") int scenarioID,
                                     @PathParam("instanceID") int scenarioInstanceID,
                                     @PathParam("activityID") int activityID,
                                     final DataAttributeUpdateJaxBean input) {
        ExecutionService executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID);

        Map<Integer, String> values = new HashMap<>();
        if (input != null) {
            values.put(input.id, input.value);
        }

        if (executionService.setDataAttributeValues(scenarioInstanceID, activityID, values)) {
            return Response.status(Response.Status.ACCEPTED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"attribute value was changed successfully.\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"error within the update of attributes\"}")
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
            @Context UriInfo uriInfo,
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
        JSONObject result = buildListForDataObjects(uriInfo, dataObjects, states, labels);
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
        dataObject.set_id = 0;
        dataObject.id = dataObjectID;
        dataObject.label = labels.get(new Integer(dataObjectID));
        dataObject.state = states.get(new Integer(dataObjectID));
        return Response.ok(dataObject, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Creates an array of DataObjects.
     * The data objects will be created out of the information received from the execution Service.
     * The array elements will be of type {@link DataObjectJaxBean), hence JSON and
     * XML can be generated automatically.
     *
     * @param uriInfo       A Context object of the server request
     * @param dataObjectIds an Arraqy of IDs used for the dataobjects inside the database.
     * @param states        The states, mapped from dataobject database id to state (String)
     * @param labels        The labels, mapped from dataobject database id to label (String)
     * @return A array with a DataObject for each entry in dataObjectIds
     */
    private JSONObject buildListForDataObjects(
            UriInfo uriInfo,
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
            dataObject.put("link", uriInfo.getAbsolutePath() + "/" + id);
            results.put("" + id, dataObject);
        }
        result.put("results", results);
        return result;
    }

    /**
     * Creates a JSON object from an HashMap.
     * The keys will be listed separately.
     *
     * @param data        The HashMap which contains the data of the Object
     * @param keyLabel    The name which will be used
     * @param resultLabel The label of the results.
     * @return The newly created JSON Object.
     */
    private JSONObject mapToKeysAndResults(Map data, String keyLabel, String resultLabel) {
        JSONObject result = new JSONObject();
        result.put(keyLabel, new JSONArray(data.keySet()));
        result.put(resultLabel, data);
        return result;
    }

    // ************************* RootElement ********************************************/

    /**
     * This is a data class for the email configuration.
     * It is used by Jersey to deserialize JSON.
     * Also it can be used for tests to provide the correct contents.
     * This class in particular is used by the POST for the email configuration.
     * See the {@link de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.rest.RestConfigurator#updateEmailConfiguration(int, de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.rest.RestConfigurator.EmailConfigJaxBean)}
     * updateEmailConfiguration} method for more information.
     */
    @XmlRootElement
    public static class EmailConfigJaxBean {
        /**
         * The receiver of the email.
         * coded as an valid email address (as String)
         */
        public String receiver;
        /**
         * The subject of the email.
         * Could be any String but null.
         */
        public String subject;
        /**
         * The content of the email.
         * Could be any String but null.
         */
        public String content;
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
     *
     */
    @XmlRootElement
    public static class ActivityJaxBean {
        /**
         *
         */
        public int id;
        /**
         *
         */
        public String label;
        /**
         *
         */
        public String outputSetLink;
        /**
         *
         */
        public String inputSetLink;
    }

    /**
     *
     */
    @XmlRootElement
    public static class DataObjectSetsJaxBean {
        /**
         *
         */
        public int id;
        /**
         *
         */
        public String linkDataObject;
        /**
         *
         */
        Map<String, String> dataObjects;

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
         * the database.
         */
        public int id;
        /**
         * The state inside the database of the dataobject
         * which is stored in the table.
         * The label not the id will be saved.
         */
        public String state;
        /**
         * An array of all dataAttributes belonging to this dataObject.
         * Each attribute has an id, name, type and value.
         */
        public DataAttributeJaxBean[] attributeConfiguration;
        /**
         *
         */
        public int set_id;
    }

    /**
     *
     */
    @XmlRootElement
    public static class DataAttributeJaxBean {
        /**
         *
         */
        public int id;
        /**
         *
         */
        public String name;
        /**
         *
         */
        public String type;
        /**
         *
         */
        public String value;

    }

    /**
     *
     */
    @XmlRootElement
    public static class DataAttributeUpdateJaxBean {
        /**
         *
         */
        public int id;
        /**
         *
         */
        public String value;

    }
}
