package de.hpi.bpt.chimera.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.rest.beans.usermanagement.MemberOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.OrganizationDetailsJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.OrganizationOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagment.Organization;
import de.hpi.bpt.chimera.usermanagment.OrganizationManager;
import de.hpi.bpt.chimera.usermanagment.User;
import de.hpi.bpt.chimera.usermanagment.UserManager;

@Path("interface/v2")
public class UserManagmentRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(UserManagmentRestService.class);

	@POST
	@Path("register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(String body) {
		try {
			JSONObject json = new JSONObject(body);
			String email = json.getString("email");
			String password = json.getString("password");
			String username = json.getString("username");
			UserManager.createUser(email, password, username);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Successfully registered user.\"}").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(@Context ContainerRequestContext requestContext) {
		try {
			retrieveUser(requestContext);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Successfully logged in.\"}").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@Context ContainerRequestContext requestContext) {
		try {
			List<MemberOverviewJaxBean> resBeans = UserManager.getUsers().stream()
													.map(MemberOverviewJaxBean::new)
													.collect(Collectors.toList());
			JSONArray result = new JSONArray(resBeans);
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	@POST
	@Path("organizations")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrganization(@Context ContainerRequestContext requestContext, String body) {
		try {
			NamedJaxBean bean = new NamedJaxBean(body);

			if (bean.name == null || bean.name.isEmpty()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("Name is not defined.")).build();
			}

			User user = this.retrieveUser(requestContext);
			Organization organization = OrganizationManager.createOrganization(user, bean.name);
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(String.format("{\"message\":\"Organization with id %s created.\"}", organization.getId())).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("organizations")
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
	@Path("organizations/{organizationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganization(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = this.retrieveUser(requestContext);

			Organization organization = OrganizationManager.getOrganizationById(orgId);
			if (!organization.isMember(user)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to see this organization.")).build();
			}

			OrganizationDetailsJaxBean result = new OrganizationDetailsJaxBean(organization);
			return Response.ok(new JSONObject(result).toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@POST
	@Path("organizations/{organizationId}/member")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignMember(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId) {
		try {
			User user = this.retrieveUser(requestContext);
			Organization organization = OrganizationManager.getOrganizationById(orgId);

			OrganizationManager.assignMember(user, organization);
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Member assigned.\"}").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@XmlRootElement
	public static class NamedJaxBean {
		/**
		 * The name which should be assigned to the entity.
		 */
		public String name;

		public NamedJaxBean() {
		}

		public NamedJaxBean(String body) {
			try {
				setName(new JSONObject(body).getString("name"));
			} catch (Exception e) {
				throw e;
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
