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
    private ExecutionService executionService = new ExecutionService();
    private HistoryService historyService = new HistoryService();

    @GET
    @Path("{Scenarioname}/{Instance}/{Status}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showEnabledActivities( @PathParam("Scenarioname") int scenarioID, @PathParam("Instance") int scenarioInstanceID,  @PathParam("Status") String status ){
        if (status.equals("enabled")) {
            if (!executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID))) return "error: not a correct scenario instance";
            LinkedList<Integer> enabledActivitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(scenarioInstanceID);
            if(enabledActivitiesIDs.size() == 0) return "empty";
            Gson gson = new Gson();
            JsonActivities json = new JsonActivities(enabledActivitiesIDs, labels);
            String jsonRepresentation = gson.toJson(json);
            return jsonRepresentation;
        }else if(status.equals("terminated")){
            if(!executionService.existScenarioInstance(scenarioID,scenarioInstanceID)) return "error: not a correct scenario instance";
            LinkedList<Integer> terminatedActivities = historyService.getTerminatedActivitysForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = historyService.getTerminatedActivityLabelsForScenarioInstance(scenarioInstanceID);
            if(terminatedActivities.size() == 0) return "empty";
            Gson gson = new Gson();
            JsonActivities json = new JsonActivities(terminatedActivities, labels);
            String jsonRepresentation = gson.toJson(json);
            return jsonRepresentation;
        }
        return "error: status not clear";
    }

    @GET
    @Path("Show")
    @Produces(MediaType.APPLICATION_JSON)
    public String showScenarios(){
        LinkedList<Integer> scenarioIDs = executionService.getAllScenarioIDs();
        if(scenarioIDs.size() == 0) return "empty";
        Gson gson = new Gson();
        JsonScenarioIDS json = new JsonScenarioIDS(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);
        return jsonRepresentation;

    }

    @GET
    @Path("Instances/{Instance}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showScenarioInstances(@PathParam("Instance") int scenarioID){
        if(!executionService.existScenario(scenarioID)) return "error: not a correct scenario";
        LinkedList<Integer> scenarioIDs = executionService.listAllScenarioInstancesForScenario(scenarioID);
        if(scenarioIDs.size() == 0) return "empty";
        Gson gson = new Gson();
        JsonScenarioIDS json = new JsonScenarioIDS(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);
        return jsonRepresentation;

    }


    @POST
    @Path("{Scenarioname}/{Instance}/{Activity}/{Status}/{Comment}")
    public Boolean doActivity( @PathParam("Scenarioname") String scenarioID, @PathParam("Instance") int scenarioInstanceID, @PathParam("Activity") int activityInstanceID, @PathParam("Status") String status, @PathParam("Comment") String comment ){
        executionService.openExistingScenarioInstance(new Integer(scenarioID),new Integer(scenarioInstanceID));
        if (status.equals("begin")) {
            executionService.beginActivity(scenarioInstanceID, activityInstanceID);
        }else if(status.equals("terminate")) {
            executionService.terminateActivity(scenarioInstanceID, activityInstanceID);
        }
        return true;
    }

    class JsonActivities{
        private LinkedList<Integer> ids;
        private HashMap<Integer, String> label;

        public JsonActivities(LinkedList<Integer> activitiesIDs, HashMap<Integer, String> labels){
            this.ids = activitiesIDs;
            this.label = labels;
        }
    }
    class JsonScenarioIDS{
        private LinkedList<Integer> ids;

        public JsonScenarioIDS(LinkedList<Integer> scenarioIDs){
            this.ids = scenarioIDs;
        }
    }
}
