package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataConditions;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.DataObjectJaxBean;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@Path("interface/v2")
public class DataDependencyRestService extends AbstractRestService {
    /**
     * This method responds to a GET request
     * by returning an array of dataObjectsInstances with their dataAttributeInstances
     * belonging to an inputSet.
     * The outcome is specified by:
     *
     * @param scenarioId         This is the databaseID of the scenario.
     * @param scenarioInstanceId This is the databaseID of the scenarioInstance of the
     *                              aforementioned scenario.
     * @param inputsetId         This is the databaseID of an inputSet belonging to this
     *                              scenarioInstance.
     * @return a response consisting of:
     * an array of dataObjectsInstances with their dataAttributeInstances [also as an array].
     * a response status code:
     * A 200 if everything is correct.
     * A 404 Not Found is returned if the scenario/scenarioInstance/inputSetInstance
     * is non-existing & with an error message instead of the array.
     */
    @GET
    @Path("scenario/{scenarioId}/instance/{instanceId}/inputset/{inputsetId}")
    public Response getInputDataObjectsAndAttributes(
            @PathParam("scenarioId") int scenarioId,
            @PathParam("instanceId") int scenarioInstanceId,
            @PathParam("inputsetId") int inputsetId) {
        ExecutionService executionService = ExecutionService.getInstance(scenarioId);
        executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId);
        return buildDataSetResponse(inputsetId, executionService, "inputSet", scenarioInstanceId);
    }

    /**
     * This method responds to a GET request
     * by returning an array of dataObjectsInstances with their dataAttributeInstances
     * belonging to an outputSet.
     * The outcome is specified by:
     *
     * @param scenarioID         This is the databaseID of the scenario.
     * @param scenarioInstanceId This is the databaseID of the scenarioInstance of the
     *                              aforementioned scenario.
     * @param outputsetID        This is the databaseID of an outputSet belonging to this
     *                              scenarioInstance.
     * @return a response consisting of:
     * an array of dataObjectsInstances with their dataAttributeInstances also as an array.
     * a response status code:
     * A 200 if everything is correct.
     * A 404 Not Found is returned if the scenario/scenarioInstance/outputSetInstance
     * is non-existing with an error message instead of the array.
     */
    @GET
    @Path("scenario/{scenarioId}/instance/{instanceId}/outputset/{outputsetId}")
    public Response getOutputDataObjectsAndAttributes(
            @PathParam("scenarioId") int scenarioID,
            @PathParam("instanceId") int scenarioInstanceId,
            @PathParam("outputsetId") int outputsetID) {
        ExecutionService executionService = ExecutionService.getInstance(scenarioID);
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceId);
        return buildDataSetResponse(outputsetID, executionService, "outputSet", scenarioInstanceId);
    }

    private Response buildDataSetResponse(
            int setId, ExecutionService executionService, String setType, int scenarioInstanceId) {
        DataObject[] dataObjectInstances = executionService
                .getDataObjectInstancesForDataSetId(setId, scenarioInstanceId);

        if (dataObjectInstances == null || dataObjectInstances.length == 0) {
            return this.buildNotFoundResponse("{\"error\":\"There is no such " + setType
                    + " instance.\"}");
        }
        DataObjectJaxBean[] dataObjects = new DataObjectJaxBean[dataObjectInstances.length];
        for (int i = 0; i < dataObjectInstances.length; i++) {
            dataObjects[i] = buildDataObjectJaxBean(setId, dataObjectInstances[i], executionService);
        }
        return Response.ok(dataObjects, MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * This method responds to a GET request by returning an array of inputSets.
     * Each contains the inputSetDatabaseID, the name of the dataObject and their state
     * as a Map & a link to get the dataObjectInstances with their dataAttributesInstances.
     * The result is determined by:
     *
     * @param uriInfo            A UriInfo object that holds the server context
     *                              used for the link.
     * @param scenarioId         The databaseID of the scenario.
     * @param instanceId The databaseID of the scenarioInstance belonging to the
     *                              aforementioned scenario.
     * @param activityInstanceId         The databaseID of the activityInstance belonging to this
     *                              scenarioInstance.
     * @return a response consisting of:
     * array of inputSets containing the inputSetDatabaseID, the name of the dataObject
     * and their state as a Map & a link to get the dataObjectInstances with their
     * dataAttributesInstances.
     * a response status code:
     * <p/>
     * A 200 if everything was correct.
     * A 404 Not Found is returned if the scenario/scenarioInstance/activityInstance
     * is non-existing or if the activity has no inputSet & with an error message
     * instead of the array.
     */
    @GET
    @Path("scenario/{scenarioId}/instance/{instanceId}/activity/{activityId}/input")
    public Response getInputDataObjects(
            @Context UriInfo uriInfo,
            @PathParam("scenarioId") int scenarioId,
            @PathParam("instanceId") int instanceId,
            @PathParam("activityId") int activityInstanceId) {

        ExecutionService executionService = ExecutionService.getInstance(scenarioId);

        if (!executionService.testActivityInstanceExists(activityInstanceId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such "
                            + "activity instance.\"}")
                    .build();
        }
        Map<String, Set<String>> inputSets = new DbDataConditions().loadInputSets(activityInstanceId);
        return Response.ok(buildIOJson(inputSets), MediaType.APPLICATION_JSON).build();
    }


    /**
     * This method responds to a GET request by returning an array of outputSets.
     * Each contains the outputSetDatabaseID, the name of the dataObject and their
     * state as a Map & a link to get the dataObjectInstances with their
     * dataAttributesInstances.
     * The result is determined by:
     *
     * @param uriInfo            A UriInfo object, which holds the server context used
     *                              for the link.
     * @param scenarioID         The databaseID of the scenario.
     * @param scenarioInstanceID The databaseID of the scenarioInstance belonging to the
     *                              aforementioned scenario.
     * @param activityInstanceId         The databaseID of the activityInstance belonging to this
     *                              scenarioInstance.
     * @return a response consisting of:
     * array of outputSets containing the outputSetDatabaseID, the name of the dataObject
     * and their state as a Map & a link to get the dataObjectInstances
     * with their dataAttributesInstances.
     * a response status code:
     * <p/>
     * A 200 if everything was correct.
     * A 404 Not Found is returned if the scenario/scenarioInstance/activityInstance
     * is non-existing or if the activity has no outputSet & with an error message
     * instead of the array.
     */
    @GET
    @Path("scenario/{scenarioId}/instance/{instanceId}/activity/{activityId}/output")
    public Response getOutputDataObjects(
            @Context UriInfo uriInfo,
            @PathParam("scenarioId") int scenarioID,
            @PathParam("instanceId") int scenarioInstanceID,
            @PathParam("activityId") int activityInstanceId) {

        ExecutionService executionService = ExecutionService.getInstance(scenarioID);
        if (!executionService.testActivityInstanceExists(activityInstanceId)) {
            return this.buildNotFoundResponse("{\"error\":\"There is no such "
                    + "activity instance.\"}");
        }
        Map<String, Set<String>> outputSets = new DbDataConditions()
                .loadOutputSets(activityInstanceId);
        return Response.ok(buildIOJson(outputSets), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("scenario/{scenarioId}/instance/{instanceId}/activity/{activityId}/availableInput")
    public Response getAvailableInput(@PathParam("scenarioId") int scenarioId,
                                      @PathParam("instanceId") int scenarioInstanceId,
                                      @PathParam("activityId") int activityInstanceId) {
        ExecutionService executionService = ExecutionService.getInstance(scenarioId);
        executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId);
        ScenarioInstance scenarioInstance = executionService.getScenarioInstance(
                scenarioInstanceId);
        List<DataObject> possibleInputs = scenarioInstance.getDataManager()
                .getAvailableInput(activityInstanceId);
        return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON)
                .entity(possibleInputs).build();
    }

    private DataObjectJaxBean buildDataObjectJaxBean(
            int setId, DataObject dataObjectInstance, ExecutionService executionService) {
        DataObjectJaxBean dataObject = new DataObjectJaxBean();
        dataObject.setSetId(setId);
        dataObject.setId(dataObjectInstance.getId());
        dataObject.setLabel(dataObjectInstance.getName());
        dataObject.setState(dataObjectInstance.getStateName());
        dataObject.setAttributeConfiguration(executionService
                .getDataAttributesForDataObjectInstance(dataObjectInstance));
        return dataObject;
    }

    private String buildIOJson(Map<String, Set<String>> ioSets) {
        JSONObject object = new JSONObject();
        for (Map.Entry<String, Set<String>> entry : ioSets.entrySet()) {
            object.put(entry.getKey(), new JSONArray(entry.getValue()));
        }
        return object.toString();
    }
}
