package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Scenario;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

/**
 * This class is the core class of the JComparser.
 * Every functionality can be used by using this class.
 */
public class JComparser {
    static Logger log = Logger.getLogger(JComparser.class.getName());

    /**
     * A main-method which should be used for Debug only.
     *
     * @param pcmUrl        The URL to a scenario which should be parsed.
     * @param processServer The URI to the Process server.
     * @return returns 0 if run was successful.
     * @throws ParserConfigurationException Exception while parsing.
     * @throws IOException                  The file could not be found (Connection Problems).
     * @throws SAXException                 An XML or query seems to be invalid.
     */
    public int fetchAndParseScenarioFromServer(final String pcmUrl,
                                               final String processServer)
            throws ParserConfigurationException, IOException, SAXException {
        Retrieval jRetrieval = new Retrieval();
        String scenarioXML = jRetrieval.getXMLWithAuth(processServer, pcmUrl);
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(scenarioXML));
        DocumentBuilder db = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder();
        Document doc = db.parse(is);
        Scenario scen = new Scenario(processServer);
        scen.initializeInstanceFromXML(doc.getDocumentElement());
        scen.save();
        return 1;
    }

    /**
     * This method fetches a List of all Scenarios from the Server.
     *
     * @param processeditorServerUrl The URL of the Server
     * @return A map of scenario names and their model ids.
     * @throws XPathExpressionException The XPath Query seems to be invalid.
     */
    public HashMap<String, String> getScenarioNamesAndIDs(
            final String processeditorServerUrl)
            throws XPathExpressionException {
        String modelXML = new Retrieval().getXMLWithAuth(
                processeditorServerUrl,
                processeditorServerUrl + "models");
        Document modelDoc = stringToDocument(modelXML);
        if (modelDoc != null) {
            HashMap<String, String> result = new HashMap<>();
            XPath xPath = XPathFactory.newInstance().newXPath();
            // select all models whose type is scenario
            String xPathQuery = "/models/model[type/text()=" +
                    "'de.uni_potsdam.hpi.bpt.bp2014.jeditor.visualization.pcm.PCMScenario']";
            NodeList models = (NodeList) xPath.compile(xPathQuery)
                    .evaluate(modelDoc, XPathConstants.NODESET);

            for (int i = 0; i < models.getLength(); i++) {
                xPathQuery = "uri/text()";
                String currentURI = (String) xPath
                        .compile(xPathQuery)
                        .evaluate(models.item(i), XPathConstants.STRING);
                // split the URI by "/" --> the last field is the ID of the scenario
                String[] splittedScenarioURI = currentURI.split("/");
                String currentScenarioID =
                        splittedScenarioURI[splittedScenarioURI.length - 1];
                xPathQuery = "name/text()";
                String currentName = xPath
                        .compile(xPathQuery)
                        .evaluate(models.item(i));
                result.put(currentScenarioID, currentName);
            }
            return result;
        }
        return null;
    }

    /**
     * This Method writes all Scenarios to the database.
     *
     * @param processeditorServerUrl The URL of the processeditor.
     * @throws XPathExpressionException The XPath or XML was wrong.
     */
    public void writeAllScenariosToDatabase(
            final String processeditorServerUrl)
            throws XPathExpressionException {

        String modelXML = new Retrieval()
                .getXMLWithAuth(
                        processeditorServerUrl,
                        processeditorServerUrl + "models");
        Document models = stringToDocument(modelXML);
        if (models != null) {
            /* get all (correct) URIs of the scenarios,
             * by reading URI from models-XML and adapting it to server_url
             */
            XPath xPath = XPathFactory.newInstance().newXPath();
            // select all URIS of models whose type is scenario
            String xPathQuery = "/models/model[type/text()=" +
                    "'de.uni_potsdam.hpi.bpt.bp2014.jeditor.visualization.pcm.PCMScenario']/uri/text()";
            NodeList xmlModelURIs = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(models, XPathConstants.NODESET);

            for (int i = 0; i < xmlModelURIs.getLength(); i++) {
                String currentXmlUri = xmlModelURIs.item(i).getTextContent();
                String[] splittedScenarioURI = currentXmlUri.split("/");
                String currentScenarioID =
                        splittedScenarioURI[splittedScenarioURI.length - 1];
                String newScenarioURI = processeditorServerUrl +
                        "models/" + currentScenarioID + ".pm";
                Scenario scenario = new Scenario(processeditorServerUrl);
                String currentScenarioXML = new Retrieval()
                        .getXMLWithAuth(processeditorServerUrl, newScenarioURI);
                scenario.initializeInstanceFromXML(
                        stringToDocument(currentScenarioXML).getFirstChild());
                scenario.save();
            }
        }
    }

    /**
     * Casts a XML from its String Representation to a w3c Document.
     *
     * @param xml The String representation of the XML.
     * @return The from String created Document.
     */
    private Document stringToDocument(final String xml) {
        try {
            DocumentBuilder db = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error("Error:", e);
        }
        return null;
    }
}