package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.jcore.ExecutionService;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.DataObjectJaxBean;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implements the REST interface for data objects.
 * It allows to retrieve information about data objects.
 * Note that interaction with input/output sets is handled by {@link DataDependencyRestService}
 */
@Path("interface/v2")
public class DataObjectRestService extends AbstractRestService {
	/**
	 * Returns a JSON-Object, which contains information about all
	 * data objects of a specified scenario instance.
	 * The data contains the id, label and state.
	 *
	 * @param scenarioId   The ID of the scenario model.
	 * @param instanceId   The ID of the scenario instance.
	 * @param filterString A String which specifies a filter. Only Data
	 *                     Objects with a label containing this string
	 *                     will be returned.
	 * @return A Response with the outcome of the GET-Request. The Response
	 * will be a 200 (OK) if the specified instance was found. Hence
	 * the JSON-Object will be returned.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/dataobject")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataObjects(@PathParam("scenarioId") int scenarioId, @PathParam("instanceId") int instanceId, @QueryParam("filter") String filterString) {

		ExecutionService executionService = ExecutionService.getInstance(scenarioId);
		executionService.openExistingScenarioInstance(scenarioId, instanceId);
		List<Integer> dataObjects = executionService.getAllDataObjectIds(instanceId);
		Map<Integer, String> states = executionService.getDataObjectStates(instanceId);
		Map<Integer, String> labels = executionService.getAllDataObjectNames(instanceId);
		if (filterString != null && !filterString.isEmpty()) {
			List<Integer> oldDataObjects = new ArrayList<>(dataObjects);
			oldDataObjects.stream().filter(objectId -> !objectId.toString().contains(filterString)).forEach(objectId -> {
				dataObjects.remove(objectId);
				states.remove(objectId);
				labels.remove(objectId);
			});
		}
		JSONObject result = buildListForDataObjects(dataObjects, states, labels);
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/dataobject/{objectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataObject(@PathParam("scenarioId") int scenarioId, @PathParam("instanceId") int instanceId, @PathParam("objectId") int objectId) {

		ExecutionService executionService = ExecutionService.getInstance(scenarioId);
		executionService.openExistingScenarioInstance(scenarioId, instanceId);
		String state = executionService.getDataObjectStates(instanceId).getOrDefault(objectId, "");
		String label = executionService.getAllDataObjectNames(instanceId).getOrDefault(objectId, "");

		if (state.isEmpty() || label.isEmpty()) {
			return this.buildBadRequestResponse("{\"error\":\"No label or state found for given data object id.\"}");
		} else {
			JSONObject result = buildJsonForDataObject(objectId, state, label);
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		}

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
	private JSONObject buildListForDataObjects(List<Integer> dataObjectIds, Map<Integer, String> states, Map<Integer, String> labels) {
		JSONObject result = new JSONObject();
		result.put("ids", dataObjectIds);
		JSONObject results = new JSONObject();
		for (Integer id : dataObjectIds) {
			JSONObject dataObject = new JSONObject();
			dataObject.put("id", id);
			dataObject.put("label", labels.get(id));
			dataObject.put("state", states.get(id));
			results.put(String.valueOf(id), dataObject);
		}
		result.put("results", results);
		return result;
	}

	private JSONObject buildJsonForDataObject(int dataObjectId, String state, String label) {
		JSONObject result = new JSONObject();
		result.put("id", dataObjectId);
		result.put("label", label);
		result.put("state", state);
		return result;
	}
}
