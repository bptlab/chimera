package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.filters;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNode;
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
        } else if (!isValidActivity()) {
            abortIllegalActivity(requestContext.getMethod());
        } else if (!isValidActivityInstance()) {
            abortIllegalActivityInstance(requestContext.getMethod());
        }
    }

    private void abortIllegalActivity(String method) {
        int activityId = Integer.parseInt(requestContext.getUriInfo()
                .getPathParameters().getFirst("activityId"));
        if (method.equals(HttpMethod.GET.toString())) {
            this.abortWithNotFound(String.format(errorMsg, "activity", activityId));
        } else {
            this.abortBadRequest(String.format(errorMsg, "activity", activityId));
        }
    }

    private void abortIllegalActivityInstance(String method) {
        int activityInstanceId = Integer.parseInt(requestContext.getUriInfo()
                .getPathParameters().getFirst("activityInstanceId"));
        if (method.equals(HttpMethod.GET.toString())) {
            this.abortWithNotFound(String.format(errorMsg, "activity instance", activityInstanceId));
        } else {
            this.abortBadRequest(String.format(errorMsg, "activity instance", activityInstanceId));
        }
    }

    private boolean isValidActivityInstance() {
        MultivaluedMap<String, String> map = requestContext.getUriInfo().getPathParameters();
        if (!map.containsKey("activityInstanceId")) {
            return true;
        }
        assert map.containsKey("instanceId") && map.containsKey("scenarioId"):
                "Activity Id is only defined in context of a scenario instance";
        int instanceId = Integer.parseInt(map.getFirst("instanceId"));
        int activityInstanceId = Integer.parseInt(map.getFirst("activityInstanceId"));

        return new DbActivityInstance().existsActivityInstance(
                activityInstanceId, instanceId);
    }

    private boolean isValidActivity() {
        MultivaluedMap<String, String> map = requestContext.getUriInfo().getPathParameters();
        if (!map.containsKey("activityId")) {
            return true;
        }
        assert map.containsKey("instanceId") && map.containsKey("scenarioId"):
                "Activity Id is only defined in context of a scenario instance";
        int scenarioId = Integer.parseInt(map.getFirst("scenarioId"));
        int activityId = Integer.parseInt(map.getFirst("activityId"));

        return new DbControlNode().existControlNode(activityId, scenarioId);
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
        int instanceId = Integer.parseInt(map.getFirst("instanceId"));
        return new DbScenarioInstance().existScenario(scenarioId, instanceId);
    }

    private void abortInvalidInstance(String method) {
        int instanceId = Integer.parseInt(requestContext.getUriInfo()
                .getPathParameters().getFirst("instanceId"));

        if (method.equals(HttpMethod.GET.toString())) {
           abortWithNotFound(String.format(errorMsg, "scenario instance", instanceId));
        } else {
           abortBadRequest(String.format(errorMsg, "scenario instance", instanceId));
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