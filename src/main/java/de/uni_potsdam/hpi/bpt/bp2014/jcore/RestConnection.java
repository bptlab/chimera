package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Created by Ihdefix on 05.01.2015.
 */
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;

@Path( "Scenario" )
public class RestConnection {
//    @GET
//    @Path( "Scenarioname/{Scenarioname}" )
//    @Produces(MediaType.APPLICATION_JSON)
//    public LinkedList showEnabledActivities( @PathParam("Scenarioname") ScenarioInstance scenarioInstance){
//        ExecutionService executionService = new ExecutionService(scenarioInstance);
//        LinkedList<Integer> enabledActivitiesIDs= executionService.getEnabledActivitiesIDs();
//        return enabledActivitiesIDs;
//    }

    @GET
    @Path( "Scenarioname/{Scenarioname}" )
    @Produces(MediaType.APPLICATION_JSON)
    public LinkedList showEnabledActivities( @PathParam("Scenarioname") int scenario_id){
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance_id = executionService.startNewScenarioInstance(scenario_id);
        LinkedList<Integer> enabledActivitiesIDs= executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance_id);
        return enabledActivitiesIDs;
    }
}
