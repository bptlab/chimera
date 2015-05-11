package de.uni_potsdam.hpi.bpt.bp2014.janalytics.rest;


import de.uni_potsdam.hpi.bpt.bp2014.janalytics.ServiceManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;


/**
 * This class implements the REST interface of the JEngine analytics.
 */
@Path("analytics/v3/")
public class RestInterface {
    static Logger log = Logger.getLogger(RestInterface.class.getName());
    private ServiceManager serviceManager = new ServiceManager();


    @GET
    @Path("services")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServices() {
        java.util.Set<String> services = serviceManager.getServices();
        return Response.ok((new JSONArray(services)).toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("services/{service}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServiceResults(@PathParam("service") String service) {
        JSONObject jsonObject = serviceManager.getResultForService(service);
        return Response.ok(jsonObject.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("services/{service}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateServiceResults(@PathParam("service") String service, String json) {
        if (json.equals("")) {
            serviceManager.calculateResultForService(service, new String[0]);
        } else {
            JSONObject jsonObject = new JSONObject(json);
            ArrayList<String> list = new ArrayList<String>();
            JSONArray jsonArray = jsonObject.getJSONArray("args");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    list.add(jsonArray.get(i).toString());
                }
            }
            serviceManager.calculateResultForService(service, (String[]) list.toArray());
        }
        return Response.ok(MediaType.APPLICATION_JSON).build();
    }
}
