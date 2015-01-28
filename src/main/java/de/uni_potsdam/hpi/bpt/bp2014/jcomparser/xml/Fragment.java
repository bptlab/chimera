package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment implements IDeserialisable, IPersistable {

    private int scenarioID;
    private String fragmentName;
    private org.w3c.dom.Node fragmentXML;
    private int fragmentID;
    private Map<Integer, Node> controlNodes;
    private List<Edge> edges;
    private int databaseID;
    private List<InputSet> inputSets;
    private List<OutputSet> outputSets;

    @Override
    public void initializeInstanceFromXML(org.w3c.dom.Node element) {

        this.fragmentXML = element;
        setFragmentName();
        generateControlNodes();
        generateEdges();
        generateSets();

    }

    private void generateSets() {
        for (Node node : controlNodes.values()) {
            if (!node.isDataNode()) {
                InputSet iSet = InputSet.createInputSetForTaskAndEdges(node, edges);
                if (null != iSet) {
                    inputSets.add(iSet);
                }
                OutputSet oSet = OutputSet.createOutputSetForTaskAndEdges(node, edges);
                if (null != oSet) {
                    outputSets.add(oSet);
                }
            }
        }
    }

    private void generateEdges() {
        try {
            //get all edges from fragmentXML
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String xPathQuery = "/model/edges/edge";
            NodeList edgeNodes = (NodeList) xPath.compile(xPathQuery).evaluate(this.fragmentXML, XPathConstants.NODESET);
            this.edges = new ArrayList<Edge>(edgeNodes.getLength());
            for(int i = 0; i < edgeNodes.getLength(); i++) {
                Edge currentEdge = new Edge();
                currentEdge.initializeInstanceFromXML(edgeNodes.item(i));
                currentEdge.setControlNodes(controlNodes);
                this.edges.add(currentEdge);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private void generateControlNodes() {

        try {
            //get all nodes from fragmentXML
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String xPathQuery = "/model/nodes/node";
            NodeList nodes = (NodeList) xPath.compile(xPathQuery).evaluate(this.fragmentXML, XPathConstants.NODESET);
            this.controlNodes = new HashMap<Integer, Node>(nodes.getLength());

            for(int i = 0; i < nodes.getLength(); i++) {
                Node currentNode = new Node();
                currentNode.initializeInstanceFromXML(nodes.item(i));
                this.controlNodes.put(currentNode.getId(), currentNode);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private void setFragmentName() {

        XPath xPath =  XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@name";
        try {
            this.fragmentName = xPath.compile(xPathQuery).evaluate(this.fragmentXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public void setScenarioID (int id) {
        this.scenarioID = id;
    }

    @Override
    public int writeToDatabase() {
        Connector conn = new Connector();
        this.databaseID = conn.insertFragmentIntoDatabase(this.fragmentName, this.scenarioID);
        for (Node node : controlNodes.values()) {
            node.setFragmentId(databaseID);
            node.writeToDatabase();
        }
        writeSetToDatabase();
        for (Edge edge : edges) {
            edge.writeToDatabase();
        }
        return databaseID;
    }

    private void writeSetToDatabase() {
        for (InputSet set : inputSets) {
            set.writeToDatabase();
        }
        for (OutputSet set : outputSets) {
            set.writeToDatabase();
        }
    }

    public List<Edge> getEdges () {
        return this.edges;
    }

    public int getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(int databaseID) {
        this.databaseID = databaseID;
    }

    public Map<Integer, Node> getControlNodes() {
        return controlNodes;
    }
}
