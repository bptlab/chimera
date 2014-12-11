package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/*
As a part of the JComparser we need to parse the retrieved XML doc in order to process it for the JEngine Database.
 */
public class Parser {

    /* parse the XML File */
    public static void parsePCM(List pcm) {

        Connector jHandler = new Connector();
        int pcm_size = pcm.size();
        String pcm_item = "";
        try {
            // for each pcm entry
            for(int i=0; i < pcm_size; i++) {
                pcm_item = (String) pcm.get(i);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(pcm_item.getBytes("utf-8"))));   // http://stackoverflow.com/questions/1706493/java-net-malformedurlexception-no-protocol
               // Document doc = dBuilder.parse(pcm_item);
                doc.getDocumentElement().normalize();
                System.out.println("Root Element:"
                        + doc.getDocumentElement().getNodeName());
                fillTables(doc, jHandler);
            }

        } catch (ParserConfigurationException e) {
            printErrorMessage(e);
        } catch (SAXException e) {
            printErrorMessage(e);
        } catch (IOException e) {
            printErrorMessage(e);
        }
    }
    private static void fillTables(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {
        filluserTask(doc, jHandler);
        fillAssociation(doc, jHandler);
        fillDataObject(doc, jHandler);
        fillEvent(doc, jHandler);
        fillFragment(doc, jHandler);
        fillGateway(doc, jHandler);
        fillGatewayRule(doc, jHandler);
        fillProcessElement(doc, jHandler);
        fillReference(doc, jHandler);
        fillScenario(doc, jHandler);
        fillSequenceflow(doc, jHandler);
        fillSet(doc, jHandler);
    }

    private static void fillSet(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {
		/* use doc to get the wanted elements & their attributes
		 * then use jHandler to call JDBC functions to put this information into database
		 * return
		 */
        System.out.println("1st");
        return;

    }

    private static void fillSequenceflow(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {
        System.out.println("2nd");
        return;

    }

    private static void fillScenario(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {
        NodeList nList = doc.getElementsByTagName("termination");
        if (nList.getLength() != 0)
            System.out.println("\nScenarios detected");
        else
            System.out.println("No Scenarios found");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nTer = nList.item(i);
            Element eTerElement = (Element) nTer;
            NodeList nListCondition = eTerElement.getElementsByTagName("dataObject");
            for (int j = 0; j < nListCondition.getLength(); j++) {
                Node nCondition = nListCondition.item(j);
                Element eConElement = (Element) nCondition;
                // String name =
                // eElement.getElementsByTagName("name").item(0).getTextContent();
                String ter = eConElement.getAttribute("name")
                        + " muss im Zustand " + eConElement.getAttribute("state")+ " sein.";
                // System.out.println(eElement.getElementsByTagName("name").item(0).getTextContent());
                System.out.println(ter);
                jHandler.insertScenarioIntoDatabase("name", ter);
            }
        }
    }

    private static void fillReference(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {

    }

    private static void fillProcessElement(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {

    }

    private static void fillGatewayRule(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {

    }

    private static void fillGateway(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {
        NodeList nList = doc.getElementsByTagName("Gateway");
        if(nList.getLength() != 0) System.out.println("Gateways detected");
        else System.out.println("No Gateways found");
        for(int i = 0; i < nList.getLength(); i++){
            Node nNode = nList.item(i);
            System.out.println("\nGateway: " + nNode.getNodeName());
            Element eElement = (Element) nNode;
            System.out.println(eElement.getTextContent());
            jHandler.insertGatewayIntoDatabase(eElement.getTextContent());
        }
    }

    private static void fillFragment(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {

    }

    private static void fillEvent(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {
        NodeList nListA = doc.getElementsByTagName("startEvent");
        NodeList nListE = doc.getElementsByTagName("endEvent");
        if (nListA.getLength() != 0 || nListE.getLength() != 0)
            System.out.println("\nEvents detected");
        else
            System.out.println("No Events found");
        for (int i = 0; i < nListA.getLength(); i++) {
            Node nNode = nListA.item(i);
            System.out.println("Event: " + nNode.getNodeName());
            Element eElement = (Element) nNode;
            System.out.println(eElement.getAttribute("name"));
            jHandler.insertEventIntoDatabase("Start");
        }
        for (int i = 0; i < nListE.getLength(); i++) {
            Node nNode = nListE.item(i);
            System.out.println("Event: " + nNode.getNodeName());
            Element eElement = (Element) nNode;
            System.out.println(eElement.getAttribute("name"));
            jHandler.insertEventIntoDatabase("End");
        }
    }

    private static void fillDataObject(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {

    }

    private static void fillAssociation(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {

    }

    private static void filluserTask(Document doc, de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector jHandler) {
        NodeList nList = doc.getElementsByTagName("userTask");
        if (nList.getLength() != 0)
            System.out.println("\nActivities detected");
        else
            System.out.println("No Activities found");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("userTask: " + nNode.getNodeName());
            Element eElement = (Element) nNode;
            System.out.println(eElement.getAttribute("name"));
            jHandler.insertActivityIntoDatabase(eElement.getAttribute("name"));
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
}
