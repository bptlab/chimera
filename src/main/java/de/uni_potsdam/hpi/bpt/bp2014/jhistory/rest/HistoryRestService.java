package de.uni_potsdam.hpi.bpt.bp2014.jhistory.rest;

import de.uni_potsdam.hpi.bpt.bp2014.jhistory.HistoryService;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.LogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.StateTransitionLog;
import de.uni_potsdam.hpi.bpt.bp2014.util.XmlUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implements the REST interface of the JEngine history.
 */
@Path("history/v2/") public class HistoryRestService {
	private HistoryService historyService = new HistoryService();

    /**
	 * This method gives the log entries for all activities for a specific scenario instance.
	 *
	 * @param scenarioID The id of the scenario belonging to the instance.
	 * @param instanceID The id of the scenario instance.
	 * @param state The current state of the instance.
	 * @return a JSON-Object with the log entries.
	 */
    @GET
    @Path("scenario/{scenarioID}/instance/{instanceID}/activities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivityLog(
            @DefaultValue("0") @PathParam("scenarioID") int scenarioID,
            @DefaultValue("0") @PathParam("instanceID") int instanceID,
            @DefaultValue(" ") @QueryParam("state") String state) {
        if (instanceID == 0 || scenarioID == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The instance or scenario ID "
                            + "is incorrect\"}")
                    .build();
        }
        List<StateTransitionLog> logEntries =
                StateTransitionLog.getStateTransitons(instanceID, LogEntry.LogType.ACTIVITY);
        JSONArray json = new JSONArray(logEntries);
        return Response.ok().type(MediaType.APPLICATION_JSON)
                .entity(json.toString()).build();
    }

    /**
     * This method gives the log entries for all dataObjects for a specific scenario instance.
     *
     * @param scenarioID The id of the scenario belonging to the instance.
     * @param scenarioInstanceID The id of the scenario instance.
     * @return a JSON-Object with the log entries.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{scenarioInstanceID}/dataobjects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataObjectLog(
            @DefaultValue("0") @PathParam("scenarioID") int scenarioID,
            @DefaultValue("0") @PathParam("scenarioInstanceID")
            int scenarioInstanceID) {
        if (scenarioInstanceID == 0 || scenarioID == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The instance or scenario ID "
                            + "is incorrect\"}")
                    .build();
        }
        List<StateTransitionLog> dataobjectLog = StateTransitionLog.getStateTransitons(
                scenarioInstanceID, LogEntry.LogType.DATA_OBJECT);
        return Response.ok().type(MediaType.APPLICATION_JSON)
                .entity(new JSONArray(dataobjectLog).toString()).build();
    }


    /**
     * This method gives the log entries for all dataObjects for a specific scenario instance.
     *
     * @param scenarioID The id of the scenario belonging to the instance.
     * @param scenarioInstanceID The id of the scenario instance.
     * @return a JSON-Object with the log entries.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{scenarioInstanceID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompleteLog(
            @DefaultValue("0") @PathParam("scenarioID") int scenarioID,
            @DefaultValue("0") @PathParam("scenarioInstanceID")
            int scenarioInstanceID) {

        List<StateTransitionLog> log = StateTransitionLog.getStateTransitons(
                scenarioInstanceID);
        return Response.ok().type(MediaType.APPLICATION_JSON)
                .entity(new JSONArray(log).toString()).build();
    }
    
    /**
     * This method gives the log entries for all DataAttributeInstances
     * for a specific scenario instance.
     *
     * @param scenarioID The id of the scenario belonging to the instance.
     * @param scenarioInstanceID The id of the scenario instance.
     * @return a JSON-Object with the log entries.
     */
    @GET
    @Path("scenario/{scenarioID}/instance/{scenarioInstanceID}/attributes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataAttributeLog(
            @DefaultValue("0") @PathParam("scenarioID") int scenarioID,
            @DefaultValue("0") @PathParam("scenarioInstanceID")
            int scenarioInstanceID) {
        if (scenarioInstanceID == 0 || scenarioID == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The instance or scenario ID "
                            + "is incorrect\"}")
                    .build();
        }

        List<StateTransitionLog> attributeLog = StateTransitionLog.getStateTransitons(
                scenarioInstanceID, LogEntry.LogType.DATA_ATTRIBUTE);
        return Response.ok().type(MediaType.APPLICATION_JSON)
                .entity(new JSONArray(attributeLog).toString()).build();
    }

    @GET
    @Path("export/{scenarioId}")
    @Produces(MediaType.APPLICATION_XML)
    public Response exportToXml(@PathParam("scenarioId") int scenarioId)
            throws TransformerConfigurationException {
        HistoryService service = new HistoryService();
        try {
            Document doc = service.getTracesForScenarioId(scenarioId);
            return Response.status(200)
                    .type(MediaType.APPLICATION_XML)
                    .entity(doc).build();
        } catch (ParserConfigurationException e) {
            return Response.status(500).entity("Error processing the xml").build();
        }
    }

}
