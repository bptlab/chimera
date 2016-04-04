package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TimerParsingTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    private String timerDefinition = "<bpmn:intermediateCatchEvent id=\"IntermediateCatchEvent_11vnoe1\">" +
            "    <bpmn:incoming>SequenceFlow_1t3sfei</bpmn:incoming>" +
            "    <bpmn:outgoing>SequenceFlow_14j6mwh</bpmn:outgoing>" +
            "    <bpmn:timerEventDefinition>" +
            "            <bpmn:timeDuration xsi:type=\"bpmn:tFormalExpression\">PT1M30S</bpmn:timeDuration>" +
            "    </bpmn:timerEventDefinition>" +
            "</bpmn:intermediateCatchEvent>";

    @Test
    public void testEventDeserialization() {
        Document doc = XmlTestHelper.getDocumentFromXmlString(timerDefinition);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(IntermediateEvent.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            IntermediateEvent event = (IntermediateEvent) jaxbUnmarshaller.unmarshal(doc);
            TimerDefinition timer = event.getTimer();
            assertEquals("PT1M30S", timer.getTimerDuration());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTimerSaving() throws JAXBException {
        File file = new File("src/test/resources/Scenarios/TimerEventScenario.json");

        try {
            String json = FileUtils.readFileToString(file);
            ScenarioData scenario = new ScenarioData(json);
            scenario.save();

            DbObject dataObject = new DbObject();
            String retrieveTimerDefinitions = "SELECT * FROM timerevent";
            List<String> timerDefinitions = dataObject.
                    executeStatementReturnsListString(retrieveTimerDefinitions, "timerDefinition");
            assertEquals(2, timerDefinitions.size());
        } catch (IOException e) {
            fail();
        }

    }
}
