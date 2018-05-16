package de.hpi.bpt.chimera.rest;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;
import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelDetailsJaxBean;
import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.casemodel.ConditionsJaxBean;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the REST interface for dealing with scenarios.
 */
@Path("interface/v2")
public class ScenarioRestService extends AbstractRestService {
	private static Logger log = Logger.getLogger(RestInterface.class);

	/**
	 * This method allows to give an overview of all scenarios. The response
	 * will return a JSON-Array containing the basic information of all
	 * scenarios currently inside the database. If different versions of an
	 * scenarios exist only the latest ones will be added to the json.
	 *
	 * @param uriInfo
	 *            Specifies the context. For example the uri of the request.
	 * @param filterString
	 *            Specifies a search. Only scenarios which name contain the
	 *            specified string will be returned.
	 * @return Returns a JSON-Object with an Array with entries for every
	 *         Scenario. Each Entry is a JSON-Object with a label and id of a
	 *         scenario.
	 */
	@GET
	@Path("scenario")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScenarios(@Context UriInfo uriInfo, @DefaultValue("") @QueryParam("filter") String filterString) {

		List<CaseModel> caseModels = CaseModelManager.getCaseModels();

		if (!filterString.isEmpty()) {
			caseModels = caseModels.stream().filter(cm -> cm.getName().contains(filterString)).collect(Collectors.toList());
		}

		caseModels.sort((c1, c2) -> c1.getDeployment().compareTo(c2.getDeployment()));

		JSONArray result = new JSONArray();
		for (CaseModel cm : caseModels) {
			result.put(new JSONObject(new CaseModelOverviewJaxBean(cm)));
		}

		return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
	}

	/**
	 * This method enables the creation of new scenarios.
	 *
	 * @param jsonString A Scenario JSON
	 * @return Information about the new scenario.
	 */
	@POST
	@Path("scenario")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postCaseModel(String jsonString) {
		try {
			CaseModelManager.parseCaseModel(jsonString);
			log.info("Successfully parsed a CaseModel");
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"case model deployed.\"}").build();
		} catch (Exception e) {
			log.error("Chimera failed to parse the CaseModel!", e);
			return Response.status(422).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);

			JSONObject result = new JSONObject(new CaseModelDetailsJaxBean(cm));

			return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
	public Response deleteScenario(@PathParam("scenarioId") String cmId) {
		try {
			CaseModelManager.deleteCaseModel(cmId);
			return Response.status(Response.Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"" + "casemodel deletion successful.\"}").build();
		} catch (IllegalArgumentException e) {
			log.error("deletion failed: " + e);
			return Response.status(422).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * This method provides information about the termination condition. Of the
	 * specified Scenario. The termination condition is a set of sets of
	 * conditions. Only if all conditions of one set are true the scenario will
	 * terminate. If the scenario does not exists a 404 with an error will be
	 * returned. If the scenario exists the JSON representation of the condition
	 * set will be returned.
	 *
	 * @param scenarioID
	 *            This id specifies the scenario. The id is the primary key
	 *            inside the database.
	 * @return Returns a response object. It will either be a 200 or 404. The
	 *         content will be either the JSON representation of the termination
	 *         condition or an JSON object with the error message.
	 */
	@GET
	@Path("scenario/{scenarioId}/terminationcondition")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTerminationCondition(@PathParam("scenarioId") String cmId) {
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);

			JSONObject result = new JSONObject(new ConditionsJaxBean(cm.getTerminationCondition()));

			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
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
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);

			JSONArray result = new JSONArray(cm.getContentXmlStrings());
			return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
