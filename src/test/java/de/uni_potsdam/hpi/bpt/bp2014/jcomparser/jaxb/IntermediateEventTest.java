package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static org.junit.Assert.*;

/**
 *
 */
public class IntermediateEventTest {
    private String testString = "<bpmn:intermediateCatchEvent id=\"IntermediateCatchEvent_1le8d7a\" \n" +
            "griffin:eventquery = \"querY\" name=\"Fell asleep while drinking coffee\"> \n" +
            "<bpmn:incoming>SequenceFlow_12g1b5j</bpmn:incoming> \n" +
            "<bpmn:outgoing>SequenceFlow_08s25px</bpmn:outgoing> \n" +
            "<bpmn:messageEventDefinition /> \n" +
            "</bpmn:intermediateCatchEvent>";

    @Test
    public void testEventDeserialization() {
        Document doc = XmlTestHelper.getDocumentFromXmlString(testString);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(IntermediateEvent.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            IntermediateEvent event = (IntermediateEvent) jaxbUnmarshaller.unmarshal(doc);
            assertEquals("IntermediateCatchEvent_1le8d7a", event.getId());
            assertEquals("SequenceFlow_12g1b5j", event.getIncoming());
            assertEquals("SequenceFlow_08s25px", event.getOutgoing());
            assertEquals("querY", event.getEventQuery());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}