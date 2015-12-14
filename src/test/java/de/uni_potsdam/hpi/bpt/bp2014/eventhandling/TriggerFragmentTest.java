package de.uni_potsdam.hpi.bpt.bp2014.eventhandling;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.DatabaseFragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Scenario;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class TriggerFragmentTest {
	final String url = "http://localhost:8080";

	@Test
	public void testEventQueryQueue() throws IOException {
//		File file = new File("src/test/resources/jaxb/ExampleScenario.json");
//		Scenario scenario = new Scenario();
//		try {
//			String json = FileUtils.readFileToString(file);
//			scenario.initializeInstanceFromJson(json);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		ExecutionService executionService = ExecutionService.getInstance(scenario.getDatabaseID());
//		ScenarioInstance si = executionService.getScenarioInstance(executionService.startNewScenarioInstance());
//
//		DatabaseFragment fragment = scenario.getFragments().get(0);
//		// TODO read start event name from database and use as query
//		String query = "";
//		EventQueryQueue q = new EventQueryQueue();
//		q.registerQuery(fragment.getFragmentName(), query, "test@test.de", url);
//		q.receiveEvent();
//
//		// TODO enable fragment
	}
}
