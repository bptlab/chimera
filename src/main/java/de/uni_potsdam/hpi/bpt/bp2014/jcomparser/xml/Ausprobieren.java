package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * This class is only for debugging.
 */
public class Ausprobieren {

    /**
     * This main Methods catches a scenario from the server and parses it.
     * You may have to change the id of the model.
     *
     * @param args the command line arguments (will be ignored)
     * @throws ParserConfigurationException The XML seems to be invalid
     * @throws IOException                  The server returns an invalid file.
     * @throws SAXException                 The XML seems to be invalid.
     */
    public static void main(String[] args) throws
            ParserConfigurationException,
            IOException,
            SAXException {

        Retrieval jRetrieval = new Retrieval();
        String scenarioXML = jRetrieval.getXMLWithAuth(
                "http://bp2014w1-dev:1205/",
                "http://bp2014w1-dev:1205/models/1225104276.pm");

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(scenarioXML));
        DocumentBuilder db = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder();
        Document doc = db.parse(is);
        Scenario scen = new Scenario("http://bp2014w1-dev:1205/");
        scen.initializeInstanceFromXML(doc.getDocumentElement());
        scen.save();
    }
}
