package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.Node;
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
    private Node fragmentXML;
    private int fragmentID;
    private Map<Integer, ControlNode> controlNodes;
    private List<Edge> edges;
    private int databaseID;

    @Override
    public void initializeInstanceFromXML(Node element) {

        this.fragmentXML = element;
        setFragmentName();
        generateControlNodes();
        generateEdges();

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
            this.controlNodes = new HashMap<Integer, ControlNode>(nodes.getLength());

            for(int i = 0; i < nodes.getLength(); i++) {
                ControlNode currentControlNode = new ControlNode();
                currentControlNode.initializeInstanceFromXML(nodes.item(i));
                this.controlNodes.put(currentControlNode.getId(), currentControlNode);
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

    public int writeToDatabase() {
        Connector conn = new Connector();
        this.databaseID = conn.insertFragmentIntoDatabase(this.fragmentName, this.scenarioID);
        for (ControlNode node : controlNodes.values()) {
            node.setFragmentId(databaseID);
            node.writeToDatabase();
        }
        return databaseID;
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

    public Map<Integer, ControlNode> getControlNodes() {
        return controlNodes;
    }
}
