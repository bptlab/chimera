package de.hpi.bpt.chimera.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelDetailsJaxBean;
import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.caze.CaseOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.NamedJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.AssignMemberJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.CreateOrganizationJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.MemberRoleJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.MemberRolesJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.OrganizationDetailsJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.OrganizationOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UpdateOrganizationJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserDetailsJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagment.MemberRole;
import de.hpi.bpt.chimera.usermanagment.Organization;
import de.hpi.bpt.chimera.usermanagment.OrganizationManager;
import de.hpi.bpt.chimera.usermanagment.User;
import de.hpi.bpt.chimera.usermanagment.UserManager;
import de.hpi.bpt.chimera.validation.NameValidation;

@Path("interface/v2/organizations")
public class OrganizationRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(OrganizationRestService.class);
	private static final String UNAUTHORIZED_MEMBER_MESSAGE = "You are not a member of this organization, and cannot view organizational details.";
	private static final String UNAUTHORIZED_VIEW_MESSAGE = "You are not allowed to view this information.";
	private static final String UNAUTHORIZED_CHANGE_MESSAGE = "You are not allowed to alter this information.";
	private static final String NOT_A_MEMBER_MESSAGE = "The requested user is not a member of the organization.";

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
	public Response createOrganization(@Context ContainerRequestContext requestContext, String body) {
		try {
			CreateOrganizationJaxBean bean = new CreateOrganizationJaxBean(body);
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
	public Response getOrganizations(@Context ContainerRequestContext requestContext) {
		try {
			User user = retrieveUser(requestContext);
			List<OrganizationOverviewJaxBean> resBeans = user.getOrganizations().stream()
															.map(OrganizationOverviewJaxBean::new)
															.collect(Collectors.toList());

			JSONObject result = new JSONObject();
			result.put("organizations", new JSONArray(resBeans));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
	 *         {@code orgId} is not assigned. The response code will be 403 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@GET
	@Path("{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user) && !user.isAdmin()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_MEMBER_MESSAGE)).build();
			}

			JSONObject result = new JSONObject(new OrganizationDetailsJaxBean(organization, user));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
	 *         {@code orgId} is not assigned. The response code will be 403 if
	 *         the user who send the request is not allowed to update those
	 *         information.
	 */
	@PUT
	@Path("{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String body) {
		try {
			User user = retrieveUser(requestContext);

			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not an owner of this organization, and cannot update organizational details.")).build();
			}

			UpdateOrganizationJaxBean update = new UpdateOrganizationJaxBean(body);
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
	 *         {@code orgId} is not assigned. The response code will be 403 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@DELETE
	@Path("{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization org = OrganizationManager.getOrganizationById(orgId);

			if (!org.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to delete the organization.")).build();
			}

			OrganizationManager.deleteOrganization(org);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Successfully deleted organization.\"}").build();
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
	 *         {@code orgId} is not assigned. The response code will be 403 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@GET
	@Path("{organizationId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveRoles(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user) && !user.isAdmin()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_MEMBER_MESSAGE)).build();
			}

			List<String> roleNames = organization.getRoles().stream()
										.map(MemberRole::getName)
										.collect(Collectors.toList());

			JSONObject result = new JSONObject();
			result.put("roles", new JSONArray(roleNames));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
	 *         {@code orgId} is not assigned. The response code will be 403 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@POST
	@Path("{organizationId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRole(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String body) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to add new roles.")).build();
			}

			NamedJaxBean bean = new NamedJaxBean(body);
			OrganizationManager.addRole(organization, bean.getName());
			
			List<String> roleNames = organization.getRoles().stream()
										.map(MemberRole::getName)
										.collect(Collectors.toList());

			JSONObject result = new JSONObject();
			result.put("roles", new JSONArray(roleNames));
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
	 *         {@code orgId} is not assigned. The response code will be 403 if
	 *         the user who send the request is not allowed to view this
	 *         information.
	 */
	@DELETE
	@Path("{organizationId}/roles/{roleName}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteRole(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("roleName") String roleName) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!user.isAdmin() && !organization.isOwner(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_CHANGE_MESSAGE)).build();
			}

			MemberRole role = organization.getRole(roleName);
			OrganizationManager.deleteRole(organization, role);

			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Role successfully deleted.\"}").build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

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
	 *         response code will be 403 if the user who send the request is not
	 *         allowed to view this information.
	 */
	@GET
	@Path("{organizationId}/casemodels")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveCaseModels(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @DefaultValue("") @QueryParam("filter") String filterString) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_MEMBER_MESSAGE)).build();
			}
			List<CaseModel> caseModels = OrganizationManager.getCaseModels(organization, user);

			if (!filterString.isEmpty()) {
				caseModels = caseModels.stream().filter(cm -> cm.getName().contains(filterString)).collect(Collectors.toList());
			}

			List<CaseModelOverviewJaxBean> beans = caseModels.stream()
													.map(CaseModelOverviewJaxBean::new)
													.collect(Collectors.toList());

			JSONObject result = new JSONObject();
			result.put("casemodels", new JSONArray(beans));
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
	 *         parsing. The response code will be 403 if the user who send the
	 *         request is not a member of the organization.
	 */
	@POST
	@Path("{organizationId}/casemodels")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deployCaseModel(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String jsonString) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_MEMBER_MESSAGE)).build();
			}

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
	 *         code will be 403 if the user who send the request is not allowed
	 *         to view this information.
	 */
	@GET
	@Path("{organizationId}/owners")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveOwners(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user) && !user.isAdmin()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_MEMBER_MESSAGE)).build();
			}

			List<UserOverviewJaxBean> resBeans = organization.getOwners().values().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());

			JSONObject result = new JSONObject();
			result.put("owners", new JSONArray(resBeans));
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
	 *            - {@link AssignMemberJaxBean}, the id of the user who will be
	 *            assigned as an owner
	 * @return the Response of POST. The response code will be 200 if the
	 *         creation was successful, 400 if the {@code orgId} or
	 *         {@code userId} is not assigned or if the specified user is
	 *         already an owner of the organization. The response code will be
	 *         403 if the user who send the request is not allowed to create a
	 *         new owner.
	 */
	@POST
	@Path("{organizationId}/owners")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addOwner(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String body) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to add new owners to the organization.")).build();
			}

			AssignMemberJaxBean assign = new AssignMemberJaxBean(body);
			User newOwner = UserManager.getUserById(assign.getId());
			OrganizationManager.addOwner(organization, newOwner);
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Member successfully assigned.\"}").build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

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
	 *         code will be 403 if the user who send the request is not allowed
	 *         to view this information.
	 */
	@GET
	@Path("{organizationId}/members")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveMembers(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user) && !user.isAdmin()) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_MEMBER_MESSAGE)).build();
			}

			List<UserOverviewJaxBean> resBeans = organization.getMembers().values().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());

			JSONObject result = new JSONObject();
			result.put("members", new JSONArray(resBeans));
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
	 *            - {@link AssignMemberJaxBean}, the id of the user who will be
	 *            assigned as a member
	 * @return the Response of POST. The response code will be 201 if the
	 *         request was successful and contains a JSONObject with a JSONArray
	 *         of {@link UserOverviewJaxBean} at key {@code members}. The
	 *         response will be 400 if the {@code orgId} is not assigned. The
	 *         response code will be 403 if the user who send the request is not
	 *         allowed to change this information.
	 */
	@POST
	@Path("{organizationId}/members")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignMember(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String body) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);

			if (!organization.isOwner(user)) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to assign members to the organization.")).build();
			}

			AssignMemberJaxBean assign = new AssignMemberJaxBean(body);
			User userToAssign = UserManager.getUserById(assign.getId());
			OrganizationManager.assignMember(organization, userToAssign);

			List<UserOverviewJaxBean> resBeans = organization.getMembers().values().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());

			JSONObject result = new JSONObject();
			result.put("members", new JSONArray(resBeans));
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
	 *         {@link UserDetailsJaxBean}. The response code will be 400 if the
	 *         {@code orgId} or {@code userId} is not assigned or if the user is
	 *         not a member of the organization. The response code will be 403
	 *         if the user who send the request is not allowed to view these
	 *         information.
	 */
	@GET
	@Path("{organizationId}/members/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveMember(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user) && !user.isAdmin()) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_MEMBER_MESSAGE)).build();
			}

			User userToView = UserManager.getUserById(userId);
			if (!organization.isMember(userToView)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
			}

			JSONObject result = new JSONObject(new UserDetailsJaxBean(organization, userToView));
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
	 *         403 if the user who send the request is not allowed to delete the
	 *         membership.
	 */
	@DELETE
	@Path("{organizationId}/members/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeMember(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId) {
		try {
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			User user = retrieveUser(requestContext);
			User userToDelete = UserManager.getUserById(userId);

			if (!user.equals(userToDelete) && !user.isAdmin() && !organization.isOwner(user)) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to remove the user from the organization.")).build();
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
	 *         not a member of the organization. The response code will be 403
	 *         if the user who send the request is not allowed to view these
	 *         information.
	 */
	@GET
	@Path("{organizationId}/members/{userId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveMemberRoles(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!user.isAdmin() && !organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_MEMBER_MESSAGE)).build();
			}

			User userToView = UserManager.getUserById(userId);
			if (!organization.isMember(userToView)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
			}

			List<MemberRole> roles = organization.getUserIdToRoles().get(userToView.getId());
			MemberRolesJaxBean bean = new MemberRolesJaxBean(roles);
			JSONObject result = new JSONObject(bean);

			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
	 *         assigned. The response code will be 403 if the user who send the
	 *         request is not allowed to change these information.
	 */
	@POST
	@Path("{organizationId}/members/{userId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignMemberRole(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId, String body) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!user.isAdmin() && !organization.isOwner(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_CHANGE_MESSAGE)).build();
			}

			User userToAssign = UserManager.getUserById(userId);
			if (!organization.isMember(userToAssign)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
			}

			MemberRoleJaxBean roleBean = new MemberRoleJaxBean(body);
			MemberRole role = organization.getRole(roleBean.getName());
			OrganizationManager.assignRole(organization, userToAssign, role);

			List<MemberRole> roles = organization.getUserIdToRoles().get(userToAssign.getId());
			MemberRolesJaxBean bean = new MemberRolesJaxBean(roles);
			JSONObject result = new JSONObject(bean);

			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
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
	 *         assigned. The response code will be 403 if the user who send the
	 *         request is not allowed to change these information.
	 */
	@DELETE
	@Path("{organizationId}/members/{userId}/roles/{roleName}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMemberRole(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId, @PathParam("roleName") String roleName) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!user.isAdmin() && !organization.isOwner(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_CHANGE_MESSAGE)).build();
			}

			User requestedUser = UserManager.getUserById(userId);
			if (!organization.isMember(requestedUser)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
			}

			MemberRole role = organization.getRole(roleName);
			UserManager.deleteRole(requestedUser, organization, role);

			List<MemberRole> roles = organization.getUserIdToRoles().get(requestedUser.getId());
			MemberRolesJaxBean bean = new MemberRolesJaxBean(roles);
			JSONObject result = new JSONObject(bean);

			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
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
	 *         response code will be 403 if the user who send the request is not
	 *         allowed to view these information.
	 */
	@GET
	@Path("{organizationId}/members/{userId}/cases")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveCases(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("userId") String userId) {
		try {
			User user = retrieveUser(requestContext);
			Organization org = OrganizationManager.getOrganizationById(orgId);
			User userToView = UserManager.getUserById(userId);
			if (!user.isAdmin() && !org.isOwner(user) && !user.equals(userToView)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(UNAUTHORIZED_VIEW_MESSAGE)).build();
			}

			if (!org.isMember(userToView)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(NOT_A_MEMBER_MESSAGE)).build();
			}

			List<CaseModel> caseModels = OrganizationManager.getCaseModels(org, userToView);
			List<CaseExecutioner> caseExecutioners = caseModels.stream()
														.map(cm -> ExecutionService.getAllCasesOfCaseModel(cm.getId()))
														.flatMap(List::stream)
														.collect(Collectors.toList());
			
			List<CaseOverviewJaxBean> beanArray = caseExecutioners.stream()
													.map(CaseOverviewJaxBean::new)
													.collect(Collectors.toList());
			
			JSONObject result = new JSONObject();
			result.put("cases", new JSONArray(beanArray));

			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

}
