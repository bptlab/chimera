package de.uni_potsdam.hpi.bpt.bp2014.jcore;

/**
 * Created by Ihdefix on 05.01.2015.
 */
import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;

@Path( "Scenario" )
public class RestConnection {
    @GET
    @Path("{Scenarioname}/{Instance}")
    //@Produces(MediaType.APPLICATION_JSON)
    public String showEnabledActivities( @PathParam("Scenarioname") String scenarioID, @PathParam("Instance") String scenarioInstanceID ){
        ScenarioInstance scenarioInstance = new ScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID));
        ExecutionService executionService = new ExecutionService(scenarioInstance);
        LinkedList<Integer> enabledActivitiesIDs= executionService.getEnabledActivitiesIDs();
        return enabledActivitiesIDs.toString();
    }
    @GET
    @Path( "Scenarioname/{Scenarioname}/Instance/{Instance}/closed" )
    @Produces(MediaType.APPLICATION_JSON)
    public LinkedList showClosedActivities( @PathParam("Scenarioname") String scenarioID, @PathParam("Instance") String scenarioInstanceID ){
        ScenarioInstance scenarioInstance = new ScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID));
        ExecutionService executionService = new ExecutionService(scenarioInstance);
        LinkedList<Integer> closedActivitiesIDs= executionService.getClosedActivitiesIDs();
        return closedActivitiesIDs;
    }
}
