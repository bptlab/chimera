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

/**
 *
 */
public class InputSetTest  {

    private Document document = new DocumentImpl();
    private List<Element> dataFlows;
    private List<Edge> dataFlowEdges;
    private Node activity;
    private List<InputSet> inputSets;
    private List<Node> dataNodes;


    @Before
    public void setUp() {
        setUpDataFlows();
        setUpEdges();
        setUpNodes();
    }

    private void setUpDataFlows() {
        dataFlows = new ArrayList<>();
        Element dataFlow = document.createElement("edge");
        dataFlow.appendChild(createProperty("label", ""));
        dataFlow.appendChild(createProperty("#id", "4"));
        dataFlow.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Association"));
        dataFlow.appendChild(createProperty("#sourceNode", "2"));
        dataFlow.appendChild(createProperty("#targetNode", "1"));
        dataFlows.add(dataFlow);
        dataFlow = document.createElement("edge");
        dataFlow.appendChild(createProperty("label", ""));
        dataFlow.appendChild(createProperty("#id", "5"));
        dataFlow.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Association"));
        dataFlow.appendChild(createProperty("#sourceNode", "3"));
        dataFlow.appendChild(createProperty("#targetNode", "1"));
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
        nodes.put(2L, datanode);
        dataNodes.add(datanode);

        datanode = new Node();
        datanode.setId(3L);
        nodes.put(3L, datanode);
        dataNodes.add(datanode);

        for (Edge edge : dataFlowEdges) {
            edge.setNodes(nodes);
        }
    }

    public void setUpInputSet() {
        inputSets = InputSet.createInputSetForTaskAndEdges(activity, dataFlowEdges);
    }

    @Test
    public void testOneInputSet() {
        dataNodes.get(0).setText("DO");
        dataNodes.get(1).setText("DO1");
        setUpInputSet();
        Assert.assertEquals("There is actually just one inputSet", 1, inputSets.size());
        Assert.assertEquals("The consumer-Node has not been set correctly", activity, inputSets.get(0).getNode());

        Assert.assertEquals("The input-Nodes have not been set correctly", 2, inputSets.get(0).getDataNodes().size());
        Assert.assertTrue("The input-Nodes have not been set correctly", inputSets.get(0).getDataNodes().contains(dataNodes.get(0)));
        Assert.assertTrue("The input-Nodes have not been set correctly", inputSets.get(0).getDataNodes().contains(dataNodes.get(1)));

        Assert.assertEquals("The associations have not been set correctly", 2, inputSets.get(0).getAssociations().size());
        Assert.assertTrue("The associations have not been set correctly", inputSets.get(0).getAssociations().contains(dataFlowEdges.get(0)));
        Assert.assertTrue("The associations have not been set correctly", inputSets.get(0).getAssociations().contains(dataFlowEdges.get(1)));

        Assert.assertTrue("Something went wrong saving the inputset", inputSets.get(0).save() > 0);
    }

    @Test
    public void testTwoInputSets() {
        dataNodes.get(0).setText("DO");
        dataNodes.get(1).setText("DO");
        setUpInputSet();
        Assert.assertEquals("There should be two inputSets", 2, inputSets.size());

        Assert.assertEquals("The consumer-Node has not been set correctly", activity, inputSets.get(0).getNode());
        Assert.assertEquals("The consumer-Node has not been set correctly", activity, inputSets.get(1).getNode());

        Assert.assertEquals("The input-Nodes have not been set correctly", 1, inputSets.get(0).getDataNodes().size());
        Assert.assertEquals("The input-Nodes have not been set correctly", 1, inputSets.get(1).getDataNodes().size());
        if (dataNodes.get(0) == inputSets.get(0).getDataNodes().get(0))
            Assert.assertEquals("The input-Nodes have not been set correctly", dataNodes.get(1), inputSets.get(1).getDataNodes().get(0));
        else if (dataNodes.get(0) == inputSets.get(1).getDataNodes().get(0))
            Assert.assertEquals("The input-Nodes have not been set correctly", dataNodes.get(1), inputSets.get(0).getDataNodes().get(0));
        else
            Assert.fail("The input-Nodes have not been set correctly");

        Assert.assertEquals("The associations have not been set correctly", 1, inputSets.get(0).getAssociations().size());
        Assert.assertEquals("The associations have not been set correctly", 1, inputSets.get(1).getAssociations().size());
        if ( dataFlowEdges.get(0) == inputSets.get(0).getAssociations().get(0))
            Assert.assertEquals("The associations have not been set correctly", dataFlowEdges.get(1), inputSets.get(1).getAssociations().get(0));
        else if (dataFlowEdges.get(0) == inputSets.get(1).getAssociations().get(0))
            Assert.assertEquals("The associations have not been set correctly", dataFlowEdges.get(1), inputSets.get(0).getAssociations().get(0));
        else
            Assert.fail("The associations have not been set correctly");

        Assert.assertTrue("Something went wrong saving the inputset", inputSets.get(0).save() > 0);
        Assert.assertTrue("Something went wrong saving the inputset", inputSets.get(1).save() > 0);
    }

    private Element createProperty(String name, String value) {
        if (null == document) {
            return null;
        }
        Element property = document.createElement("property");
        property.setAttribute("name", name);
        property.setAttribute("value", value);
        return property;
    }
}