package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.filters;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 *
 */
@Provider
@IdValidation
public class AuthorizationRequestFilter implements ContainerRequestFilter {
    private ContainerRequestContext requestContext;
    String errorMsg = "{\"error\":\"There is no %s with id %d\"}";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        this.requestContext = requestContext;
        validateScenarioExists();
        validateScenarioInstanceExists();
    }

    private void validateScenarioExists() {
        MultivaluedMap<String, String> map = requestContext.getUriInfo().getPathParameters();
        if (map.containsKey("scenarioId")) {
            int scenarioId = Integer.parseInt(map.getFirst("scenarioId"));
            this.abortWith(String.format(errorMsg, scenarioId));
        }
    }

    private void validateScenarioInstanceExists() {
        MultivaluedMap<String, String> map = requestContext.getUriInfo().getPathParameters();
        if (map.containsKey("scenarioInstanceId")) {
            int scenarioId = Integer.parseInt(map.getFirst("scenarioId"));
            int scenarioInstanceId = Integer.parseInt(map.getFirst("scenarioInstanceId"));
            ExecutionService executionService = ExecutionService.getInstance(scenarioId);
            if (!executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId)) {
                abortWith(String.format(errorMsg, "scenarioInstance", scenarioInstanceId));
            }
        }
    }

    private void abortWith(String errorMsg) {
        Response notFound = Response.status(Response.Status.NOT_FOUND).entity(errorMsg).build();
        this.requestContext.abortWith(notFound);

    }
}q