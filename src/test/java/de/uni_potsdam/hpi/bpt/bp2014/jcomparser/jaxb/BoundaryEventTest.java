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
public class BoundaryEventTest {
    private String testString =
            "<bpmn:boundaryEvent id=\"BoundaryEvent_0dgouhn\" " +
                    "name=\"Fell asleep while drinking coffee\" attachedToRef=\"Task_0c9phqs\">\n" +
            "  <bpmn:outgoing>SequenceFlow_178gzyw</bpmn:outgoing>\n" +
            "  <bpmn:conditionalEventDefinition />\n" +
            "</bpmn:boundaryEvent>\n";

    @Test
    public void testEventDeserialization() {
        Document doc = XmlTestHelper.getDocumentFromXmlString(testString);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BoundaryEvent.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BoundaryEvent task = (BoundaryEvent) jaxbUnmarshaller.unmarshal(doc);
            assertEquals("Task_0c9phqs", task.getAttachedToRef());
            assertEquals("BoundaryEvent_0dgouhn", task.getId());
            assertEquals("Fell asleep while drinking coffee", task.getName());
            assertEquals("SequenceFlow_178gzyw", task.getOutgoing());
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}