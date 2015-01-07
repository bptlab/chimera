package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Created by Ihdefix on 05.01.2015.
 */
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;

@Path( "Scenario" )
public class RestConnection {
    @GET
    @Path( "Scenarioname/{Scenarioname}" )
    @Produces(MediaType.TEXT_XML)
    public void executeScenario( @PathParam("Scenarioname") ScenarioInstance scenarioInstance){
        ExecutionService executionService = new ExecutionService(scenarioInstance);
        LinkedList<Integer> enabledActivitiesIDs= executionService.getEnabledActivitiesIDs();
        System.out.println("enabled Aktivität ID");
        for(int activityID: enabledActivitiesIDs){
            System.out.println(activityID);
        }
    }
}
