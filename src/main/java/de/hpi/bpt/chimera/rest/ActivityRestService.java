package de.hpi.bpt.chimera.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hpi.bpt.chimera.rest.beans.activity.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.execution.data.ObjectLifecycleTransition;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.MessageJaxBean;
import de.hpi.bpt.chimera.usermanagement.Organization;
import de.hpi.bpt.chimera.usermanagement.OrganizationManager;
import de.hpi.bpt.chimera.usermanagement.User;
import de.hpi.bpt.chimera.usermanagement.UserManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "activities")
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
public class ActivityRestService extends AbstractRestService {
	private static final Logger log = Logger.getLogger(ActivityRestService.class);

	@GET
	@Path("")
	@Operation(
		summary = "Receive the activity instances of a case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the activity instances.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleActivitiesJaxBean.class))) })
	public Response receiveActivityInstances(@Context ContainerRequestContext requestContext, @PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @DefaultValue("") @QueryParam("filter") String filterString, @DefaultValue("") @QueryParam("state") String stateName) {
		User user = retrieveUser(requestContext);
		Organization org = OrganizationManager.getOrganizationById(orgId);
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(caseId);

		List<AbstractActivityInstance> activityInstances = new ArrayList<>();
		if (stateName.isEmpty()) {
			activityInstances.addAll(caseExecutioner.getActivitiesWithState(State.READY));
			activityInstances.addAll(caseExecutioner.getActivitiesWithState(State.RUNNING));
			activityInstances.addAll(caseExecutioner.getActivitiesWithState(State.TERMINATED));
			activityInstances.addAll(caseExecutioner.getActivitiesWithState(State.DATAFLOW_ENABLED));
			activityInstances.addAll(caseExecutioner.getActivitiesWithState(State.CONTROLFLOW_ENABLED));
		} else {
			try {
				State state = State.fromString(stateName);
				activityInstances.addAll(caseExecutioner.getActivitiesWithState(state));
			} catch (IllegalStateException e) {
				log.error(e);
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
			}
		}

		if (!filterString.isEmpty()) {
			activityInstances = activityInstances.stream()
									.filter(instance -> instance.getControlNode().getName().contains(filterString))
									.collect(Collectors.toList());
		}

		Predicate<AbstractActivityInstance> canAccess = a -> UserManager.hasAccess(user, org, a);
		List<AbstractActivityInstance> accessableInstances = activityInstances.stream()
																.filter(canAccess)
																.collect(Collectors.toList());
		JSONObject result = new JSONObject(new MultipleActivitiesJaxBean(accessableInstances));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("{activityInstanceId}")
	@Operation(
		summary = "Receive a specific activity instance of a case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the activity instance.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityJaxBean.class)))})
	public Response receiveActivityInstance(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @PathParam("activityInstanceId") String activityInstanceId) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(caseId);
		AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);
		JSONObject result = new JSONObject(new ActivityJaxBean(activityInstance));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	// TODO: maybe this needs to be solely for data objects but then ids needs
	// to be used and not names
	@PUT
	@Path("{activityInstanceId}")
	@Operation(
		summary = "Update the dataobjects of an activity instance",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully updated the dataobjects of an activity instance.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response updateDataObjects(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @PathParam("activityInstanceId") String activityInstanceId, List<UpdateDataObjectJaxBean> update) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(caseId);
			AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);
			if (!activityInstance.getState().equals(State.RUNNING)) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("Activity Instance needs to be running.")).build();
			}
			List<DataObject> workingItems = activityInstance.getSelectedDataObjects();
			if (workingItems.isEmpty()) {
				return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("activity instance has no dataobject that could be updated.")).build();
			}

			DataManager dataManager = caseExecutioner.getDataManager();
			dataManager.setDataAttributeValuesByNames(update, workingItems);

			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"data attribute instance values updated.\"}").build();
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@POST
	@Path("{activityInstanceId}/begin")
	@Operation(
			summary = "Begin a specific activity instance",
			responses = {
				@ApiResponse(
					responseCode = "200", description = "Successfully begun the activity instance.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response beginActivityInstance(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId,
											@PathParam("activityInstanceId") String activityInstanceId, @Parameter(description = "The ids of the selected data objects") List<String> selectedDataObjectIds) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
			AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);

			List<DataObject> selectedDataObjects = caseExecutioner.getDataManager().getDataObjectsById(selectedDataObjectIds);

			caseExecutioner.beginDataControlNodeInstance(activityInstance, selectedDataObjects);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("activity begun.")).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	// TODO: think about whether ids should be used instead of names
	@POST
	@Path("{activityInstanceId}/terminate")
	@Operation(
		summary = "Terminate a specific activity instance",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully terminated the activity instance.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageJaxBean.class)))})
	public Response terminateActivityInstance(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @PathParam("activityInstanceId") String activityInstanceId, List<DataUpdateRaw> post) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
			DataManager dataManager = caseExecutioner.getDataManager();
			AbstractActivityInstance activityInstance = caseExecutioner.getActivityInstance(activityInstanceId);

			// validate and find duplicates
			// List<ObjectLifecycleTransition> objectLifecycleTransitions = caseExecutioner.getDataManager().resolveDataClassToStateTransition(post.getTransitions());
			// List<UpdateDataObjectJaxBean> rawDataAttributeValues = post.getAttributeUpdates();
			List<String> dataclassNames = post.stream().map(DataUpdateRaw::getDataclassName).collect(Collectors.toList());
			Optional<String> duplicateNameOptional = dataclassNames.stream()
														.filter(n -> Collections.frequency(dataclassNames, n) > 1)
														.findFirst();
			if (duplicateNameOptional.isPresent()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(String.format("The post contains a duplicate update for %s", duplicateNameOptional.get()))).build();
			}
			List<DataUpdate> dataUpdates = post.stream()
											.map(u -> u.convert(dataManager))
											.collect(Collectors.toList());
			// TODO: find duplicates
			caseExecutioner.terminateDataControlNodeInstance(activityInstance, dataUpdates);

			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("activity terminated.")).build();
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
