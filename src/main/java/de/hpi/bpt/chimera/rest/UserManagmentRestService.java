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
import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.rest.beans.usermanagement.UpdateUserJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagment.User;
import de.hpi.bpt.chimera.usermanagment.UserManager;

@Path("interface/v2/users")
public class UserManagmentRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(UserManagmentRestService.class);

	/**
	 * Register a new user.
	 * 
	 * @param body
	 * @return the Response of POST. The response code will be 201 if the
	 *         request was successful and contains a JSONObject of
	 *         {@link UserOverviewJaxBean}.
	 */
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(String body) {
		try {
			JSONObject json = new JSONObject(body);
			String email = json.getString("email");
			String password = json.getString("password");
			String username = json.getString("username");
			User user = UserManager.createUser(email, password, username);
			JSONObject result = new JSONObject(new UserOverviewJaxBean(user));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Try to login a user.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @return the response of POST. The response code will be 200 if the
	 *         request was successful or 403 if the login failed.
	 */
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(@Context ContainerRequestContext requestContext) {
		try {
			retrieveUser(requestContext);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Successfully logged in.\"}").build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Receive all users that exist.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @return the response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject with a JSONArray of
	 *         {@link UserOverviewJaxBean} at key {@code users}.
	 */
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@Context ContainerRequestContext requestContext) {
		try {
			List<UserOverviewJaxBean> resBeans = UserManager.getUsers().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());

			JSONObject result = new JSONObject();
			result.put("users", new JSONArray(resBeans));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
	
	/**
	 * Receive information about a specific user.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param userId
	 *            - id of the user.
	 * @return the response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject with
	 *         {@link UserOverviewJaxBean}. The response will be 400 if the
	 *         {@code userId} is not assigned.
	 */
	@GET
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@Context ContainerRequestContext requestContext, @PathParam("userId") String userId) {
		try {
			User user = UserManager.getUserById(userId);

			JSONObject result = new JSONObject(new UserOverviewJaxBean(user));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Update information about a specific user. Therefore, the user must match
	 * the user who requests the update.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param userId
	 *            - id of the user.
	 * @param body
	 *            - {@link UpdateUserJaxBean} used for the update
	 * @return the response of GET. The response code will be 200 if the request
	 *         was successful and contains a JSONObject with
	 *         {@link UserOverviewJaxBean}. The response will be 400 if the
	 *         {@code userId} is not assigned. The response code will be 403 if
	 *         the user who send the request is not allowed to update this
	 *         information.
	 */
	@PUT
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@Context ContainerRequestContext requestContext, @PathParam("userId") String userId, String body) {
		try {
			User user = retrieveUser(requestContext);

			if (user.getId() != userId || user.isAdmin()) {
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to update the user's details.")).build();
			}

			UpdateUserJaxBean update = new UpdateUserJaxBean(body);
			user.setEmail(update.getEmail());
			user.setPassword(update.getPassword());
			JSONObject result = new JSONObject(new UserOverviewJaxBean(user));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Delete a specific user. Therefore, the user specified by the url must
	 * match the user who requests the update.
	 * 
	 * @param requestContext
	 *            - information about the request.
	 * @param userId
	 *            - id of the user.
	 * @return the response of DELETE. The response code will be 200 if the
	 *         request was successful. The response will be 400 if the
	 *         {@code userId} is not assigned. The response code will be 403 if
	 *         the user who send the request is not allowed to delete the user.
	 */
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
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

}
