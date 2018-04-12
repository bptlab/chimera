package de.hpi.bpt.chimera.history.rest;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.history.HistoryService;
import de.hpi.bpt.chimera.history.LogEntry;
import de.hpi.bpt.chimera.history.StateTransitionLog;

import org.json.JSONArray;
import org.w3c.dom.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class implements the REST interface of the JEngine history.
 */
@Path("history/v2/")
public class HistoryRestService {
	/**
	 * This method gives the log entries for all activities for a specific scenario instance.
	 *
	 * @param scenarioID The id of the scenario belonging to the instance.
	 * @param instanceID The id of the scenario instance.
	 * @param state      The current state of the instance.
	 * @return a JSON-Object with the log entries.
	 */
	// TODO: think about implementation of @DefaultValue(" ")
	// @QueryParam("state") String state
	@GET
	@Path("scenario/{casemodelId}/instance/{caseId}/activities")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivityLog(@PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		if (caseExecutioner == null) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The instance or scenario ID " + "is incorrect\"}").build();
		}
		JSONArray result = new JSONArray(caseExecutioner.getActivityLogs());
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * This method gives the log entries for all dataObjects for a specific scenario instance.
	 *
	 * @param scenarioId         The id of the scenario belonging to the instance.
	 * @param scenarioInstanceId The id of the scenario instance.
	 * @return a JSON-Object with the log entries.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{scenarioInstanceId}/dataobjects")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataObjectLog(@PathParam("scenarioId") String cmId, @PathParam("scenarioInstanceId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		if (caseExecutioner == null) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The instance or scenario ID " + "is incorrect\"}").build();
		}
		JSONArray result = new JSONArray(caseExecutioner.getDataObjectLogs());
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}


	/**
	 * This method gives the log entries for all DataAttributeInstances
	 * for a specific scenario instance.
	 *
	 * @param scenarioID         The id of the scenario belonging to the instance.
	 * @param scenarioInstanceID The id of the scenario instance.
	 * @return a JSON-Object with the log entries.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{scenarioInstanceId}/attributes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataAttributeLog(@PathParam("scenarioId") String cmId, @PathParam("scenarioInstanceId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		if (caseExecutioner == null) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The instance or scenario ID " + "is incorrect\"}").build();
		}
		JSONArray result = new JSONArray(caseExecutioner.getDataAttributeLogs());
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * This method gives all log entries for a specific scenario instance.
	 *
	 * @param scenarioId
	 *            The id of the scenario belonging to the instance.
	 * @param scenarioInstanceId
	 *            The id of the scenario instance.
	 * @return a JSON-Object with the log entries.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{scenarioInstanceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompleteLog(@PathParam("scenarioId") String cmId, @PathParam("scenarioInstanceId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		if (caseExecutioner == null) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The instance or scenario ID " + "is incorrect\"}").build();
		}
		
		List<de.hpi.bpt.chimera.history.transportationbeans.LogEntry> logEntries = new ArrayList<>();
		logEntries.addAll(caseExecutioner.getActivityLogs());
		logEntries.addAll(caseExecutioner.getDataObjectLogs());
		logEntries.addAll(caseExecutioner.getDataAttributeLogs());
		JSONArray result = new JSONArray(logEntries);
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	// TODO
	@GET
	@Path("scenario/{scenarioId}/export")
	@Produces(MediaType.APPLICATION_XML)
	public Response exportToXml(@PathParam("scenarioId") int scenarioId) throws TransformerConfigurationException {
		HistoryService service = new HistoryService();
		try {
			Document doc = service.getTracesForScenarioId(scenarioId);
			return Response.status(200).type(MediaType.APPLICATION_XML).entity(doc).build();
		} catch (ParserConfigurationException e) {
			return Response.status(500).entity("Error processing the xml").build();
		}
	}

}
