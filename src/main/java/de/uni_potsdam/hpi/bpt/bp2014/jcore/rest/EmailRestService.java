package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.EmailConfigJaxBean;
import de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
public class EmailRestService {
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
    @Produces(MediaType.APPLICATION_JSON) public Response getAllEmailTasks(
            @PathParam("scenarioID") int scenarioID,
            @QueryParam("filter") String filterString) {
        DbScenario scenario = new DbScenario();
        DbEmailConfiguration mail = new DbEmailConfiguration();
        if (!scenario.existScenario(scenarioID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        String jsonRepresentation = JsonUtil
                .jsonWrapperLinkedList(
                        mail.getAllEmailTasksForScenario(scenarioID));
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
    @Produces(MediaType.APPLICATION_JSON) public Response getEmailTaskConfiguration(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("emailTaskID") int mailTaskID) {
        DbScenario scenario = new DbScenario();
        DbEmailConfiguration mail = new DbEmailConfiguration();
        EmailConfigJaxBean mailConfig = new EmailConfigJaxBean();
        mailConfig.setReceiver(mail.getReceiverEmailAddress(mailTaskID));
        if (!scenario.existScenario(scenarioID) || mailConfig.getReceiver().equals("")) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        mailConfig.setContent(mail.getMessage(mailTaskID));
        mailConfig.setSubject(mail.getSubject(mailTaskID));
        return Response.ok(mailConfig, MediaType.APPLICATION_JSON).build();
    }

}
