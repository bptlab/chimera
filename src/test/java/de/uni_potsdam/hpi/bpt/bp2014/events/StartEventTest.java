package de.uni_potsdam.hpi.bpt.bp2014.events;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.Scenario;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public class StartEventTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
	public void testScenario() {
		File file = new File("src/test/resources/Testscenario/TestStartEvent.json");
		Scenario scenario = new Scenario();
		try {
			String json = FileUtils.readFileToString(file);
			scenario.initializeInstanceFromJson(json);
            ScenarioInstance instance = new ScenarioInstance(1, 3);
            List<AbstractEvent> events = instance.getEventsForScenarioInstance();
            System.out.println(events);
        } catch (IOException e) {
			e.printStackTrace();
			assert(false);
		}
	}
}
