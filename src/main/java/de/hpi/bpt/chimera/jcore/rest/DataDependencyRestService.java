package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.database.data.DbDataConditions;
import de.hpi.bpt.chimera.execution.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.DataObjectInstance;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.jcore.ExecutionService;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.DataObjectJaxBean;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class implements the REST interface for data based elements.
 * It allows to generate and retrieve information about inputs/outputs.
 * Note that direct interaction with data objects is handled by {@link DataObjectRestService}
 */
@Path("interface/v2")
public class DataDependencyRestService extends AbstractRestService {

	/**
	 * This method responds to a GET request by returning an array of inputSets.
	 * Each contains the inputSetDatabaseID, the name of the dataObject and their state
	 * as a Map & a link to get the dataObjectInstances with their dataAttributesInstances.
	 * The result is determined by:
	 *
	 * @param uriInfo            A UriInfo object that holds the server context
	 *                           used for the link.
	 * @param scenarioId         The databaseID of the scenario.
	 * @param instanceId         The databaseID of the scenarioInstance belonging to the
	 *                           aforementioned scenario.
	 * @param activityInstanceId The databaseID of the activityInstance belonging to this
	 *                           scenarioInstance.
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
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/input")
	public Response getInputDataObjects(@Context UriInfo uriInfo, @PathParam("scenarioId") int scenarioId, @PathParam("instanceId") int instanceId, @PathParam("activityInstanceId") int activityInstanceId) {

		int activityId = new DbControlNodeInstance().getControlNodeId(activityInstanceId);
		Map<String, Set<String>> inputSets = new DbDataConditions().loadInputSets(activityId);
		if (inputSets.size() == 0) {
			String errorMsg = "{\"error\":\"There is no input set for activity instance %d\"}";
			errorMsg = String.format(errorMsg, activityInstanceId);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(errorMsg).build();
		}
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
	 *                           for the link.
	 * @param scenarioID         The databaseID of the scenario.
	 * @param scenarioInstanceID The databaseID of the scenarioInstance belonging to the
	 *                           aforementioned scenario.
	 * @param activityInstanceId The databaseID of the activityInstance belonging to this
	 *                           scenarioInstance.
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
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/output")
	public Response getOutputDataObjects(@Context UriInfo uriInfo, @PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {

		AbstractActivityInstance activityInstance = de.hpi.bpt.chimera.execution.ExecutionService.getActivityInstance(cmId, caseId, activityInstanceId);

		List<DataNode> outgoingDataNodes = activityInstance.getActivity().getOutgoingDataNodes();

		Map<String, Set<String>> dataClassStateAssociation = new HashMap<>();
		for (DataNode dataNode : outgoingDataNodes) {
			if (dataClassStateAssociation.containsKey(dataNode.getDataClass().getName())) {
				Set<String> states = dataClassStateAssociation.get(dataNode.getDataClass().getName());
				states.add(dataNode.getObjectLifecycleState().getName());
			} else {
				Set<String> states = new HashSet<>();
				states.add(dataNode.getObjectLifecycleState().getName());
				dataClassStateAssociation.put(dataNode.getDataClass().getName(), states);
			}
		}

		return Response.ok(buildIOJson(dataClassStateAssociation), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * @param scenarioId         The databaseID of the scenario.
	 * @param scenarioInstanceId The databaseID of the scenarioInstance belonging
	 *                           to the aforementioned scenario.
	 * @param activityId         The databaseID of the activity in this scenarioInstance.
	 * @return an array with the available inputs
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/availableInput")
	public Response getAvailableInput(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		// AbstractActivityInstance activityInstance =
		// de.hpi.bpt.chimera.execution.ExecutionService.getActivityInstance(cmId,
		// caseId, activityInstanceId);
		List<DataObjectInstance> availableInput = de.hpi.bpt.chimera.execution.ExecutionService.getAvailableInputForAcitivityInstance(cmId, caseId, activityInstanceId);

		List<DataObjectJaxBean> resultBeans = new ArrayList<>();
		for (DataObjectInstance instance : availableInput) {
			resultBeans.add(new DataObjectJaxBean(instance));
		}
		JSONArray result = new JSONArray(resultBeans);

		// List<DataObjectJaxBean> outputBeans = possibleInputs.stream().map(x
		// -> buildDataObjectJaxBean(x,
		// executionService)).collect(Collectors.toList());
		// JSONArray array = new JSONArray(outputBeans);
		return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * Create a JSONObject for a DataObjectInstance.
	 * 
	 * @param instance
	 * @return JSONObject
	 */
	private JSONObject buildJsonForDataObjectInstance(DataObjectInstance instance) {
		JSONObject result = new JSONObject();
		result.put("id", instance.getId());
		result.put("label", instance.getDataClass().getName());
		result.put("state", instance.getObjectLifecycleState().getName());
		
		JSONArray attributeConfiguration = new JSONArray();
		for (DataAttributeInstance attributeInstance : instance.getDataAttributeInstances()) {
			JSONObject attribute = new JSONObject();
			attribute.put("name", attributeInstance.getDataAttribute().getName());
			attribute.put("id", attributeInstance.getId());
			attribute.put("type", attributeInstance.getDataAttribute().getType());
			Object value = attributeInstance.getValue();
			if (value == null)
				value = "";
			attribute.put("value", value);
			attributeConfiguration.put(attribute);
		}
		result.put("attributeConfiguration", attributeConfiguration);
		return result;
	}

	private String buildIOJson(Map<String, Set<String>> ioSets) {
		JSONObject object = new JSONObject();
		for (Map.Entry<String, Set<String>> entry : ioSets.entrySet()) {
			object.put(entry.getKey(), new JSONArray(entry.getValue()));
		}
		return object.toString();
	}
}
