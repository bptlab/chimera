package de.uni_potsdam.hpi.bpt.bp2014.janalytics.rest;

import de.uni_potsdam.hpi.bpt.bp2014.janalytics.AnalyticsService;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * This class implements the REST interface of the JEngine analytics.
 */
@Path("analytics/v2/")
public class RestInterface {
    private AnalyticsService analyticsService = new AnalyticsService();

    /**
    * This method returns the result set of a specific algorithm as REST call
    */
    @GET
    @Path("algorithm/{algorithmID}/instance/{instanceID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnalyticsResult(@PathParam("algorithmID") int algorithmID,
                                       @PathParam("instanceID") int instanceID) {
        switch (algorithmID) {
            case 1:
                Boolean result = analyticsService.getAnalysisResultForInstance(instanceID);
                break;
            default:
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                        .build();
        }
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject("").toString())
                .build();
    }

    /**
    * This method triggers the execution of the defined algorithm for analytic purposes
    */
    @POST
    @Path("algorithm/{algorithmID}/instance/{instanceID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postAnalyticsResult(@PathParam("algorithmID") int algorithmID,
                                       @PathParam("instanceID") int instanceID) {
        switch (algorithmID) {
            case 1:
                Boolean result = analyticsService.getAnalysisResultForInstance(instanceID);
                break;
            default:
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                        .build();
        }
        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject("").toString())
                .build();
    }
}
