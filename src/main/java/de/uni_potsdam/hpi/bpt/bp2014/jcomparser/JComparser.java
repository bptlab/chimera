package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Node;
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
import java.io.*;
import java.util.*;
import java.lang.String;


/***********************************************************************************
*   
*   _________ _______  _        _______ _________ _        _______ 
*   \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
*      )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
*      |  |  | (__    |   \ | || |         | |   |   \ | || (__    
*      |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)   
*      |  |  | (      | | \   || | \_  )   | |   | | \   || (      
*   |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
*   (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
*
*******************************************************************
*
*   Copyright © All Rights Reserved 2014 - 2015
*
*   Please be aware of the License. You may found it in the root directory.
*
************************************************************************************/


public class JComparser {

    public static void main(String[] args) {
        //start JComparser
    }

    public HashMap<String, String> getScenarioNamesAndIDs(String processeditor_server_url) throws XPathExpressionException {
        String modelXML = new Retrieval().getHTMLwithAuth(processeditor_server_url, processeditor_server_url + "models");
        Document modelDoc = stringToDocument(modelXML);
        if(modelDoc != null) {
            HashMap<String, String> result = new HashMap<>();
            XPath xPath =  XPathFactory.newInstance().newXPath();
            // select all models whose type is scenario
            String xPathQuery = "/models/model[type/text()='net.frapu.code.visualization.pcm.PCMScenario']";
            NodeList models = (NodeList) xPath.compile(xPathQuery).evaluate(modelDoc, XPathConstants.NODESET);

            for (int i = 0; i < models.getLength(); i++) {
                xPathQuery = "uri/text()";
                String currentURI = (String) xPath.compile(xPathQuery).evaluate(models.item(i), XPathConstants.STRING);
                // split the URI by "/" --> the last field is the ID of the scenario
                String[] splittedScenarioURI = currentURI.split("/");
                String currentScenarioID = splittedScenarioURI[splittedScenarioURI.length-1];
                xPathQuery = "name/text()";
                String currentName = xPath.compile(xPathQuery).evaluate(models.item(i));
                result.put(currentScenarioID, currentName);
            }
            return result;
        }
        return null;
    }

    public static void writeAllScenariosToDatabase (String processeditor_server_url) throws XPathExpressionException {

        String modelXML = new Retrieval().getHTMLwithAuth(processeditor_server_url, processeditor_server_url + "models");
        Document models = stringToDocument(modelXML);
        if(models != null) {
            // get all (correct) URIs of the scenarios by reading URI from models-XML and adapting it to server_url
            XPath xPath =  XPathFactory.newInstance().newXPath();
            // select all URIS of models whose type is scenario
            String xPathQuery = "/models/model[type/text()='net.frapu.code.visualization.pcm.PCMScenario']/uri/text()";
            NodeList xmlModelURIs = (NodeList) xPath.compile(xPathQuery).evaluate(models, XPathConstants.NODESET);

            for (int i = 0; i < xmlModelURIs.getLength(); i++) {
                //TODO: avoid string-replacement
                String currentXmlUri = xmlModelURIs.item(i).getTextContent();
                String[] splittedScenarioURI = currentXmlUri.split("/");
                String currentScenarioID = splittedScenarioURI[splittedScenarioURI.length-1];
                String newScenarioURI = processeditor_server_url + "models/" + currentScenarioID + ".pm";
                Scenario scenario = new Scenario();
                String currentScenarioXML = new Retrieval().getHTMLwithAuth(processeditor_server_url, newScenarioURI);
                scenario.initializeInstanceFromXML(stringToDocument(currentScenarioXML).getFirstChild());
                scenario.save();
            }
        }
    }

    private static Document stringToDocument (String xml) {

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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

        public static void handleFileUpload(List pcm) {
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

    public static void handleFileRetrieval(String pcm) {

        ArrayList<String> pcm_list = new ArrayList<String>();
        pcm_list.add(pcm);

        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parsePCM(pcm_list);
    }

}
