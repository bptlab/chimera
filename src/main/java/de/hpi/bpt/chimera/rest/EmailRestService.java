package de.hpi.bpt.chimera.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.configuration.EmailConfiguration;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.beans.email.EmailActivityJaxBean;
import de.hpi.bpt.chimera.rest.beans.email.EmailConfigJaxBean;
import de.hpi.bpt.chimera.rest.beans.email.MultipleEmailActivityJaxBean;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "email")
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
@Path("v3/organizations/{organizationId}/casemodels/{casemodelId}/emailtasks")
public class EmailRestService extends AbstractRestService {
	private static final Logger log = Logger.getLogger(EmailRestService.class);

	@GET
	@Path("")
	@Operation(
		summary = "Receive all email activites of a casemodel",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all email activities.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleEmailActivityJaxBean.class)))})
	public Response getAllEmailActivities(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @DefaultValue("") @QueryParam("filter") String filterString) {
		CaseModel cm = CaseModelManager.getCaseModel(cmId);
		List<EmailActivity> emailActivities = cm.getFragments().stream()
												.map(fragment -> fragment.getBpmnFragment().getActivities())
												.flatMap(List::stream).filter(a -> a instanceof EmailActivity)
												.map(EmailActivity.class::cast)
												.collect(Collectors.toList());
		
		if (!filterString.isEmpty()) {
			emailActivities = emailActivities.stream()
								.filter(emailActivity -> emailActivity.getName().contains(filterString))
								.collect(Collectors.toList());
		}

		JSONObject result = new JSONObject(new MultipleEmailActivityJaxBean(emailActivities));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("{activityId}")
	@Operation(
		summary = "Receive a specefic email activity of a casemodel",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the email activity.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmailActivityJaxBean.class)))})
	public Response getEmailTaskConfiguration(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("activityId") @Parameter(description = "Id of the email task.") String mailTaskId) {
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);
			AbstractActivity activity = cm.getActivityById(mailTaskId);

			if (!(activity instanceof EmailActivity)) {
				String message = "The activity for the given Id isn't an EmailActivity";
				log.error(message);
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildMessage(message)).build();
			}
			EmailActivity emailActivity = (EmailActivity) activity;

			JSONObject result = new JSONObject(new EmailActivityJaxBean(emailActivity));
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	@PUT
	@Path("{activityId}")
	public Response updateEmailConfiguration(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("activityId") @Parameter(description = "Id of the email task.") String mailTaskId, EmailConfigJaxBean configuration) {
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);
			AbstractActivity activity = cm.getActivityById(mailTaskId);

			if (!(activity instanceof EmailActivity)) {
				String message = "The activity for the given Id isn't an EmailActivity";
				log.error(message);
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildMessage(message)).build();
			}
			EmailActivity emailActivity = (EmailActivity) activity;
			EmailConfiguration emailConfig = emailActivity.getEmailConfiguration();
			emailConfig.setReceiverEmailAddress(configuration.getReceiver());
			emailConfig.setSubject(configuration.getSubject());
			emailConfig.setMessage(configuration.getMessage());
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(buildMessage("email task updated")).build();
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
