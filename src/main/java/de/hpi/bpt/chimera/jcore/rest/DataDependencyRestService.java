package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.DataManager;
import de.hpi.bpt.chimera.execution.DataObject;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.jcore.rest.beans.datamodel.DataAttributeJaxBean;
import de.hpi.bpt.chimera.jcore.rest.beans.datamodel.DataObjectJaxBean;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class implements the REST interface for data based elements.
 * It allows to generate and retrieve information about inputs/outputs.
 * Note that direct interaction with data objects is handled by {@link DataObjectRestService}
 */
@Path("interface/v2")
public class DataDependencyRestService extends AbstractRestService {
	/**
	 * @param scenarioId
	 *            The databaseID of the scenario.
	 * @param scenarioInstanceId
	 *            The databaseID of the scenarioInstance belonging to the
	 *            aforementioned scenario.
	 * @param activityId
	 *            The databaseID of the activity in this scenarioInstance.
	 * @return an array with the available inputs
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/availableInput")
	public Response getAvailableInput(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		try {
			CaseExecutioner caseExecutioner = de.hpi.bpt.chimera.execution.ExecutionService.getCaseExecutioner(cmId, caseId);
			AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);

			DataManager dataManager = caseExecutioner.getDataManager();
			DataStateCondition activityPreCondition = activityInstance.getControlNode().getPreCondition();
			List<ConditionSet> fulfilledConditionSets = activityPreCondition.getFulfilledConditions(dataManager.getDataStateConditions());
			Set<DataObject> availableInput = dataManager.getAvailableDataObjects(fulfilledConditionSets);

			List<DataObjectJaxBean> resultBeans = new ArrayList<>();
			for (DataObject dataObject : availableInput) {
				resultBeans.add(new DataObjectJaxBean(dataObject));
			}
			JSONArray result = new JSONArray(resultBeans);

			return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildException(e.getMessage())).build();
		}
	}

	/**
	 * This method responds to a GET request by returning an array of
	 * outputSets. Each contains the outputSetDatabaseID, the name of the
	 * dataObject and their state as a Map & a link to get the
	 * dataObjectInstances with their dataAttributesInstances. The result is
	 * determined by:
	 *
	 * @param uriInfo
	 *            A UriInfo object, which holds the server context used for the
	 *            link.
	 * @param scenarioID
	 *            The databaseID of the scenario.
	 * @param scenarioInstanceID
	 *            The databaseID of the scenarioInstance belonging to the
	 *            aforementioned scenario.
	 * @param activityInstanceId
	 *            The databaseID of the activityInstance belonging to this
	 *            scenarioInstance.
	 * @return a response consisting of: array of outputSets containing the
	 *         outputSetDatabaseID, the name of the dataObject and their state
	 *         as a Map & a link to get the dataObjectInstances with their
	 *         dataAttributesInstances. a response status code:
	 *         <p/>
	 *         A 200 if everything was correct. A 404 Not Found is returned if
	 *         the scenario/scenarioInstance/activityInstance is non-existing or
	 *         if the activity has no outputSet & with an error message instead
	 *         of the array.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/outputStates")
	public Response getOutputDataObjects(@Context UriInfo uriInfo, @PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		try {
			CaseExecutioner caseExecutioner = de.hpi.bpt.chimera.execution.ExecutionService.getCaseExecutioner(cmId, caseId);
			AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);

			Map<DataClass, Set<AtomicDataStateCondition>> dataClassToAtomicConditions = activityInstance.getControlNode().getPostCondition().getDataClassToAtomicDataStateConditions();

			JSONObject dataClassStateAssociation = new JSONObject();
			for (Entry<DataClass, Set<AtomicDataStateCondition>> dataClassToAtomicCondition : dataClassToAtomicConditions.entrySet()) {
				JSONArray conditionStateStrings = new JSONArray();
				for (AtomicDataStateCondition condition : dataClassToAtomicCondition.getValue()) {
					conditionStateStrings.put(condition.getStateName());
				}
				dataClassStateAssociation.put(dataClassToAtomicCondition.getKey().getName(), conditionStateStrings);
			}

			return Response.ok(dataClassStateAssociation.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildException(e.getMessage())).build();
		}

	}

	/**
	 * Get the DataAttributes of all DataClasses that occur in the Output
	 * Condition of the AbstractActivityInstance. If the DataAttributes are
	 * already instantiated by a DataObject in the workingItems return those.
	 * 
	 * @param cmId
	 * @param caseId
	 * @param activityInstanceId
	 * @return
	 */
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/outputAttributes")
	public Response getOuptutAttributes(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		try {
			CaseExecutioner caseExecutioner = de.hpi.bpt.chimera.execution.ExecutionService.getCaseExecutioner(cmId, caseId);
			AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);
			JSONObject result = new JSONObject();
			// early exit
			if (activityInstance.getControlNode().getPostCondition().getAtomicDataStateConditions().isEmpty()) {
				return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
			}
			Set<AtomicDataStateCondition> outputConditions = activityInstance.getControlNode().getPostCondition().getAtomicDataStateConditions();
			for (AtomicDataStateCondition outputConiditon : outputConditions) {
				JSONArray dataAttributeArray = new JSONArray();
				for (DataAttribute dataAttribute : outputConiditon.getDataClass().getDataAttributes()) {
					dataAttributeArray.put(new JSONObject(new DataAttributeJaxBean(dataAttribute)));
				}
				result.put(outputConiditon.getDataClassName(), dataAttributeArray);
			}

			List<DataObject> selectedDataObjects = activityInstance.getSelectedDataObjects();
			for (DataObject dataObject : selectedDataObjects) {
				JSONArray dataAttributeArray = new JSONArray();
				for (DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstances().values()) {
					dataAttributeArray.put(new JSONObject(new DataAttributeJaxBean(dataAttributeInstance)));
				}
				result.put(dataObject.getDataClass().getName(), dataAttributeArray);
			}

			return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildException(e.getMessage())).build();
		}
	}

	// List<DataObjectJaxBean> outputBeans =
	// possibleInputs.stream().map(x
	// -> buildDataObjectJaxBean(x,
	// executionService)).collect(Collectors.toList());
	// JSONArray array = new JSONArray(outputBeans);
}
