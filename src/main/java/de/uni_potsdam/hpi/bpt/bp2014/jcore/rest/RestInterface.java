package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

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
@Path("v2/")
public class RestInterface {


    /**
     * Updates the email configuration for a specified task.
     * The Task is specified by the email Task ID and the new
     * configuration will submitted as a JSON-Object.
     *
     * @param emailTaskID The ControlNode id of the email task.
     * @param input       The new configuration.
     * @return A Response 202 (ACCEPTED) if the update was successful.
     * A 404 (NOT_FOUND) if the mail task could not be found.
     */
    @POST
    @Path("config/emailtask/{emailtaskID}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmailConfiguration(
            @PathParam("emailtaskID") int emailTaskID,
            final EmailConfigJaxBean input) {
        DbEmailConfiguration dbEmailConfiguration = new DbEmailConfiguration();
        int result = dbEmailConfiguration.setEmailConfiguration(emailTaskID,
                input.receiver, input.subject, input.content);
        return Response.status(
                result > 0 ? Response.Status.ACCEPTED : Response.Status.NOT_ACCEPTABLE)
                .build();
    }

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
    //TODO: Write a POST to change the name of an scenario

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
     * @param orderBy      Specifies the order of the result, per default
     *                     they will be sorted by id, it could also be the
     *                     name.
     * @return A JSON-Object with an array of information about all instances of
     * one specified scenario. The information contains the id and name.
     */
    @GET
    @Path("scenario/{scenarioID}/instance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenarioInstances(
            @PathParam("scenarioID") int scenarioID,
            @QueryParam("filter") @DefaultValue("") String filterString,
            @QueryParam("order") @DefaultValue("id") String orderBy) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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
    @POST
    @Path("sceanrio/{scenarioID}/instance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startNewInstance(@PathParam("scenarioID") int scenarioID) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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
    @Path("sceanrio/{scenarioID}/instance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startNewNamedInstance(@PathParam("scenarioID") int scenarioID, NamedJaxBean name) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

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
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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
     * @param orderBy      Defines an attribute which will be used to order
     *                     the results, the default will be the id
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
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID,
            @QueryParam("filter") @DefaultValue("") String filterString,
            @QueryParam("order") @DefaultValue("id") String orderBy) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    /**
     * This method provides detailed information about one activity Instance.
     * The Response will contain a JSON-Object with all necessary Information.
     * This Information include the instance id, the label, type and state.
     * An Activity instance is defined by:
     *
     * @param scenarioID The ID of a scenario model.
     * @param instanceID The ID of a scenario instance.
     * @param activityID The ID of the control node, which represents the activity.
     * @return A Response Object, if the defined activity does exist a 200 (OK) with
     * JSON-Object as content will be returned.
     * Else a 404 (NOT_FOUND) will be returned, with a more detailed Error-
     * Message.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activity/{activityID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivityInstance(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID,
            @PathParam("activityID") int activityID) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    /**
     * Updates the state of an activity instance.
     * The state will be changed to the specified one.
     * The activity Instance is specified by:
     *
     * @param scenarioID         The id of a scenario model.
     * @param scenarioInstanceID the id of an scenario instance.
     * @param activityID         the control node id of the activity.
     * @param status             the new status of the activity.
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
                                         @QueryParam("status") String status) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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
     * @param orderBy      The results will be ordered, the default is the id,
     *                     you may change it to label.
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
            @QueryParam("filter") @DefaultValue("") String filterString,
            @QueryParam("order") @DefaultValue("id") String orderBy) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    /*
     * Helper
     */

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
     * This is a data class for the email configuration.
     * It is used by Jersey to deserialize JSON.
     * Also it can be used for tests to provide the correct contents.
     * This class in particular is used by the POST for the email configuration.
     * See the {@link #updateEmailConfiguration(int,
     * de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface.EmailConfigJaxBean)}
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
    private class NamedJaxBean {
        /**
         * The name which should be assigned to the entity.
         */
        public String name;
    }
}
