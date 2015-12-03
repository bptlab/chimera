package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static org.junit.Assert.*;

public class StartEventTest {
    private String testString =
            "<bpmn:startEvent id=\"StartEvent_1\">\n" +
                    "<bpmn:outgoing>SequenceFlow_1r09dad</bpmn:outgoing>\n" +
                    "</bpmn:startEvent>\n";

    @Test
    public void testEventDeserialization() {
        Document doc = XmlTestHelper.getDocumentFromXmlString(testString);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(StartEvent.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StartEvent startEvent = (StartEvent) jaxbUnmarshaller.unmarshal(doc);
            assertEquals("StartEvent_1", startEvent.getId());
            assertEquals("SequenceFlow_1r09dad", startEvent.getOutgoing());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}