package de.hpi.bpt.chimera.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelDetailsJaxBean;
import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.casemodel.ConditionsJaxBean;
import de.hpi.bpt.chimera.rest.beans.casemodel.FragmentRepresentationJaxBean;
import de.hpi.bpt.chimera.rest.beans.casemodel.MultipleCaseModelsJaxBean;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.MessageJaxBean;
import de.hpi.bpt.chimera.usermanagement.MemberRole;
import de.hpi.bpt.chimera.usermanagement.Organization;
import de.hpi.bpt.chimera.usermanagement.OrganizationManager;
import de.hpi.bpt.chimera.usermanagement.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "casemodels")
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
@Path("v3/organizations/{organizationId}/casemodels")
public class CaseModelRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(CaseModelRestService.class);
	
	/**
	 * Receive all casemodels that a member of an organization is allowed to see
	 * according to its {@link MemberRole}.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param filterString
	 *            - for filtering the casemodels by their names
	 * @return the Response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject with a JSONArray of
	 *         {@link CaseModelOverviewJaxBean} at key {@code casemodels}. The
	 *         response will be 400 if the {@code orgId} is not assigned. The
	 *         response code will be 401 if the user who send the request is not
	 *         allowed to view this information.
	 */
	@GET
	@Path("")
	@Operation(
		summary = "Receive all visible casemodels for the user in an organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all casemodels of an organization.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleCaseModelsJaxBean.class)))})
	public Response receiveCaseModels(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @DefaultValue("") @QueryParam("filter") String filterString) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			List<CaseModel> caseModels = OrganizationManager.getCaseModels(organization, user);

			if (!filterString.isEmpty()) {
				caseModels = caseModels.stream().filter(cm -> cm.getName().contains(filterString)).collect(Collectors.toList());
			}

			List<CaseModelOverviewJaxBean> beans = caseModels.stream()
													.map(CaseModelOverviewJaxBean::new)
													.collect(Collectors.toList());

			JSONObject result = new JSONObject(new MultipleCaseModelsJaxBean(beans));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	/**
	 * Deploy a new casemodel in an organization as a member of the
	 * organization.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param jsonString
	 *            - the json with information about the casemodel
	 * @return the Response of POST. The response code will be 201 if the
	 *         request was successful and contains a
	 *         {@link CaseModelOverviewJaxBean}. The response will be 400 if the
	 *         {@code orgId} is not assigned or an error occured during the
	 *         parsing. The response code will be 401 if the user who send the
	 *         request is not a member of the organization.
	 */
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Deploy a new casemodel for an organization",
		responses = {
			@ApiResponse(
				responseCode = "201", description = "Successfully deployed a the casemodel.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CaseModelDetailsJaxBean.class)))})
	public Response deployCaseModel(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String jsonString) {
		// TODO: make a jax bean for the casemodel
		try {
			// TODO: add something like deployer
			// User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);

			CaseModel cm = CaseModelManager.parseCaseModel(jsonString);
			cm.setOrganization(organization);
			organization.getCaseModels().put(cm.getId(), cm);
			log.info("Successfully parsed a CaseModel");
			JSONObject result = new JSONObject(new CaseModelDetailsJaxBean(cm));
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error("Chimera failed to parse the CaseModel!", e);
			return Response.status(422).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{casemodelId}")
	@Operation(
		summary = "Receive a specific casemodel of an organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the casemodel.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CaseModelDetailsJaxBean.class)))})
	public Response receiveCaseModel(@Context UriInfo uri, @PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId) {
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);

			JSONObject result = new JSONObject(new CaseModelDetailsJaxBean(cm));

			return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@DELETE
	@Path("{casemodelId}")
	@Operation(
		summary = "Delete a specific casemodel of an organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully deleted the casemodel.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response deleteCaseModel(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId) {
		// TODO: think about who should be allowed to delete a casemodel. Maybe
		// the owner and the deployer or everyone who is allowed to view it.
		try {
			CaseModelManager.deleteCaseModel(cmId);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("casemodel deletion successful.")).build();
		} catch (IllegalArgumentException e) {
			log.error("deletion failed: " + e);
			return Response.status(422).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	@GET
	@Path("{casemodelId}/terminationcondition")
	@Operation(
		summary = "Receive the termination condition of specific casemodel",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the termination condition.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConditionsJaxBean.class)))})
	public Response getTerminationCondition(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId) {
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);

			JSONObject result = new JSONObject(new ConditionsJaxBean(cm.getTerminationCondition()));

			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{casemodelId}/xml")
	@Operation(
		summary = "Receive the bpmn xml representation of the fragments",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the bpmn representation.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = FragmentRepresentationJaxBean.class)))})
	public Response getFragmentXmlStrings(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId) {
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);

			JSONObject result = new JSONObject(new FragmentRepresentationJaxBean(cm.getContentXmlStrings()));
			return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
