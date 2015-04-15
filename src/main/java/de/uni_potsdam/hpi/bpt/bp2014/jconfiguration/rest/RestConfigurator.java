package de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.rest;

import de.uni_potsdam.hpi.bpt.bp2014.jconfiguration.Execution;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbEmailConfiguration;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
            final RestInterface.EmailConfigJaxBean input) {
        DbEmailConfiguration dbEmailConfiguration = new DbEmailConfiguration();
        int result = dbEmailConfiguration.setEmailConfiguration(emailTaskID,
                input.receiver, input.subject, input.content);
        return Response.status(
                result > 0 ? Response.Status.ACCEPTED : Response.Status.NOT_ACCEPTABLE)
                .build();
    }

    //TODO: delete scenario
    @DELETE
    @Path("scenario/{scenarioID}/")
    public Response updateActivityState(@PathParam("scenarioID") Integer scenarioID) throws Exception {

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
}
