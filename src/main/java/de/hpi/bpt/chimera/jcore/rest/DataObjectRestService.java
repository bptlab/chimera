package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.execution.DataObjectInstance;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.DataObjectJaxBean;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

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
	public Response getDataObjects(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @DefaultValue("") @QueryParam("filter") String filterString) {

		List<DataObjectInstance> dataObjectInstances = ExecutionService.getDataObjectInstances(cmId, caseId);

		if (!filterString.isEmpty()) {
			dataObjectInstances = dataObjectInstances.stream().filter(instance -> instance.getId().contains(filterString)).collect(Collectors.toList());
		}

		JSONArray result = new JSONArray();
		for (DataObjectInstance instance : dataObjectInstances) {
			result.put(new JSONObject(new DataObjectJaxBean(instance)));
		}
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/dataobject/{objectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataObject(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("objectId") String instanceId) {

		DataObjectInstance dataObjectInstance = ExecutionService.getDataObjectInstance(cmId, caseId, instanceId);

		JSONObject result = new JSONObject(new DataObjectJaxBean(dataObjectInstance));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}
}
