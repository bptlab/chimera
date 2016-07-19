package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.jcore.ExecutionService;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
public class AbstractRestService {
    public Response buildNotFoundResponse(String errorMsg) {
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMsg)
                .build();
    }

    public Response buildBadRequestResponse(String errorMsg) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMsg)
                .build();
    }

    public Response buildAcceptedResponse(String errorMsg) {
        return Response.status(Response.Status.ACCEPTED)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMsg)
                .build();
    }

    public boolean isScenarioAndInstanceValid(int scenarioId, int scenarioInstanceId) {
        ExecutionService executionService = ExecutionService.getInstance(scenarioId);
        return executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId);
    }
}
