package de.hpi.bpt.chimera.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

/**
 *
 */
public class AbstractRestService {
	protected final Response casemodelNotFoundResponse(String cmId) {
		String responseText = String.format("Casemodel id: %s is not assigned", cmId);
		return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(responseText).build();
	}

	protected final Response caseNotFoundResponse(String cmId, String caseId) {
		if (!CaseModelManager.isExistingCaseModel(cmId)) {
			return casemodelNotFoundResponse(cmId);
		}

		String responseText = String.format("Case id: %s is not assigned", caseId);
		return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(responseText).build();
	}

	protected final Response activityInstanceNotFoundResponse(String activityInstanceId) {
		String responseText = String.format("{\"error\":\"Acitivity Instance id: %s is not assigned.\"}", activityInstanceId);
		return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(responseText).build();
	}

	protected final Response dataObjectNotFoundResponse(String dataObjectId) {
		String responseText = String.format("{\"error\":\"Data Object id: %s is not assigned.\"}", dataObjectId);
		return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(responseText).build();
	}

	protected final Response stateNotFoundResponse(String state) {
		String responseText = String.format("{\"error\":\"The state: %s is not a valid state\"}", state);
		return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(responseText).build();
	}

	protected final String buildException(String text) {
		JSONArray result = new JSONArray();
		JSONObject content = new JSONObject();
		content.put("text", text);
		content.put("type", "danger");
		result.put(content);
		return result.toString();
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