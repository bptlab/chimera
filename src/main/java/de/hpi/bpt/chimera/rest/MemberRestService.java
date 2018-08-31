package de.hpi.bpt.chimera.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.rest.beans.caze.CaseOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.caze.MultipleCasesJaxBean;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.MessageJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.NamedJaxBean;
import de.hpi.bpt.chimera.rest.beans.organization.MultipleMemberJaxBean;
import de.hpi.bpt.chimera.rest.beans.organization.MultipleRolesJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.AssignUserJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.MemberDetailsJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagement.MemberRole;
import de.hpi.bpt.chimera.usermanagement.Organization;
import de.hpi.bpt.chimera.usermanagement.OrganizationManager;
import de.hpi.bpt.chimera.usermanagement.User;
import de.hpi.bpt.chimera.usermanagement.UserManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "members")
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
@Path("v3/organizations/{organizationId}/members")
public class MemberRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(OrganizationRestService.class);
	
	private static final String UNAUTHORIZED_VIEW_MESSAGE = "You are not allowed to view this information.";
	private static final String UNAUTHORIZED_CHANGE_MESSAGE = "You are not allowed to alter this information.";
	private static final String NOT_A_MEMBER_MESSAGE = "The requested user is not a member of the organization.";

	/**
	 * Receive information about all members of the organization. Only admins
	 * and members of the organization are able to see those information.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @return the Response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject with a JSONArray of
	 *         {@link UserOverviewJaxBean} at key {@code members}. The response
	 *         will be 400 if the {@code orgId} is not assigned. The response
	 *         code will be 401 if the user who send the request is not allowed
	 *         to view this information.
	 */
	@GET
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Receive all members of a specific organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all members.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleMemberJaxBean.class))) })
	public Response receiveMembers(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		Organization organization = OrganizationManager.getOrganizationById(orgId);

		List<UserOverviewJaxBean> resBeans = organization.getMembers().values().stream()
												.map(UserOverviewJaxBean::new)
												.collect(Collectors.toList());

		JSONObject result = new JSONObject(new MultipleMemberJaxBean(resBeans));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Assign a new member to an organization. Only admins and owners of the
	 * organization are allowed to do so.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param body
	 *            - {@link AssignUserJaxBean}, the id of the user who will be
	 *            assigned as a member
	 * @return the Response of POST. The response code will be 201 if the
	 *         request was successful and contains a JSONObject with a JSONArray
	 *         of {@link UserOverviewJaxBean} at key {@code members}. The
	 *         response will be 400 if the {@code orgId} is not assigned. The
	 *         response code will be 401 if the user who send the request is not
	 *         allowed to change this information.
	 */
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Assign an user as a new member of a specific organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully made an user a new member.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleMemberJaxBean.class))) })
	public Response assignMember(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId,
			@Parameter(description = "The id of the user.", required = true) AssignUserJaxBean assign) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);

			if (!organization.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to assign members to the organization.")).build();
			}

			User userToAssign = UserManager.getUserById(assign.getId());
			OrganizationManager.assignMember(organization, userToAssign);

			List<UserOverviewJaxBean> resBeans = organization.getMembers().values().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());

			JSONObject result = new JSONObject(new MultipleMemberJaxBean(resBeans));
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	/**
	 * Receive information about a member of an {@link Organization}. Every
	 * member can view these information.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param userId
	 *            - id of the user
	 * @return the Response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject of an
	 *         {@link MemberDetailsJaxBean}. The response code will be 400 if
	 *         the {@code orgId} or {@code userId} is not assigned or if the
	 *         user is not a member of the organization. The response code will
	 *         be 401 if the user who send the request is not allowed to view
	 *         these information.
	 */
	@GET
	@Path("{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Receive information about a specific member",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested a member.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDetailsJaxBean.class))) })
	public Response receiveMember(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId) {
		Organization organization = OrganizationManager.getOrganizationById(orgId);

		User userToView = UserManager.getUserById(userId);
		if (!organization.isMember(userToView)) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
		}

		JSONObject result = new JSONObject(new MemberDetailsJaxBean(organization, userToView));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Delete the membership of a specific user in an organization. Only the
	 * user itself, the owner of the organization and an admin can delete this
	 * membership.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param userId
	 *            - id of the user
	 * @return the Response of DELETE. The response code will be 200 if the
	 *         deletion was successful, 400 if the {@code orgId} or
	 *         {@code userId} is not assigned, if the user the not a member of
	 *         the organization or is the last owner. The response code will be
	 *         401 if the user who send the request is not allowed to delete the
	 *         membership.
	 */
	@DELETE
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Remove a member from an organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully removed the member from the organization.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class))) })
	public Response removeMember(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId) {
		try {
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			User user = retrieveUser(requestContext);
			User userToDelete = UserManager.getUserById(userId);

			if (!user.equals(userToDelete) && !user.isAdmin() && !organization.isOwner(user)) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to remove the user from the organization.")).build();
			}

			OrganizationManager.removeMember(organization, userToDelete);

			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Successfully removed member.\"}").build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	/**
	 * Receive all organizational role names of a member in an
	 * {@link Organization}. Every member of the organization is allowed to see
	 * those information.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param userId
	 *            - id of the user
	 * @return the Response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject of an
	 *         {@link MemberRolesJaxBean}. The response code will be 400 if the
	 *         {@code orgId} or {@code userId} is not assigned or if the user is
	 *         not a member of the organization. The response code will be 401
	 *         if the user who send the request is not allowed to view these
	 *         information.
	 */
	@GET
	@Path("{userId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Get all roles from a member in an organization",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the roles.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleRolesJaxBean.class))) })
	public Response receiveMemberRoles(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId) {
		Organization organization = OrganizationManager.getOrganizationById(orgId);

		User userToView = UserManager.getUserById(userId);
		if (!organization.isMember(userToView)) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
		}

		List<MemberRole> roles = organization.getMemberRoles(userToView);
		JSONObject result = new JSONObject(new MultipleRolesJaxBean(roles));

		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Assign an organizational role to a member in an {@link Organization}.
	 * Only the owners are able to do so.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param userId
	 *            - id of the user
	 * @return the Response of POST. The response code will be 201 if the
	 *         request was successful and contains a JSONObject of an
	 *         {@link MemberRolesJaxBean}. The response code will be 400 if the
	 *         {@code orgId} or {@code userId} is not assigned, if the user is
	 *         not a member of the organization or the role name is not
	 *         assigned. The response code will be 401 if the user who send the
	 *         request is not allowed to change these information.
	 */
	@POST
	@Path("{userId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Assign a role to a member",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully assigned a new role.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleRolesJaxBean.class))) })
	public Response assignMemberRole(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId,
			@Parameter(description = "The name for the new role.", required = true) NamedJaxBean bean) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!user.isAdmin() && !organization.isOwner(user)) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_CHANGE_MESSAGE)).build();
			}

			User userToAssign = UserManager.getUserById(userId);
			if (!organization.isMember(userToAssign)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
			}

			MemberRole role = organization.getRole(bean.getName());
			OrganizationManager.assignRole(organization, userToAssign, role);

			List<MemberRole> roles = organization.getMemberRoles(userToAssign);
			JSONObject result = new JSONObject(new MultipleRolesJaxBean(roles));

			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	/**
	 * Delete a specific organizational role of a member in an organization.
	 * Only an owner of the organization is allowed to do so.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param userId
	 *            - id of the user
	 * @param roleName
	 *            - name of the organizational role.
	 * @return the Response of DELETE. The response code will be 200 if the
	 *         request was successful and contains a JSONObject of an
	 *         {@link MemberRolesJaxBean}. The response code will be 400 if the
	 *         {@code orgId} or {@code userId} is not assigned, if the user is
	 *         not a member of the organization or the role name is not
	 *         assigned. The response code will be 401 if the user who send the
	 *         request is not allowed to change these information.
	 */
	@DELETE
	@Path("{userId}/roles/{roleName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Remove a specific role from a member",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully removed the role.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleRolesJaxBean.class))) })
	public Response deleteMemberRole(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId, @PathParam("roleName") String roleName) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!user.isAdmin() && !organization.isOwner(user)) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_CHANGE_MESSAGE)).build();
			}

			User requestedUser = UserManager.getUserById(userId);
			if (!organization.isMember(requestedUser)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
			}

			MemberRole role = organization.getRole(roleName);
			UserManager.deleteRole(requestedUser, organization, role);

			List<MemberRole> roles = organization.getMemberRoles(requestedUser);
			JSONObject result = new JSONObject(new MultipleRolesJaxBean(roles));

			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	/**
	 * Receive all cases in an organization that a member of this organization
	 * is allowed to access. This information can be viewed by the user itself
	 * or by an owner of an organization.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param orgId
	 *            - id of the organization.
	 * @param userId
	 *            - id of the user
	 * @return the Response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject with a JSONArray of
	 *         {@link CaseOverviewJaxBean} at key {@code cases}. The response
	 *         code will be 400 if the {@code orgId} or {@code userId} is not
	 *         assigned or if the user is not a member of the organization. The
	 *         response code will be 401 if the user who send the request is not
	 *         allowed to view these information.
	 */
	@GET
	@Path("{userId}/cases")
	@Tag(name = "cases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Get all cases a member has access to",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all cases.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleRolesJaxBean.class))) })
	public Response receiveCases(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId) {
		User user = retrieveUser(requestContext);
		Organization org = OrganizationManager.getOrganizationById(orgId);
		User userToView = UserManager.getUserById(userId);
		if (!user.isAdmin() && !org.isOwner(user) && !user.equals(userToView)) {
			return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_VIEW_MESSAGE)).build();
		}

		if (!org.isMember(userToView)) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
		}

		List<CaseModel> caseModels = OrganizationManager.getCaseModels(org, userToView);
		List<CaseExecutioner> caseExecutioners = caseModels.stream()
													.map(cm -> ExecutionService.getAllCasesOfCaseModel(cm.getId()))
													.flatMap(List::stream)
													.collect(Collectors.toList());
		
		JSONObject result = new JSONObject(new MultipleCasesJaxBean(caseExecutioners));

		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}
}
