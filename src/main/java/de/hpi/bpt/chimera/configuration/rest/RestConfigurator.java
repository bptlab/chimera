package de.hpi.bpt.chimera.configuration.rest;

import de.hpi.bpt.chimera.configuration.rest.beans.EmailActivityJaxBean;
import de.hpi.bpt.chimera.execution.Case;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.configuration.EmailConfiguration;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
@Path("config/v2")
public class RestConfigurator {
	private static final Logger log = Logger.getLogger(RestConfigurator.class);

	// ************************** EMAIL SERVICE TASKS **********************************/

	/**
	 * Updates the email configuration for a specified task.
	 * The Task is specified by the email Task ID and the new
	 * configuration will submitted as a JSON-Object.
	 *
	 * @param emailTaskId The ControlNode id of the email task.
	 * @param input       The new configuration.
	 * @return A Response 202 (ACCEPTED) if the update was successful.
	 * A 404 (NOT_FOUND) if the mail task could not be found.
	 */
	@PUT
	@Path("scenario/{caseModelID}/emailtask/{emailtaskID}/")
	public Response updateEmailConfiguration(@PathParam("caseModelID") String caseModelId, @PathParam("emailtaskID") String emailTaskId, final String input) {
		
		if (!CaseModelManager.isExistingCaseModel(caseModelId)) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		AbstractActivity emailActivity = CaseModelManager.getCaseModel(caseModelId).getActivityById(emailTaskId);
		if (!(emailActivity instanceof EmailActivity)) {
			log.error("The activty for the given Id isn't an EmailActivit");
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		EmailConfiguration emailConfig = ((EmailActivity) (emailActivity)).getEmailConfiguration();

		JSONObject json = new JSONObject(input);
		emailConfig.setReceiverEmailAddress(json.getString("receiver"));
		emailConfig.setSubject(json.getString("subject"));
		emailConfig.setMessage(json.getString("message"));
		return Response.status(Response.Status.ACCEPTED).build();
	}
	// @PUT
	// @Path("/scenario2/{caseModelID}/emailtask/{emailtaskID}/")
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response updateEmailConfiguration(@PathParam("caseModelID") String
	// caseModelId, @PathParam("emailtaskID") String emailTaskID, final
	// RestConfigurator.EmailConfigJaxBean input) {
	// log.info(caseModelId);
	// if (!CaseModelManager.isExistingCaseModel(caseModelId)) {
	// return Response.status(Response.Status.NOT_ACCEPTABLE).build();
	// }
	// CaseModel cm = CaseModelManager.getCaseModel(caseModelId);
	// EmailConfiguration emailConfig = cm.getEmailConfiguration();
	// emailConfig.setReceiverEmailAddress(input.getReceiver());
	// emailConfig.setSubject(input.getSubject());
	// emailConfig.setMessage(input.getMessage());
	// return Response.status(Response.Status.ACCEPTED).build();
	//
	// // DbEmailConfiguration dbEmailConfiguration = new
	// // DbEmailConfiguration();
	// // int result = dbEmailConfiguration.setEmailConfiguration(emailTaskID,
	// // input.getReceiver(), input.getSubject(), input.getMessage());
	// // if (result > 0) {
	// // return Response.status(Response.Status.ACCEPTED).build();
	// // } else {
	// // return Response.status(Response.Status.NOT_ACCEPTABLE).build();
	// // }
	// }

	/**
	 * This method provides information about all EmailActivities inside a given
	 * scenario. The information consists of the id and the label. A Json Object
	 * will be returned with an array of ids and a Map from ids to labels.
	 *
	 * @param caseModelID
	 *            The ID of the CaseModel, its mail tasks will be returned.
	 * @return The JSON Object with ids and labels.
	 */
	@GET
	@Path("scenario/{scenarioId}/emailtask")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEmailActivities(@PathParam("scenarioId") String caseModelID) {
		List<EmailActivity> emailActivities = getAllEmailActiviesFromCaseModel(CaseModelManager.getCaseModel(caseModelID));
		JSONArray jsonArray = new JSONArray(emailActivities.stream().map(emailActivty -> new EmailActivityJaxBean(emailActivty)).collect(Collectors.toList()));
		return Response.ok(jsonArray.toString(), MediaType.APPLICATION_JSON).build();
//		DbEmailConfiguration mail = new DbEmailConfiguration();
//		String jsonRepresentation = JsonUtil.jsonWrapperList(mail.getAllEmailTasksForScenario(scenarioID));
//		return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * This functions returns all Ids of all EmailActivities of a given
	 * CaseModel.
	 * 
	 * @param caseModel
	 *            the CaseModel of which all EmailActivityIds should be returned
	 * @return a List of all Ids of the EmailActivities of the given CaseModel
	 */
	private List<EmailActivity> getAllEmailActiviesFromCaseModel(CaseModel caseModel) {
		// first create a List of all Activities of the CaseModel. Therefore add
		// all Activities of each Fragment to a List of all Activities.
		List<AbstractActivity> activities = caseModel.getFragments().stream().map(fragment -> fragment.getBpmnFragment().getActivities()).flatMap(List::stream).collect(Collectors.toList());
		// now filter the List of all Activities for the Activities which are
		// from Type EmailActivity
		return activities.stream().filter(activity -> activity instanceof EmailActivity).map(activity -> (EmailActivity) activity).collect(Collectors.toList());
	}

	/**
	 * This method provides information about an EmailActivity. It will return a
	 * JSON-Object with information about the mail configuration. A
	 * Configuration contains a receiver, a subject and a content. A
	 * EmailActivity is specified by:
	 *
	 * @param caseModelId
	 *            The ID of the scenario model.
	 * @param EmailActivityId
	 *            The control node ID of the mail Task.
	 * @return Returns a 404 if the EmailActivty or CaseModel does not exist and
	 *         a 200 (OK) with a JSON-Object if the EmailActivty was found.
	 */
	@GET
	@Path("scenario/{scenarioId}/emailtask/{emailTaskID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmailTaskConfiguration(@PathParam("scenarioId") String caseModelId, @PathParam("emailTaskID") String mailTaskId) {
		if (!CaseModelManager.isExistingCaseModel(caseModelId)) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{}").build();
		}
		EmailConfigJaxBean emailConfigJaxB = new EmailConfigJaxBean();
		AbstractActivity emailActivity = CaseModelManager.getCaseModel(caseModelId).getActivityById(mailTaskId);
		if (!(emailActivity instanceof EmailActivity)) {
			log.error("The activty for the given Id isn't an EmailActivit");
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{}").build();
		}
		EmailConfiguration emailConfig = ((EmailActivity) (emailActivity)).getEmailConfiguration();
		emailConfigJaxB.setReceiver(emailConfig.getReceiverEmailAddress());
		emailConfigJaxB.setMessage(emailConfig.getMessage());
		emailConfigJaxB.setSubject(emailConfig.getSubject());
		return Response.ok(emailConfigJaxB, MediaType.APPLICATION_JSON).build();

		// DbScenario scenario = new DbScenario();
		// DbEmailConfiguration mail = new DbEmailConfiguration();
		// EmailConfigJaxBean mailConfig = new EmailConfigJaxBean();
		// mailConfig.setReceiver(mail.getReceiverEmailAddress(mailTaskID));
		// if (!scenario.existScenario(caseModelId)) {
		// return
		// Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("{}").build();
		// }
		// mailConfig.setMessage(mail.getMessage(mailTaskID));
		// mailConfig.setSubject(mail.getSubject(mailTaskID));
		// return Response.ok(mailConfig, MediaType.APPLICATION_JSON).build();
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
	@XmlRootElement
	public static class EmailConfigJaxBean {

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
	@XmlRootElement
	public static class WebserviceConfigJaxBean {

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

		public List<HashMap<String, Object>> getAttributes() {
			return attributes;
		}

		public void setAttributes(ArrayList<HashMap<String, Object>> attributes) {
			this.attributes = attributes;
		}
	}
}
