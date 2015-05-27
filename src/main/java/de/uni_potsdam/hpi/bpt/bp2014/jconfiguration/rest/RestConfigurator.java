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
@Path("config/v2")
public class RestConfigurator {

    /**
     * Deletes a scenario with all its instances.
     * internally realizes via a flag.
     *
     * @param scenarioID The ID of the scenario which is supposed to be deleted
     * @return The status code if the operation was successful or not
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

    // ************************** EMAIL SERVICE TASKS **********************************/

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


    // ************************** WEB SERVICE TASKS **********************************/

    /**
     * Get a list of all webservices for a specific scenario
     *
     * @param scenarioID   The ID of the scenario model.
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
     * Get all details for a specific webservice ID
     *
     * @param scenarioID   The ID of the scenario model.
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
        Map<Integer, String> attributes = webService.getOutputAttributesForWebservice(webserviceID);

        HashMap response = new HashMap();
        response.put("attributes", list);
        response.put("method", webService.getMethod(webserviceID));
        response.put("link", webService.getLinkForControlNode(webserviceID));
        response.put("body", webService.getPOST(webserviceID));
        response.put("allAttributes", attributes);

        String jsonResponse = JsonUtil.JsonWrapperHashMapOnly(response);
        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }


    /**
     * Update details for a specific webserviceID
     *
     * @param scenarioID   The ID of the scenario model.
     * @param webserviceID The ID of the webservice tasks
     * @param input        The new webservice task configuration
     * @return
     */
    @PUT
    @Path("webservice/{webserviceID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWebservice(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("webserviceID") int webserviceID,
            final String input) {
        //input: {method, link, attributeID, value:[{order,key}], body}
        JSONObject jsonObject = new JSONObject(input);
        boolean con1 = setWebServiceTaskAttributes(jsonObject, webserviceID);
        boolean con2 = setWebServiceTaskLink(jsonObject, webserviceID);
        boolean con3 = setWebServiceTaskPostBody(jsonObject, webserviceID);
        if (con1 || con2 || con3) {
            return Response.status(
                    Response.Status.ACCEPTED)
                    .build();
        } else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
    }

    /**
     *
     * @param jsonObject a jsonObject..
     * @param webserviceID The ID of the webservice tasks
     * @return
     */
    private boolean setWebServiceTaskAttributes(JSONObject jsonObject, int webserviceID) {
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        if (jsonObject.has("attributes")) {
            JSONArray jsonArray = jsonObject.getJSONArray("attributes");
            if (jsonArray.length() > 0) {
                HashSet<Integer> ids = new HashSet<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);
                    ids.add(o.getInt("dataattribute_id"));
                }
                for (int id : ids) {
                    dbWebServiceTask.deleteWebServiceTaskAtribute(webserviceID, id);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject entry = jsonArray.getJSONObject(i);
                    int attributeID = entry.getInt("dataattribute_id");
                    int order = entry.getInt("order");
                    String key = entry.getString("key");
                    dbWebServiceTask.insertWebServiceTaskAttributeIntoDatabase(order, webserviceID, attributeID, key);
                }
            } else {
                dbWebServiceTask.deleteAllAttributes(webserviceID);
            }
            return true;
        }
        return false;
    }

    /**
     *
     */
    private boolean setWebServiceTaskLink(JSONObject jsonObject, int webserviceID) {
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        boolean back = false;
        if (jsonObject.has("link")) {
            String link = jsonObject.get("link").toString();
            if (!link.isEmpty()) {
                if (dbWebServiceTask.existWebServiceTaskIDinLink(webserviceID)) {
                    dbWebServiceTask.updateWebServiceTaskLink(webserviceID, link);
                } else {
                    dbWebServiceTask.insertWebServiceTaskLinkIntoDatabase(webserviceID, link, "");
                }
                back = true;
            }
        }
        if (jsonObject.has("method")) {
            String method = jsonObject.get("method").toString();
            if (!method.isEmpty()) {
                if (dbWebServiceTask.existWebServiceTaskIDinLink(webserviceID)) {
                    dbWebServiceTask.updateWebServiceTaskMethod(webserviceID, method);
                } else {
                    dbWebServiceTask.insertWebServiceTaskLinkIntoDatabase(webserviceID, "", method);
                }
                back = true;
            }
        }
        return back;
    }

    /**
     *
     */
    private boolean setWebServiceTaskPostBody(JSONObject jsonObject, int webserviceID) {
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        if (jsonObject.has("body")) {
            String value = jsonObject.get("body").toString();
            if (!value.isEmpty()) {
                if (dbWebServiceTask.existWebServiceTaskIDinPost(webserviceID)) {
                    dbWebServiceTask.updateWebServiceTaskPOST(webserviceID, value);
                } else {
                    dbWebServiceTask.insertWebServiceTaskPOSTIntoDatabase(webserviceID, value);
                }
                return true;
            }
        }
        return false;
    }

    // ************************** HELPER **********************************/


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
        public ArrayList<HashMap<String, Object>> attributes;

    }
}
