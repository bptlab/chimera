package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class Ausprobieren {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        Retrieval jRetrieval = new Retrieval();
        String scenarioXML = jRetrieval.getHTMLwithAuth("http://localhost:1205/", "http://localhost:1205/models/180566079.pm");

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(scenarioXML));
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(is);
        Scenario scen = new Scenario();
        scen.initializeInstanceFromXML(doc.getDocumentElement());
        scen.writeToDatabase();
    }
}
