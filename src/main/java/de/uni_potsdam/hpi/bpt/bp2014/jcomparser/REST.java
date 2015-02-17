package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import com.google.gson.Gson;
import org.xml.sax.SAXException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * ********************************************************************************
 *
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 *
 * ******************************************************************
 *
 * Copyright © All Rights Reserved 2014 - 2015
 *
 * Please be aware of the License. You may found it in the root directory.
 *
 * **********************************************************************************
 */


/**
 * As a part of the JComparser we need to provide a REST API.
 * The REST API provides an Interface to to manage changes
 * or updates in the JEngine Database.
 */

@Path("jcomparser")
public class REST {
    /**
     * The URL to the ProcessEditor Model repository.
     */
    private static final String PCM_URL = "http://172.16.64.113:1205/models/";
    /**
     * The URL to the ProcessEditor.
     */
    private static final String PROCESS_SERVER_URI = "http://172.16.64.113:1205/";

    /**
     * This methods parses a new scenario and saves it to the database.
     *
     * @param scenarioID The id of the scenario to be parsed.
     * @return The successCode of the JComparser.
     * @throws IOException Connection to the server has not been successful.
     * @throws SAXException The XML could not be parsed.
     * @throws ParserConfigurationException The parsing was not successful.
     */
    @POST
    @Path("launch/{scenarioID}")
    public int startComparser(@PathParam("scenarioID")final int scenarioID)
            throws IOException, SAXException, ParserConfigurationException {
        JComparser comparser = new JComparser();
        // TODO: The Main Method should not be used!
        String scenarioURL = PCM_URL + scenarioID + ".pm";
        return comparser.fetchAndParseScenarioFromServer(scenarioURL, PROCESS_SERVER_URI);
    }

    /**
     * Fetches a List of all Scenarios and their IDs from the PE-Server.
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
            e.printStackTrace();
        }

        if (scenarioIDs.size() == 0) {
            // no scenarios present
            return Response.ok(new String("{empty}"),
                    MediaType.APPLICATION_JSON_TYPE).build();
        }

        Gson gson = new Gson();
        JsonIntegerList json = new JsonIntegerList(scenarioIDs);
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
    public Response showModelImage(@PathParam("modelID")final String modelID) {
        String urlToRead = PROCESS_SERVER_URI + "models/" + modelID + ".png";

        Response scenarioImage = new Retrieval()
                .getImagewithAuth(PROCESS_SERVER_URI, urlToRead);
        return scenarioImage;

    }

    //Necessary for JSON encoding
    class JsonHashMapIntegerString {
        private LinkedList<Integer> ids;
        private HashMap<Integer, String> label;

        public JsonHashMapIntegerString(LinkedList<Integer> ids, HashMap<Integer, String> labels) {
            this.ids = ids;
            this.label = labels;
        }
    }

    class JsonIntegerList {
        private HashMap<String, String> ids;

        public JsonIntegerList(HashMap<String, String> ids) {
            this.ids = ids;
        }
    }

    class JsonInteger {
        private Integer id;

        public JsonInteger(Integer id) {
            this.id = id;
        }
    }
}
