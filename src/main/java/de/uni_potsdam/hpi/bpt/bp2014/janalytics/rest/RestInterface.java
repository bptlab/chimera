package de.uni_potsdam.hpi.bpt.bp2014.janalytics.rest;

import de.uni_potsdam.hpi.bpt.bp2014.janalytics.AnalyticsService;
import de.uni_potsdam.hpi.bpt.bp2014.util.JsonUtil;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This class implements the REST interface of the JEngine analytics.
 */
@Path("analytics/v2/")
public class RestInterface {
    static Logger log = Logger.getLogger(RestInterface.class.getName());
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

        Object result = null;
        try {
            result = analyticsService.getAnalysisResultForInstance(instanceID, algorithmID);
        } catch (NoSuchMethodException e) {
            log.error("Error:", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                    .build();
        } catch (InvocationTargetException e) {
            log.error("Error:", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                    .build();
        } catch (IllegalAccessException e) {
            log.error("Error:", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                    .build();
        }
        String jsonRepresentation = JsonUtil.JsonWrapperObject(result);
        return Response.ok(jsonRepresentation, MediaType.APPLICATION_JSON).build();
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
        Boolean result = null;
        try {
            result = (Boolean) analyticsService.executeAnalysisResultForInstance(instanceID, algorithmID);
        } catch (NoSuchMethodException e) {
            log.error("Error:", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                    .build();
        } catch (InvocationTargetException e) {
            log.error("Error:", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                    .build();
        } catch (IllegalAccessException e) {
            log.error("Error:", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"The algorithm is not supported: " + algorithmID + "\"}")
                    .build();
        }

        return Response
                .ok()
                .build();
    }
}
