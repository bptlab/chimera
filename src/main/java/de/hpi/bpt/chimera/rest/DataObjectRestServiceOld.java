package de.hpi.bpt.chimera.rest;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.rest.beans.datamodel.DataObjectJaxBean;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the REST interface for data objects.
 * It allows to retrieve information about data objects.
 * Note that interaction with input/output sets is handled by {@link DataDependencyRestService}
 */
@Path("interface/v2")
public class DataObjectRestServiceOld extends AbstractRestService {
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
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

			DataManager dataManager = caseExecutioner.getDataManager();
			List<DataObject> dataObjects = dataManager.getDataObjects();

			if (!filterString.isEmpty()) {
				dataObjects = dataObjects.stream().filter(instance -> instance.getId().contains(filterString)).collect(Collectors.toList());
			}

			JSONArray result = new JSONArray();
			for (DataObject dataObject : dataObjects) {
				result.put(new JSONObject(new DataObjectJaxBean(dataObject)));
			}
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/dataobject/{dataObjectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataObject(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("dataObjectId") String dataObjectId) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

			DataManager dataManager = caseExecutioner.getDataManager();
			DataObject dataObject = dataManager.getDataObjectById(dataObjectId);

			JSONObject result = new JSONObject(new DataObjectJaxBean(dataObject));
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
