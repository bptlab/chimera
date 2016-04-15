package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbTerminationCondition;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation.InvalidDataTransitionException;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation.InvalidDataclassReferenceExeption;
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
 *
 */
@Path("interface/v2/scenario")
public class ScenarioRestService {
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
    @Path("{scenarioId}/terminationcondition")
    @Produces(MediaType.APPLICATION_JSON) public Response getTerminationCondition(
            @PathParam("scenarioId") int scenarioID) {
        DbScenario dbScenario = new DbScenario();
        if (!dbScenario.existScenario(scenarioID)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no scenario with the id "
                            + scenarioID + "\"}")
                    .build();
        }
        DbTerminationCondition terminationCondition = new DbTerminationCondition();
        Map<String, List<Map<String, Object>>> conditionSets = terminationCondition
                .getDetailedConditionsForScenario(scenarioID);
        JSONObject conditions = new JSONObject(conditionSets);
        JSONObject result = new JSONObject();
        result.put("conditions", conditions);
        result.put("setIDs", new JSONArray(conditionSets.keySet()));
        return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
    }


    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postInstance(String scenario) {
        try {
            ScenarioData newScenario = new ScenarioData(scenario);
            newScenario.save();
            return Response.status(201).build();
        } catch (IllegalArgumentException e){
            return Response.status(422)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        } catch (JAXBException e) {
            return Response.status(400)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Invalid xml " + e.getMessage())
                    .build();
        } catch (InvalidDataTransitionException e) {
            return Response.status(422)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        } catch (InvalidDataclassReferenceExeption e) {
            return Response.status(422)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
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
     * @param scenarioID The Id of the scenario used inside the database.
     * @param uri Request URI
     * @return Returns a JSON-Object with detailed information about one scenario.
     * The Information contain the id, label, number of instances, latest version
     * and more.
     */
    @GET
    @Path("{scenarioID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenario(
            @Context UriInfo uri, @PathParam("scenarioID") int scenarioID) {
        DbScenario dbScenario = new DbScenario();
        Map<String, Object> data = dbScenario.getScenarioDetails(scenarioID);

        if (data.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{}")
                    .build();
        }
        data.put("instances", uri.getAbsolutePath() + "/instance");
        return Response.ok().type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject(data).toString()).build();
    }

}
