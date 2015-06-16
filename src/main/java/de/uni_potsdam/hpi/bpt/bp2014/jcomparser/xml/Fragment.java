package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.util.*;

/**
 * Represents a Fragment of the XML-Model.
 * It implements the IDeserialisable interface,
 * which allows to initialize a fragment Object
 * from an XML and the IPersistable Interface,
 * which allows to save the Object to the Database.
 */
public class Fragment implements IDeserialisable, IPersistable {
    static Logger log = Logger.getLogger(Fragment.class.getName());
    /**
     * The url of the process Editor.
     */
    private String processeditorServerUrl;
    /**
     * The databaseID of the scenario.
     */
    private int scenarioID = -1;
    /**
     * The name of the Fragment.
     */
    private String fragmentName;
    /**
     * The XML of this fragment Model.
     */
    private org.w3c.dom.Node fragmentXML;
    /**
     * The Model-XML-ID of the Fragment.
     */
    private long fragmentID;
    /**
     * A Map which maps Model-XML-Element-IDs to nodes (might be either controlNodes or dataNodes).
     */
    private Map<Long, Node> nodes;
    /**
     * The List of Edges created from the FragmentXML.
     */
    private List<Edge> edges;
    /**
     * The database ID of the fragment.
     */
    private int databaseID;
    /**
     * A list of all Inputs sets,
     * which are used by any Activities inside this Fragment.
     */
    private List<InputSet> inputSets;
    /**
     * A List of all Outputs sets,
     * which are used by any Activities inside this Fragment.
     */
    private List<OutputSet> outputSets;
    /**
     * The version of the current Scenario.
     */
    private int versionNumber;

    /**
     * Sets the processeditorServerUrl which is needed for connecting to the server
     * in order to get the XML-files for the fragments.
     *
     * @param serverURL URL of the processEditorServer
     */
    public Fragment(String serverURL) {
        processeditorServerUrl = serverURL;
    }

    /**
     * This constructor is only used for testcases as a connection to the server is not needed therefore.
     */
    public Fragment() {
    }

    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {

        this.fragmentXML = element;
        setFragmentName();
        setFragmentID();
        generateNodes();
        generateEdges();
        generateSets();
        setVersionNumber();
    }

