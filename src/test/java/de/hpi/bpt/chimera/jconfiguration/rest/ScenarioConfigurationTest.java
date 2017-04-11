package de.hpi.bpt.chimera.jconfiguration.rest;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.AbstractTest;
import de.hpi.bpt.chimera.database.ConnectionWrapper;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.jcore.rest.RestInterface;
import de.hpi.bpt.chimera.util.ScriptRunner;
import net.javacrumbs.jsonunit.core.Option;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.io.StringReader;
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
	public void setUpBase() throws IOException, SQLException {
		// base = target("interface/v2/");
		// TODO don't hard-code the base variable
		base = ClientBuilder.newClient().target("http://localhost:8080/Chimera/api/interface/v2");
		String insertScenarios = "INSERT INTO `scenario` (`id`, `name`, `deleted`, `modelversion`, `datamodelversion`) VALUES " + "(1, 'Testszenario', 0, 0, 0), " + "(5, 'Testszenario', 0, 0, 0), " + "(6, 'Testszenario', 0, 0, 0);";
		String insertScenarioInstances = "INSERT INTO `scenarioinstance` (`id`, `terminated`, `scenario_id`) VALUES " + "(1, 1, 1), " + "(2, 0, 1), " + "(3, 1, 5), " + "(4, 1, 6), " + "(5, 1, 6);";
		ScriptRunner runner = new ScriptRunner(ConnectionWrapper.getInstance().connect(), false, false);
		runner.runScript(new StringReader(insertScenarios));
		runner.runScript(new StringReader(insertScenarioInstances));
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
		assertEquals("The Response code of deleting a scenario was not 404",
				400, response.getStatus());
    }
}
