package de.uni_potsdam.hpi.bpt.bp2014.events;


import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class IntermediateEventTest {
	@Test
	public void testScenario() {
		File file = new File("src/test/resources/Testscenario/TestIntermediateEvent.json");
		try {
			String json = FileUtils.readFileToString(file);
            ScenarioData scenario = new ScenarioData(json);
            scenario.save();
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
