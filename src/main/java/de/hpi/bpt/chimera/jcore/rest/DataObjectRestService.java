package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.jcore.ExecutionService;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.DataObjectJaxBean;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implements the REST interface for data objects.
 * It allows to retrieve information about data objects.
 * Note that interaction with input/output sets is handled by {@link DataDependencyRestService}
 */
@Path("interface/v2")
public class DataObjectRestService {
    /**
     * Returns a JSON-Object, which contains information about all
     * data objects of a specified scenario instance.
     * The data contains the id, label and state.
     *
     * @param scenarioID   The ID of the scenario model.
     * @param instanceID   The ID of the scenario instance.
     * @param filterString A String which specifies a filter. Only Data
     *                     Objects with a label containing this string
     *                     will be returned.
     * @param uriInfo      A Context object of the server request.
     * @return A Response with the outcome of the GET-Request. The Response
     * will be a 200 (OK) if the specified instance was found. Hence
     * the JSON-Object will be returned.
     */
    @GET
    @Path("scenario/{scenarioId}/instance/{instanceId}/dataobject")
    @Produces(MediaType.APPLICATION_JSON) public Response getDataObjects(
            @Context UriInfo uriInfo,
            @PathParam("scenarioId") int scenarioID,
            @PathParam("instanceId") int instanceID,
            @QueryParam("filter") String filterString) {

        ExecutionService executionService = ExecutionService.getInstance(scenarioID);
        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        List<Integer> dataObjects =
                executionService.getAllDataObjectIds(instanceID);
        Map<Integer, String> states =
                executionService.getDataObjectStates(instanceID);
        Map<Integer, String> labels =
                executionService.getAllDataObjectNames(instanceID);
        if (filterString != null && !filterString.isEmpty()) {
            List<Integer> oldDataObjects = new ArrayList<>(dataObjects);
            oldDataObjects.stream()
                    .filter(objectId -> !objectId.toString().contains(filterString))
                    .forEach(objectId -> {
                        dataObjects.remove(objectId);
                        states.remove(objectId);
                        labels.remove(objectId);
            });
        }
        JSONObject result = buildListForDataObjects(uriInfo, dataObjects, states, labels);
        return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
    }


    /**
     * This method provides detailed information about an data Object.
     * The information contain the id, parent scenario instance, label
     * and the current state,
     * This information will be provided as a JSON-Object.
     * A Data object is specified by:
     *
     * @param scenarioID   The scenario Model ID.
     * @param instanceID   The scenario Instance ID.
     * @param dataObjectID The Data Object ID.
     * @return Returns a JSON-Object with information about the dataObject,
     * the response code will be a 200 (OK).
     * If the data object does not exist a 404 (NOT_FOUND) will not be
     * returned.
     */
    @GET
    @Path("scenario/{scenarioId}/instance/{instanceId}/dataobject/{dataObjectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataObject(
            @PathParam("scenarioId") int scenarioID,
            @PathParam("instanceId") int instanceID,
            @PathParam("dataObjectId") int dataObjectID) {
        ExecutionService executionService = ExecutionService.getInstance(scenarioID);
        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        List<Integer> dataObjects =
                executionService.getAllDataObjectIds(instanceID);
        Map<Integer, String> states =
                executionService.getDataObjectStates(instanceID);
        Map<Integer, String> labels =
                executionService.getAllDataObjectNames(instanceID);
        if (!dataObjects.contains(dataObjectID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no dataobject "
                            + "with the id " + dataObjectID
                            + " for the scenario instance "
                            + instanceID
                            + "\"}")
                    .build();
        }
        DataObjectJaxBean dataObject = new DataObjectJaxBean();
        dataObject.setId(dataObjectID);
        dataObject.setLabel(labels.get(dataObjectID));
        dataObject.setState(states.get(dataObjectID));
        return Response.ok(dataObject, MediaType.APPLICATION_JSON).build();
    }


    /**
    * Creates an array of DataObjects.
    * The data objects will be created out of the information
    * received from the execution Service.
    * The array elements will be of type {@link DataObjectJaxBean ), hence JSON and
    * XML can be generated automatically.
    *
    * @param uriInfo       A Context object of the server request
    * @param dataObjectIds an Arraqy of IDs used for the dataobjects inside the database.
            * @param states        The states, mapped from dataobject database id to state (String)
            * @param labels        The labels, mapped from dataobject database id to label (String)
            * @return A array with a DataObject for each entry in dataObjectIds
    */
    private JSONObject buildListForDataObjects(
            UriInfo uriInfo,
            List<Integer> dataObjectIds,
            Map<Integer, String> states,
            Map<Integer, String> labels) {
        JSONObject result = new JSONObject();
        result.put("ids", dataObjectIds);
        JSONObject results = new JSONObject();
        for (Integer id : dataObjectIds) {
            JSONObject dataObject = new JSONObject();
            dataObject.put("id", id);
            dataObject.put("label", labels.get(id));
            dataObject.put("state", states.get(id));
            dataObject.put("link", uriInfo.getAbsolutePath() + "/" + id);
            results.put(String.valueOf(id), dataObject);
        }
        result.put("results", results);
        return result;
    }
}
