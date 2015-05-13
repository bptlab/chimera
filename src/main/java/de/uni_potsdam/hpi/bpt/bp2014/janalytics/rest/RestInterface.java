package de.uni_potsdam.hpi.bpt.bp2014.janalytics.rest;


import de.uni_potsdam.hpi.bpt.bp2014.janalytics.ServiceManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

/**
 * This class implements the REST interface of the JEngine analytics.
 */
@Path("analytics/v2/")
public class RestInterface {
    private ServiceManager serviceManager = ServiceManager.getInstance();


    /**
     * Get all services in a JSONArray.
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
     * @param service the specific service.
     * @return JSON with the result.
     */
    @GET
    @Path("services/{service}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServiceResults(@PathParam("service") String service) {
        if (!serviceManager.existService(service)){
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no service " + service + "\"}")
                    .build();
        }
        JSONObject jsonObject = serviceManager.getResultForService(service);
        return Response.ok(jsonObject.toString(), MediaType.APPLICATION_JSON).build();
    }

    /**
     * Starts a service with optional json.
     * @param service the specific service.
     * @param json optional json with arguments.
     * @return
     */
    @POST
    @Path("services/{service}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateServiceResults(@PathParam("service") String service, String json) {
        if (!serviceManager.existService(service)){
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"There is no service " + service + "\"}")
                    .build();
        }
        if (json.equals("")) {
            serviceManager.calculateResultForService(service, new String[0]);
        } else {
            ArrayList<String> list = new ArrayList<String>();
            JSONArray jsonArray;
            try {
                JSONObject jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("args");
            }catch(Exception e){
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
            serviceManager.calculateResultForService(service, list.toArray(new String[list.size()]));
        }
        return Response.ok("{}", MediaType.APPLICATION_JSON).build();
    }
}
