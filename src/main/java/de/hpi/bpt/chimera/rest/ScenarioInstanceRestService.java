package de.hpi.bpt.chimera.rest;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.exception.IllegalCaseModelIdException;
import de.hpi.bpt.chimera.rest.beans.caze.CaseOverviewJaxBean;

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
import java.util.stream.Collectors;

/**
 * This class implements the REST interface for scenario instances.
 */
@Path("interface/v2")
public class ScenarioInstanceRestService extends AbstractRestService {
	private static final Logger log = Logger.getLogger(RestInterface.class);

	/**
	 * This method provides information about all instances of one scenario. The
	 * scenario is specified by an given id. If there is no scenario with the
	 * specific id a 404 response with a meaningful error message will be
	 * returned. If the Scenario exists a JSON-Array containing JSON-Objects
	 * with important information about an instance of the scenario will be
	 * returned.
	 *
	 * @param uri
	 *            Request URI.
	 * @param scenarioId
	 *            The id of the scenario which instances should be returned.
	 * @param filterString
	 *            Specifies a search. Only scenarios which name contain the
	 *            specified string will be returned.
	 * @return A JSON-Object with an array of information about all instances of
	 *         one specified scenario. The information contains the id and name.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScenarioInstances(@Context UriInfo uri, @PathParam("scenarioId") String cmId, @DefaultValue("") @QueryParam("filter") String filterString) {
		try {
			List<CaseExecutioner> caseExecutions = ExecutionService.getAllCasesOfCaseModel(cmId);

			if (!filterString.isEmpty())
				caseExecutions = caseExecutions.stream().filter(instance -> instance.getCase().getName().contains(filterString)).collect(Collectors.toList());

			caseExecutions.sort((e1, e2) -> e1.getCase().getInstantiation().compareTo(e2.getCase().getInstantiation()));

			JSONArray result = new JSONArray();
			for (CaseExecutioner caseExecutioner : caseExecutions) {
				result.put(new JSONObject(new CaseOverviewJaxBean(caseExecutioner)));
			}

			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalCaseModelIdException e) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Creates a new instance of a specified scenario.
	 * This method assumes that the name of then new instance will be the same
	 * as the name of the scenario.
	 * Hence no additional information should be transmitted.
	 * The response will imply if the post was successful.
	 *
	 * @param uri        a context, which holds information about the server
	 * @param scenarioId the id of the scenario.
	 * @return The Response of the POST. The Response code will be
	 * either a 201 (CREATED) if the post was successful or 400 (BAD_REQUEST)
	 * if the scenarioID was invalid.
	 * The content of the Response will be a JSON-Object containing information
	 * about the new instance.
	 */
	@POST
	@Path("scenario/{scenarioId}/instance")
	@Produces(MediaType.APPLICATION_JSON)
	public Response startNewInstance(@Context UriInfo uri, @PathParam("scenarioId") String cmId) {
		try {
			return initializeNewInstance(uri, cmId);
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Creates a new instance of a specified scenario. This method assumes that
	 * the new instance will be named. The name will be received as a
	 * JSON-Object inside the request Body. The JSON should have the format
	 * {"name": "nameOfInstance"}. The response will imply if the post was
	 * successful.
	 *
	 * @param uriInfo
	 *            - The context of the server.
	 * @param scenarioId
	 *            - The id of the scenario.
	 * @param name
	 *            - The name, which will be used for the new instance.
	 * @return The Response of the PUT. The Response code will be either a 201
	 *         (CREATED) if the post was successful or 400 (BAD_REQUEST) if the
	 *         scenarioId was invalid. The content of the Response will be a
	 *         JSON-Object containing information about the new instance.
	 */
	@PUT
	@Path("scenario/{scenarioId}/instance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response startNewNamedInstance(@Context UriInfo uriInfo, @PathParam("scenarioId") String cmId, @DefaultValue("") final String name) {
		try {
			JSONObject nameJson = new JSONObject(name);

			if (nameJson.has("name")) {
				return initializeNewInstance(uriInfo, cmId, nameJson.getString("name"));
			} else {
				return initializeNewInstance(uriInfo, cmId);
			}
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}

	}

	/**
	 * Initialize a new Case.
	 * 
	 * @param uriInfo
	 * @param cmId
	 * @return Response
	 * @see {@link #initializeNewInstance(UriInfo, String, String) initialize
	 *      with custom name}
	 */
	private Response initializeNewInstance(UriInfo uriInfo, String cmId) {
		try {
			return initializeNewInstance(uriInfo, cmId, "");
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * Initialize a new Case with custom Name.
	 * 
	 * @param uriInfo
	 * @param cmId
	 * @param name
	 * @return Response
	 */
	private Response initializeNewInstance(UriInfo uriInfo, String cmId, String name) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.createCaseExecutioner(cmId, name);
			caseExecutioner.startCase();

			JSONObject result = new JSONObject(new CaseOverviewJaxBean(caseExecutioner));
			return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(result.toString()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * This method provides detailed information about a scenario instance. The
	 * information will contain the id, name, parent scenario and the number of
	 * activities in the different states. The Response is JSON-Object.
	 *
	 * @param uriInfo
	 *            - Contains the context information, is used to build links to
	 *            other resources.
	 * @param scenarioId
	 *            - The ID of the scenario.
	 * @param instanceId
	 *            - The ID of the instance.
	 * @return Will return a Response with a JSON-Object body, containing the
	 *         information about the instance. If the scenarioId or the
	 *         instanceId is incorrect 404 (NOT_FOUND) will be returned. If both
	 *         IDs are correct a 200 (OK) with the expected JSON-Content will be
	 *         returned.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScenarioInstance(@Context UriInfo uriInfo, @PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

			JSONObject result = new JSONObject(new CaseOverviewJaxBean(caseExecutioner));
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * This method provides a list of registered events for the instance.
	 *
	 * @param uriInfo    Contains the context information, is used to build
	 *                   links to other resources.
	 * @param scenarioId The ID of the scenario.
	 * @param instanceId The ID of the instance.
	 * @return A list of events for the specified scenario instance.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/events")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEvents(@Context UriInfo uriInfo, @PathParam("scenarioId") int scenarioId, @PathParam("instanceId") int instanceId) {
		// TODO
		
		// ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId, instanceId);
		// List<String> eventKeys = scenarioInstance.getRegisteredEventKeys();
		// return Response.ok(MediaType.APPLICATION_JSON).entity(eventKeys).build();
		return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Not implemented yet").build();
	}

	/**
	 * This get is used to determine whether a given scenario instance can terminate,
	 * meaning whether one of its termination conditions is fulfilled.
	 *
	 * @param scenarioId The Id of the scenario
	 * @param instanceId The Id of the instance
	 * @return A Response: 200 if termination conditions are fulfilled,
	 * 400 if none is fulfilled, 404 if the scenario instance is not found.
	 */
	@GET
	@Path("scenario/{scenarioId}/instance/{instanceId}/canTerminate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkTermination(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);

			JSONObject result = new JSONObject();
			result.put("canTerminate", caseExecutioner.canTerminate());
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}

	/**
	 * This method is used to terminate a scenario instance.
	 *
	 * @param cmId
	 *            The Id of the scenario
	 * @param caseId
	 *            The Id of the instance
	 * @return A Response: 200 if termination conditions are fulfilled and it
	 *         has been terminated, 400 if the termination conditions are not
	 *         fulfilled, 404 if the scenario instance is not found.
	 */
	@POST
	@Path("scenario/{scenarioId}/instance/{instanceId}/terminate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response terminateScenarioInstance(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
			if (caseExecutioner.isTerminated()) {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("Case is already terminated.")).build();
			}

			if (caseExecutioner.canTerminate()) {
				caseExecutioner.terminate();
				return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"case terminated.\"}").build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(buildError("termination condition is not fulfilled.")).build();
			}
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
