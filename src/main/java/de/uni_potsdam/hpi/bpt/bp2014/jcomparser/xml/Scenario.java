package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scenario implements IDeserialisable, IPersistable {

    private String scenarioName;
    private int scenarioID;
    private org.w3c.dom.Node scenarioXML;
    private List<Fragment> fragments;
    private String processeditor_server_url = "http://172.16.64.113:1205/";
    private int databaseID;
    private Map<String, DataObject> dataObjects = new HashMap<String, DataObject>();

    @Override
    public void initializeInstanceFromXML(org.w3c.dom.Node element) {

        this.scenarioXML = element;
        setScenarioName();
        //generateFragmentList();
        createDataObjects();
    }

    public int writeToDatabase() {
        Connector conn = new Connector();
        this.databaseID = conn.insertScenarioIntoDatabase(this.scenarioName);
        writeDataObjectsToDatabase();
        return this.databaseID;
    }

    private void writeDataObjectsToDatabase() {
        for (DataObject dataObject : dataObjects.values()) {
            dataObject.writeToDatabase();
        }
    }

    private void createDataObjects() {
        for (Fragment fragment : fragments) {
            for (Node node : fragment.getControlNodes().values()) {
                if (node.isDataNode()) {
                    if (null == dataObjects.get(node.getText())) {
                        dataObjects.put(node.getText(), new DataObject());
                    }
                    dataObjects.get(node.getText()).addDataNode(node);
                }
            }
        }
    }


    private void generateFragmentList() {

        try {
            //look for all fragments in the scenarioXML and save their IDs
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String xPathQuery = "/model/nodes/node/property[@name = '#type' and @value = 'net.frapu.code.visualization.pcm.PCMFragmentNode']/preceding-sibling::property[@name='fragment mid']/@value";
            NodeList fragmentIDsList = (NodeList) xPath.compile(xPathQuery).evaluate(this.scenarioXML, XPathConstants.NODESET);

            // create URI from fragmentID and retrieve xml for all fragments of the scenario
            Retrieval jRetrieval = new Retrieval();
            this.fragments = new ArrayList<Fragment>(fragmentIDsList.getLength());
            String currentFragmentXML;
            DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder;
            Document doc;

            for (int i = 0; i  < fragmentIDsList.getLength(); i++) {
                currentFragmentXML = jRetrieval.getHTMLwithAuth(this.processeditor_server_url, this.processeditor_server_url + "models/" + fragmentIDsList.item(i).getNodeValue() + ".pm");
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(currentFragmentXML.getBytes("utf-8"))));
                doc.getDocumentElement().normalize();
                Fragment fragment = new Fragment();
                fragment.setScenarioID(this.scenarioID);
                fragment.initializeInstanceFromXML((org.w3c.dom.Node)doc.getDocumentElement());
                this.fragments.add(fragment);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setScenarioName() {

        XPath xPath =  XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@name";
        try {
            this.scenarioName = xPath.compile(xPathQuery).evaluate(this.scenarioXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public String getScenarioName() {
        return this.scenarioName;
    }

    public int getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(int databaseID) {
        this.databaseID = databaseID;
    }

    public void setScenarioID(int scenarioID) {
        this.scenarioID = scenarioID;
    }
}
