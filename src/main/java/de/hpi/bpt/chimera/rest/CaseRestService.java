package de.hpi.bpt.chimera.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hpi.bpt.chimera.rest.beans.caze.CaseDetailsJaxBean;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.rest.beans.caze.CanTerminateJaxBean;
import de.hpi.bpt.chimera.rest.beans.caze.CaseOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.caze.MultipleCasesJaxBean;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.MessageJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.NamedJaxBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "cases")
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
@Path("v3/organizations/{organizationId}/casemodels/{casemodelId}/cases")
public class CaseRestService extends AbstractRestService {
	private static final Logger log = Logger.getLogger(CaseRestService.class);

	@GET
	@Path("")
	@Operation(
		summary = "Receive all cases of a casemodel",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all cases.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleCasesJaxBean.class)))})
	public Response receiveCases(@Context UriInfo uri, @PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @DefaultValue("") @QueryParam("filter") String filterString) {
		List<CaseExecutioner> caseExecutions = ExecutionService.getAllCasesOfCaseModel(cmId);

		if (!filterString.isEmpty())
			caseExecutions = caseExecutions.stream().filter(instance -> instance.getCase().getName().contains(filterString)).collect(Collectors.toList());

		JSONObject result = new JSONObject(new MultipleCasesJaxBean(caseExecutions));

		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Start a new case of a casemodel",
		responses = {
			@ApiResponse(
				responseCode = "201", description = "Successfully started a new case.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CaseOverviewJaxBean.class)))})
	public Response startNewCase(@Context UriInfo uri, @PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId) {
		try {
			return initializeNewCase(cmId, "");
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@PUT
	@Path("")
	@Operation(
		summary = "Start a new named case of a casemodel",
		responses = {
			@ApiResponse(
				responseCode = "201", description = "Successfully started a new case.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CaseOverviewJaxBean.class)))})
	public Response startNewNamedCase(@Context UriInfo uriInfo, @PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, NamedJaxBean bean) {
		try {
			return initializeNewCase(cmId, bean.getName());
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}

	}

	/**
	 * Initialize a new Case with custom name and start it.
	 * 
	 * @param uriInfo
	 * @param cmId
	 * @param name
	 * @return Response
	 */
	private Response initializeNewCase(String cmId, String name) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.createCaseExecutioner(cmId, name);
			caseExecutioner.startCase();

			JSONObject result = new JSONObject(new CaseOverviewJaxBean(caseExecutioner));
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{caseId}")
	@Operation(
		summary = "Receive a specific case of a casemodel",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the case.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CaseDetailsJaxBean.class)))})
	public Response receiveCase(@Context UriInfo uriInfo, @PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

		JSONObject result = new JSONObject(new CaseDetailsJaxBean(caseExecutioner));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("{caseId}/canTerminate")
	@Operation(
		summary = "Receive whether a specific case can be terminated",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the information.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CanTerminateJaxBean.class)))})
	public Response checkTermination(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

		JSONObject result = new JSONObject(new CanTerminateJaxBean(caseExecutioner));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("{caseId}/terminate")
	@Operation(
		summary = "Terminate a specific case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully terminated the case.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response terminateCase(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		if (caseExecutioner.isTerminated()) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("Case is already terminated.")).build();
		}

		if (caseExecutioner.canTerminate()) {
			caseExecutioner.terminate();
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("case terminated.")).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("termination condition is not fulfilled.")).build();
		}
	}
}
