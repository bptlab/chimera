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
    *
     * @param algorithmID
     * @param instanceID The scenario Instance ID.
     * @return
     */
    @GET
    @Path("algorithm/{algorithmID}/instance/{instanceID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnalyticsResult(@PathParam("algorithmID") String algorithmID,
                                       @PathParam("instanceID") int instanceID) {

        //TODO: to be edited with regards to the detailed implementation of the Algorithm

        Boolean result = analyticsService.getAnalysisResultForInstance(instanceID, algorithmID);

        if(!result) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                    .build();
        } else {
            return Response
                    .ok()
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new JSONObject("").toString())
                    .build();
        }
    }

    /**
     * This method triggers the execution of the defined algorithm for analytic purposes
     *
     * @param algorithmID
     * @param instanceID The scenario Instance ID.
     * @return
     */
    @POST
    @Path("algorithm/{algorithmID}/instance/{instanceID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postAnalyticsResult(@PathParam("algorithmID") String algorithmID,
                                       @PathParam("instanceID") int instanceID) {
        Boolean result = analyticsService.executeAnalysisResultForInstance(instanceID, algorithmID);

        if(!result) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                    .build();
        } else {
            return Response
                    .ok()
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new JSONObject("").toString())
                    .build();
        }
    }
}
