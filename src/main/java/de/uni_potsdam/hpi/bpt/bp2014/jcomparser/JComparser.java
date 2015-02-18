package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Scenario;
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
import java.util.List;


/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

/**
 * This class is the core class of the JComparser.
 * Every functionality can be used by using this class.
 */
public class JComparser {

    /**
     * A main-method which should be used for Debug only.
     * @param pcmUrl The URL to a scenario which should be parsed.
     * @param processServer The URI to the Process server.
     * @return returns 0 if run was successful.
     * @throws ParserConfigurationException Exception while parsing.
     * @throws IOException The file could not be found (Connection Problems).
     * @throws SAXException An XML or query seems to be invalid.
     */
    public int fetchAndParseScenarioFromServer(final String pcmUrl,
                                                  final String processServer)
            throws ParserConfigurationException, IOException, SAXException {
        Retrieval jRetrieval = new Retrieval();
        String scenarioXML = jRetrieval.getHTMLwithAuth(processServer, pcmUrl);
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
     * @param processeditorServerUrl The URL of the Server
     * @return A map of scenario names and their model ids.
     * @throws XPathExpressionException The XPath Query seems to be invalid.
     */
    public HashMap<String, String> getScenarioNamesAndIDs(
            final String processeditorServerUrl)
            throws XPathExpressionException {
        String modelXML = new Retrieval().getHTMLwithAuth(
                processeditorServerUrl,
                processeditorServerUrl + "models");
        Document modelDoc = stringToDocument(modelXML);
        if (modelDoc != null) {
            HashMap<String, String> result = new HashMap<>();
            XPath xPath = XPathFactory.newInstance().newXPath();
            // select all models whose type is scenario
            String xPathQuery = "/models/model[type/text()=" +
                    "'net.frapu.code.visualization.pcm.PCMScenario']";
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
     * @param processeditorServerUrl The URL of the processeditor.
     * @throws XPathExpressionException The XPath or XML was wrong.
     */
    public void writeAllScenariosToDatabase(
            final String processeditorServerUrl)
            throws XPathExpressionException {

        String modelXML = new Retrieval()
                .getHTMLwithAuth(
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
                    "'net.frapu.code.visualization.pcm.PCMScenario']/uri/text()";
            NodeList xmlModelURIs = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(models, XPathConstants.NODESET);

            for (int i = 0; i < xmlModelURIs.getLength(); i++) {
                //TODO: avoid string-replacement
                String currentXmlUri = xmlModelURIs.item(i).getTextContent();
                String[] splittedScenarioURI = currentXmlUri.split("/");
                String currentScenarioID =
                        splittedScenarioURI[splittedScenarioURI.length - 1];
                String newScenarioURI = processeditorServerUrl +
                        "models/" + currentScenarioID + ".pm";
                Scenario scenario = new Scenario(processeditorServerUrl);
                String currentScenarioXML = new Retrieval()
                        .getHTMLwithAuth(processeditorServerUrl, newScenarioURI);
                scenario.initializeInstanceFromXML(
                        stringToDocument(currentScenarioXML).getFirstChild());
                scenario.save();
            }
        }
    }

    /**
     * Casts a XML from its String Representation to a w3c Document.
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
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void handleFileUpload(List pcm) {
/*
        int pcm_size = pcm.size();
        String pcm_item = "";

        List<String> pcm_list = new ArrayList<String>();
        Object xml_path_url = pcm.get(1);

        for(int i=0; i < pcm_size; i++) {
            pcm_item = (String) pcm.get(i);
            xml_path_url = pcm_item;
            InputStream xml_content;

            try {
                xml_content = new FileInputStream(xml_path_url);
                pcm_list.add(xml_content);
            } catch (IOException e) {
                System.out.println("Error in Reading file ");
                System.out.println(xml_path_url);
            }
        }

        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parsePCM(pcm);
*/
    }

    public void handleFileRetrieval(String pcm) {
/*
        ArrayList<String> pcm_list = new ArrayList<String>();
        pcm_list.add(pcm);

        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parsePCM(pcm_list);
*/
    }
}
