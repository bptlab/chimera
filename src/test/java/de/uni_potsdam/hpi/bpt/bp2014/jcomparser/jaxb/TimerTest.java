package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TimerTest {
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
}
