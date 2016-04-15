package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataObjectInstance;
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
@Path("interface/v2/scenario/{scenarioId}/instance/{instanceId}/")
public class DataDependencyWebService {
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
    @Path("inputset/{inputsetId}")
    public Response getInputDataObjectsAndAttributes(
            @PathParam("scenarioId") int scenarioId,
            @PathParam("instanceId") int scenarioInstanceId,
            @PathParam("inputsetId") int inputsetId) {
        ExecutionService executionService = ExecutionService.getInstance(scenarioId);
        if (!executionService.existScenarioInstance(scenarioId, scenarioInstanceId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such "
                            + "scenario instance.\"}")
                    .build();
        }
        executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId);

        if (executionService.getDataObjectInstancesForDataSetId(
                        inputsetId, scenarioInstanceId)	== null
                || executionService
                .getDataObjectInstancesForDataSetId(inputsetId, scenarioInstanceId)
                .length == 0) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such "
                            + "inputSet instance.\"}")
                    .build();
        }
        DataObjectInstance[] dataObjectInstances = executionService
                .getDataObjectInstancesForDataSetId(inputsetId, scenarioInstanceId);
        DataObjectJaxBean[] dataObjects = new DataObjectJaxBean[dataObjectInstances.length];
        for (int i = 0; i < dataObjectInstances.length; i++) {
            DataObjectJaxBean dataObject = new DataObjectJaxBean();
            dataObject.setSetId(inputsetId);
            dataObject.setId(dataObjectInstances[i].getDataObjectInstanceId());
            dataObject.setLabel(dataObjectInstances[i].getName());
            dataObject.setState(executionService
                    .getStateNameForDataObjectInstanceInput(
                            dataObjectInstances[i]));
            dataObject.setAttributeConfiguration(executionService
                    .getDataAttributesForDataObjectInstance(
                            dataObjectInstances[i]));
            dataObjects[i] = dataObject;
        }
        return Response.ok(dataObjects, MediaType.APPLICATION_JSON_TYPE).build();
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
    @Path("outputset/{outputsetId}")
    public Response getOutputDataObjectsAndAttributes(
            @PathParam("scenarioId") int scenarioID,
            @PathParam("instanceId") int scenarioInstanceId,
            @PathParam("outputsetId") int outputsetID) {
        ExecutionService executionService = ExecutionService.getInstance(scenarioID);
        if (!executionService.existScenarioInstance(
                scenarioID, scenarioInstanceId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such "
                            + "scenario instance.\"}")
                    .build();
        }
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceId);
        DataObjectInstance[] dataObjectInstances = executionService
                .getDataObjectInstancesForDataSetId(
                        outputsetID, scenarioInstanceId);

        if (dataObjectInstances == null || dataObjectInstances.length == 0) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no such "
                            + "outputSet instance.\"}")
                    .build();
        }
        DataObjectJaxBean[] dataObjects = new DataObjectJaxBean[dataObjectInstances.length];
        for (int i = 0; i < dataObjectInstances.length; i++) {
            DataObjectJaxBean dataObject = new DataObjectJaxBean();
            dataObject.setSetId(outputsetID);
            dataObject.setId(dataObjectInstances[i].getDataObjectInstanceId());
            dataObject.setLabel(dataObjectInstances[i].getName());
            dataObject.setState(executionService
                    .getStateNameForDataObjectInstanceOutput(
                            dataObjectInstances[i], outputsetID));
            dataObject.setAttributeConfiguration(executionService
                    .getDataAttributesForDataObjectInstance(
                            dataObjectInstances[i]));
            dataObjects[i] = dataObject;
        }
        return Response.ok(dataObjects, MediaType.APPLICATION_JSON_TYPE).build();
    }

}
