package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.DataObjectJaxBean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("interface/v2")
public class DataDependencyWebService extends AbstractRestService {
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
                .getDataObjectInstancesForDataSetId(
                        setId, scenarioInstanceId);

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

    private DataObjectJaxBean buildDataObjectJaxBean(
            int setId, DataObject dataObjectInstance, ExecutionService executionService) {
        DataObjectJaxBean dataObject = new DataObjectJaxBean();
        dataObject.setSetId(setId);
        dataObject.setId(dataObjectInstance.getId());
        dataObject.setLabel(dataObjectInstance.getName());
        dataObject.setState(executionService
                .getStateNameForDataObjectInstanceOutput(
                        dataObjectInstance, setId));
        dataObject.setAttributeConfiguration(executionService
                .getDataAttributesForDataObjectInstance(dataObjectInstance));
        return dataObject;

    }

}
