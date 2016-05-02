package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.filters;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.HttpMethod;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 *
 */
@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {
    private ContainerRequestContext requestContext;
    String errorMsg = "{\"error\":\"There is no %s with id %d\"}";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        this.requestContext = requestContext;
        if (!isValidScenario()) {
            abortIllegalScenario(requestContext.getMethod());
        } else if (!isValidInstance()) {
            abortInvalidInstance(requestContext.getMethod());
        }
    }

    private void abortIllegalScenario(String method) {
        int scenarioId = Integer.parseInt(requestContext.getUriInfo()
                .getPathParameters().getFirst("scenarioId"));
        if (method.equals(HttpMethod.GET.toString())) {
            this.abortWithNotFound(String.format(errorMsg, "scenario", scenarioId));
        } else {
            this.abortBadRequest(String.format(errorMsg, "scenario", scenarioId));
        }

    }

    private boolean isValidScenario() {
        MultivaluedMap<String, String> map = requestContext.getUriInfo().getPathParameters();
        if (!map.containsKey("scenarioId")) {
            return true;
        }
        int scenarioId = Integer.parseInt(map.getFirst("scenarioId"));
        return new DbScenario().existScenario(scenarioId);
    }

    private boolean isValidInstance() {
        MultivaluedMap<String, String> map = requestContext.getUriInfo().getPathParameters();
        if (!map.containsKey("instanceId")) {
            return true;
        }

        int scenarioId = Integer.parseInt(map.getFirst("scenarioId"));
        int scenarioInstanceId = Integer.parseInt(map.getFirst("instanceId"));
        return new DbScenarioInstance().existScenario(scenarioId, scenarioInstanceId);
    }

    private void abortInvalidInstance(String method) {
        int scenarioInstanceId = Integer.parseInt(requestContext.getUriInfo()
                .getPathParameters().getFirst("instanceId"));

        if (method.equals(HttpMethod.GET.toString())) {
           abortWithNotFound(String.format(errorMsg, "scenario instance", scenarioInstanceId));
        } else {
           abortBadRequest(String.format(errorMsg, "scenario instance", scenarioInstanceId));
        }
    }

    private void abortWithNotFound(String errorMsg) {
        Response notFound = Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMsg).build();
        this.requestContext.abortWith(notFound);
    }

    private void abortBadRequest(String errorMsg) {
        Response badRequest = Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMsg).build();
        this.requestContext.abortWith(badRequest);
    }
}