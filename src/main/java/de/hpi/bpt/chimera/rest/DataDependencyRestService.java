package de.hpi.bpt.chimera.rest;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.rest.beans.datamodel.DataAttributeJaxBean;
import de.hpi.bpt.chimera.rest.beans.datamodel.DataObjectJaxBean;

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
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
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

			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildException(e.getMessage())).build();
		}
	}

	/**
	 * This method responds to a GET request by returning an possible output
	 * states and the attribute configuration for each of outputSets. Each
	 * contains the outputSetDatabaseID, the name of the dataObject and their
	 * state as a Map & a link to get the dataObjectInstances with their
	 * dataAttributesInstances. Get the DataAttributes of all DataClasses that
	 * occur in the Output Condition of the AbstractActivityInstance. If the
	 * DataAttributes are already instantiated by a DataObject in the
	 * workingItems return those. The result is determined by:
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
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/output")
	public Response getOutputDataObjects(@Context UriInfo uriInfo, @PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
			AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);

			Map<DataClass, List<ObjectLifecycleState>> possibleDataClassToObjectLifecycleStates = getPossibleObjectLifecycleTransitions(activityInstance);
			JSONObject result = new JSONObject();
			for (Map.Entry<DataClass, List<ObjectLifecycleState>> doToOlc : possibleDataClassToObjectLifecycleStates.entrySet()) {
				JSONObject output = new JSONObject();
				output.put("state", "<TO BE CREATED>");
				DataClass dataClass = doToOlc.getKey();
				List<String> olcNames = doToOlc.getValue().stream().map(ObjectLifecycleState::getName).collect(Collectors.toList());
				JSONArray states = new JSONArray(olcNames);
				output.put("states", states);

				List<DataAttributeJaxBean> attributes = dataClass.getDataAttributes().stream().map(DataAttributeJaxBean::new).collect(Collectors.toList());
				JSONArray attributeConfiguration = new JSONArray(attributes);
				output.put("attributeConfiguration", attributeConfiguration);
				result.put(dataClass.getName(), output);
			}
			
			// override attribute configuration if dataclass is instantiated by
			// data object
			for (DataObject workingItem : activityInstance.getSelectedDataObjects()) {
				DataClass dataClass = workingItem.getDataClass();
				List<DataAttributeJaxBean> attributes = workingItem.getDataAttributeInstances().stream().map(DataAttributeJaxBean::new).collect(Collectors.toList());
				JSONArray attributeConfiguration = new JSONArray(attributes);
				JSONObject output = result.getJSONObject(dataClass.getName());
				output.put("attributeConfiguration", attributeConfiguration);
				output.put("state", workingItem.getObjectLifecycleState().getName());
			}
			
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildException(e.getMessage())).build();
		}
	}

	/**
	 * Retrieve all possible ObjectLifecycle-Transitions for an ActivityInstance
	 * with the available transitions in the post condition and the working
	 * items of the instance.
	 * 
	 * @param activityInstance
	 * @return
	 */
	private Map<DataClass, List<ObjectLifecycleState>> getPossibleObjectLifecycleTransitions(AbstractActivityInstance activityInstance) {
		Map<DataClass, List<ObjectLifecycleState>> possibleTransitions = activityInstance.getControlNode().getPostCondition().getDataClassToObjectLifecycleStates();
		List<DataObject> workingItems = activityInstance.getSelectedDataObjects();

		for (DataObject dataObject : workingItems) {
			DataClass dataClass = dataObject.getDataClass();
			ObjectLifecycleState olcState = dataObject.getObjectLifecycleState();

			possibleTransitions.get(dataClass).removeIf(x -> !x.isSuccessorOf(olcState));
		}

		return possibleTransitions;
	}
}