    /**
     * Extracts the Version from the XML.
     * Corresponding values will be saved to the corresponding fields.
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
                for (int i = 0; i < versions.getLength(); i++) {
                    xPathQuery = "@id";
                    int currentID = Integer.parseInt((String) xPath.compile(xPathQuery).evaluate(versions.item(i), XPathConstants.STRING));
                    if (maxID < currentID) {
                        maxID = currentID;
                    }
                }
                versionNumber = maxID;
            } catch (XPathExpressionException e) {
                log.error("Error:", e);
            }
        }
    }

    /**
     * Get the XML which contains all the versions of the current scenario from the processEditorServer.
     *
     * @return XML containing versions
     */
    private Element fetchVersionXML() {
        try {
            Retrieval jRetrieval = new Retrieval();
            String versionXML = jRetrieval.getXMLWithAuth(
                    processeditorServerUrl,
                    processeditorServerUrl + "models/" + fragmentID + "/versions");
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(versionXML));
            DocumentBuilder db = null;
            db = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(is);
            return doc.getDocumentElement();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("Error:", e);
        }
        return null;
    }

    /**
     * This method extracts the id from the Model-XML.
     * It saves the id inside the fragmentID field.
     */
    private void setFragmentID() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@id";
        try {
            this.fragmentID = Long.parseLong(xPath
                    .compile(xPathQuery)
                    .evaluate(this.fragmentXML));
        } catch (XPathExpressionException e) {
            log.error("Error:", e);
        }
    }

    /**
     * generates Sets (I/O) for all Activities,
     * which are part of DataFlow inside the Fragment.
     * Assert that first all Control- and DataNodes have been initialized.
     */
    private void generateSets() {
        inputSets = new LinkedList<InputSet>();
        outputSets = new LinkedList<OutputSet>();
        for (Node node : nodes.values()) {
            if (node.isTask()) {
                List<InputSet> iSets = InputSet.createInputSetForTaskAndEdges(node,
                        edges);
                if (null != iSets) {
                    for (InputSet iSet : iSets) {
                        inputSets.add(iSet);
                    }
                }
                List<OutputSet> oSets = OutputSet.createOutputSetForTaskAndEdges(node,
                        edges);
                if (null != oSets) {
                    for (OutputSet oSet : oSets) {
                        outputSets.add(oSet);
                    }
                }
            }
        }
    }

    /**
     * Extracts all Edges from the XML and creates Edge objects.
     */
    private void generateEdges() {
        try {
            //get all edges from fragmentXML
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery = "/model/edges/edge";
            NodeList edgeNodes = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(this.fragmentXML, XPathConstants.NODESET);
            this.edges = new ArrayList<Edge>(edgeNodes.getLength());
            for (int i = 0; i < edgeNodes.getLength(); i++) {
                Edge currentEdge = new Edge();
                currentEdge.initializeInstanceFromXML(edgeNodes.item(i));
                currentEdge.setNodes(nodes);
                this.edges.add(currentEdge);
            }
        } catch (XPathExpressionException e) {
            log.error("Error:", e);
        }
    }

    /**
     * Extracts all Nodes from the XML and creates Node objects.
     */
    private void generateNodes() {

        try {
            //get all nodes from fragmentXML
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery = "/model/nodes/node";
            NodeList xmlNodes = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(this.fragmentXML, XPathConstants.NODESET);
            this.nodes = new HashMap<Long, Node>(xmlNodes.getLength());

            for (int i = 0; i < xmlNodes.getLength(); i++) {
                Node currentNode = new Node();
                currentNode.initializeInstanceFromXML(xmlNodes.item(i));
                this.nodes.put(currentNode.getId(), currentNode);
            }
        } catch (XPathExpressionException e) {
            log.error("Error:", e);
        }
    }

    /**
     * This method extracts the name from the Model-XML.
     * The name will be saved inside the fragmentName field
     */
    private void setFragmentName() {

        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@name";
        try {
            this.fragmentName = xPath
                    .compile(xPathQuery)
                    .evaluate(this.fragmentXML);
        } catch (XPathExpressionException e) {
            log.error("Error:", e);
        }
    }

    /**
     * Sets the scenario Id.
     * The scenario Id should be the primary key of the scenario
     *
     * @param id the primary key of the scenario
     */
    public void setScenarioID(final int id) {
        this.scenarioID = id;
    }

    @Override
    public int save() {
        Connector conn = new Connector();
        this.databaseID = conn.insertFragmentIntoDatabase(
                this.fragmentName,
                this.scenarioID,
                this.fragmentID,
                this.versionNumber);
        for (Node node : nodes.values()) {
            node.setFragmentId(databaseID);
            node.save();
        }
        saveSet();
        for (Edge edge : edges) {
            edge.save();
        }
        return databaseID;
    }

    /**
     * Saves the input and output sets to the database.
     */
    private void saveSet() {
        for (InputSet set : inputSets) {
            set.save();
        }
        for (OutputSet set : outputSets) {
            set.save();
        }
    }

    /**
     * Migrate fragmentinstances.
     *
     * @param scenarioDbID databaseId of the old scenario whose instances get migrated
     */
    public void migrate(int scenarioDbID) {
        Connector connector = new Connector();
        int oldFragmentID = connector.getFragmentID(scenarioDbID, fragmentID);
        connector.migrateFragmentInstance(oldFragmentID, databaseID);
        // migrate controlNodes
        for (Map.Entry<Long, Node> node : nodes.entrySet()) {
            if (node.getValue().isTask()) {
                node.getValue().migrate(oldFragmentID);
            }
        }
    }

    /**
     * Returns the list of edges.
     * This is a Composition, if you change the list
     * you will change the state of the Fragment.
     *
     * @return The List of Edges
     */
    public List<Edge> getEdges() {
        return this.edges;
    }

    /**
     * Returns a Map of Node-Ids (from XML) and their nodes.
     * Any changes will manipulate the state of the Fragment.
     *
     * @return Map<XML_ID, ControlNode>
     */
    public Map<Long, Node> getControlNodes() {
        return nodes;
    }

    /**
     * The list of InputSets. Changes will alter the state of the fragment.
     * Used only for test cases.
     *
     * @return List of InputSets
     */
    public List<InputSet> getInputSets() {
        return inputSets;
    }

    /**
     * The list of OutputSets. Changes will affect the state of the fragment.
     * Used only for test cases.
     *
     * @return List of Output Sets
     */
    public List<OutputSet> getOutputSets() {
        return outputSets;
    }

    /**
     * Get a list of all Input- and OutputSets.
     *
     * @return List of Sets
     */
    public List<Set> getSets() {
        List<Set> sets = new LinkedList<>();
        sets.addAll(outputSets);
        sets.addAll(inputSets);
        return sets;
    }

    /**
     * Returns the name of the fragment.
     *
     * @return fragmentName
     */
    public String getFragmentName() {
        return fragmentName;
    }

    /**
     * Returns the Model-XML-ID of the Fragment.
     *
     * @return fragmentID
     */
    public long getFragmentID() {
        return fragmentID;
    }

    /**
     * Returns the Database-ID of the Fragment which is available after saving the fragment.
     *
     * @return DatabaseID
     */
    public int getDatabaseID() {
        return databaseID;
    }

    /**
     * Returns the model-version of the Fragment.
     *
     * @return versionNumber
     */
    public int getVersion() {
        return versionNumber;
    }
}
