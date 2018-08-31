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
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.exception.IllegalCaseModelIdException;
import de.hpi.bpt.chimera.execution.exception.IllegalControlNodeInstanceTypeException;
import de.hpi.bpt.chimera.execution.exception.IllegalIdentifierException;
import de.hpi.bpt.chimera.execution.exception.IllegalOrganizationIdException;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.usermanagement.MemberRole;
import de.hpi.bpt.chimera.usermanagement.Organization;
import de.hpi.bpt.chimera.usermanagement.OrganizationManager;
import de.hpi.bpt.chimera.usermanagement.User;
import de.hpi.bpt.chimera.usermanagement.UserManager;

import java.io.IOException;
import java.util.List;

// TODO: extend this Provider. 
// Either by using 'getMatchedResources' to look for the called RestService and not the id.
// Or create a costume request filter for every RestService via static or dynamic 'NameBinding'.
@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {
	private static Logger log = Logger.getLogger(AuthorizationRequestFilter.class);
	String errorMsg = "{\"error\":\"There is no %s with id %d\"}";
	private static final String UNAUTHORIZED_MEMBER_MESSAGE = "You are not a member of this organization, and cannot view organizational details.";
	private static final String UNAUTHORIZED_VIEW_MESSAGE = "You are not allowed to view this information.";

	private ContainerRequestContext requestContext;
	private User requester;
	private Organization organization;
	private CaseExecutioner caseExecutioner;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		this.requestContext = requestContext;
		String path = requestContext.getUriInfo().getPath();
		String[] pathParts = path.split("/");
		// TODO: remove this workaround so the old version of the api is not
		// filtered as soon
		// as version 2 is removed
		if (pathParts[0].equals("v2") || pathParts[1].equals("v2")) {
			return;
		}

		try {
			validateUser();
			validateOrganization();
			validateCaseModels();
			validateCase();
			validateActivityInstance();
		} catch (WebApplicationException e) {
			throw e;
		} catch (IllegalIdentifierException e) {
			log.error(e);
			JSONObject message = new JSONObject(new DangerExceptionJaxBean(e.getMessage()));
			Response notFound = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(message.toString()).build();
			this.requestContext.abortWith(notFound);
		} catch (IllegalControlNodeInstanceTypeException e) {
			log.error(e);
			JSONObject message = new JSONObject(new DangerExceptionJaxBean(e.getMessage()));
			Response badRequest = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(message.toString()).build();
			this.requestContext.abortWith(badRequest);
		} catch (Exception e) {
			log.error(e);
			JSONObject message = new JSONObject(new DangerExceptionJaxBean(e.getMessage()));
			Response unauthorized = Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(message.toString()).build();
			this.requestContext.abortWith(unauthorized);
		}
	}

	private void validateUser() {
		// TODO: think about costum response message for WebApplicationException
		String method = requestContext.getMethod();
		String path = requestContext.getUriInfo().getPath();
		// every request is allowed to create a new user
		if ("POST".equals(method) && path.matches("(v3/users)(/)?")) {
			return;
		}
		if (!path.startsWith("v3/users") && !path.startsWith("v3/organizations") && !path.startsWith("v3/authenticate")) {
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
			this.requester = UserManager.authenticateUser(lap[0], lap[1]);
		} catch (Exception e) {
			throw e;
		}

		// We configure your Security Context here
		// String scheme = request.getUriInfo().getRequestUri().getScheme();
		// request.setSecurityContext(new MyApplicationSecurityContext(user,
		// scheme);
	}

	private void validateOrganization() {
		MultivaluedMap<String, String> parameters = requestContext.getUriInfo().getPathParameters();
		if (!parameters.containsKey("organizationId")) {
			return;
		}

		String orgId = parameters.getFirst("organizationId");
		try {
			organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(requester) && !requester.isAdmin()) {
				throw new IllegalArgumentException(UNAUTHORIZED_MEMBER_MESSAGE);
			}
		} catch (IllegalOrganizationIdException e) {
			throw e;
		}
	}

	private void validateCaseModels() {
		MultivaluedMap<String, String> parameters = requestContext.getUriInfo().getPathParameters();
		if (!parameters.containsKey("casemodelId")) {
			return;
		}

		String cmId = parameters.getFirst("casemodelId");

		if (!organization.getCaseModels().containsKey(cmId)) {
			throw new IllegalCaseModelIdException(cmId);
		}

		CaseModel cm = organization.getCaseModels().get(cmId);

		if (cm.getAllowedRoles().isEmpty() || organization.isOwner(requester)) {
			return;
		}

		List<MemberRole> memberRoles = organization.getMemberRoles(requester);
		for (MemberRole role : memberRoles) {
			if (cm.getAllowedRoles().contains(role)) {
				return;
			}
		}

		throw new IllegalArgumentException(UNAUTHORIZED_VIEW_MESSAGE);
	}

	private void validateCase() {
		MultivaluedMap<String, String> parameters = requestContext.getUriInfo().getPathParameters();
		if (!parameters.containsKey("caseId")) {
			return;
		}

		String cmId = parameters.getFirst("casemodelId");
		String caseId = parameters.getFirst("caseId");

		try {
			caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		} catch (Exception e) {
			throw e;
		}
	}

	private void validateActivityInstance() {
		MultivaluedMap<String, String> parameters = requestContext.getUriInfo().getPathParameters();
		if (!parameters.containsKey("activityInstanceId")) {
			return;
		}

		String activityInstanceId = parameters.getFirst("activityInstanceId");

		try {
			caseExecutioner.getActivityInstance(activityInstanceId);
		} catch (Exception e) {
			throw e;
		}
	}
}