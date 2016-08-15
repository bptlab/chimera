package de.hpi.bpt.chimera.jconfiguration.rest;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.jcore.rest.RestInterface;

import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.sql.SQLException;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class ScenarioConfigurationTest extends JerseyTest {
    WebTarget base;

    @Override
    protected Application configure() {
        return new ResourceConfig(RestConfigurator.class);
    }

    @Before
    public void setUpBase() {
        base = target("config/v2");
    }

    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    /**
     * When one sends a DELETE to {@link RestConfigurator#deleteScenario(Integer)}
     * the returned Status Code should be 202 since the scenario will be deleted in any case.
     */
    @Test
    public void testDeleteScenarioWithRunningInstances() {
        Response response = base.path("scenario/1/").request().delete();
        assertEquals("The Response code of deleting a scenario was not 202",
                202, response.getStatus());
    }

    /**
     * When one sends a DELETE to {@link RestConfigurator#deleteScenario(Integer)}
     * the returned Status Code should be 202 since the scenario will be deleted in any case.
     */
    @Test
    public void testDeleteScenarioWithoutRunningInstances() {
        Response response = base.path("scenario/152/").request().delete();
        assertEquals("The Response code of deleting a scenario was not 202",
                202, response.getStatus());
    }
}
