package de.hpi.bpt.chimera.jcomparser.jaxb;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.util.Arrays;

/**
 *
 */
public class ExclusiveGatewayTest {
    private String testString =
            "<bpmn:exclusiveGateway id=\"ExclusiveGateway_0ns9z26\">\n" +
            "  <bpmn:incoming>SequenceFlow_17mh7cg</bpmn:incoming>\n" +
            "  <bpmn:incoming>SequenceFlow_1gyvb8d</bpmn:incoming>\n" +
            "  <bpmn:outgoing>SequenceFlow_11341ca</bpmn:outgoing>\n" +
            "</bpmn:exclusiveGateway>\n";

    @Test
    public void testDeserialization() {
        Document doc = XmlTestHelper.getDocumentFromXmlString(testString);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ExclusiveGateway.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ExclusiveGateway gateway = (ExclusiveGateway) jaxbUnmarshaller.unmarshal(doc);
            Assert.assertEquals("ExclusiveGateway_0ns9z26", gateway.getId());
            Assert.assertEquals("SequenceFlow_11341ca", gateway.getOutgoing().get(0));
            Assert.assertEquals(Arrays.asList("SequenceFlow_17mh7cg", "SequenceFlow_1gyvb8d"),
                    gateway.getIncoming());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}