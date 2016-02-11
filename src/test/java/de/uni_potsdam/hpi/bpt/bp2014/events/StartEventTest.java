package de.uni_potsdam.hpi.bpt.bp2014.events;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.Scenario;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Maarten on 11.02.2016.
 */
public class StartEventTest {
	@Test
	public void testScenario() {
		File file = new File("src/test/resources/Testscenario/TestStartEvent.json");
		Scenario scenario = new Scenario();
		try {
			String json = FileUtils.readFileToString(file);
			scenario.initializeInstanceFromJson(json);
		} catch (IOException e) {
			e.printStackTrace();
			assert(false);
		}
	}
}
