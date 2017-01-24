package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.database.DbEmailConfiguration;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.EmailConfigJaxBean;
import de.hpi.bpt.chimera.util.JsonUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class implements the REST interface for email tasks.
 * It allows to retrieve information about email tasks.
 */
@Path("interface/v2")
public class EmailRestService {
	/**
	 * This method provides information about all email Tasks inside
	 * a given scenario.
	 * The information consists of the id and the label.
	 * A Json Object will be returned with an array of ids and a Map
	 * from ids to labels.
	 *
	 * @param scenarioId   The ID of the scenario
	 * @param filterString A filter String. only mail tasks with a label containing
	 *                     this filter String will be returned.
	 * @return The JSON Object with ids and labels.
	 */
	@GET
	@Path("scenario/{scenarioId}/emailtask")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEmailTasks(@PathParam("scenarioId") int scenarioId, @QueryParam("filter") String filterString) {
		DbEmailConfiguration mail = new DbEmailConfiguration();
		String jsonRepresentation = JsonUtil.jsonWrapperList(mail.getAllEmailTasksForScenario(scenarioId));
		return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
	}


	/**
	 * This method provides information about an email Task.
	 * It will return a JSON-Object with information about the mail
	 * configuration.
	 * A Configuration contains a receiver, a subject and a content.
	 * A Mail task is specified by:
	 *
	 * @param scenarioId The ID of the scenario model.
	 * @param mailTaskId The control node ID of the mail Task.
	 * @return Returns a 404 if the mail Task or scenario does not exist
	 * and a 200 (OK) with a JSON-Object if the emailTask was found.
	 */
	@GET
	@Path("scenario/{scenarioId}/emailtask/{emailTaskId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmailTaskConfiguration(@PathParam("scenarioId") int scenarioId, @PathParam("emailTaskId") int mailTaskId) {
		DbEmailConfiguration mail = new DbEmailConfiguration();
		EmailConfigJaxBean mailConfig = new EmailConfigJaxBean();
		mailConfig.setReceiver(mail.getReceiverEmailAddress(mailTaskId));
		if ("".equals(mailConfig.getReceiver())) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{}").build();
		}
		mailConfig.setContent(mail.getMessage(mailTaskId));
		mailConfig.setSubject(mail.getSubject(mailTaskId));
		return Response.ok(mailConfig, MediaType.APPLICATION_JSON).build();
	}
}
