package de.hpi.bpt.chimera.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.MessageJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.NamedJaxBean;
import de.hpi.bpt.chimera.rest.beans.organization.CreateOrganizationJaxBean;
import de.hpi.bpt.chimera.rest.beans.organization.MultipleOrganizationsOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.organization.MultipleOwnersJaxBean;
import de.hpi.bpt.chimera.rest.beans.organization.MultipleRolesJaxBean;
import de.hpi.bpt.chimera.rest.beans.organization.OrganizationDetailsJaxBean;
import de.hpi.bpt.chimera.rest.beans.organization.OrganizationOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.organization.UpdateOrganizationJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.AssignUserJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagement.MemberRole;
import de.hpi.bpt.chimera.usermanagement.Organization;
import de.hpi.bpt.chimera.usermanagement.OrganizationManager;
import de.hpi.bpt.chimera.usermanagement.User;
import de.hpi.bpt.chimera.usermanagement.UserManager;
import de.hpi.bpt.chimera.validation.NameValidation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

// TODO: allow deletion of ownership
@Tag(name = "organizations")
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
@Path("v3/organizations")
public class OrganizationRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(OrganizationRestService.class);
	private static final String UNAUTHORIZED_CHANGE_MESSAGE = "You are not allowed to alter this information.";

	/**
	 * Create a new organization with a specified name. Every logged in user can
	 * create a new organization but the name must pass the validation. The user
	 * who creates the organization will be its owner.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param body
	 *            - {@link NamedJaxBean}
	 * @return the response of POST. The response will be an
	 *         {@link OrganizationDetailsJaxBean} with response code 201 if the
	 *         request was successful. The response will be 400 if the name for
	 *         the organization does not match the guideline.
	 * @see {@link NameValidation#validateName(String) validateName}
	 */
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Create a new organization",
		responses = {
				@ApiResponse(
					responseCode = "201", description = "Successfully created a new organization.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationDetailsJaxBean.class)))})
	public Response createOrganization(@Context ContainerRequestContext requestContext,
			@Parameter(description = "Information about the new organization", required = true) CreateOrganizationJaxBean bean) {
		try {
			String name = bean.getName();
			NameValidation.validateName(name);

			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.createOrganization(user, name);
			organization.setDescription(bean.getDescription());

			JSONObject result = new JSONObject(new OrganizationDetailsJaxBean(organization, user));
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Receive an overview over all existing organizations. Every logged in user
	 * is able to see the overview.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @return the Response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject with a JSONArray of
	 *         {@link OrganizationOverviewJaxBean} at key {@code organizations}.
	 */
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Receive all organizations",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all organizations.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleOrganizationsOverviewJaxBean.class)))})
	public Response getOrganizations(@Context ContainerRequestContext requestContext) {
		User user = retrieveUser(requestContext);
		List<OrganizationOverviewJaxBean> resBeans = user.getOrganizations().stream()
														.map(OrganizationOverviewJaxBean::new)
														.collect(Collectors.toList());
		MultipleOrganizationsOverviewJaxBean resBean = new MultipleOrganizationsOverviewJaxBean(resBeans);
		JSONObject result = new JSONObject(resBean);
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * Receive information about a specific organization. Only admins and
	 * members of the organization are allowed to view those information.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @return the response of GET. The response will be an
	 *         {@link OrganizationDetailsJaxBean} with response code 200 if the
	 *         request was successful. The response will be 400 if the
	 *         {@code orgId} is not assigned. The response code will be 401 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@GET
	@Path("{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Receive a specific organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the organization.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationDetailsJaxBean.class)))})
	public Response getOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		User user = retrieveUser(requestContext);
		Organization organization = OrganizationManager.getOrganizationById(orgId);

		JSONObject result = new JSONObject(new OrganizationDetailsJaxBean(organization, user));
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * Update certain information about a specific organization. Only admins and
	 * owners of the organization can update the organization.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param body
	 *            - {@link UpdateOrganizationJaxBean}, information that are
	 *            needed to update the organization.
	 * @return the response of PUT. The response will be an
	 *         {@link OrganizationDetailsJaxBean} with response code 200 if the
	 *         request was successful. The response will be 400 if the
	 *         {@code orgId} is not assigned. The response code will be 401 if
	 *         the user who send the request is not allowed to update those
	 *         information.
	 */
	@PUT
	@Path("{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Update a specific organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully updated the organization and receive its information.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationDetailsJaxBean.class)))})
	public Response updateOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId,
			@Parameter(description = "", required = true) UpdateOrganizationJaxBean update) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			
			if (!organization.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError("You are not an owner of this organization, and cannot update organizational details.")).build();
			}
			// TODO: let the user update one without the other
			organization.setName(update.getName());
			organization.setDescription(update.getDescription());

			JSONObject result = new JSONObject(new OrganizationDetailsJaxBean(organization, user));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Delete an organization. Only admins and owners of the organization are
	 * allowed to delete it. All members will be removed and get unassigned.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @return the Response of DELETE. The response code will be 200 if the
	 *         request was successful. The response will be 400 if the
	 *         {@code orgId} is not assigned. The response code will be 401 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@DELETE
	@Path("{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Delete a specific organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully deleted the organization.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response deleteOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization org = OrganizationManager.getOrganizationById(orgId);

			if (!org.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to delete the organization.")).build();
			}

			OrganizationManager.deleteOrganization(org);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("Successfully deleted organization.")).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	// TODO: should all members be able to see this?
	/**
	 * Receive the names of all organizational roles of the organization. Only
	 * admins and members of the organization are allowed to see those.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @return the Response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject with a JSONArray of
	 *         Strings at key {@code roles}. The response will be 400 if the
	 *         {@code orgId} is not assigned. The response code will be 401 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@GET
	@Path("{organizationId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Receive all roles of a specific organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the roles of an organization.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleRolesJaxBean.class)))})
	public Response receiveRoles(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		Organization organization = OrganizationManager.getOrganizationById(orgId);

		JSONObject result = new JSONObject(new MultipleRolesJaxBean(organization.getRoles()));
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * Create a new organizational role for an organization. Only admins and
	 * owners of the organization are allowed to do this. The role name must be
	 * unique in the organization.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param body
	 *            - {@link NamedJaxBean}, the name for the new role.
	 * @return the Response of POST. The response code will be 201 if the
	 *         request was successful and contains a JSONObject with a JSONArray
	 *         of Strings at key {@code roles}. The response will be 400 if the
	 *         {@code orgId} is not assigned. The response code will be 401 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@POST
	@Path("{organizationId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Create a new role",
		responses = {
			@ApiResponse(
				responseCode = "201", description = "Successfully created a new role for an organization.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleRolesJaxBean.class)))})
	public Response addRole(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId,
			@Parameter(description = "The name for the new role.", required = true) NamedJaxBean bean) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to add new roles.")).build();
			}

			OrganizationManager.addRole(organization, bean.getName());

			JSONObject result = new JSONObject(new MultipleRolesJaxBean(organization.getRoles()));
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Delete a organizational role of an organization. Only the owner is
	 * allowed to do so.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param roleName
	 *            - name of the role
	 * @return the Response of DELETE. The response code will be 200 if the
	 *         request was successful. The response will be 400 if the
	 *         {@code orgId} is not assigned. The response code will be 401 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@DELETE
	@Path("{organizationId}/roles/{roleName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Delete a specific role of an organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully deleted the role of an organization.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response deleteRole(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("roleName") String roleName) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!user.isAdmin() && !organization.isOwner(user)) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_CHANGE_MESSAGE)).build();
			}

			MemberRole role = organization.getRole(roleName);
			OrganizationManager.deleteRole(organization, role);

			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("Role successfully deleted.")).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Receive information about all owners of the organization. Only admins and
	 * members of the organization are able to see those information.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @return the Response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject with a JSONArray of
	 *         {@link UserOverviewJaxBean} at key {@code owners}. The response
	 *         will be 400 if the {@code orgId} is not assigned. The response
	 *         code will be 401 if the user who send the request is not allowed
	 *         to view this information.
	 */
	@GET
	@Path("{organizationId}/owners")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Receive all owners of an organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all owners of an organization.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleOwnersJaxBean.class)))})
	public Response receiveOwners(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		Organization organization = OrganizationManager.getOrganizationById(orgId);

		List<UserOverviewJaxBean> resBeans = organization.getOwners().values().stream()
												.map(UserOverviewJaxBean::new)
												.collect(Collectors.toList());

		JSONObject result = new JSONObject(new MultipleOwnersJaxBean(resBeans));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Assign a new owner for the organization. Only an admin or an owner of the
	 * organization can add a new owner. If the specified user is not a member
	 * of the organization he will receive a membership.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param body
	 *            - {@link AssignUserJaxBean}, the id of the user who will be
	 *            assigned as an owner
	 * @return the Response of POST. The response code will be 200 if the
	 *         creation was successful, 400 if the {@code orgId} or
	 *         {@code userId} is not assigned or if the specified user is
	 *         already an owner of the organization. The response code will be
	 *         401 if the user who send the request is not allowed to create a
	 *         new owner.
	 */
	@POST
	@Path("{organizationId}/owners")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Make a user a new owner of an organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully made a user a new owner of an organization.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response addOwner(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId,
							@Parameter(description = "The id of the user to be assigned.", required = true) AssignUserJaxBean assign) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to add new owners to the organization.")).build();
			}

			User newOwner = UserManager.getUserById(assign.getId());
			OrganizationManager.addOwner(organization, newOwner);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("User successfully assigned.")).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
