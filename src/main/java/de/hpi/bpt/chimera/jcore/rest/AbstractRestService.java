package de.hpi.bpt.chimera.jcore.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
public class AbstractRestService {
	protected final Response CASEMODEL_NOT_FOUND = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("Casemodel id is not assigned").build();

	protected final Response CASE_NOT_FOUND = Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Casemodel id or Case id is not assigned.").build();

	protected final Response ACTIVITY_INSTANCE_NOT_FOUND = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Acitivity Instance id is not assigned.\"}").build();

	protected final Response DATAOBJECT_NOT_FOUND = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"Data Object id is not assigned.\"}").build();

	public Response stateNotFoundResponse(String state) {
		return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The state " + "is not allowed " + state + "\"}").build();
	}

	public Response buildNotFoundResponse(String errorMsg) {
		return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(errorMsg).build();
	}

	public Response buildBadRequestResponse(String errorMsg) {
		return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(errorMsg).build();
	}

	public Response buildAcceptedResponse(String errorMsg) {
		return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(errorMsg).build();
	}
}
