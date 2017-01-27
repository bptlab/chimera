package de.hpi.bpt.chimera.jcore.rest;

import de.hpi.bpt.chimera.database.DbFragment;
import de.hpi.bpt.chimera.database.DbScenario;
import de.hpi.bpt.chimera.database.data.DbTerminationCondition;
import de.hpi.bpt.chimera.jcomparser.json.ScenarioData;
import de.hpi.bpt.chimera.jcomparser.validation.InvalidDataTransitionException;
import de.hpi.bpt.chimera.jcomparser.validation.InvalidDataclassReferenceException;
import de.hpi.bpt.chimera.jcore.Scenario;
import de.hpi.bpt.chimera.jcore.ScenarioFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Map;

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
	public Response getTerminationCondition(@PathParam("scenarioId") int scenarioID) {
		DbTerminationCondition terminationCondition = new DbTerminationCondition();
		Map<String, List<Map<String, Object>>> conditionSets = terminationCondition.getDetailedConditionsForScenario(scenarioID);
		JSONObject conditions = new JSONObject(conditionSets);
		JSONObject result = new JSONObject();
		result.put("conditions", conditions);
		result.put("setIDs", new JSONArray(conditionSets.keySet()));
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
			ScenarioData newScenario = new ScenarioData(scenario);
			newScenario.save();
			return Response.status(201).build();
		} catch (IllegalArgumentException | InvalidDataTransitionException | InvalidDataclassReferenceException e) {
			log.error("Error: ", e);
			return Response.status(422).type(MediaType.APPLICATION_JSON).entity(buildException(e.getMessage())).build();
		} catch (JAXBException e) {
			log.error("Error: ", e);
			return Response.status(400).type(MediaType.APPLICATION_JSON).entity(buildException("Invalid xml " + e.getMessage())).build();
		}
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
	public Response getScenario(@Context UriInfo uri, @PathParam("scenarioId") int scenarioId) {
		DbScenario dbScenario = new DbScenario();
		Map<String, Object> data = dbScenario.getScenarioDetails(scenarioId);
		data.put("instances", uri.getAbsolutePath() + "/instance");
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(new JSONObject(data).toString()).build();
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
	public Response getFragmentXmlStrings(@PathParam("scenarioId") int scenarioId) {
		DbFragment dbFragment = new DbFragment();
		List<String> xmls = dbFragment.getXmlStringsForScenario(scenarioId);
		JSONObject xmlJson = new JSONObject();
		xmlJson.put("xml", new JSONArray(xmls));
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(xmlJson.toString()).build();
	}

	 /**
   * Deletes a scenario with all its instances.
   * Internally, this is realized by setting a 'deleted' flag in the database.
   *
   * @param scenarioID The ID of the scenario which is supposed to be deleted
   * @return The status code if the operation was successful or not
   * @throws Exception in case something goes wrong.
   */
  @DELETE
  @Path("scenario/{scenarioId}/")
  public Response deleteScenario(@PathParam("scenarioId") Integer scenarioId) throws Exception {
    Scenario scenario = ScenarioFactory.createScenarioFromDatabase(scenarioId);
    if (scenario.exists()) {
      scenario.delete();
      return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"" + "scenario deletion successful.\"}").build();
    } else {
      return this.buildNotFoundResponse(String.format("Scenario with Id %s does not exist.", scenarioId));
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
	public Response getScenarios(@Context UriInfo uriInfo, @QueryParam("filter") String filterString) {
		DbScenario scenario = new DbScenario();
		Map<Integer, String> scenarios;
		if (filterString == null || "".equals(filterString)) {
			scenarios = scenario.getScenarios();
		} else {
			scenarios = scenario.getScenariosLike(filterString);
		}
		JSONObject jsonResult = mapToKeysAndResults(scenarios, "ids", "labels");
		JSONObject refs = new JSONObject();
		for (int id : scenarios.keySet()) {
			refs.put(String.valueOf(id), uriInfo.getAbsolutePath() + "/" + id);
		}
		jsonResult.put("links", refs);
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(jsonResult.toString()).build();
	}

	/**
	 * Creates a JSON object from an HashMap.
	 * The keys will be listed separately.
	 *
	 * @param data        The HashMap which contains the data of the Object
	 * @param keyLabel    The name which will be used
	 * @param resultLabel The label of the results.
	 * @return The newly created JSON Object.
	 */
	private JSONObject mapToKeysAndResults(Map data, String keyLabel, String resultLabel) {
		JSONObject result = new JSONObject();
		result.put(keyLabel, new JSONArray(data.keySet()));
		result.put(resultLabel, data);
		return result;
	}
}
