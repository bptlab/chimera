package de.hpi.bpt.chimera.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.beans.history.ActivityLog;
import de.hpi.bpt.chimera.rest.beans.history.CompleteLogJaxBean;
import de.hpi.bpt.chimera.rest.beans.history.DataAttributeLog;
import de.hpi.bpt.chimera.rest.beans.history.DataObjectLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "history")
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
@Path("v3/organizations/{organizationId}/casemodels/{casemodelId}/cases/{caseId}/history")
public class CaseHistoryRestService extends AbstractRestService {

	@GET
	@Path("")
	@Operation(
		summary = "Receive all logs of a case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all logs.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompleteLogJaxBean.class)))})
	public Response getCompleteLog(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

		CompleteLogJaxBean logEntries = new CompleteLogJaxBean(caseExecutioner.getActivityLogs(), caseExecutioner.getDataObjectLogs(), caseExecutioner.getDataAttributeLogs());
		JSONObject result = new JSONObject(logEntries);
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	@GET
	@Path("activities")
	@Operation(
		summary = "Receive all activity logs of a case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all activity logs.",
				content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ActivityLog.class))))})
	public Response getActivityLog(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

		JSONArray result = new JSONArray(caseExecutioner.getActivityLogs());
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}
	
	@GET
	@Path("dataobjects")
	@Operation(
		summary = "Receive all data objects logs of a case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all data objects logs.",
				content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DataObjectLog.class))))})
	public Response getDataObjectLog(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

		JSONArray result = new JSONArray(caseExecutioner.getDataObjectLogs());
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}
	
	@GET
	@Path("attributes")
	@Operation(
		summary = "Receive all data attributes logs of a case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all data attributes logs.",
				content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DataAttributeLog.class))))})
	public Response getDataAttributeLog(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

		JSONArray result = new JSONArray(caseExecutioner.getDataAttributeLogs());
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}
}
