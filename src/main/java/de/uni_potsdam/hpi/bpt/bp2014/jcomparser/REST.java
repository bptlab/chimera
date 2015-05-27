package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import com.google.gson.Gson;
import de.uni_potsdam.hpi.bpt.bp2014.settings.Settings;
import de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.HashMap;


/**
 * As a part of the JComparser we need to provide a REST API.
 * The REST API provides an Interface to to manage changes
 * or updates in the JEngine Database.
 *
 */

@Path("jcomparser")
public class REST {
    static Logger log = Logger.getLogger(REST.class.getName());
    /**
     * The URL to the ProcessEditor Model repository.
     */
    private static final String PCM_URL = Settings.processeditorServerUrl + "models/";
    /**
     * The URL to the ProcessEditor.
     */
    private static final String PROCESS_SERVER_URI = Settings.processeditorServerUrl;

    /**
     * This methods parses a new scenario and saves it to the database.
     *
     * @param scenarioID The id of the scenario to be parsed.
     * @return The successCode of the JComparser.
     * @throws IOException                  Connection to the server has not been successful.
     * @throws SAXException                 The XML could not be parsed.
     * @throws ParserConfigurationException The parsing was not successful.
     */
    @POST
    @Path("launch/{scenarioID}")
    public int startComparser(@PathParam("scenarioID") final int scenarioID)
            throws IOException, SAXException, ParserConfigurationException {
        JComparser comparser = new JComparser();
        String scenarioURL = PCM_URL + scenarioID + ".pm";
        return comparser.fetchAndParseScenarioFromServer(scenarioURL, PROCESS_SERVER_URI);
    }


    /**
     * Fetches a List of all Scenarios and their IDs from the PE-Server.
     *
     * @return A response with a JSON-Map with the scenario names and ids.
     */
    @GET
    @Path("scenarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScenarios() {
        HashMap<String, String> scenarioIDs = null;
        JComparser comparser = new JComparser();

        try {
            scenarioIDs = comparser.getScenarioNamesAndIDs(PROCESS_SERVER_URI);
        } catch (XPathExpressionException e) {
            log.error("Error:", e);
        }

        if (scenarioIDs.size() == 0) {
            // no scenarios present
            return Response.ok("{empty}",
                    MediaType.APPLICATION_JSON_TYPE).build();
        }

        Gson gson = new Gson();
        JsonUtil.JsonStringHashMap json = new JsonUtil.JsonStringHashMap(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);

        return Response.ok(jsonRepresentation,
                MediaType.APPLICATION_JSON).build();

    }

    /**
     * This method fetches the image from the PE-Server and returns it.
     *
     * @param modelID The ID of the model to be visualized.
     * @return A Response containing the image.
     */
    @GET
    @Path("scenarios/{modelID}/image/")
    @Produces("image/png")
    public Response showModelImage(@PathParam("modelID") final String modelID) {
        String urlToRead = PROCESS_SERVER_URI + "models/" + modelID + ".png";

        return new Retrieval()
                .getImagewithAuth(PROCESS_SERVER_URI, urlToRead);
    }
}
