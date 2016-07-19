package de.hpi.bpt.chimera.jcomparser.jaxb;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static org.junit.Assert.*;

/**
 *
 */
public class SequenceFlowTest {
    private String testString =
            "<bpmn:sequenceFlow id=\"SequenceFlow_0ct04t8\" " +
                    "sourceRef=\"ExclusiveGateway_0pjea83\" " +
                    "targetRef=\"Task_0c9phqs\" />\n";

    @Test
    public void testEventDeserialization() {
        Document doc = XmlTestHelper.getDocumentFromXmlString(testString);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SequenceFlow.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SequenceFlow sequenceFlow = (SequenceFlow) jaxbUnmarshaller.unmarshal(doc);
            assertEquals("SequenceFlow_0ct04t8", sequenceFlow.getId());
            assertEquals("ExclusiveGateway_0pjea83", sequenceFlow.getSourceRef());
            assertEquals("Task_0c9phqs", sequenceFlow.getTargetRef());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}