package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


/*
As a part of the JComparser we need to parse the retrieved XML doc in order to process it for the JEngine Database.
 */
public class Parser {
    private String scenarioXML;

    static de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler;
    private String scenarioName;
    private HashMap<Integer, String> fragments;

    public Parser(String scenarioXML){
        this.scenarioXML = scenarioXML;
    }
    /* parse the XML Files in the list */
  public static void parsePCM(ArrayList<String> ScenarioXML_List) {

        String pcm_item = "";
        try {
            // for each pcm entry
            for(int i=0; i < ScenarioXML_List.size(); i++) {
                pcm_item = ScenarioXML_List.get(i);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(pcm_item.getBytes("utf-8"))));   // http://stackoverflow.com/questions/1706493/java-net-malformedurlexception-no-protocol
               // Document doc = dBuilder.parse(pcm_item);
                doc.getDocumentElement().normalize();
                int scenarioID = fillScenario(doc);
            }
        } catch (ParserConfigurationException e) {
            printErrorMessage(e);
        } catch (SAXException e) {
            printErrorMessage(e);
        } catch (IOException e) {
            printErrorMessage(e);
        }
    }

    /* get modellist and return urls of fragments and scenarios*/
    public static List<String> selectScenarioURLS(String xml_list) {
        List<String> scenario_urls = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml_list.getBytes("utf-8"))));   // http://stackoverflow.com/questions/1706493/java-net-malformedurlexception-no-protocol
            // Document doc = dBuilder.parse(pcm_item);
            doc.getDocumentElement().normalize();

            XPath xPath =  XPathFactory.newInstance().newXPath();

            String xPathQuery = "/models/model[type/text()='net.frapu.code.visualization.pcm.PCMScenario']/uri/text()";

            NodeList scenarioName = (NodeList) xPath.compile(xPathQuery).evaluate(xml_list, XPathConstants.NODESET);

            ArrayList<String> resultList = new ArrayList<>();

            for (int i = 0; i < scenarioName.getLength(); i++) {
            //TODO: richtige URIs zurückgeben anstelle von Stringersetzung
                String scenario = scenarioName.item(i).toString();
              String[] transformScenarioName = scenario.split(":");
                String newScenarioName = "localhost:" + transformScenarioName[transformScenarioName.length-1] + ".pm";
                resultList.add(newScenarioName);
            }

            return resultList;
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }



    private static void fillTables(Document doc) {
      /*  fillScenario(doc);
        filluserTask(doc);
        fillAssociation(doc);
        fillDataObject(doc);
        fillEvent(doc);
        fillFragment(doc);
        fillGateway(doc);
        fillGatewayRule(doc);
        fillProcessElement(doc);
        fillReference(doc);

        fillSequenceflow(doc);
        fillSet(doc);*/
    }

    private static void fillSet(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {
		/* use doc to get the wanted elements & their attributes
		 * then use jHandler to call JDBC functions to put this information into database
		 * return
		 */
        System.out.println("1st");
        return;

    }

    private static void fillSequenceflow(Document doc) {
        System.out.println("2nd");
        return;

    }

    private static int fillScenario(Document scenarioXML) {

        XPath xPath =  XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@name";
        try {
            String scenarioName = xPath.compile(xPathQuery).evaluate(scenarioXML);
            int scenarioId = jHandler.insertScenarioIntoDatabase(scenarioName);
            return scenarioId;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void fillReference(Document doc) {

    }

    private static void fillProcessElement(Document doc) {

    }

    private static void fillGatewayRule(Document doc) {

    }

    private static void fillGateway(Document doc) {
        }

    private static void fillFragment(Document fragmentXML, int scenarioID) {

        XPath xPath =  XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@name";
        try {
            String fragmentName = xPath.compile(xPathQuery).evaluate(fragmentXML);
            System.out.println("FragmentName:" + fragmentName);
            jHandler.insertFragmentIntoDatabase(fragmentName, scenarioID);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }



    }

    private static void fillEvent(Document doc) {

    }

    private static void fillDataObject(Document doc) {

    }

    private static void fillAssociation(Document doc) {

    }

    private static void filluserTask(Document fragmentXML, int fragmentID) {

        XPath xPath =  XPathFactory.newInstance().newXPath();

        String xPathQuery = "/model/nodes/node/property[contains(@value,'net.frapu.code.visualization.bpmn.Task')]/preceding-sibling::property[@name='text']/@value";

        try {
            NodeList nList = (NodeList) xPath.compile(xPathQuery).evaluate(fragmentXML, XPathConstants.NODESET);

            if (nList.getLength() != 0) {
                System.out.println("\nActivities detected");
                for (int i = 0; i < nList.getLength(); i++) {
                    System.out.println("userTask: " + nList.item(i).getNodeValue());
                    //jHandler.insertActivityIntoDatabase(nList.item(i).getNodeValue(), fragmentID);
                }
            }
            else
                System.out.println("No Activities found");

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private static void printErrorMessage(Exception e) {
        if (e.getClass().equals(ParserConfigurationException.class)) {
            System.out.println("no valid xml");
        } else if (e.getClass().equals(SAXException.class)) {
            System.out.println("could not not normalize xml");
        } else if (e.getClass().equals(IOException.class)) {
            System.out.println("no file found");
        } else {
            e.printStackTrace();
        }
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public HashMap<Integer, String> getFragmentDetails() {
        return fragments;
    }

    public ArrayList<String> getControlNodesForFragment(Integer id) {
        return null;
    }
}
