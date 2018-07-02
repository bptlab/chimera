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
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.rest.beans.usermanagement.UpdateOrganizationJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UpdateUserJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagment.User;
import de.hpi.bpt.chimera.usermanagment.UserManager;

@Path("interface/v2/users")
public class UserManagmentRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(UserManagmentRestService.class);

	@POST
	@Path("")
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
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@Context ContainerRequestContext requestContext) {
		try {
			List<UserOverviewJaxBean> resBeans = UserManager.getUsers().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());
			JSONArray result = new JSONArray(resBeans);
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	@GET
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@Context ContainerRequestContext requestContext, @PathParam("userId") String userId) {
		try {
			User user = retrieveUser(requestContext);

			JSONObject res = new JSONObject(new UserOverviewJaxBean(user));
			return Response.ok(res.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@PUT
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@Context ContainerRequestContext requestContext, @PathParam("userId") String userId, String body) {
		try {
			User user = retrieveUser(requestContext);

			if (user.getId() != userId || user.isAdmin()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to update the user's details.")).build();
			}

			UpdateUserJaxBean update = new UpdateUserJaxBean(body);
			user.setEmail(update.getEmail());
			user.setPassword(update.getPassword());
			JSONObject res = new JSONObject(new UserOverviewJaxBean(user));
			return Response.ok(res.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@DELETE
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@Context ContainerRequestContext requestContext, @PathParam("userId") String userId) {
		try {
			User user = retrieveUser(requestContext);
			if (user.getId() != userId && !user.isAdmin()) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to delete the user.")).build();
			}

			User userToDelete = UserManager.getUserById(userId);
			UserManager.deleteUser(userToDelete);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Successfully deleted user.\"}").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

}
