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
    private List<OutputSet> outputSets;
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
        HashMap<Long, Node> nodes = new HashMap<Long, Node>();
        dataNodes = new ArrayList<>();

        // activity/ConsumerNode
        activity = new Node();
        activity.setId(1L);
        nodes.put(1L, activity);

        // dataNodes
        Node datanode = new Node();
        datanode.setId(2L);
        datanode.setText("DO1");
        nodes.put(2L, datanode);
        dataNodes.add(datanode);

        datanode = new Node();
        datanode.setId(3L);
        datanode.setText("DO2");
        nodes.put(3L, datanode);
        dataNodes.add(datanode);

        for (Edge edge : dataFlowEdges) {
            edge.setControlNodes(nodes);
        }
    }

    public void setUpOutputSet() {
        outputSets = OutputSet.createOutputSetForTaskAndEdges(activity, dataFlowEdges);
    }
    // END: Set-Up

    // BEGIN: Tests
    @Test
    public void testOutputSetDeserialization() {
        Assert.assertEquals("There is actually just one outputSet", 1, outputSets.size());
        Assert.assertEquals("The producer-Node has not been set correctly", activity, outputSets.get(0).getNode());
        Assert.assertEquals("The output-Nodes have not been set correctly", dataNodes.get(0), outputSets.get(0).getDataObjects().get(1));
        Assert.assertEquals("The output-Nodes have not been set correctly", dataNodes.get(1), outputSets.get(0).getDataObjects().get(0));
        Assert.assertEquals("The associations have not been set correctly", dataFlowEdges.get(0), outputSets.get(0).getAssociations().get(1));
        Assert.assertEquals("The associations have not been set correctly", dataFlowEdges.get(1), outputSets.get(0).getAssociations().get(0));
    }

    @Test
    public void testSaveSequenceFlow() {
        outputSets.get(0).save();
        Assert.assertTrue("No database-ID set", outputSets.get(0).getDatabaseId() != 0);
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
