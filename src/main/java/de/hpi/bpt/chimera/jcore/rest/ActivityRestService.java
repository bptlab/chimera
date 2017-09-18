package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.DataObjectInstance;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.jcore.ExecutionService;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.ActivityJaxBean;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.DataObjectJaxBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements the REST interface for activities.
 */
@Path("interface/v2")
public class ActivityRestService extends AbstractRestService {
	private static final String READY = "ready";
	private static final String READY_DATA = "ready(Data)";
	private static final String READY_CF = "ready(ControlFlow)";
	private static final String RUNNING = "running";
	private static final String TERMINATED = "terminated";
	private static Logger log = Logger.getLogger(RestInterface.class);

	/**
	 * Returns a JSON-Object containing information about all activity
	 * instances of a specified scenario instance.
	 * The JSON-Object will group the activities regarding their state.
	 * If the scenario instance does not exist, the response code will
	 * specify the error which occurred.
	 *
	 * @param uriInfo      The context object. It provides information
	 *                     the server context.
	 * @param scenarioID   The id of the scenario
	 * @param instanceID   The id of the instance.
	 * @param filterString Defines a search strings. Only activities
	 *                     with a label containing this String will be
	 *                     shown.
	 * @param state        The state of the instance.
	 * @return A Response with the status and content of the request.
	 * A 200 (OK) implies that the instance was found and the
	 * result contains the JSON-Object.
	 * If only the scenario ID is incorrect a 301 (REDIRECT)
	 * will point to the correct URL.
	 * If the instance ID is incorrect a 404 (NOT_FOUND) will
	 * be returned.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/activity")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivitiesOfInstance(@Context UriInfo uriInfo, @PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @QueryParam("filter") String filterString, @QueryParam("state") String stateName) {
		CaseExecutioner caseExecutioner = de.hpi.bpt.chimera.execution.ExecutionService.getCase(cmId, caseId).getCaseExecutioner();
		// common
		if (StringUtils.isEmpty(filterString)) {
			if (StringUtils.isEmpty(stateName)) {
				// for log
				return getAllActivitiesOfInstance(caseExecutioner, uriInfo);
			}
			// for execution
			return getAllActivitiesOfInstanceWithState(caseExecutioner, stateName, uriInfo);
		}
		// uncommon
		if (StringUtils.isEmpty(stateName)) {
			return getAllActivitiesOfInstanceWithFilter(caseExecutioner, filterString, uriInfo);
		}
		return getAllActivitiesWithFilterAndState(caseExecutioner, filterString, stateName, uriInfo);
	}

	/**
	 * Returns a Response Object for all activities with the instance Id. We
	 * assume that the instanceId is correct. The Response will be a 200 with
	 * json content. The Content will be a json object with information about
	 * each activity.
	 *
	 * @param instanceID
	 *            the instance id of the scenario instance.
	 * @return The Response Object, with 200 and JSON Content.
	 */
	private Response getAllActivitiesOfInstance(CaseExecutioner caseExecutioner, UriInfo uriInfo) {
		return getAllActivitiesOfInstanceWithFilter(caseExecutioner, "", uriInfo);
	}

