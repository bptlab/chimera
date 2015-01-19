package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Created by Ihdefix on 05.01.2015.
 */
import com.google.gson.Gson;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.LinkedList;

@Path( "Scenario" )
public class RestConnection {
    @GET
    @Path("{Scenarioname}/{Instance}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showEnabledActivities( @PathParam("Scenarioname") String scenarioID, @PathParam("Instance") String scenarioInstanceID ){
        ExecutionService executionService = new ExecutionService();
        int id = executionService.startNewScenarioInstance(new Integer(scenarioID));
        LinkedList<Integer> enabledActivitiesIDs= executionService.getEnabledActivitiesIDsForScenarioInstance(id);
        HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(id);
        Gson gson = new Gson();
        JsonActivities json = new JsonActivities(enabledActivitiesIDs, labels);
        String jsonRepresentation = gson.toJson(json);
        return jsonRepresentation;
    }
    @GET
    @Path( "{Scenarioname}/{Instance}/closed" )
    @Produces(MediaType.APPLICATION_JSON)
    public String showClosedActivities( @PathParam("Scenarioname") String scenarioID, @PathParam("Instance") String scenarioInstanceID ){
        HistoryService historyService = new HistoryService();
        //LinkedList<Integer> terminatedActivities = historyService.getTerminatedActivitysForScenarioInstance(scenarioInstanceID);
        //HashMap<Integer, String> labels = historyService.getTerminatedActivityLabelsForScenarioInstance(scenarioInstanceID);
        LinkedList<Integer> closedActivitiesIDs = new LinkedList<Integer>();
        closedActivitiesIDs.add(2);
        closedActivitiesIDs.add(4);
        HashMap<Integer, String> labels = new HashMap<Integer, String>();
        Gson gson = new Gson();
        labels.put(2, "Essen kochen");
        labels.put(4, "Zutaten kaufen");
        JsonActivities json = new JsonActivities(closedActivitiesIDs, labels);
        String jsonRepresentation = gson.toJson(json);
        return jsonRepresentation;
    }
    class JsonActivities{
        private LinkedList<Integer> ids;
        private HashMap<Integer, String> label;

        public JsonActivities(LinkedList<Integer> activitiesIDs, HashMap<Integer, String> labels){
            this.ids = activitiesIDs;
            this.label = labels;
        }
    }
}
