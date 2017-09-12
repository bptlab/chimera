package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.DataObjectStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.TerminationConditionComponent;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class implements the REST interface for dealing with scenarios.
 */
@Path("interface/v2")
public class ScenarioRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(RestInterface.class);

	/**
	 * This method provides information about the termination condition.
	 * Of the specified Scenario.
	 * The termination condition is a set of sets of conditions.
	 * Only if all conditions of one set are true the scenario will
	 * terminate.
	 * If the scenario does not exists a 404 with an error will be returned.
	 * If the scenario exists the JSON representation of the condition set
	 * will be returned.
	 *
	 * @param scenarioID This id specifies the scenario. The id is the
	 *                   primary key inside the database.
	 * @return Returns a response object. It will either  be a 200 or
	 * 404. The content will be either the JSON representation of the termination
	 * condition or an JSON object with the error message.
	 */
	@GET
	@Path("scenario/{scenarioId}/terminationcondition")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTerminationCondition(@PathParam("scenarioId") String cmId) {
		TerminationCondition terminationCondition = CaseModelManager.getTerminationConditionOfCaseModel(cmId);

		JSONObject result = new JSONObject();
		JSONObject conditions = new JSONObject();
		int id = 1;
		for (TerminationConditionComponent component : terminationCondition.getConditions()) {
			JSONArray dataObjectStateConditions = new JSONArray();
			for (DataObjectStateCondition dosc : component.getConditions()) {
				JSONObject dataObjectStateCondition = new JSONObject();
				dataObjectStateCondition.put("data_object", dosc.getDataClass().getName());
				dataObjectStateCondition.put("state", dosc.getState().getName());
				dataObjectStateConditions.put(dataObjectStateCondition);
			}
			conditions.put(String.format("%d", id++), dataObjectStateConditions);
		}
		result.put("conditions", conditions);

		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * This method enables the creation of new scenarios.
	 *
	 * @param scenario A Scenario JSON
	 * @return Information about the new scenario.
	 */
	@POST
	@Path("scenario")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postInstance(String scenario) {
		try {
			CaseModelManager.parseCaseModel(scenario);
			log.info("Successfully parsed a CaseModel");
			return Response.status(201).build();
		} catch (Exception e) {
			log.error(e);
			return Response.status(422).type(MediaType.APPLICATION_JSON).entity(buildException(e.getMessage())).build();
		}
		/*
		 * catch (JAXBException e) { log.error("Error: ", e); return
		 * Response.status(400).type(MediaType.APPLICATION_JSON).entity(
		 * buildException("Invalid xml " + e.getMessage())).build(); }
		 */
	}

	private String buildException(String text) {
		JSONArray result = new JSONArray();
		JSONObject content = new JSONObject();
		content.put("text", text);
		content.put("type", "danger");
		result.put(content);
		return result.toString();
	}

	/**
	 * This method provides information about one scenario.
	 * The scenario is specified by an given id.
	 * The response of this request will contain a valid JSON-Object
	 * containing detailed information about the scenario.
	 * If there is no scenario with the specific id a 404 will be returned,
	 * with a meaningful error message.
	 *
	 * @param scenarioId The Id of the scenario used inside the database.
	 * @param uri        Request URI
	 * @return Returns a JSON-Object with detailed information about one scenario.
	 * The Information contain the id, label, number of instances, latest version
	 * and more.
	 */
	@GET
	@Path("scenario/{scenarioId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScenario(@Context UriInfo uri, @PathParam("scenarioId") String cmId) {
		// DbScenario dbScenario = new DbScenario();
		// Map<String, Object> data = dbScenario.getScenarioDetails(scenarioId);
		CaseModel cm = CaseModelManager.getCaseModel(cmId);

		JSONObject result = new JSONObject();
		result.put("id", cm.getId());
		result.put("name", cm.getName());
		result.put("modelversion", cm.getVersionNumber());
		result.put("instances", uri.getAbsolutePath() + "/instance");

		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * Get the fragment bpmn-xml representations for all fragments of a scenario.
	 *
	 * @param scenarioId The id of the scenario.
	 * @return a JsonObject containing a JSONArray with all fragment xml strings.
	 */
	@GET
	@Path("scenario/{scenarioId}/xml")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFragmentXmlStrings(@PathParam("scenarioId") String cmId) {
		/*
		 * DbFragment dbFragment = new DbFragment(); List<String> xmls =
		 * dbFragment.getXmlStringsForScenario(scenarioId); JSONObject xmlJson =
		 * new JSONObject(); xmlJson.put("xml", new JSONArray(xmls));
		 */
		List<String> xmlStrings = CaseModelManager.getFragmentXmlOfCaseModel(cmId);
		JSONObject result = new JSONObject();
		result.put("xml", new JSONArray(xmlStrings));
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * Deletes a scenario with all its instances. Internally, this is realized
	 * by setting a 'deleted' flag in the database.
	 *
	 * @param scenarioID
	 *            The ID of the scenario which is supposed to be deleted
	 * @return The status code if the operation was successful or not
	 * @throws Exception
	 *             in case something goes wrong.
	 */
	@DELETE
	@Path("scenario/{scenarioId}/")
	public Response deleteScenario(@PathParam("scenarioId") String caseModelId) {
		try {
			CaseModelManager.deleteCaseModel(caseModelId);
			return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"" + "casemodel deletion successful.\"}").build();
		} catch (IllegalArgumentException e) {
			log.error("deletion failed: " + e);
			return Response.status(422).type(MediaType.APPLICATION_JSON).entity(buildException(e.getMessage())).build();
		}
	}

	/**
	 * This method allows to give an overview of all scenarios.
	 * The response will return a JSON-Array containing the basic
	 * information of all scenarios currently inside the database.
	 * If different versions of an scenarios exist only the latest
	 * ones will be added to the json.
	 *
	 * @param uriInfo      Specifies the context. For example the uri
	 *                     of the request.
	 * @param filterString Specifies a search. Only scenarios which
	 *                     name contain the specified string will be
	 *                     returned.
	 * @return Returns a JSON-Object with an Array with entries for
	 * every Scenario.
	 * Each Entry is a JSON-Object with a label and id of a scenario.
	 */
	@GET
	@Path("scenario")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScenarios(@Context UriInfo uriInfo, @DefaultValue("") @QueryParam("filter") String filterString) {
		List<CaseModel> caseModels = CaseModelManager.getCaseModels();

		if (!filterString.isEmpty()) {
			caseModels = caseModels.stream().filter(cm -> cm.getName().contains(filterString)).collect(Collectors.toList());
		}

		List<String> ids = new ArrayList<>(caseModels.size());
		JSONObject links = new JSONObject();
		JSONObject labels = new JSONObject();
		for (CaseModel cm : caseModels) {
			ids.add(cm.getId());
			links.put(cm.getId(), uriInfo.getAbsolutePath() + "/" + cm.getId());
			labels.put(cm.getId(), cm.getName());
		}
		JSONObject result = new JSONObject();
		result.put("ids", new JSONArray(ids));
		result.put("links", links);
		result.put("labels", labels);
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}
}
