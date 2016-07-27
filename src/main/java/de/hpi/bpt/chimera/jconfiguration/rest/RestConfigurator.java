package de.hpi.bpt.chimera.jconfiguration.rest;

import de.hpi.bpt.chimera.database.DbEmailConfiguration;
import de.hpi.bpt.chimera.database.DbScenario;
import de.hpi.bpt.chimera.database.controlnodes.DbWebServiceTask;
import de.hpi.bpt.chimera.jconfiguration.Execution;
import de.hpi.bpt.chimera.jcore.ExecutionService;
import de.hpi.bpt.chimera.util.JsonUtil;
import de.hpi.bpt.chimera.database.ConnectionWrapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * This class implements the REST interface of the JEngine core.
 * The core module provides methods to begin PCM instances
 * and to access the date inside the engine.
 * This REST interface provides methods to access this information
 * and to control the instances.
 * Methods which are necessary for the controlling can be found
 * inside the {@link ExecutionService}.
 * This class will use {@link ConnectionWrapper}
 * to access the database directly.
 */
@Path("config/v2") public class RestConfigurator {

	/**
	 * Deletes a scenario with all its instances.
	 * internally realizes via a flag.
	 *
	 * @param scenarioID The ID of the scenario which is supposed to be deleted
	 * @return The status code if the operation was successful or not
	 * @throws Exception in case something goes wrong.
	 */
	@DELETE @Path("scenario/{scenarioID}/") public Response deleteScenario(
			@PathParam("scenarioID") Integer scenarioID) throws Exception {

		Execution execution = new Execution();
		execution.deleteScenario(scenarioID);
		return Response.status(Response.Status.ACCEPTED).type(
					MediaType.APPLICATION_JSON).entity("{\"message\":\""
					+ "scenario deletion successful.\"}").build();
	}

	// ************************** EMAIL SERVICE TASKS **********************************/

	/**
	 * Updates the email configuration for a specified task.
	 * The Task is specified by the email Task ID and the new
	 * configuration will submitted as a JSON-Object.
	 *
	 * @param emailTaskID The ControlNode id of the email task.
	 * @param input       The new configuration.
	 * @return A Response 202 (ACCEPTED) if the update was successful.
	 * A 404 (NOT_FOUND) if the mail task could not be found.
	 */
	@PUT @Path("emailtask/{emailtaskID}/")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response updateEmailConfiguration(
			@PathParam("emailtaskID") int emailTaskID,
			final RestConfigurator.EmailConfigJaxBean input) {
		DbEmailConfiguration dbEmailConfiguration = new DbEmailConfiguration();
		int result = dbEmailConfiguration.setEmailConfiguration(emailTaskID, input.getReceiver(),
				input.getSubject(), input.getMessage());
		if (result > 0) {
			return Response.status(Response.Status.ACCEPTED).build();
		} else {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
	}

	/**
	 * This method provides information about all email Tasks inside
	 * a given scenario.
	 * The information consists of the id and the label.
	 * A Json Object will be returned with an array of ids and a Map
	 * from ids to labels.
	 *
	 * @param scenarioID   The ID of the scenario, its mail tasks will be returned.
	 * @param filterString A Filter String, only mail tasks with a label containing
	 *                     this filter String will be returned.
	 * @return The JSON Object with ids and labels.
	 */
	@GET @Path("scenario/{scenarioID}/emailtask")
		@Produces(MediaType.APPLICATION_JSON) public Response getAllEmailTasks(
			@PathParam("scenarioID") int scenarioID,
			@QueryParam("filter") String filterString) {
		DbScenario scenario = new DbScenario();
		DbEmailConfiguration mail = new DbEmailConfiguration();
		if (!scenario.existScenario(scenarioID)) {
			return Response.status(Response.Status.NOT_FOUND).type(
					MediaType.APPLICATION_JSON).entity("{}").build();
		}
		String jsonRepresentation = JsonUtil.jsonWrapperList(
				mail.getAllEmailTasksForScenario(scenarioID));
		return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * This method provides information about an email Task.
	 * It will return a JSON-Object with information about the mail
	 * configuration.
	 * A Configuration contains a receiver, a subject and a content.
	 * A Mail task is specified by:
	 *
	 * @param scenarioID The ID of the scenario model.
	 * @param mailTaskID The control node ID of the mail Task.
	 * @return Returns a 404 if the mail Task or scenario does not exist
	 * and a 200 (OK) with a JSON-Object if the emailTask was found.
	 */
	@GET @Path("scenario/{scenarioID}/emailtask/{emailTaskID}")
		@Produces(MediaType.APPLICATION_JSON) public Response getEmailTaskConfiguration(
			@PathParam("scenarioID") int scenarioID,
			@PathParam("emailTaskID") int mailTaskID) {
		DbScenario scenario = new DbScenario();
		DbEmailConfiguration mail = new DbEmailConfiguration();
		EmailConfigJaxBean mailConfig = new EmailConfigJaxBean();
		mailConfig.setReceiver(mail.getReceiverEmailAddress(mailTaskID));
		if (!scenario.existScenario(scenarioID) || mailConfig.getReceiver().equals("")) {
			return Response.status(Response.Status.NOT_FOUND).type(
					MediaType.APPLICATION_JSON).entity("{}").build();
		}
		mailConfig.setMessage(mail.getMessage(mailTaskID));
		mailConfig.setSubject(mail.getSubject(mailTaskID));
		return Response.ok(mailConfig, MediaType.APPLICATION_JSON).build();
	}

	// ************************** HELPER **********************************/

	/**
	 * This is a data class for the email configuration.
	 * It is used by Jersey to deserialize JSON.
	 * Also it can be used for tests to provide the correct contents.
	 * This class in particular is used by the POST for the email configuration.
	 * See the {@link #updateEmailConfiguration(int, EmailConfigJaxBean)}
	 * updateEmailConfiguration} method for more information.
	 */
	@XmlRootElement public static class EmailConfigJaxBean {

		private String receiver;

		private String subject;

		private String message;

		public String getReceiver() {
			return receiver;
		}

		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	/**
	 *
	 */
	@XmlRootElement public static class WebserviceConfigJaxBean {

		private String link;

		private String method;

		private ArrayList<HashMap<String, Object>> attributes;

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public ArrayList<HashMap<String, Object>> getAttributes() {
			return attributes;
		}

		public void setAttributes(ArrayList<HashMap<String, Object>> attributes) {
			this.attributes = attributes;
		}
	}
}
