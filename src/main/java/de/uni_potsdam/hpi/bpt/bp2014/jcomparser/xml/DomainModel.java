package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
import org.w3c.dom.*;
import org.w3c.dom.Node;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ihdefix on 04.03.2015.
 */
public class DomainModel implements IDeserialisable, IPersistable {
    /**
     * The url of the process Editor.
     */
    private String processeditorServerUrl;
    /**
     * The modelID found in the XML.
     */
    private long domainModelModelID;
    /**
     * The version number of the domainModel
     */
    private int versionNumber;
    /**
     * The databaseID of the corresponding scenario.
     */
    private int scenarioID;
    /**
     * A Mao of modelID's and corresponding dataClasses belonging to this domainModel.
     */
    private Map<Long,DataClass> dataClasses;
    /**
     * A List of all aggregation between the dataClasses belonging to this domainModel.
     */
    private List<Aggregation> aggregations;
    /**
     * The XML representation of a domainModel
     */
    private org.w3c.dom.Node domainModelXML;

    /**
     * The constructor.
     *
     * @param serverURL the severURL wher the XML are to be found.
     */
    public DomainModel(String serverURL) {
        processeditorServerUrl = serverURL;
    }

    /**
     * The constructor.
     */
    public DomainModel(){

    }

    /**
     * This method calls all needed methods to set up the domainModel.
     *
     * @param element The XML Node which will be used for deserialisation
     */
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {
        this.domainModelXML = element;

        setDomainModelModelID();
        generateDataClasses();
        generateAggregations();
        setVersionNumber();
    }

    /**
     * This sets the scenario ID needed for the update.
     *
     * @param id databaseID of the corresponding scenario.
     */
    public void setScenarioID(final int id) {
        this.scenarioID = id;
    }

    /**
     * This method generates a List of aggregates from the XML.
     */
    private void generateAggregations() {
        try {
            //get all edges from fragmentXML
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery = "/model/edges/edge";
            NodeList aggregates = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(this.domainModelXML, XPathConstants.NODESET);
            this.aggregations = new ArrayList<Aggregation>(aggregates.getLength());
            for (int i = 0; i < aggregates.getLength(); i++) {
                Aggregation currentAggregation = new Aggregation();
                currentAggregation.setDataClasses(dataClasses);
                currentAggregation.initializeInstanceFromXML(aggregates.item(i));
                this.aggregations.add(currentAggregation);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method generates a Map of dataClasses with their modelID as keys.
     */
    private void generateDataClasses() {
        try {
            //get all nodes from fragmentXML
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery = "/model/nodes/node";
            NodeList nodes = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(this.domainModelXML, XPathConstants.NODESET);
            this.dataClasses = new HashMap<Long, DataClass>(nodes.getLength());

            for (int i = 0; i < nodes.getLength(); i++) {
                DataClass currentClass = new DataClass();
                currentClass.initializeInstanceFromXML(nodes.item(i));
                this.dataClasses.put(currentClass.getDataClassModelID(), currentClass);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method gets and sets the modelId of the domainModel from the XML.
     */
    private void setDomainModelModelID() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@id";
        try {
            this.domainModelModelID = Long.parseLong(xPath
                    .compile(xPathQuery)
                    .evaluate(this.domainModelXML));
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets the versionNumber of the domainModel.
     */
    private void setVersionNumber() {
        Element versionXML = fetchVersionXML();
        if (versionXML != null) {
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery = "/versions/version";
            try {
                NodeList versions = (NodeList) xPath.compile(xPathQuery).evaluate(versionXML, XPathConstants.NODESET);
                int maxID = 0;
                // We assume that the version that needs to be saved is the newest one
                //TODO: Do we want to save versions that are currently not in the Database?
                for (int i = 0; i < versions.getLength(); i++) {
                    xPathQuery = "@id";
                    int currentID = Integer.parseInt((String) xPath.compile(xPathQuery).evaluate(versions.item(i), XPathConstants.STRING));
                    if (maxID < currentID)
                        maxID = currentID;
                }
                versionNumber = maxID;
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method gets the versionNumber from the given XML.
     *
     * @return The XML Element for the version.
     */
    private Element fetchVersionXML() {
        try {
            Retrieval jRetrieval = new Retrieval();
            String versionXML = jRetrieval.getHTMLwithAuth(
                    processeditorServerUrl,
                    processeditorServerUrl + "models/" + domainModelModelID + "/versions");
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(versionXML));
            DocumentBuilder db = null;
            db = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(is);
            return doc.getDocumentElement();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method saves the domainModel to the database as well as for the dataClasses and aggregations.
     *
     * @return
     */
    @Override
    public int save() {
        Connector conn = new Connector();
        conn.insertDomainModelIntoDatabase(
                this.domainModelModelID,
                this.versionNumber,
                this.scenarioID
        );
        for (DataClass dataClass : dataClasses.values()){
            dataClass.save();
        }
        for (Aggregation aggregation : aggregations) {
            aggregation.save();
        }
        return 1;
    }
    public List<Aggregation> getAggregations() {
        return aggregations;
    }

    public Map<Long, DataClass> getDataClasses() {
        return dataClasses;
    }

    public int getScenarioID() {
        return scenarioID;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public long getDomainModelModelID() {
        return domainModelModelID;
    }
}
