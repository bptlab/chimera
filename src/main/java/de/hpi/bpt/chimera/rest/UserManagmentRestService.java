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

import de.hpi.bpt.chimera.execution.exception.IllegalUserIdException;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.MessageJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.MultipleUserJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.NewUserJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UpdateUserJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagment.User;
import de.hpi.bpt.chimera.usermanagment.UserManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "users")
@ApiResponses(value = {
	@ApiResponse(
		responseCode = "400", description = "A problem occured during the processing.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class)))})
@Path("v3")
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
	@Path("users")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Register a new user",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully registered a new user.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response registerUser(
		@Parameter(description = "Information about the user to be created.", required = true) NewUserJaxBean bean) {
		try {
			String email = bean.getEmail();
			String password = bean.getPassword();
			String username = bean.getUsername();
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
	@SecurityRequirement(name = "BasicAuth")
	@Operation(
		summary = "Login a user",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully logged in.",
				content = @Content(mediaType = "application/json",	schema = @Schema(implementation = MessageJaxBean.class))),
			@ApiResponse(
				responseCode = "401", description = "A problem occured during the authentication.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))) })
	public Response login(@Context ContainerRequestContext requestContext) {
		try {
			retrieveUser(requestContext);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Successfully logged in.\"}").build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
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
	@Path("users")
	@Produces(MediaType.APPLICATION_JSON)
	@SecurityRequirement(name = "BasicAuth")
	@Operation(
		summary = "Receive all users",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all users.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleUserJaxBean.class))),
			@ApiResponse(
				responseCode = "401", description = "A problem occured during the authentication.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))) })
	public Response getUsers(@Context ContainerRequestContext requestContext) {
		try {
			List<UserOverviewJaxBean> resBeans = UserManager.getUsers().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());
			MultipleUserJaxBean resBean = new MultipleUserJaxBean(resBeans);
			JSONObject result = new JSONObject(resBean);
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
	@Path("users/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@SecurityRequirement(name = "BasicAuth")
	@Operation(
		summary = "Receive a specific user",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested information about the user.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserOverviewJaxBean.class))),
			@ApiResponse(
				responseCode = "401", description = "A problem occured during the authentication.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))),
			@ApiResponse(
				responseCode = "404", description = "The user id is not assigned.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class)))})
	public Response getUser(@Context ContainerRequestContext requestContext, @PathParam("userId") String userId) {
		try {
			User user = UserManager.getUserById(userId);

			JSONObject result = new JSONObject(new UserOverviewJaxBean(user));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
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
	@Path("users/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@SecurityRequirement(name = "BasicAuth")
	@Operation(
		summary = "Update a specific user",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully updated the specified user.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserOverviewJaxBean.class))),
			@ApiResponse(
				responseCode = "401", description = "A problem occured during the authentication.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))),
			@ApiResponse(
				responseCode = "404", description = "The user id is not assigned.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))) })
	public Response updateUser(@Context ContainerRequestContext requestContext, @PathParam("userId") String userId,
			@Parameter(description = "", required = true) UpdateUserJaxBean bean) {
		try {
			User user = retrieveUser(requestContext);
			User userToUpdate = UserManager.getUserById(userId);
			if (!user.equals(userToUpdate) && !user.isAdmin()) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to update the user's details.")).build();
			}

			// TODO: make it so that email and password can be adapted without
			// the other
			UserManager.updateUser(userToUpdate, bean.getEmail(), bean.getPassword());
			JSONObject result = new JSONObject(new UserOverviewJaxBean(userToUpdate));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalUserIdException e) {
			log.error(e);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
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
	@Path("users/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@SecurityRequirement(name = "BasicAuth")
	@Operation(
		summary = "Delete a specific user",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully deleted specified user.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class))),
			@ApiResponse(
				responseCode = "401", description = "A problem occured during the authentication.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))),
			@ApiResponse(
				responseCode = "404", description = "The user id is not assigned.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))) })
	public Response deleteUser(@Context ContainerRequestContext requestContext, @PathParam("userId") String userId) {
		try {
			User user = retrieveUser(requestContext);
			User userToDelete = UserManager.getUserById(userId);
			if (!user.equals(userToDelete) && !user.isAdmin()) {
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(buildError("You are not allowed to delete the user.")).build();
			}

			UserManager.deleteUser(userToDelete);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"Successfully deleted user.\"}").build();
		} catch (IllegalUserIdException e) {
			log.error(e);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
