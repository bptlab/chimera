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

    @GET    //um Aktivitaeten zu bekommen
    @Path("{Scenarioname}/{Instance}/{Status}") //scenarioID = (int) Scenarioname, scenarioInstanceID = (int) Instance, status = {enabled, terminated}
    @Produces(MediaType.APPLICATION_JSON)
    public Response showEnabledActivities( @PathParam("Scenarioname") int scenarioID, @PathParam("Instance") int scenarioInstanceID,  @PathParam("Status") String status ){
        if (status.equals("enabled")) {//offene Aktivitaeten
            if (!executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID))){
                return Response.serverError().entity("Error: not a correct scenario instance").build();
                
            }
            LinkedList<Integer> enabledActivitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(scenarioInstanceID);
            if(enabledActivitiesIDs.size() == 0) return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//keine offenen Aktivitaeten vorhanden
            Gson gson = new Gson();
            JsonHashMapIntegerString json = new JsonHashMapIntegerString(enabledActivitiesIDs, labels);
            String jsonRepresentation = gson.toJson(json);
            return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
        }else if(status.equals("terminated")){//geschlossene Aktivitaeten
            if(!executionService.existScenarioInstance(scenarioID,scenarioInstanceID)) return Response.serverError().entity("Error: not a correct scenario instance").build();
            LinkedList<Integer> terminatedActivities = historyService.getTerminatedActivitysForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = historyService.getTerminatedActivityLabelsForScenarioInstance(scenarioInstanceID);
            if(terminatedActivities.size() == 0) return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//keine geschlossenen Aktivitaeten vorhanden
            Gson gson = new Gson();
            JsonHashMapIntegerString json = new JsonHashMapIntegerString(terminatedActivities, labels);
            String jsonRepresentation = gson.toJson(json);
            return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();
        }
        return Response.serverError().entity("Error: status not clear").build();//status != {enabled,terminated}
    }

    @GET    //um Datenobjekte + Zustand zu bekommen
    @Path("DataObjects/{Scenarioname}/{Instance}")  //scenarioID = (int) Scenarioname, scenarioInstanceID = (int) Instance
    @Produces(MediaType.APPLICATION_JSON)
    public Response showDataObjects( @PathParam("Scenarioname") int scenarioID, @PathParam("Instance") int scenarioInstanceID,  @PathParam("Status") String status ){
        if (!executionService.openExistingScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID))) return Response.serverError().entity("Error: not a correct scenario instance").build();
        LinkedList<Integer> dataObjects = executionService.getAllDataObjectIDs(scenarioInstanceID);
        HashMap<Integer, String> labels = executionService.getAllDataObjectStates(scenarioInstanceID);
        if(dataObjects.size() == 0) return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//keine Datenobjekte vorhanden
        Gson gson = new Gson();
        JsonHashMapIntegerString json = new JsonHashMapIntegerString(dataObjects, labels);
        String jsonRepresentation = gson.toJson(json);
        return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();
    }

    @GET    //um IDs aller Szenarien anzuzeigen
    @Path("Show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScenarios(){
        LinkedList<Integer> scenarioIDs = executionService.getAllScenarioIDs();
        if(scenarioIDs.size() == 0) return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//keine Szenarien vorhanden
        Gson gson = new Gson();
        JsonIntegerList json = new JsonIntegerList(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);
        return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();

    }

    @GET    //um die scnerioId einer SzenrioInstanz zu bekommen
    @Path("Get/ScenarioID/{ScenarioInstance}")  //scenarioInstanceID = (int) ScenarioInstance
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenarioID(@PathParam("ScenarioInstance") int scenarioInstanceID){
        if(!executionService.existScenarioInstance(scenarioInstanceID)) return Response.serverError().entity("Error: not a correct scenario instance").build();
        int scenarioID = executionService.getScenarioIDForScenarioInstance(scenarioInstanceID);
        Gson gson = new Gson();
        JsonInteger json = new JsonInteger(scenarioID);
        String jsonRepresentation = gson.toJson(json);
        return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();

    }

    @GET    //um alle scenarioInstanceIDs aller SzenarioInstanzen eines Szenarios zu bekommmen
    @Path("Instances/{Scenarioname}")   //scenarioID = (int) Scenarioname
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScenarioInstances(@PathParam("Scenarioname") int scenarioID){
        if(!executionService.existScenario(scenarioID)) return Response.serverError().entity("Error: not a correct scenario").build();
        LinkedList<Integer> scenarioIDs = executionService.listAllScenarioInstancesForScenario(scenarioID);
        if(scenarioIDs.size() == 0) return Response.ok(new String("{empty}"), MediaType.APPLICATION_JSON_TYPE).build();//keine Instanzen vorhanden
        Gson gson = new Gson();
        JsonIntegerList json = new JsonIntegerList(scenarioIDs);
        String jsonRepresentation = gson.toJson(json);
        return Response.ok(jsonRepresentation,MediaType.APPLICATION_JSON).build();

    }

    @GET    //um das Label einer Aktivitaet zu bekommen für eine aktivitaetsID
    @Path("ActivityID/{Activity}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showLabelForActivity(@PathParam("Activity") int activityInstanceID){
        String label = executionService.getLabelForControlNodeID(activityInstanceID);
        if(label.equals("")) return Response.serverError().entity("Error: not correct Activity ID").build();
        Gson gson = new Gson();
        String jsonRepresentation = gson.toJson("{"+label+"}");
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
    }


    @POST   //um eine Aktivitaet zu beginnen/beenden + Kommentar
    @Path("{Scenarioname}/{Instance}/{Activity}/{Status}/{Comment}")    //scenarioID = (int) Scenarioname, scenarioInstanceID = (int) Instance, activityInstanceID = (int) Activity, status = {enabled, terminated}, comment = Comment
    public Boolean doActivity( @PathParam("Scenarioname") String scenarioID, @PathParam("Instance") int scenarioInstanceID, @PathParam("Activity") int activityInstanceID, @PathParam("Status") String status, @PathParam("Comment") String comment ){
        executionService.openExistingScenarioInstance(new Integer(scenarioID),new Integer(scenarioInstanceID));
        if (status.equals("begin")) {//Aktivitaet beginnen
            executionService.beginActivity(scenarioInstanceID, activityInstanceID);
            return true;
        }else if(status.equals("terminate")) {//Aktivitaet beenden
            executionService.terminateActivity(scenarioInstanceID, activityInstanceID);
            return true;
        }
        return false;
    }

    @POST   //um eine Instanz eines Szenarios zu starten
    @Path("Start/{Scenarioname}")   //scenarioID = (int) Scenarioname
    public int startNewActivity( @PathParam("Scenarioname") int scenarioID){
        if(executionService.existScenario(scenarioID)) {//Szenario existiert
            return executionService.startNewScenarioInstance(scenarioID);
        }else{//Szenario existiert nicht
            return -1;
        }
    }
    //Alles hier darunter wird benutzt um ein von Nikolai's entwickelten Front-End verstaendliches Json zu erzeugen
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
