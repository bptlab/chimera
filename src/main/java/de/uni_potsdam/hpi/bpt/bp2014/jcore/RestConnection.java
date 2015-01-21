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
            executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID));
            //int id = executionService.startNewScenarioInstance(new Integer(scenarioID));
            LinkedList<Integer> enabledActivitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(scenarioInstanceID);
            Gson gson = new Gson();
            JsonActivities json = new JsonActivities(enabledActivitiesIDs, labels);
            String jsonRepresentation = gson.toJson(json);
            return jsonRepresentation;
        }else if(status.equals("terminated")){
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
        return "error: status not clear";
    }

    @GET
    @Path("show")
    @Produces(MediaType.APPLICATION_JSON)
    public String showScenarios(){
        LinkedList<Integer> scenarioIDs = executionService.getAllScenarioIDs();
        Gson gson = new Gson();
        JsonScenarioIDS json = new JsonScenarioIDS(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);
        return jsonRepresentation;

    }

    @GET
    @Path("instances/{Instance}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showScenarioInstances(@PathParam("Instance") int scenarioID){
        LinkedList<Integer> scenarioIDs = executionService.listAllScenarioInstancesForScenario(scenarioID);
        Gson gson = new Gson();
        JsonScenarioIDS json = new JsonScenarioIDS(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);
        return jsonRepresentation;

    }


    @POST
    @Path("{Scenarioname}/{Instance}/{Activity}/{Status}/{Comment}")
    public void doActivity( @PathParam("Scenarioname") String scenarioID, @PathParam("Instance") int scenarioInstanceID, @PathParam("Activity") int activityInstanceID, @PathParam("Status") String status, @PathParam("Comment") String comment ){
        executionService.openExistingScenarioInstance(new Integer(scenarioID),new Integer(scenarioInstanceID));
        if (status.equals("begin")) {
            executionService.beginActivity(scenarioInstanceID, activityInstanceID);
        }else if(status.equals("terminate")) {
            executionService.terminateActivity(scenarioInstanceID, activityInstanceID);
        }
    }

    @GET
    @Path( "{Scenarioname}/{Instance}/closed" )
    @Produces(MediaType.APPLICATION_JSON)
    public String showClosedActivities( @PathParam("Scenarioname") String scenarioID, @PathParam("Instance") String scenarioInstanceID ){
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
    class JsonScenarioIDS{
        private LinkedList<Integer> ids;

        public JsonScenarioIDS(LinkedList<Integer> scenarioIDs){
            this.ids = scenarioIDs;
        }
    }
}
