package de.hpi.bpt.chimera.events;


import de.hpi.bpt.chimera.jcomparser.json.ScenarioData;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class IntermediateCatchEventTest {
	@Test @Ignore
	public void testScenario() throws JAXBException {
        // TODO implement
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
