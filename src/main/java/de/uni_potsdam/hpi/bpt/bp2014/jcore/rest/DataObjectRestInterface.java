package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.DataAttributeJaxBean;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.DataObjectJaxBean;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 */
public class DataObjectRestInterface {
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
     * If the instance does exist but some params are wrong a 301
     * (REDIRECT) will be returned.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/dataobject/{dataObjectID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataObject(
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID,
            @PathParam("dataObjectID") int dataObjectID) {

        ExecutionService executionService = ExecutionService.getInstance(scenarioID);
        if (!executionService.existScenarioInstance(instanceID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no instance "
                            + "with the id " + instanceID + "\"}")
                    .build();
        } else if (!executionService.existScenario(scenarioID)) {
            try {
                return Response.seeOther(new URI("interface/v2/scenario/"
                        + executionService
                        .getScenarioIDForScenarioInstance(
                                instanceID)
                        + "/instance/" + instanceID
                        + "/dataobject/" + dataObjectID))
                        .build();
            } catch (URISyntaxException e) {
                return Response.serverError().build();
            }
        }
        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        LinkedList<Integer> dataObjects =
                executionService.getAllDataObjectIDs(instanceID);
        Map<Integer, String> states =
                executionService.getAllDataObjectStates(instanceID);
        Map<Integer, String> labels =
                executionService.getAllDataObjectNames(instanceID);
        if (!dataObjects.contains(new Integer(dataObjectID))) {
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
        dataObject.setSetId(0);
        dataObject.setId(dataObjectID);
        dataObject.setLabel(labels.get(dataObjectID));
        dataObject.setState(states.get(dataObjectID));
        return Response.ok(dataObject, MediaType.APPLICATION_JSON).build();
    }

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
     * It will be a 301 (REDIRECT) if the scenarioID is wrong.
     * And a 404 if the instance id is wrong.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/dataobject")
    @Produces(MediaType.APPLICATION_JSON) public Response getDataObjects(
            @Context UriInfo uriInfo,
            @PathParam("scenarioID") int scenarioID,
            @PathParam("instanceID") int instanceID,
            @QueryParam("filter") String filterString) {

        ExecutionService executionService = ExecutionService.getInstance(scenarioID);
        if (!executionService.existScenarioInstance(instanceID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no instance "
                            + "with the id " + instanceID + "\"}")
                    .build();
        } else if (!executionService.existScenario(scenarioID)) {
            try {
                return Response.seeOther(new URI("interface/v2/scenario/"
                        + executionService
                        .getScenarioIDForScenarioInstance(
                                instanceID)
                        + "/instance/" + instanceID + "/dataobject"))
                        .build();
            } catch (URISyntaxException e) {
                return Response.serverError().build();
            }
        }

        executionService.openExistingScenarioInstance(scenarioID, instanceID);
        LinkedList<Integer> dataObjects =
                executionService.getAllDataObjectIDs(instanceID);
        Map<Integer, String> states =
                executionService.getAllDataObjectStates(instanceID);
        Map<Integer, String> labels =
                executionService.getAllDataObjectNames(instanceID);
        if (filterString != null && !filterString.isEmpty()) {
            for (Map.Entry<Integer, String> labelEntry : labels.entrySet()) {
                if (!labelEntry.getValue().contains(filterString)) {
                    dataObjects.remove(labelEntry.getKey());
                    states.remove(labelEntry.getKey());
                    labels.remove(labelEntry.getKey());
                }
            }
        }
        JSONObject result = buildListForDataObjects(uriInfo, dataObjects, states, labels);
        return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
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
                LinkedList<Integer> dataObjectIds,
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
                results.put("" + id, dataObject);
            }
            result.put("results", results);
            return result;
        }



}
