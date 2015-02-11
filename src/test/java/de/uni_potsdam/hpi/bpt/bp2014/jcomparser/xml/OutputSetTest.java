package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OutputSetTest {

    private Document document = new DocumentImpl();
    private List<Element> dataFlows;
    private List<Edge> dataFlowEdges;
    private Node activity;
    private OutputSet outputSet;
    private List<Node> dataNodes;

    // BEGIN: Set-Up
    @Before
    public void setUp() {
        setUpDataFlows();
        setUpEdges();
        setUpNodes();
        setUpOutputSet();
    }

    private void setUpDataFlows() {
        dataFlows = new ArrayList<>();
        Element dataFlow = document.createElement("edge");
        dataFlow.appendChild(createProperty("label", ""));
        dataFlow.appendChild(createProperty("#id", "4"));
        dataFlow.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Association"));
        dataFlow.appendChild(createProperty("#sourceNode", "1"));
        dataFlow.appendChild(createProperty("#targetNode", "2"));
        dataFlows.add(dataFlow);
        dataFlow = document.createElement("edge");
        dataFlow.appendChild(createProperty("label", ""));
        dataFlow.appendChild(createProperty("#id", "5"));
        dataFlow.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Association"));
        dataFlow.appendChild(createProperty("#sourceNode", "1"));
        dataFlow.appendChild(createProperty("#targetNode", "3"));
        dataFlows.add(dataFlow);
    }

    public void setUpEdges() {
        dataFlowEdges = new ArrayList<Edge>();
        for (Element dataFlow : dataFlows) {
            Edge edge = new Edge();
            edge.initializeInstanceFromXML(dataFlow);
            dataFlowEdges.add(edge);
        }
    }

    public void setUpNodes() {
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        dataNodes = new ArrayList<>();

        // activity/ConsumerNode
        activity = new Node();
        activity.setId(1);
        nodes.put(1, activity);

        // dataNodes
        Node datanode = new Node();
        datanode.setId(2);
        nodes.put(2, datanode);
        dataNodes.add(datanode);

        datanode = new Node();
        datanode.setId(3);
        nodes.put(3, datanode);
        dataNodes.add(datanode);

        for (Edge edge : dataFlowEdges) {
            edge.setControlNodes(nodes);
        }
    }

    public void setUpOutputSet() {
        outputSet = OutputSet.createOutputSetForTaskAndEdges(activity, dataFlowEdges);
    }
    // END: Set-Up

    // BEGIN: Tests
    @Test
    public void testOutputSetDeserialization() {
        Assert.assertEquals("The producer-Node has not been set correctly", activity, outputSet.getProducer());
        Assert.assertEquals("The output-Nodes have not been set correctly", dataNodes, outputSet.getOutputs());
        Assert.assertEquals("The associations have not been set correctly", dataFlowEdges, outputSet.getAssociations());
    }

    @Test
    public void testSaveSequenceFlow() {
        outputSet.save();
        Assert.assertTrue("No database-ID set", outputSet.getDatabaseId() != 0);
    }
    // END: Tests

    //BEGIN: Util-Methods
    private Element createProperty(String name, String value) {
        if (null == document) {
            return null;
        }
        Element property = document.createElement("property");
        property.setAttribute("name", name);
        property.setAttribute("value", value);
        return property;
    }
    // END: Util-Methods


}
