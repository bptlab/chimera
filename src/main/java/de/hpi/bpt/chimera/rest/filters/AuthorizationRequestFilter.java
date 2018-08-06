package de.hpi.bpt.chimera.rest.filters;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.usermanagment.User;
import de.hpi.bpt.chimera.usermanagment.UserManager;

import java.io.IOException;

/**
 *
 */
@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {
	private static Logger log = Logger.getLogger(AuthorizationRequestFilter.class);
	String errorMsg = "{\"error\":\"There is no %s with id %d\"}";
	private ContainerRequestContext requestContext;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		this.requestContext = requestContext;

		try {
			// TODO activate when frontend sends authorization request filter
			// validateUser();
		} catch (Exception e) {
			throw e;
		}

		// if (!isValidScenario()) {
		// abortIllegalScenario(requestContext.getMethod());
		// } else if (!isValidInstance()) {
		// abortInvalidInstance(requestContext.getMethod());
		// } else if (!isValidActivity()) {
		// abortIllegalActivity(requestContext.getMethod());
		// } else if (!isValidActivityInstance()) {
		// abortIllegalActivityInstance(requestContext.getMethod());
		// }
	}
	/*
	private void abortIllegalActivity(String method) {
		int activityId = Integer.parseInt(requestContext.getUriInfo().getPathParameters().getFirst("activityId"));
		if (method.equals(HttpMethod.GET.toString())) {
			this.abortWithNotFound(String.format(errorMsg, "activity", activityId));
		} else {
			this.abortBadRequest(String.format(errorMsg, "activity", activityId));
		}
	}

	private void abortIllegalActivityInstance(String method) {
		int activityInstanceId = Integer.parseInt(requestContext.getUriInfo().getPathParameters().getFirst("activityInstanceId"));
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
		assert map.containsKey("instanceId") && map.containsKey("scenarioId") : "Activity Id is only defined in context of a scenario instance";
		int instanceId = Integer.parseInt(map.getFirst("instanceId"));
		int activityInstanceId = Integer.parseInt(map.getFirst("activityInstanceId"));

		return new DbActivityInstance().existsActivityInstance(activityInstanceId, instanceId);
	}

	private boolean isValidActivity() {
		MultivaluedMap<String, String> map = requestContext.getUriInfo().getPathParameters();
		if (!map.containsKey("activityId")) {
			return true;
		}
		assert map.containsKey("instanceId") && map.containsKey("scenarioId") : "Activity Id is only defined in context of a scenario instance";
		int scenarioId = Integer.parseInt(map.getFirst("scenarioId"));
		int activityId = Integer.parseInt(map.getFirst("activityId"));

		return new DbControlNode().existControlNode(activityId, scenarioId);
	}

	private void abortIllegalScenario(String method) {
		int scenarioId = Integer.parseInt(requestContext.getUriInfo().getPathParameters().getFirst("scenarioId"));
		if (method.equals(HttpMethod.GET.toString())) {
			this.abortWithNotFound(String.format(errorMsg, "scenario", scenarioId));
		} else {
			this.abortBadRequest(String.format(errorMsg, "scenario", scenarioId));
		}
	}

	private boolean isValidScenario() {
		/*
		 * MultivaluedMap<String, String> map =
		 * requestContext.getUriInfo().getPathParameters(); if
		 * (!map.containsKey("scenarioId")) { return true; } int scenarioId =
		 * Integer.parseInt(map.getFirst("scenarioId")); return new
		 * DbScenario().existScenario(scenarioId);
		 *//*
		return true;
	}

	private boolean isValidInstance() {
		MultivaluedMap<String, String> map = requestContext.getUriInfo().getPathParameters();
		if (!map.containsKey("instanceId")) {
			return true;
		}

		int scenarioId = Integer.parseInt(map.getFirst("scenarioId"));
		int instanceId = Integer.parseInt(map.getFirst("instanceId"));
		return new DbScenarioInstance().doesScenarioInstanceBelongToScenario(scenarioId, instanceId);
	}

	private void abortInvalidInstance(String method) {
		int instanceId = Integer.parseInt(requestContext.getUriInfo().getPathParameters().getFirst("instanceId"));

		if (method.equals(HttpMethod.GET.toString())) {
			abortWithNotFound(String.format(errorMsg, "scenario instance", instanceId));
		} else {
			abortBadRequest(String.format(errorMsg, "scenario instance", instanceId));
		}
	}

	private void abortWithNotFound(String errorMsg) {
		Response notFound = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(errorMsg).build();
		this.requestContext.abortWith(notFound);
	}

	private void abortBadRequest(String errorMsg) {
		Response badRequest = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(errorMsg).build();
		this.requestContext.abortWith(badRequest);
	}
	*/

	private void validateUser() {
		String method = requestContext.getMethod();
		String path = requestContext.getUriInfo().getPath();
		if (method.equals("POST") && path.equals("interface/v2/register")) {
			return;
		}
		// Get the authentification passed in HTTP headers parameters
		String auth = requestContext.getHeaderString("authorization");

		// If the user does not have the right (does not provide any HTTP Basic
		// Auth)
		if (auth == null) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		// lap : loginAndPassword
		String[] lap = BasicAuth.decode(auth);

		// If login or password fail
		if (lap == null || lap.length != 2) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		try {
			UserManager.authenticateUser(lap[0], lap[1]);
		} catch (Exception e) {
			throw e;
		}

		// Our system refuse login and password
		// if(authentificationResult == null) {
		// throw new WebApplicationException(Status.UNAUTHORIZED);
		// }

		// We configure your Security Context here
		// String scheme = request.getUriInfo().getRequestUri().getScheme();
		// request.setSecurityContext(new MyApplicationSecurityContext(user,
		// scheme);
	}
}