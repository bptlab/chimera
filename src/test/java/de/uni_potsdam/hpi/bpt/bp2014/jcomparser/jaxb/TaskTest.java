package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderImpl;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;
import java.util.regex.Matcher;

import static org.junit.Assert.*;

/**
 *
 */
public class TaskTest {
    private String testString =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<bpmn:task id=\"Task_1qp9gsh\" name=\"Do great work\">\n" +
                        "   <bpmn:incoming>SequenceFlow_1r09dad</bpmn:incoming>\n" +
                        "   <bpmn:outgoing>SequenceFlow_0306jnu</bpmn:outgoing>\n" +
                    "</bpmn:task>";
    @Test
    public void testTaskDeserialization() {
        Document doc = XmlTestHelper.getDocumentFromXmlString(testString);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Task task = (Task) jaxbUnmarshaller.unmarshal(doc);
            assertEquals("Task_1qp9gsh", task.getId());
            assertEquals("Do great work", task.getName());
            assertEquals("SequenceFlow_1r09dad", task.getIncoming());
            assertEquals("SequenceFlow_0306jnu", task.getOutgoing());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerialization() {
        Task task = new Task();
        task.setId("Task_1qp9gsh");
        task.setName("Do great work");
        task.setIncoming("SequenceFlow_1r09dad");
        task.setOutgoing("SequenceFlow_0306jnu");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
            String output = XmlTestHelper.asString(jaxbContext, task);
            org.hamcrest.Matcher<String> matcher =
                    IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                    this.testString);
            assert(matcher.matches(output));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}