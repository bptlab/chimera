package de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbWebServiceTask;
import de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.Execution;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil;

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
    @PUT //would be PATCH if only selected fields are updated
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
     * Updates the email configuration for a specified task.
     * The Task is specified by the email Task ID and the new
     * configuration will submitted as a JSON-Object.
     *
     * @param emailTaskID The ControlNode id of the email task.
     * @param input       The new configuration.
     * @return A Response 202 (ACCEPTED) if the update was successful.
     * A 404 (NOT_FOUND) if the mail task could not be found.
     */
    @POST //TODO: twice to PUT? should we take out the POST ?
    @Path("emailtask/{emailtaskID}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmailConfiguration2(
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

    /**
     * This is a data class for the email configuration.
     * It is used by Jersey to deserialize JSON.
     * Also it can be used for tests to provide the correct contents.
     * This class in particular is used by the POST for the email configuration.
     * See the {@link #updateEmailConfiguration(int, EmailConfigJaxBean)}
     * updateEmailConfiguration} method for more information.
     */
    /*************************** WEB SERVICE TASKS **********************************/

    @GET
    @Path("scenario/{scenarioID}/webservice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllWebserviceTasks(
            @PathParam("scenarioID") int scenarioID,
            @QueryParam("filter") String filterString) {
        DbWebServiceTask dbWebServiceTask = new DbWebServiceTask();
        ArrayList<HashMap<String, Object>> webServiceTaskIDs = dbWebServiceTask.getAllWebServiceTaskAttributeFancy();
        DbScenario scenario = new DbScenario();
        if (!scenario.existScenario(scenarioID)) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"scenario ID is not existing\"}")
                    .build();
        }
        String jsonRepresentation = JsonUtil.JsonWrapperArrayListHashMap(webServiceTaskIDs);
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("scenario/{scenarioID}/webservice/{webserviceID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecificWebserviceTask(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("webserviceID") int webserviceID) {
        DbWebServiceTask webService = new DbWebServiceTask();
        //String link = webService.getLinkForControlNode(webserviceID);
        //String method = webService.getMethod(webserviceID);
        ArrayList<HashMap<String, Object>> attributes = webService.getSpecificWebServiceTaskAttributeFancy(webserviceID);
       // WebserviceConfigJaxBean webserviceConfigJaxBean = new WebserviceConfigJaxBean();
        //webserviceConfigJaxBean.link = link;
        //webserviceConfigJaxBean.method = method;
        //webserviceConfigJaxBean.attributes = attributes;
        //TODO: check if return value is empty
        String jsonRepresentation = JsonUtil.JsonWrapperArrayListHashMap(attributes);
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("webservice/{webserviceID}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateWebserviceConfiguration(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("webserviceID") int webserviceID,
            final RestConfigurator.WebserviceConfigJaxBean input) {
        //TODO: set Webservice details
        //DbEmailConfiguration dbEmailConfiguration = new DbEmailConfiguration();
        //int result = dbEmailConfiguration.setEmailConfiguration(webserviceID,
         //       input.test);
        int result = 1;
        return Response.status(
                result > 0 ? Response.Status.ACCEPTED : Response.Status.NOT_ACCEPTABLE)
                .build();
    }

    /*************************** HELPER **********************************/
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

        public String link;
        public String method;
        // Map <AttributeID, List<key> >
        public HashMap<Integer, List<String>> attributes;

    }
}
