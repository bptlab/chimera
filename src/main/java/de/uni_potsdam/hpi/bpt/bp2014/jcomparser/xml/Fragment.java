package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.*;

/**
 * Represents a Fragment of the XML-Model.
 * It implements the IDeserialisabel interface,
 * which allows to initialize a fragment Object
 * from an XML and the IPersistable Interface,
 * which allows to save the Object to the Database.
 */
public class Fragment implements IDeserialisable, IPersistable {

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
     * The Model-XML-Id of the Fragment.
     */
    private String fragmentID;
    /**
     * A Map which maps Model-XML-Element-IDs to ControlNodes.
     */
    private Map<Integer, Node> controlNodes;
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

    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {

        this.fragmentXML = element;
        setFragmentName();
        setFragmentID();
        generateControlNodes();
        generateEdges();
        generateSets();

    }

    /**
     * This method extracts the id from the Model-XML.
     * It saves the id inside the fragmentID field.
     */
    private void setFragmentID() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@id";
        try {
            this.fragmentID = xPath
                    .compile(xPathQuery)
                    .evaluate(this.fragmentXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /**
     * generates Sets (I/O) for all Activities,
     * which are part of DataFlow inside the Fragment.
     * Assert that first all ControlNodes have to be initialized.
     */
    private void generateSets() {
        inputSets = new LinkedList<InputSet>();
        outputSets = new LinkedList<OutputSet>();
        for (Node node : controlNodes.values()) {
//            boolean isInput = false;
//            boolean isOutput = false;
//            for (Edge edge : edges) {
//                if (edge.getSource() == node && edge.getTarget().isDataNode()) {
//                    isOutput = true;
//                } else if (edge.getTarget() == node && edge.getSource().isDataNode()) {
//                    isInput = true;
//                } else {
//                    break;
//                }
//            }
            if (node.isTask()) {
                InputSet iSet = InputSet.createInputSetForTaskAndEdges(node,
                        edges);
                if (null != iSet) {
                    inputSets.add(iSet);
                }
                OutputSet oSet = OutputSet.createOutputSetForTaskAndEdges(node,
                        edges);
                if (null != oSet) {
                    outputSets.add(oSet);
                }
            }
        }
    }

    /**
     * Checks if a specific node has an output set or not.
     *
     * @param node The node which will be checked
     * @return true if an output set exists else false.
     */
    private boolean nodeHasOutputSet(final Node node) {
        for (Edge edge : edges) {
            if (edge.getSource() == node && edge.getTarget().isDataNode()) {
                return true;
            }
        }
        return false;
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
                currentEdge.setControlNodes(controlNodes);
                this.edges.add(currentEdge);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts all Nodes from the XML and creates Node objects.
     */
    private void generateControlNodes() {

        try {
            //get all nodes from fragmentXML
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery = "/model/nodes/node";
            NodeList nodes = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(this.fragmentXML, XPathConstants.NODESET);
            this.controlNodes = new HashMap<Integer, Node>(nodes.getLength());

            for (int i = 0; i < nodes.getLength(); i++) {
                Node currentNode = new Node();
                currentNode.initializeInstanceFromXML(nodes.item(i));
                this.controlNodes.put(currentNode.getId(), currentNode);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    /**
     * Sets the scenario Id.
     * The scenario Id should be the primary key of the scenario
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
                this.scenarioID);
        for (Node node : controlNodes.values()) {
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
    public Map<Integer, Node> getControlNodes() {
        return controlNodes;
    }

    /**
     * The list of inputSets. Changes will alter the state of the fragment.
     *
     * @return List of InputSets
     */
    public List<InputSet> getInputSets() {
        return inputSets;
    }

    /**
     * The list of OutputSets. Changes will affect the state of the fragment.
     *
     * @return List of Output Sets
     */
    public List<OutputSet> getOutputSets() {
        return outputSets;
    }
}
