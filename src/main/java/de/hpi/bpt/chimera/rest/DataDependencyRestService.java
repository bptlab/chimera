package de.hpi.bpt.chimera.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.rest.beans.activity.AvailableOutputJaxBean;
import de.hpi.bpt.chimera.rest.beans.datamodel.MultipleDataObjectsJaxBean;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "activities")
@Tag(name = "data")
@ApiResponses(value = {
	@ApiResponse(
		responseCode = "400", description = "A problem occured during the processing.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))),
	@ApiResponse(
		responseCode = "401", description = "A problem occured during the authentication.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))),
	@ApiResponse(
		responseCode = "404", description = "An unassigned identifier was used.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))) })
@SecurityRequirement(name = "BasicAuth")
@Path("v3/organizations/{organizationId}/casemodels/{casemodelId}/cases/{caseId}/activities")
public class DataDependencyRestService extends AbstractRestService {

	@GET
	@Path("{activityInstanceId}/availableInput")
	@Operation(
		summary = "Receive the available dataobjects for a ready activity instance",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the dataobjects.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleDataObjectsJaxBean.class)))})
	public Response getAvailableInput(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);
		if (!activityInstance.getState().equals(State.READY)) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("Activity Instance needs to be ready.")).build();
		}

		DataManager dataManager = caseExecutioner.getDataManager();
		DataStateCondition activityPreCondition = activityInstance.getControlNode().getPreCondition();
		List<ConditionSet> fulfilledConditionSets = activityPreCondition.getFulfilledConditions(dataManager.getDataStateConditions());
		List<DataObject> availableInput = new ArrayList<>(dataManager.getAvailableDataObjects(fulfilledConditionSets));

		JSONObject result = new JSONObject(new MultipleDataObjectsJaxBean(availableInput));
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	@GET
	@Path("{activityInstanceId}/workingItems")
	@Operation(
		summary = "Receive the selected dataobjects of a running activity instance",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the dataobjects.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleDataObjectsJaxBean.class)))})
	public Response getWorkingItems(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);
		if (!activityInstance.getState().equals(State.RUNNING)) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("Activity Instance needs to be running.")).build();
		}

		List<DataObject> selectedDataObjects = activityInstance.getSelectedDataObjects();
		JSONObject result = new JSONObject(new MultipleDataObjectsJaxBean(selectedDataObjects));
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	@GET
	@Path("{activityInstanceId}/availableOutput")
	@Operation(
		summary = "Receive the available ouput dataclasses and dataobjects for a running activity instance",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the dataobjects and dataclasses.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvailableOutputJaxBean.class)))})
	public Response getOutputDataObjects(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
		AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);
		if (!activityInstance.getState().equals(State.RUNNING)) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("Activity Instance needs to be running.")).build();
		}

		JSONObject result = new JSONObject(new AvailableOutputJaxBean(activityInstance));
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}
}
