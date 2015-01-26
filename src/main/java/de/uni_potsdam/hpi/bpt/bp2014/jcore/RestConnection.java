package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Created by Ihdefix on 05.01.2015.
 */
import com.google.gson.Gson;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedList;

@Path( "Scenario" )
public class RestConnection {
    private ExecutionService executionService = new ExecutionService();
    private HistoryService historyService = new HistoryService();

    @GET
    @Path("{Scenarioname}/{Instance}/{Status}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showEnabledActivities( @PathParam("Scenarioname") int scenarioID, @PathParam("Instance") int scenarioInstanceID,  @PathParam("Status") String status ){
        if (status.equals("enabled")) {
            if (!executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID))){
                return Response.serverError().entity("Error: not a correct scenario instance").build();
                
            }
            LinkedList<Integer> enabledActivitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(scenarioInstanceID);
            if(enabledActivitiesIDs.size() == 0) return Response.serverError().entity("Error: empty").build();
            Gson gson = new Gson();
            JsonHashMapIntegerString json = new JsonHashMapIntegerString(enabledActivitiesIDs, labels);
            String jsonRepresentation = gson.toJson(json);
            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
        }else if(status.equals("terminated")){
            if(!executionService.existScenarioInstance(scenarioID,scenarioInstanceID)) return Response.serverError().entity("Error: not a correct scenario instance").build();
            LinkedList<Integer> terminatedActivities = historyService.getTerminatedActivitysForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = historyService.getTerminatedActivityLabelsForScenarioInstance(scenarioInstanceID);
            if(terminatedActivities.size() == 0) return Response.serverError().entity("Error: empty").build();
            Gson gson = new Gson();
            JsonHashMapIntegerString json = new JsonHashMapIntegerString(terminatedActivities, labels);
            String jsonRepresentation = gson.toJson(json);
            return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();
        }
        return Response.serverError().entity("Error: status not clear").build();
    }

    @GET
    @Path("DataObjects/{Scenarioname}/{Instance}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showDataObjects( @PathParam("Scenarioname") int scenarioID, @PathParam("Instance") int scenarioInstanceID,  @PathParam("Status") String status ){
        if (!executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID))) return Response.serverError().entity("Error: not a correct scenario instance").build();
        LinkedList<Integer> dataObjects = executionService.getAllDataObjectIDs(scenarioInstanceID);
        HashMap<Integer, String> labels = executionService.getAllDataObjectStates(scenarioInstanceID);
        if(dataObjects.size() == 0) return Response.serverError().entity("Error: empty").build();
        Gson gson = new Gson();
        JsonHashMapIntegerString json = new JsonHashMapIntegerString(dataObjects, labels);
        String jsonRepresentation = gson.toJson(json);
        return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("Show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScenarios(){
        LinkedList<Integer> scenarioIDs = executionService.getAllScenarioIDs();
        if(scenarioIDs.size() == 0) return Response.serverError().entity("Error: empty").build();
        Gson gson = new Gson();
        JsonIntegerList json = new JsonIntegerList(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);
        return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();

    }

    @GET
    @Path("Get/ScenarioID/{ScenarioInstance}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenarioID(@PathParam("ScenarioInstance") int scenarioInstanceID){
        if(!executionService.existScenarioInstance(scenarioInstanceID)) return Response.serverError().entity("Error: not a correct scenario instance").build();
        int scenarioID = executionService.getScenarioIDForScenarioInstance(scenarioInstanceID);
        Gson gson = new Gson();
        JsonInteger json = new JsonInteger(scenarioID);
        String jsonRepresentation = gson.toJson(json);
        return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();

    }

    @GET
    @Path("Instances/{Instance}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScenarioInstances(@PathParam("Instance") int scenarioID){
        if(!executionService.existScenario(scenarioID)) return Response.serverError().entity("Error: not a correct scenario").build();
        LinkedList<Integer> scenarioIDs = executionService.listAllScenarioInstancesForScenario(scenarioID);
        if(scenarioIDs.size() == 0) return Response.serverError().entity("Error: empty").build();
        Gson gson = new Gson();
        JsonIntegerList json = new JsonIntegerList(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);
        return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();

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

    @POST
    @Path("Start/{Scenarioname}")
    public int startNewActivity( @PathParam("Scenarioname") int scenarioID){
        if(executionService.existScenario(scenarioID)) {
            return executionService.startNewScenarioInstance(scenarioID);
        }else{
            return -1;
        }
    }

    class JsonHashMapIntegerString{
        private LinkedList<Integer> ids;
        private HashMap<Integer, String> label;

        public JsonHashMapIntegerString(LinkedList<Integer> ids, HashMap<Integer, String> labels){
            this.ids = ids;
            this.label = labels;
        }
    }
    class JsonIntegerList{
        private LinkedList<Integer> ids;

        public JsonIntegerList(LinkedList<Integer> ids){
            this.ids = ids;
        }
    }
    class JsonInteger{
        private Integer id;

        public JsonInteger(Integer id){
            this.id = id;
        }
    }
}
