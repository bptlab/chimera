package de.hpi.bpt.chimera.rest;

import java.util.ArrayList;
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

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.NamedJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.AssignMemberJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.OrganizationOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UpdateOrganizationJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagment.Organization;
import de.hpi.bpt.chimera.usermanagment.OrganizationManager;
import de.hpi.bpt.chimera.usermanagment.User;
import de.hpi.bpt.chimera.usermanagment.UserManager;

@Path("interface/v2/organizations")
public class OrganizationRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(OrganizationRestService.class);

	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrganization(@Context ContainerRequestContext requestContext, String body) {
		try {
			NamedJaxBean bean = new NamedJaxBean(body);

			if (bean.getName() == null || bean.getName().isEmpty()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("Name is not defined.")).build();
			}

			User user = this.retrieveUser(requestContext);
			Organization organization = OrganizationManager.createOrganization(user, bean.getName());
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(String.format("{\"message\":\"Organization with id %s created.\"}", organization.getId())).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganizations(@Context ContainerRequestContext requestContext) {
		try {
			User user = this.retrieveUser(requestContext);
			List<OrganizationOverviewJaxBean> resBeans = user.getOrganizations().stream()
															.map(OrganizationOverviewJaxBean::new)
															.collect(Collectors.toList());
			JSONArray result = new JSONArray(resBeans);
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = this.retrieveUser(requestContext);

			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not a member of this organization, and cannot view organizational details.")).build();
			}

			JSONObject res = new JSONObject(new OrganizationOverviewJaxBean(organization));
			// OrganizationJSONObject res = new OrganizationJSONObject(organization, requestContext.getUriInfo());
			return Response.ok(res.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@PUT
	@Path("{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String body) {
		try {
			User user = retrieveUser(requestContext);

			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isOwner(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not an owner of this organization, and cannot update organizational details.")).build();
			}

			UpdateOrganizationJaxBean update = new UpdateOrganizationJaxBean(body);
			organization.setName(update.getName());
			organization.setDescription(update.getDescription());

			JSONObject res = new JSONObject(new OrganizationOverviewJaxBean(organization));
			return Response.ok(res.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@DELETE
	@Path("{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization org = OrganizationManager.getOrganizationById(orgId);

			if (org.isOwner(user) && !user.isAdmin()) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to delete the organization.")).build();
			}

			OrganizationManager.deleteOrganization(org);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Successfully deleted organization.\"}").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{organizationId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveRoles(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not a member of this organization, and cannot view organizational details.")).build();
			}

			JSONArray result = new JSONArray(organization.getRoles());
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@POST
	@Path("{organizationId}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRole(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String body) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isOwner(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to add new roles.")).build();
			}

			NamedJaxBean bean = new NamedJaxBean(body);
			OrganizationManager.addRole(organization, bean.getName());
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Role successfully created.\"}").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{organizationId}/casemodels")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveCaseModels(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @DefaultValue("") @QueryParam("filter") String filterString) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not a member of this organization, and cannot view organizational details.")).build();
			}

			List<CaseModel> caseModels = new ArrayList<>(organization.getCaseModels().values());
			if (!filterString.isEmpty()) {
				caseModels = caseModels.stream().filter(cm -> cm.getName().contains(filterString)).collect(Collectors.toList());
			}

			caseModels.sort((c1, c2) -> c1.getDeployment().compareTo(c2.getDeployment()));

			JSONArray result = new JSONArray();
			for (CaseModel cm : caseModels) {
				result.put(new JSONObject(new CaseModelOverviewJaxBean(cm)));
			}

			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@POST
	@Path("{organizationId}/casemodels")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deployCaseModel(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String jsonString) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not a member of this organization, and cannot view organizational details.")).build();
			}

			CaseModel cm = CaseModelManager.parseCaseModel(jsonString);
			cm.setOrganization(organization);
			organization.getCaseModels().put(cm.getId(), cm);
			log.info("Successfully parsed a CaseModel");
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"case model deployed.\"}").build();
		} catch (Exception e) {
			log.error("Chimera failed to parse the CaseModel!", e);
			return Response.status(422).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{organizationId}/owners")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveOwners(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not a member of this organization, and cannot view organizational details.")).build();
			}

			List<UserOverviewJaxBean> resBeans = organization.getOwners().values().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());

			JSONArray result = new JSONArray(resBeans);
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	@POST
	@Path("{organizationId}/owners")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addOwner(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, String body) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isOwner(user)) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to add new owners to the organization.")).build();
			}

			AssignMemberJaxBean assign = new AssignMemberJaxBean(body);
			User newOwner = UserManager.getUserById(assign.getId());
			OrganizationManager.addOwner(organization, newOwner);
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Member successfully assigned.\"}").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{organizationId}/members")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveMembers(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not a member of this organization, and cannot view organizational details.")).build();
			}

			List<UserOverviewJaxBean> resBeans = organization.getMembers().values().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());

			JSONArray result = new JSONArray(resBeans);
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

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
			OrganizationManager.assignMember(userToAssign, organization);
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Member successfully assigned.\"}").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
