package de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbWebServiceTask;
import de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.Execution;
import de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
@Path("config/v2")
public class RestConfigurator {

    /**
     * Deletes a scenario with all its instances.
     * internally realizes via a flag.
     *
     * @param scenarioID The ID of the scenario which is supposed to be deleted
     * @return           The status code if the operation was successful or not
     * @throws Exception
     */
    @DELETE
    @Path("scenario/{scenarioID}/")
    public Response deleteScenario(@PathParam("scenarioID") Integer scenarioID) throws Exception {

        boolean result;
        Execution execution = new Execution();
        result = execution.deleteScenario(scenarioID);

        if (result) {
            return Response.status(Response.Status.ACCEPTED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"scenario deletion successfully.\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"scenario deletion failed\"}")
                    .build();
        }
    }

    /*************************** EMAIL SERVICE TASKS **********************************/

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
    @PUT
    @Path("emailtask/{emailtaskID}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmailConfiguration(
            @PathParam("emailtaskID") int emailTaskID,
            final RestConfigurator.EmailConfigJaxBean input) {
        DbEmailConfiguration dbEmailConfiguration = new DbEmailConfiguration();
        int result = dbEmailConfiguration.setEmailConfiguration(emailTaskID,
                input.receiver, input.subject, input.message);
        return Response.status(
                result > 0 ? Response.Status.ACCEPTED : Response.Status.NOT_ACCEPTABLE)
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
        mailConfig.message = mail.getMessage(mailTaskID);
        mailConfig.subject = mail.getSubject(mailTaskID);
        return Response.ok(mailConfig, MediaType.APPLICATION_JSON).build();
    }


    /*************************** WEB SERVICE TASKS **********************************/

    /**
     *
     * @param scenarioID The ID of the scenario model.
     * @param filterString A Filter String, only web service tasks with a label containing
     *                     this filter String will be returned.
     * @return
     */
    @GET
    @Path("scenario/{scenarioID}/webservice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllWebserviceTasks(
            @PathParam("scenarioID") int scenarioID,
            @QueryParam("filter") String filterString) {
        DbScenario scenario = new DbScenario();
        if (!scenario.existScenario(scenarioID)) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"scenario ID is not existing\"}")
                    .build();
        }
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        LinkedList<Integer> webServiceTaskIDs = dbWebServiceTask.getWebServiceTasks(scenarioID);
        String jsonRepresentation = JsonUtil.JsonWrapperLinkedList(webServiceTaskIDs);
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    /**
     *
     * @param scenarioID The ID of the scenario model.
     * @param webserviceID The ID of the webservice tasks
     * @return
     */
    @GET
    @Path("scenario/{scenarioID}/webservice/{webserviceID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecificWebserviceTask(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("webserviceID") int webserviceID) {
        DbWebServiceTask webService = new DbWebServiceTask();
        ArrayList<HashMap<String, Object>> list = webService.getComplexAttributeMap(webserviceID);

        HashMap response = new HashMap();
        response.put("attributes", list);
        response.put("method", webService.getMethod(webserviceID));
        response.put("link", webService.getLinkForControlNode(webserviceID));

        String jsonResponse = JsonUtil.JsonWrapperHashMapOnly(response);
        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }

    /**
     *
     * @param scenarioID The ID of the scenario model.
     * @param webserviceID The ID of the webservice tasks
     * @param input       The new configuration.
     * @return
     */
    @PUT
    @Path("scenario/{scenarioID}/webservice/{webserviceID}/link")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWebserviceLink(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("webserviceID") int webserviceID,
            final String input) {
        //input: {link, method}
        JSONObject jsonObject = new JSONObject(input);
        if (!jsonObject.has("method") && !jsonObject.has("link")){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        String link = jsonObject.get("link").toString();
        String method = jsonObject.get("method").toString();
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        if(dbWebServiceTask.existWebServiceTaskIDinLink(webserviceID)){
            dbWebServiceTask.updateWebServiceTaskLink(webserviceID, link, method);
        } else {
            dbWebServiceTask.insertWebServiceTaskLinkIntoDatabase(webserviceID,link, method);
        }
        return Response.status(
                Response.Status.ACCEPTED)
                .build();
    }

    @PUT
    @Path("scenario/{scenarioID}/webservice/{webserviceID}/attribute")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateWebserviceAttribute(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("webserviceID") int webserviceID,
            final String input) {
        //input: {attributeID, value:[order, key]}
        JSONObject jsonObject = new JSONObject(input);
        if (!jsonObject.has("attributeID") || !jsonObject.has("value")){
            return Response.status(
                    Response.Status.NOT_ACCEPTABLE)
                    .build();
        }
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        int attributeID = jsonObject.getInt("attributeID");
        JSONArray values = jsonObject.getJSONArray("value");
        for (int i = 0; i < values.length(); i++) {
            JSONObject entry = values.getJSONObject(i);
            int order = entry.getInt("order");
            String key = entry.getString("key");
            dbWebServiceTask.insertWebServiceTaskAttributeIntoDatabase(order, webserviceID, attributeID, key);
        }
        return Response.status(
                Response.Status.ACCEPTED)
                .build();
    }

    /**
     *
     * @param scenarioID The ID of the scenario model.
     * @param webserviceID The ID of the webservice tasks
     * @return
     */
    @GET
    @Path("scenario/{scenarioID}/webservice/{webserviceID}/post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPOSTForWebserviceTask(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("webserviceID") int webserviceID) {
        DbScenario scenario = new DbScenario();
        if (!scenario.existScenario(scenarioID)) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        String jsonRepresentation = JsonUtil.JsonWrapperString(dbWebServiceTask.getPOST(webserviceID));
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    /**
     *
     * @param scenarioID The ID of the scenario model.
     * @param webserviceID The ID of the webservice tasks
     * @return
     */
    @PUT
    @Path("scenario/{scenarioID}/webservice/{webserviceID}/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateWebservicePost(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("webserviceID") int webserviceID,
            final String input) {
        DbScenario scenario = new DbScenario();
        if (!scenario.existScenario(scenarioID)) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        JSONObject jsonObject = new JSONObject(input);
        if (!jsonObject.has("value")){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        String value = jsonObject.get("value").toString();
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        if(dbWebServiceTask.existWebServiceTaskIDinPost(webserviceID)){
            dbWebServiceTask.updateWebServiceTaskPOST(webserviceID, value);
        } else {
            dbWebServiceTask.insertWebServiceTaskPOSTIntoDatabase(webserviceID, value);
        }
        return Response.status(
                Response.Status.ACCEPTED)
                .build();
    }

    /*************************** HELPER **********************************/

    /**
     * This is a data class for the email configuration.
     * It is used by Jersey to deserialize JSON.
     * Also it can be used for tests to provide the correct contents.
     * This class in particular is used by the POST for the email configuration.
     * See the {@link #updateEmailConfiguration(int, EmailConfigJaxBean)}
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
        public String message;
    }

    @XmlRootElement
    public static class WebserviceConfigJaxBean {
        /**
         *
         */
        public String link;
        /**
         *
         */
        public String method;
        /**
         *
         */
        public String attributeDetails;
        /**
         *
         */
        public ArrayList<HashMap<String, Object>> attributes;

    }
}
