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
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */


/*
As a part of the JComparser we need to provide a REST API in order to manage changes or updates in the JEngine Database.
 */

@Path("JComparser")
public class REST {
    String pcm_url = "http://localhost:1205/models/";
    String processserver = "http://localhost:1205";


    //fire Comparser Execution
    @POST
    @Path("launch/{scenarioID}")
    public int startComparser(@PathParam("scenarioID")  int scenarioID) throws IOException, SAXException, ParserConfigurationException {
        pcm_url = pcm_url + scenarioID + ".pem";
        return de.uni_potsdam.hpi.bpt.bp2014.jcomparser.JComparser.main(pcm_url, processserver);
    }

    @GET    //to show ids and labels of all available scenarios
    @Path("Scenarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScenarios() {
        HashMap<String, String> scenarioIDs = null;

        try {
            scenarioIDs = JComparser.getScenarioNamesAndIDs(processserver);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        if (scenarioIDs.size() == 0)
            return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//no scenarios present

        Gson gson = new Gson();
        JsonIntegerList json = new JsonIntegerList(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);

        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();

    }

    //Everything below is needed to make a Json that Janny's Front-End understands
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
