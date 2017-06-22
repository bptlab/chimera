package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.jcore.ExecutionService;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.ActivityJaxBean;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.DataObjectJaxBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.*;
import java.util.stream.Collectors;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.sql.*;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import de.hpi.bpt.chimera.database.ConnectionWrapper;
import javax.sql.rowset.serial.SerialBlob;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import org.apache.commons.io.*;
import java.io.FileInputStream;

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
	public Response getActivitiesOfInstance(@Context UriInfo uriInfo, @PathParam("scenarioId") int scenarioID, @PathParam("instanceId") int instanceID, @QueryParam("filter") String filterString, @QueryParam("state") String state) {
		if (StringUtils.isEmpty(filterString)) {
			if (StringUtils.isEmpty(state)) {
				return getAllActivitiesOfInstance(scenarioID, instanceID, uriInfo);
			}
			return getAllActivitiesOfInstanceWithState(scenarioID, instanceID, state, uriInfo);
		}
		if (StringUtils.isEmpty(state)) {
			return getAllActivitiesOfInstanceWithFilter(scenarioID, instanceID, filterString, uriInfo);
		}
		return getAllActivitiesWithFilterAndState(scenarioID, instanceID, filterString, state, uriInfo);
	}

	/**
	 * Returns a Response object.
	 * The Object will be either a 200 with the activities in an JSON-Object
	 * or an 400 with an error message if the state is invalid
	 *
	 * @param instanceID   The id of the scenario instance
	 * @param filterString the filter string to be applied
	 * @param state        the state of the activity
	 * @return The Response object as described above.
	 */
	private Response getAllActivitiesWithFilterAndState(int scenarioID, int instanceID, String filterString, String state, UriInfo uriInfo) {
		Collection<ActivityInstance> instances = getActivitiesOfState(state, scenarioID, instanceID);
		if (!isLegalState(state)) {
			this.buildNotFoundResponse("{\"error\":\"The state is not allowed " + state + "\"}");
		}
		Collection<ActivityInstance> selection = instances.stream().filter(instance -> instance.getLabel().contains(filterString)).collect(Collectors.toList());
		JSONObject result = buildJSONObjectForActivities(selection, state, uriInfo);
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	private boolean isLegalState(String state) {
		List<String> allowedStates = Arrays.asList(READY, READY_DATA, READY_CF, TERMINATED, RUNNING);
		return allowedStates.contains(state);
	}

	private Collection<ActivityInstance> getActivitiesOfState(String state, int scenarioId, int scenarioInstanceId) {
		ExecutionService executionService = ExecutionService.getInstance(scenarioId);
		executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId);
		switch (state) {
			case READY:
				return executionService.getEnabledActivities(scenarioInstanceId);
			case TERMINATED:
				return executionService.getTerminatedActivities(scenarioInstanceId);
			case RUNNING:
				return executionService.getRunningActivities(scenarioInstanceId);
			case READY_DATA:
				return executionService.getDataEnabledActivities(scenarioInstanceId);
			case READY_CF:
				return executionService.getControlFlowEnabledActivities(scenarioInstanceId);
			default:
				throw new IllegalArgumentException("State has to be one of ready, terminated or running");
		}
	}


	/**
	 * Returns a Response Object.
	 * The Response Object will be a 200 with JSON content.
	 * The Content will be a JSON Object, containing information about activities.
	 * The Label of the activities mus correspond to the filter String and be
	 * part of the scenario instance specified by the instanceId.
	 *
	 * @param instanceId   The id of the scenario instance.
	 * @param filterString The string which will be the filter condition for the activity ids.
	 * @return The created Response object with a 200 and a JSON.
	 */
	private Response getAllActivitiesOfInstanceWithFilter(int scenarioId, int instanceId, String filterString, UriInfo uriInfo) {
		ExecutionService executionService = ExecutionService.getInstance(scenarioId);
		executionService.openExistingScenarioInstance(scenarioId, instanceId);
		Map<String, Collection<ActivityInstance>> stateToActivities = new HashMap<>();
		stateToActivities.put(READY, executionService.getEnabledActivities(instanceId));
		stateToActivities.put(RUNNING, executionService.getRunningActivities(instanceId));
		stateToActivities.put(TERMINATED, executionService.getTerminatedActivities(instanceId));
		stateToActivities.put(READY_DATA, executionService.getDataEnabledActivities(instanceId));
		stateToActivities.put(READY_CF, executionService.getControlFlowEnabledActivities(instanceId));

		JSONArray ids = new JSONArray();
		JSONObject activities = new JSONObject();
		for (Map.Entry<String, Collection<ActivityInstance>> entry : stateToActivities.entrySet()) {
			entry.getValue().stream().filter(instance -> instance.getLabel().contains(filterString)).forEach(instance -> {
				ids.put(instance.getControlNodeInstanceId());
				JSONObject activityJson = buildActivityJson(entry.getKey(), instance, uriInfo);
				activities.put(String.valueOf(instance.getControlNodeInstanceId()), activityJson);
			});
		}
		JSONObject result = new JSONObject();
		result.put("ids", ids);
		result.put("activities", activities);
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	private JSONObject buildActivityJson(String state, ActivityInstance instance, UriInfo uriInfo) {
		JSONObject activityJSON = new JSONObject();
		activityJSON.put("id", instance.getControlNodeInstanceId());
		activityJSON.put("label", instance.getLabel());
		activityJSON.put("state", state);
		activityJSON.put("link", uriInfo.getAbsolutePath() + "/" + instance.getControlNodeInstanceId());
		return activityJSON;
	}

	/**
	 * This method creates a Response object for all specified activities.
	 * The activities are specified by an scenario instance and a state.
	 * In addition UriInfo object is needed in order to create the links
	 * to the activity instances.
	 *
	 * @param scenarioID The ID of the scenario (model).
	 * @param instanceID The ID of the scenario instance.
	 * @param state      A String identifying the state.
	 * @param uriInfo    A UriInfo object, which holds the server context.
	 * @return A Response object, which is either a 404 if the state is invalid,
	 * or a 200 if with json content.
	 */
	private Response getAllActivitiesOfInstanceWithState(int scenarioID, int instanceID, String state, UriInfo uriInfo) {
		ExecutionService executionService = ExecutionService.getInstance(scenarioID);
		executionService.openExistingScenarioInstance(scenarioID, instanceID);
		Collection<ActivityInstance> instances;
		switch (state) {
			case READY:
				instances = executionService.getEnabledActivities(instanceID);
				break;
			case TERMINATED:
				instances = executionService.getTerminatedActivities(instanceID);
				break;
			case RUNNING:
				instances = executionService.getRunningActivities(instanceID);
				break;
			case READY_CF:
				instances = executionService.getControlFlowEnabledActivities(instanceID);
				break;
			case READY_DATA:
				instances = executionService.getDataEnabledActivities(instanceID);
				break;
			default:
				return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"The state " + "is not allowed " + state + "\"}").build();
		}
		JSONObject result = buildJSONObjectForActivities(instances, state, uriInfo);
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Returns a Response Object for all activities with the instance Id.
	 * We assume that the instanceId is correct.
	 * The Response will be a 200 with json content.
	 * The Content will be a json object with information about each activity.
	 *
	 * @param instanceID the instance id of the scenario instance.
	 * @return The Response Object, with 200 and JSON Content.
	 */
	private Response getAllActivitiesOfInstance(int scenarioID, int instanceID, UriInfo uriInfo) {
		ExecutionService executionService = ExecutionService.getInstance(scenarioID);
		executionService.openExistingScenarioInstance(scenarioID, instanceID);
		Map<String, Collection<ActivityInstance>> instances = new HashMap<>();
		instances.put(READY, executionService.getEnabledActivities(instanceID));
		instances.put(RUNNING, executionService.getRunningActivities(instanceID));
		instances.put(TERMINATED, executionService.getTerminatedActivities(instanceID));
		JSONArray ids = new JSONArray();
		JSONObject activities = new JSONObject();
		for (Map.Entry<String, Collection<ActivityInstance>> entry : instances.entrySet()) {
			for (ActivityInstance instance : entry.getValue()) {
				ids.put(instance.getControlNodeInstanceId());
				JSONObject activityJSON = new JSONObject();
				activityJSON.put("id", instance.getControlNodeInstanceId());
				activityJSON.put("activityid", instance.getControlNodeId());
				activityJSON.put("label", instance.getLabel());
				activityJSON.put("state", entry.getKey());
				activityJSON.put("link", uriInfo.getAbsolutePath() + "/" + instance.getControlNodeInstanceId());
				activities.put(String.valueOf(instance.getControlNodeInstanceId()), activityJSON);
			}
		}
		JSONObject result = new JSONObject();
		result.put("ids", ids);
		result.put("activities", activities);
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}


	/**
	 * Builds a JSON Object for a Map with data
	 * corresponding to a set of activities.
	 *
	 * @param instances The Map containing information about the activity instances.
	 *                  We Assume that the key is a the id and the value is a Map
	 *                  from String to Object with the properties of the instance.
	 * @return The newly created JSON Object with the activity data.
	 */
	private JSONObject buildJSONObjectForActivities(Collection<ActivityInstance> instances, String state, UriInfo uriInfo) {
		List<Integer> ids = new ArrayList<>(instances.size());
		JSONArray activities = new JSONArray();
		for (ActivityInstance instance : instances) {
			JSONObject activityJSON = new JSONObject();
			ids.add(instance.getControlNodeInstanceId());
			activityJSON.put("id", instance.getControlNodeInstanceId());
			activityJSON.put("activityid", instance.getControlNodeId());
			activityJSON.put("label", instance.getLabel());
			activityJSON.put("state", state);
			activityJSON.put("link", uriInfo.getAbsolutePath() + "/" + instance.getControlNodeInstanceId());
			activities.put(activityJSON);
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
	public Response getActivity(@Context UriInfo uriInfo, @PathParam("scenarioId") int scenarioId, @PathParam("instanceId") int scenarioInstanceId, @PathParam("activityInstanceId") int activityInstanceId) {

		ExecutionService executionService = ExecutionService.getInstance(scenarioId);
		if (!executionService.testActivityInstanceExists(activityInstanceId)) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{\"error\":\"There is no such " + "activity instance.\"}").build();
		}
		ActivityJaxBean activity = new ActivityJaxBean();
		activity.setId(activityInstanceId);
		ExecutionService.getInstance(scenarioId).openExistingScenarioInstance(scenarioId, scenarioInstanceId);
		List<AbstractControlNodeInstance> controlNodeInstances = executionService.getScenarioInstance(scenarioInstanceId).getControlNodeInstances();
		controlNodeInstances.stream().filter(controlNodeInstance -> controlNodeInstance.getControlNodeInstanceId() == activityInstanceId).forEach(controlNodeInstance -> activity.setLabel(executionService.getLabelForControlNodeId(controlNodeInstance.getControlNodeId())));
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
	public Response getWorkingItems(@PathParam("scenarioId") int scenarioId, @PathParam("instanceId") int scenarioInstanceId, @PathParam("activityInstanceId") int activityInstanceId) {
		ExecutionService executionService = ExecutionService.getInstance(scenarioId);
		executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId);

		List<DataObjectJaxBean> selectedDataObjects = executionService.getSelectedWorkingItems(scenarioInstanceId, activityInstanceId);
		JSONArray selectedDataObjectsJson = new JSONArray(selectedDataObjects);
		return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(selectedDataObjectsJson.toString()).build();
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
	public Response beginActivity(@PathParam("scenarioId") int scenarioId,
	          @PathParam("instanceId") int scenarioInstanceId, @PathParam("activityInstanceId") int activityInstanceId, String postBody) {
		ExecutionService executionService = ExecutionService.getInstance(scenarioId);
		if (!executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId)) {
		  return this.buildNotFoundResponse("{\"message\":\"Case does not exist.\"}");
		}

		List<Integer> selectedDataObjectIds = new ArrayList<>();
		JSONObject postJson = new JSONObject(postBody);
		if (postJson.has("dataobjects")) {
			JSONArray dataObjectsJson = postJson.getJSONArray("dataobjects");
			for (int i = 0; i < dataObjectsJson.length(); i++) {
				selectedDataObjectIds.add(dataObjectsJson.getInt(i));
			}
		}
		// TODO: begin of activity could fail in which case another Response needs to be sent
		executionService.beginActivityInstance(scenarioInstanceId, activityInstanceId, selectedDataObjectIds);
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
	public Response terminateActivity(@PathParam("scenarioId") int scenarioId, @PathParam("instanceId") int scenarioInstanceId, @PathParam("activityInstanceId") int activityInstanceId, String postBody) {
	
	  ExecutionService executionService = ExecutionService.getInstance(scenarioId);
		executionService.openExistingScenarioInstance(scenarioId, scenarioInstanceId);
		boolean successful;
		JSONObject postJson = new JSONObject(postBody);
		if (postJson.length() != 0) {
			Map<String, String> dataClassNameToState = new HashMap<>();
			for (Object dataClassName : postJson.keySet()) {
				dataClassNameToState.put((String) dataClassName, postJson.getString((String) dataClassName));
				
			}
			
			executionService.terminateActivityInstance(scenarioInstanceId, activityInstanceId, dataClassNameToState);
		} else {
			executionService.terminateActivityInstance(scenarioInstanceId, activityInstanceId);
		}
		return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"activity terminated.\"}").build();
	}
	
	
	//Upload File to Server via REST below
	@POST
	@Path("files/{attributeID}/")  //Your Path or URL to call this service
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
	@FormDataParam("file") InputStream uploadedInputStream,
	@FormDataParam("file") FormDataContentDisposition fileDetail, @PathParam("attributeID") String attributeID) {
				
		String filename = fileDetail.getFileName();
		File tempFile = null;
		try {
			tempFile = File.createTempFile("temp-", "filename");
			try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(uploadedInputStream, out);
        }	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		java.sql.Connection con = null;
			
		try{
			con = ConnectionWrapper.getInstance().connect();
			String sql = "INSERT INTO fileUploads VALUES (?,?,?)";
			PreparedStatement statement = con.prepareStatement(sql);
			FileInputStream   fis = new FileInputStream(tempFile);
			statement.setString(1, attributeID);
			statement.setBinaryStream(2, fis, (int) tempFile.length());
			statement.setString(3, filename);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}  
		finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return Response.status(200).entity("done").build();
	}	
	
	@GET
	@Path("/files/{attributeID}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@PathParam("attributeID") String attributeID) {
		
		Connection con;
		Statement su = null;
		String sql = null;
		ResultSet rs = null;			
		try {
			con = ConnectionWrapper.getInstance().connect();
			su=con.createStatement();
			sql = "SELECT * FROM fileUploads WHERE ATTRIBUTE_ID = " + attributeID;
			rs = su.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		Blob blob = null;
		String filename = null;
		FileOutputStream out = null;
		ResponseBuilder response = null;
		Response actualResponse = null;
		
		try {
			while(rs.next()) {
				// take the blob
				blob = rs.getBlob("file");
				filename = rs.getString("filename");
				
				int blobLength = (int) blob.length();  
				byte[] blobAsBytes = blob.getBytes(1, blobLength);

				File file = File.createTempFile("temp-", filename, new File("."));
				out = new FileOutputStream( file );
				out.write( blobAsBytes );
				response = Response.ok((Object) file, MediaType.APPLICATION_OCTET_STREAM);
				String headerString = "attachment; filename="+ filename;
				response.header("Content-Disposition", headerString);
				actualResponse = response.build();
				out.close();
			}
			blob.free();
			su.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return actualResponse;
		
	} 
	
	
	@GET
	@Path("/files/{attributeID}/filename")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFileName(@PathParam("attributeID") String attributeID) {
		Connection con;
		Statement su = null;
		String sql = null;
		ResultSet rs = null;
		String filename = null;
		
		try {
			con = ConnectionWrapper.getInstance().connect();
			su=con.createStatement();
			sql = "SELECT * FROM fileUploads WHERE ATTRIBUTE_ID = " + attributeID;
			rs = su.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ResponseBuilder response = null;
		Response actualResponse = null;
		
		try {
			while(rs.next()) {
				// take the blob
				filename = rs.getString("filename");
				response = Response.ok(filename, MediaType.APPLICATION_OCTET_STREAM);
				String headerString = "attachment; filename="+ filename;
				response.header("Content-Disposition", headerString);
				actualResponse = response.build();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return actualResponse;
	}   
}
	