package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Date;

import static org.junit.Assert.*;

/**
 *
 */
public class LogEntryTest {
    @Test
    public void testXmlSerialization()
            throws ParserConfigurationException, TransformerException {
        LogEntry exampleLogEntry = new LogEntry(1, new Date(), LogEntry.LogType.ACTIVITY, "running",
                "anActivity", 2, 3, 4);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element trace = doc.createElement("traceElement");
        doc.appendChild(trace);
        exampleLogEntry.createXml(trace);
        NodeList children = trace.getChildNodes();
        assertEquals(1, children.getLength());

        Node logEntry = children.item(0);
        assertEquals(7, logEntry.getChildNodes().getLength());

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(System.out));
    }
}