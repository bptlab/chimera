package de.uni_potsdam.hpi.bpt.bp2014.janalytics.rest;


import de.uni_potsdam.hpi.bpt.bp2014.janalytics.ServiceManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * This class implements the REST interface of the JEngine analytics.
 */
@Path("analytics/v2/")
public class RestInterface {
    private final ServiceManager serviceManager = new ServiceManager();


    /**
     * Get all services in a JSONArray.
     *
     * @return JSONArray with all services
     */
    @GET
    @Path("services")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServices() {
        java.util.Set<String> services = serviceManager.getServices();
        return Response.ok((new JSONArray(services)).toString(), MediaType.APPLICATION_JSON).build();
    }

    /**
     * Returns the result of an service.
     * @param resultId represents the calculated result for a service in the database
     * @param service the specific service.
     * @return JSON with the result.
     */
    @GET
    @Path("services/{service}/result/{resultID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServiceResults(@PathParam("service") String service, @PathParam("resultID") int resultId) {
        if (!serviceManager.existService(service)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no service " + service + "\"}")
                    .build();
        }
        JSONObject jsonObject = serviceManager.getResultForServiceViaId(service, resultId);
        return Response.ok(jsonObject.toString(), MediaType.APPLICATION_JSON).build();
    }

    /**
     * Starts a service with optional json.
     *
     * @param service the specific service.
     * @param json    optional json with arguments.
     * @return JSON with the result.
     */
    @POST
    @Path("services/{service}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateServiceResults(
            @Context UriInfo uriInfo, @PathParam("service") String service, String json) {
        int resultId;
        if (!serviceManager.existService(service)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no service " + service + "\"}")
                    .build();
        }
        if (json.equals("")) {
           resultId = serviceManager.calculateResultForService(service, new String[0]);
        } else {
            ArrayList<String> list = new ArrayList<>();
            JSONArray jsonArray;
            try {
                JSONObject jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("args");
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"error\":\"Not correct json syntax!\"}")
                        .build();
            }
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    list.add(jsonArray.get(i).toString());
                }
            }
            resultId = serviceManager.calculateResultForService(service, list.toArray(new String[list.size()]));
        }
        //return Response.ok("{}", MediaType.APPLICATION_JSON).build();
        try {
            return Response.seeOther(new URI(uriInfo.getAbsolutePath() + "/result/"+ resultId)).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Response.ok("{}", MediaType.APPLICATION_JSON).build();
    }
}
