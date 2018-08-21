package de.hpi.bpt.chimera.rest;

import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.CaseStarter;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.SseNotifier;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.beans.event.MultipleCaseStartersJaxBean;
import de.hpi.bpt.chimera.rest.beans.event.MultipleReceiveEventJaxBean;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.MessageJaxBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "events")
@ApiResponses(value = {
	@ApiResponse(
		responseCode = "400", description = "A problem occured during the processing.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))),
	@ApiResponse(
		responseCode = "401", description = "A problem occured during the authentication.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))),
	@ApiResponse(
		responseCode = "404", description = "An unassigned identifier was used.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))) })
@SecurityRequirement(name = "BasicAuth")
@Path("v3/organizations/{organizationId}/casemodels/{casemodelId}")
public class EventRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(EventRestService.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("casestarters")
	@Operation(
		summary = "Receive all case starters of a casemodels",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all case starters.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response getCaseStartTriggers(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId) {
		CaseModel cm = CaseModelManager.getCaseModel(cmId);

		JSONObject result = new JSONObject(new MultipleCaseStartersJaxBean(cm.getStartCaseTrigger()));
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("casestarters/{caseStarterId}")
	@Operation(
		summary = "Start a case with a specific casestarter",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully started a new case.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response startCase(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseStarterId") String requestKey, @Parameter(description = "Abitrary json string.") String eventJson) {
		log.info("An Event started a Case via REST-Interface.");
		CaseModel cm = CaseModelManager.getCaseModel(cmId);
		Optional<CaseStartTrigger> caseStartTrigger = cm.getStartCaseTrigger().stream()
														.filter(c -> c.getId().equals(requestKey))
														.findFirst();
		if (!caseStartTrigger.isPresent()) {
			String message = String.format("The case start trigger id: %s is not assigned", requestKey);
			log.error(message);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(message)).build();
		}

		CaseExecutioner caseExecutioner = ExecutionService.createCaseExecutioner(cm, "Automatically Created Case");

		CaseStarter caseStarter = new CaseStarter(caseStartTrigger.get());
		caseStarter.startCase(eventJson, caseExecutioner);
		SseNotifier.notifyRefresh();
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("Event received.")).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("cases/{caseId}/events")
	@Operation(
		summary = "Receive information about all registered events of a case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all registered events.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleReceiveEventJaxBean.class)))})
	public Response getRegisteredEvents(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		JSONObject result = new JSONObject(new MultipleReceiveEventJaxBean(caseExecutioner.getRegisteredEventBehaviors()));
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("cases/{caseId}/events/{eventInstanceId}")
	@Operation(
		summary = "Send a specific event",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Event was successfully received.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleReceiveEventJaxBean.class)))})
	public Response receiveEvent(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @PathParam("eventInstanceId") String requestId, @Parameter(description = "Abitrary json string.") String eventJson) {
		log.info("Receiving an event");
		// TODO: reset event json or find a better way to terminate the event
		// instance
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
			MessageReceiveEventBehavior receiveBehavior = caseExecutioner.getRegisteredEventBehavior(requestId);
			if (eventJson.isEmpty() || "{}".equals(eventJson)) {
				receiveBehavior.setEventJson("");
			} else {
				receiveBehavior.setEventJson(eventJson);
			}

			AbstractEventInstance eventInstance = receiveBehavior.getEventInstance();
			caseExecutioner.terminateDataControlNodeInstance(eventInstance);
			SseNotifier.notifyRefresh();
		} catch (Exception e) {
			log.error("Error while processing a received event", e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("Event received.")).build();
	}
}
