package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by jaspar.mang on 19.02.15.
 */
public class RestConnection2Test extends JerseyTest{

    @Override
    protected Application configure() {
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jcore.RestConnection.class);
    }

    @Test
    public void test(){
        final Response test = target("/interface/v1/en/scenario/0").request().get();
        assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));
    }


}
