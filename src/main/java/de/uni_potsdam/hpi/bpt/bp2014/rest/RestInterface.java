package de.uni_potsdam.hpi.bpt.bp2014.rest;

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
import java.util.Map;


@Path("interface/v1")
public class RestInterface {

    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUser(@QueryParam("filter") String filterString) {
            String jsonRepresentation = JsonUtil.JsonWrapperArrayListHashMap(Controller.RetrieveAllItems("user"));
            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("role")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles(@QueryParam("filter") String filterString) {
        String jsonRepresentation = JsonUtil.JsonWrapperArrayListHashMap(Controller.RetrieveAllItems("role"));
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("role")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRole(
            @Context UriInfo uriInfo,
            NamedJaxBean name) {

            int roleId = Controller.CreateNewRole(name.name, name.description, name.admin_id);
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"id\":" + roleId +
                            ",\"link\":\"" + uriInfo.getAbsolutePath() + "/role/" + roleId + "\"}")
                    .build();
    }

    @PUT
    @Path("role/{roleID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoleSpecs(
            @Context UriInfo uriInfo,
            @PathParam("roleID") int roleID,
            NamedJaxBean name) {

        int roleId = Controller.UpdateRole(roleID, name.name, name.description, name.admin_id);
        return Response.status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"id\":" + roleId +
                        ",\"link\":\"" + uriInfo.getAbsolutePath() + "/role/" + roleId + "\"}")
                .build();
    }

    @PUT
    @Path("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(
            @Context UriInfo uriInfo,
            NamedJaxBean name) {

        int roleId = Controller.CreateNewUser(name.name, name.role_id, name.description);
        return Response.status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"id\":" + roleId +
                        ",\"link\":\"" + uriInfo.getAbsolutePath() + "/role/" + roleId + "\"}")
                .build();
    }

    @PUT
    @Path("user/{userID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserSpecs(
            @Context UriInfo uriInfo,
            @PathParam("userID") int userID,
            NamedJaxBean name) {

        int roleId = Controller.UpdateUser(userID, name.name, name.role_id, name.description);
        return Response.status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"id\":" + userID +
                        ",\"link\":\"" + uriInfo.getAbsolutePath() + "/user/" + userID + "\"}")
                .build();
    }


    /**********************************************************************************************
     * Helper
     */

    /**
     * Creates an array of DataObjects.
     * The data objects will be created out of the information received from the execution Service.
     * The array elements will be of type {@link RestInterface.DataObjectJaxBean), hence JSON and
     * XML can be generated automatically.
     *
     * @param dataObjectIds an Arraqy of IDs used for the dataobjects inside the database.
     * @param states        The states, mapped from dataobject database id to state (String)
     * @param labels        The labels, mapped from dataobject database id to label (String)
     * @return A array with a DataObject for each entry in dataObjectIds
     */
    private JSONObject buildListForDataObjects(
            LinkedList<Integer> dataObjectIds,
            HashMap<Integer, String> states,
            HashMap<Integer, String> labels) {
        JSONObject result = new JSONObject();
        result.put("ids", dataObjectIds);
        JSONObject results = new JSONObject();
        for (Integer id : dataObjectIds) {
            JSONObject dataObject = new JSONObject();
            dataObject.put("id", id);
            dataObject.put("label", labels.get(id));
            dataObject.put("state", states.get(id));
            results.put("" + id, dataObject);
        }
        result.put("results", results);
        return result;
    }

    /**
     * Creates a JSON object from an HashMap.
     * The keys will be listed separately.
     *
     * @param data        The HashMap which contains the data of the Object
     * @param keyLabel    The name which will be used
     * @param resultLabel The label of the results.
     * @return The newly created JSON Object.
     */
    public JSONObject mapToKeysAndResults(Map data, String keyLabel, String resultLabel) {
        JSONObject result = new JSONObject();
        result.put(keyLabel, new JSONArray(data.keySet()));
        result.put(resultLabel, data);
        return result;
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
