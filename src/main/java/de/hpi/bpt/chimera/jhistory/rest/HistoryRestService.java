package de.hpi.bpt.chimera.jhistory.rest;

import de.hpi.bpt.chimera.jhistory.HistoryService;
import de.hpi.bpt.chimera.jhistory.LogEntry;
import de.hpi.bpt.chimera.jhistory.StateTransitionLog;
import org.json.JSONArray;
import org.w3c.dom.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
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
	@GET
	@Path("scenario/{scenarioID}/instance/{instanceID}/activities")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivityLog(@DefaultValue("0") @PathParam("scenarioID") int scenarioID, @DefaultValue("0") @PathParam("instanceID") int instanceID, @DefaultValue(" ") @QueryParam("state") String state) {
		if (instanceID == 0 || scenarioID == 0) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The instance or scenario ID " + "is incorrect\"}").build();
		}
		List<StateTransitionLog> activityLog = StateTransitionLog.getStateTransitions(instanceID, LogEntry.LogType.ACTIVITY);
		activityLog.sort((log1, log2) -> log2.getTimeStamp().compareTo(log1.getTimeStamp()));
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(new JSONArray(activityLog).toString()).build();
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
	public Response getDataObjectLog(@DefaultValue("0") @PathParam("scenarioId") int scenarioId, @DefaultValue("0") @PathParam("scenarioInstanceId") int scenarioInstanceId) {
		if (scenarioInstanceId == 0 || scenarioId == 0) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The instance or scenario Id " + "is incorrect\"}").build();
		}
		List<StateTransitionLog> dataObjectLog = StateTransitionLog.getStateTransitions(scenarioInstanceId, LogEntry.LogType.DATA_OBJECT);
		dataObjectLog.sort((log1, log2) -> log2.getTimeStamp().compareTo(log1.getTimeStamp()));
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(new JSONArray(dataObjectLog).toString()).build();
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
	@Path("scenario/{scenarioID}/instance/{scenarioInstanceID}/attributes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataAttributeLog(@DefaultValue("0") @PathParam("scenarioID") int scenarioID, @DefaultValue("0") @PathParam("scenarioInstanceID") int scenarioInstanceID) {
		if (scenarioInstanceID == 0 || scenarioID == 0) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The instance or scenario ID " + "is incorrect\"}").build();
		}

		List<StateTransitionLog> attributeLog = StateTransitionLog.getStateTransitions(scenarioInstanceID, LogEntry.LogType.DATA_ATTRIBUTE);
		attributeLog.sort((log1, log2) -> log2.getTimeStamp().compareTo(log1.getTimeStamp()));
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(new JSONArray(attributeLog).toString()).build();
	}

	/**
	 * This method gives the log entries for all dataObjects for a specific scenario instance.
	 *
	 * @param scenarioId         The id of the scenario belonging to the instance.
	 * @param scenarioInstanceId The id of the scenario instance.
	 * @return a JSON-Object with the log entries.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{scenarioInstanceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompleteLog(@DefaultValue("0") @PathParam("scenarioId") int scenarioId, @DefaultValue("0") @PathParam("scenarioInstanceId") int scenarioInstanceId) {

		List<StateTransitionLog> log = StateTransitionLog.getStateTransitions(scenarioInstanceId);
		log.sort((log1, log2) -> log2.getTimeStamp().compareTo(log1.getTimeStamp()));
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(new JSONArray(log).toString()).build();
	}

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
