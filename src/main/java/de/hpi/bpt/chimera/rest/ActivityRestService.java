package de.hpi.bpt.chimera.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.rest.beans.activity.MultipleActivitiesJaxBean;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import io.swagger.v3.oas.annotations.Operation;
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
	// TODO: find out why combining this rest service and the
	// ExtendedActivityRestService so that the path annotation for methods in
	// ExtendedActivityRestService would be @Path("{activityInstanceId") lead to
	// an error that the corresponding urls are not assigned.
	private static final Logger log = Logger.getLogger(ActivityRestService.class);

	@GET
	@Path("")
	@Operation(summary = "Receive the activity instances of a case", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully requested the activity instances.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleActivitiesJaxBean.class))) })
	public Response receiveActivityInstances(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @DefaultValue("") @QueryParam("filter") String filterString, @DefaultValue("") @QueryParam("state") String stateName) {
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
			activityInstances = activityInstances.stream().filter(instance -> instance.getControlNode().getName().contains(filterString)).collect(Collectors.toList());
		}

		JSONObject result = new JSONObject(new MultipleActivitiesJaxBean(activityInstances));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}
}
