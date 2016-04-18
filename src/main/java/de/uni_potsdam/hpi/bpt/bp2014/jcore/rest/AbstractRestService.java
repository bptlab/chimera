package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
public class AbstractRestService {
    public Response buildNotFoundResponse(String errorMsg) {
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMsg)
                .build();
    }
}