	/**
	 * Returns a Response Object. The Response Object will be a 200 with JSON
	 * content. The Content will be a JSON Object, containing information about
	 * activities. The Label of the activities must correspond to the filter
	 * String and be part of the scenario instance specified by the instanceId.
	 * 
	 * @param caseExecutioner
	 * @param filterString
	 * @param uriInfo
	 * @return The created Response object with a 200 and a JSON.
	 */
	private Response getAllActivitiesOfInstanceWithFilter(CaseExecutioner caseExecutioner, String filterString, UriInfo uriInfo) {
		Collection<AbstractActivityInstance> activityInstances = new ArrayList<>();


		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.READY));
		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.RUNNING));
		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.TERMINATED));
		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.DATAFLOW_ENABLED));
		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.CONTROLFLOW_ENABLED));

		if (!filterString.isEmpty()) {
			activityInstances = activityInstances.stream().filter(instance -> instance.getActivity().getName().contains(filterString)).collect(Collectors.toList());
		}

		JSONObject result = buildJSONObjectForActivities(activityInstances, uriInfo);
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * This method creates a Response object for all specified activities. The
	 * activities are specified by an scenario instance and a state. In addition
	 * UriInfo object is needed in order to create the links to the activity
	 * instances.
	 * 
	 * @param caseExecutioner
	 * @param stateName
	 * @param uriInfo
	 * @return
	 */
	private Response getAllActivitiesOfInstanceWithState(CaseExecutioner caseExecutioner, String stateName, UriInfo uriInfo) {
		return getAllActivitiesWithFilterAndState(caseExecutioner, "", stateName, uriInfo);
	}

	/**
	 * Returns a Response object. The Object will be either a 200 with the
	 * activities in an JSON-Object or an 400 with an error message if the state
	 * is invalid.
	 * 
	 * @param caseExecutioner
	 * @param filterString
	 * @param stateName
	 * @param uriInfo
	 * @return
	 */
	private Response getAllActivitiesWithFilterAndState(CaseExecutioner caseExecutioner, String filterString, String stateName, UriInfo uriInfo) {
		State state = State.fromString(stateName);
		if (state == null) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The state " + "is not allowed " + state + "\"}").build();
		}
		Collection<AbstractActivityInstance> activityInstances = caseExecutioner.getAllActivitiesWithState(state);
		if (!filterString.isEmpty()) {
			activityInstances = activityInstances.stream().filter(instance -> instance.getActivity().getName().contains(filterString)).collect(Collectors.toList());
		}

		JSONObject result = buildJSONObjectForActivities(activityInstances, uriInfo);
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	/*
	 * private boolean isLegalState(String state) { List<String> allowedStates =
	 * Arrays.asList(READY, READY_DATA, READY_CF, TERMINATED, RUNNING); return
	 * allowedStates.contains(state); }
	 */

	/**
	 * Build an JSON Object for an ActivityInstance.
	 * 
	 * @param activityInstance
	 * @param state
	 * @param uriInfo
	 * @return
	 */
	private JSONObject buildActivityJson(AbstractActivityInstance activityInstance, UriInfo uriInfo) {
		JSONObject activityJSON = new JSONObject();
		activityJSON.put("id", activityInstance.getId());
		activityJSON.put("label", activityInstance.getActivity().getName());
		activityJSON.put("state", activityInstance.getState());
		activityJSON.put("fragmentInstanceId", activityInstance.getFragmentInstance().getId());
		activityJSON.put("link", uriInfo.getAbsolutePath() + "/" + activityInstance.getId());
		return activityJSON;
	}
	
	/**
	 * Builds a JSON Object for a Map with data corresponding to a set of
	 * activities.
	 *
	 * @param activityInstances
	 *            The Map containing information about the activity instances.
	 *            We Assume that the key is a the id and the value is a Map from
	 *            String to Object with the properties of the instance.
	 * @return The newly created JSON Object with the activity data.
	 */
	private JSONObject buildJSONObjectForActivities(Collection<AbstractActivityInstance> activityInstances, UriInfo uriInfo) {
		List<String> ids = new ArrayList<>(activityInstances.size());
		JSONObject activities = new JSONObject();
		for (AbstractActivityInstance activityInstance : activityInstances) {
			activities.put(activityInstance.getId(), buildActivityJson(activityInstance, uriInfo));
			ids.add(activityInstance.getId());
		}
		JSONObject result = new JSONObject();
		result.put("ids", new JSONArray(ids));
		result.put("activities", activities);
		return result;
	}

	/**
	 * This method is used to get all the information for an activity.
	 * This means the label, id and a link for the input-/outputSets.
	 *
	 * @param uriInfo            A UriInfo object, which holds the server context.
	 * @param scenarioId         The databaseID of a scenario.
	 * @param scenarioInstanceId The databaseID of a scenarioInstance.
	 * @param activityInstanceId The databaseID of an activityInstance.
	 * @return a response Object with the status code:
	 * 200 if everything was correct and holds the information about the activityInstance.
	 * A 404 Not Found is returned if the scenario/scenarioInstance/activityInstanceID is wrong.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}")
	public Response getActivity(@Context UriInfo uriInfo, @PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {

		AbstractActivityInstance activityInstance = de.hpi.bpt.chimera.execution.ExecutionService.getActivityInstance(cmId, caseId, activityInstanceId);
		if (activityInstance == null) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"There is no such " + "activity instance.\"}").build();
		}

		ActivityJaxBean activity = new ActivityJaxBean();
		activity.setId(activityInstanceId);
		activity.setLabel(activityInstance.getActivity().getName());
		activity.setInputSetLink(uriInfo.getAbsolutePath() + "/input");
		activity.setOutputSetLink(uriInfo.getAbsolutePath() + "/output");
		return Response.ok(activity, MediaType.APPLICATION_JSON).build();
	}


	/**
	 * This method updates the data attributes of a specific activity
	 * defined via its activityID.
	 *
	 * @param scenarioId         The id of a scenario model.
	 * @param scenarioInstanceId the id of an scenario instance.
	 * @param activityInstanceId the control node instance id of the activity.
	 * @param input              data input.
	 * @return Status code with regard to its success / failure
	 */
	@PUT
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}")
	public Response setDataAttribute(@PathParam("scenarioId") int scenarioId, @PathParam("instanceId") int scenarioInstanceId, @PathParam("activityInstanceId") int activityInstanceId, final String input) {
		ExecutionService executionService = ExecutionService.getInstance(scenarioId);
		executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId);

		Map<Integer, String> idToValue = new HashMap<>();
		JSONObject object = new JSONObject(input);

		for (Object key : object.keySet()) {
			String keyString = String.valueOf(key);
			idToValue.put(Integer.valueOf(keyString), object.getString(keyString));
		}

		boolean successful = executionService.setDataAttributeValues(scenarioInstanceId, activityInstanceId, idToValue);

		if (input != null && successful) {
			return this.buildAcceptedResponse("{\"message\":\"attribute value was " + "changed successfully.\"}");
		} else {
			return this.buildBadRequestResponse("{\"error\":\"error within the " + "update of attributes\"}");
		}
	}

	/**
	 * @param scenarioId         The id of the scenario.
	 * @param scenarioInstanceId The id of the scenario instance.
	 * @param activityInstanceId The id of the activity instance.
	 * @return A list with working items for the activity instance.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/workingItems")
	//TODO for some reason this appears to be the only endpoint with capital letters
	public Response getWorkingItems(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		// ExecutionService executionService =
		// ExecutionService.getInstance(scenarioId);
		// executionService.openExistingScenarioInstance(scenarioId,
		// scenarioInstanceId);

		AbstractActivityInstance activityInstance = de.hpi.bpt.chimera.execution.ExecutionService.getActivityInstance(cmId, caseId, activityInstanceId);
		Collection<DataObjectInstance> selectedInstances = activityInstance.getSelectedDataObjectInstances().values();

		List<DataObjectJaxBean> resultBeans = new ArrayList<>();
		for (DataObjectInstance instance : selectedInstances) {
			resultBeans.add(new DataObjectJaxBean(instance));
		}
		JSONArray result = new JSONArray(resultBeans);
		return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * Changes the state of an activityInstance from enabled to running.
	 *
	 * @param scenarioId         The id of a scenario model.
	 * @param scenarioInstanceId the id of an scenario instance.
	 * @param activityInstanceId the id of the activity instance.
	 * @param postBody           Json Object containing the data objects on which the activity operates
	 * @return a message regarding the success of the operation
	 * A 202 (ACCEPTED) means that the POST was successful.
	 * A 400 (BAD_REQUEST) if the transition was not allowed.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/begin")
	public Response beginActivity(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId, String postBody) {
		/*
		 * ExecutionService executionService =
		 * ExecutionService.getInstance(scenarioId); if
		 * (!executionService.openExistingScenarioInstance(scenarioId,
		 * scenarioInstanceId)) { return
		 * this.buildNotFoundResponse("{\"message\":\"Case does not exist.\"}");
		 * }
		 */
		List<String> selectedDataObjectInstanceIds = new ArrayList<>();
		JSONObject postJson = new JSONObject(postBody);
		if (postJson.has("dataobjects")) {
			JSONArray dataObjectsJson = postJson.getJSONArray("dataobjects");
			for (int i = 0; i < dataObjectsJson.length(); i++) {
				selectedDataObjectInstanceIds.add(dataObjectsJson.getString(i));
			}
		}
		// TODO: begin of activity could fail in which case another Response needs to be sent
		// ExecutionService.beginActivityInstance(scenarioInstanceId,
		// activityInstanceId, selectedDataObjectIds);
		de.hpi.bpt.chimera.execution.ExecutionService.beginActivityInstance(cmId, caseId, activityInstanceId, selectedDataObjectInstanceIds);
		return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"activity begun.\"}").build();
	}

	/**
	 * Changes the state of of an activity instance from running to terminated.
	 *
	 * @param scenarioId         Id of the scenario model.
	 * @param scenarioInstanceId Id of the model instance.
	 * @param activityInstanceId Id of the activity instance to terminate
	 * @param postBody           Json Body containing a map from name of data object to state
	 *                           specifying the resulting states of the data objects the activity works on
	 * @return 202 (ACCEPTED) means that the activity was terminated successfully
	 * 400 (BAD_REQUEST) Termination of the activity failed. Possible reasons are:
	 * 1) The activity was not running
	 * 2) The wanted state does not comply to the OLC
	 * 3) The Body specifies not a resulting state for each data object.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/instance/{instanceId}/activityinstance/{activityInstanceId}/terminate")
	public Response terminateActivity(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("activityInstanceId") String activityInstanceId, String postBody) {

		/*
		 * ExecutionService executionService =
		 * ExecutionService.getInstance(scenarioId);
		 * executionService.openExistingScenarioInstance(scenarioId,
		 * scenarioInstanceId); boolean successful;
		 */

		JSONObject postJson = new JSONObject(postBody);
		Map<String, String> dataClassNameToState = new HashMap<>();
		for (Object dataClassName : postJson.keySet()) {
			dataClassNameToState.put((String) dataClassName, postJson.getString((String) dataClassName));
		}
		de.hpi.bpt.chimera.execution.ExecutionService.terminateActivity(cmId, caseId, activityInstanceId, dataClassNameToState);
		// ExecutionService.terminateActivityInstance(scenarioInstanceId,
		// activityInstanceId, dataClassNameToState);

		return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"activity terminated.\"}").build();
	}
}
