package de.uni_potsdam.hpi.bpt.bp2014.rest;

import com.google.gson.Gson;
import de.uni_potsdam.hpi.bpt.bp2014.core.Controller;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Path("interface/v1")
public class RestInterface {

    /**********************************************************************************************
     * GETs
     */

    /**
     * By sending an GET on user, the method returns all available
     * resources regarding the users as json.
     *
     * @param filterString the filter params filters the result set
     * @return a json with all user information in its content
     */
    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUser(@QueryParam("filter") String filterString) {
            String jsonRepresentation = toJson(Controller.retrieveAllItems("user"));
            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }


    /**
     * By sending an GET on a user id, the method returns details
     * regarding this userID as json.
     *
     * @param filterString the filter params filters the result set
     * @param userID defining the specific resource
     * @return a json with all user information in its content
     */
    @GET
    @Path("user/{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecificUser(@QueryParam("filter") String filterString,
                                    @PathParam("userID") int userID) {
        String jsonRepresentation = toJson(Controller.retrieveItem("user", userID));
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    /**
     * By sending an GET on role, the method returns all available
     * resources regarding the users as json.
     *
     * @param filterString the filter params filters the result set
     * @return a json with all user information in its content
     */
    @GET
    @Path("role")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles(@QueryParam("filter") String filterString) {
        String jsonRepresentation = toJson(Controller.retrieveAllItems("role"));
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    /**
     * By sending an GET on a role ID, the method returns details
     * regarding this userID as json.
     *
     * @param filterString the filter params filters the result set
     * @param roleID defining resource which is supposed to be requested in detail
     * @return a json with all user information in its content
     */
    @GET
    @Path("role/{roleID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecificRole(@QueryParam("filter") String filterString,
                                    @PathParam("roleID") int roleID) {
        String jsonRepresentation = toJson(Controller.retrieveItem("role", roleID));
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    /**********************************************************************************************
     * Delete
     */

    /**
     * By sending an DELETE on a user ID, the method deletes the
     * resource and returns a status code regarding the success.
     *
     * @param userID defining resource which is supposed to be deleted
     * @return a json with all user information in its content
     * @throws Exception
     */
    @DELETE
    @Path("user/{userID}/")
    public Response deleteUser(@PathParam("userID") Integer userID) throws Exception {
        boolean result;
        result = Controller.updateUser(userID);
        if (result) {
            return Response.status(Response.Status.ACCEPTED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"user deletion successfully.\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"user deletion failed\"}")
                    .build();
        }
    }

    /**
     * By sending an DELETE on a role ID, the method deletes the
     * resource and returns a status code regarding the success.
     *
     * @param roleID defining resource which is supposed to be deleted
     * @return a json with all user information in its content
     * @throws Exception
     */
    @DELETE
    @Path("role/{roleID}/")
    public Response deleteRole(@PathParam("roleID") Integer roleID) throws Exception {
        boolean result;
        result = Controller.updateRole(roleID);
        if (result) {
            return Response.status(Response.Status.ACCEPTED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"user deletion successfully.\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"user deletion failed\"}")
                    .build();
        }
    }

    /**********************************************************************************************
     * PUTs
     */

    /**
     * By sending an PUT on role, the method creates a new
     * resource with regards to the transmitting data and
     * returns a status code regarding the success.
     *
     * @param uriInfo
     * @param name processed PUT input for a new resource
     * @return a json with all user information in its content
     */
    @PUT
    @Path("role")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRole(
            @Context UriInfo uriInfo,
            NamedJaxBean name) {

            int roleId = Controller.createNewRole(name.name, name.description, name.admin_id);
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"id\":" + roleId +
                            ",\"link\":\"" + uriInfo.getAbsolutePath() + "/" + roleId + "\"}")
                    .build();
    }

    /**
     * By sending an PUT on a role ID, the method updates the
     * resource with regards to the transmitting data and
     * returns a status code regarding the success.
     *
     * @param uriInfo
     * @param roleID defining resource which is supposed to be updated
     * @param name processed PUT input for updating defined resource
     * @return a json with all user information in its content
     */
    @PUT
    @Path("role/{roleID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoleSpecs(
            @Context UriInfo uriInfo,
            @PathParam("roleID") int roleID,
            NamedJaxBean name) {

        boolean result = Controller.updateRole(roleID, name.name, name.description, name.admin_id);

        if(!result) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The Scenario instance could not be found!\"}")
                    .build();

        } else {
            return Response.status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"The user details were updated.\"}")
                    .build();
        }
    }

    /**
     * By sending an PUT on user, the method creates a new
     * resource with regards to the transmitting data and
     * returns a status code regarding the success.
     *
     * @param uriInfo
     * @param name processed PUT input for a new resource
     * @return a json with all user information in its content
     */
    @PUT
    @Path("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(
            @Context UriInfo uriInfo,
            NamedJaxBean name) {

        int userId = Controller.createNewUser(name.name, name.role_id, name.description);

        return Response.status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"id\":" + userId +
                        ",\"link\":\"" + uriInfo.getAbsolutePath() + "/" + userId + "\"}")
                .build();
    }

    /**
     * By sending an PUT on a user ID, the method updates the
     * resource with regards to the transmitting data and
     * returns a status code regarding the success.
     *
     * @param uriInfo
     * @param userID defining resource which is supposed to be updated
     * @param name processed PUT input for updating defined resource
     * @return a json with all user information in its content
     */
    @PUT
    @Path("user/{userID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserSpecs(
            @Context UriInfo uriInfo,
            @PathParam("userID") int userID,
            NamedJaxBean name) {

        boolean result = Controller.updateUser(userID, name.name, name.role_id, name.description);

        if(!result) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The Scenario instance could not be found!\"}")
                    .build();

        } else {
            return Response.status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"The user details were updated.\"}")
                    .build();
         }
    }


    /**********************************************************************************************
     * Helper
     */
    public static String toJson(List<Map<String,Object>> content) {
        Gson gson = new Gson();
        JSONArray json = new JSONArray(content);
        return gson.toJson(json);
    }

    /**
     * A JAX bean which is used for a naming an entity.
     * Therefor a name can be transmitted.
     */
    @XmlRootElement
    public static class NamedJaxBean {
        /**
         * The name which should be assigned to the entity.
         */
        public String name;
        public String description;
        public int admin_id;
        public int role_id;
    }

    /**
     * A JAX bean which is used for dataobject data.
     * It contains the data of one dataobject.
     * It can be used to create a JSON Object
     */
    @XmlRootElement
    public static class DataObjectJaxBean {
        /**
         * The label of the data object.
         */
        public String label;
        /**
         * The id the dataobject (not the instance) has inside
         * the database
         */
        public int id;
        /**
         * The state inside the database of the dataobject
         * which is stored in the table.
         * The label not the id will be saved.
         */
        public String state;
    }
}
