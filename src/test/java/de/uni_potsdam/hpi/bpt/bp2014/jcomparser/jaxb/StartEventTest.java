package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static org.junit.Assert.*;

public class StartEventTest {
    private String testString =
        "<bpmn:startEvent id=\"StartEvent_1jwyrhz\" griffin:eventquery=\"querY\">"  +
                "<bpmn:outgoing>SequenceFlow_1r09dad</bpmn:outgoing>\n" +
                "</bpmn:startEvent>\n";

    @Test
    public void testEventDeserialization() {
        Document doc = XmlTestHelper.getDocumentFromXmlString(testString);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(StartEvent.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StartEvent startEvent = (StartEvent) jaxbUnmarshaller.unmarshal(doc);
            assertEquals("StartEvent_1jwyrhz", startEvent.getId());
            assertEquals(1,startEvent.getOutgoing().size());
            assertEquals("SequenceFlow_1r09dad", startEvent.getOutgoing().get(0));
            assertEquals("querY" , startEvent.getEventQuery());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}