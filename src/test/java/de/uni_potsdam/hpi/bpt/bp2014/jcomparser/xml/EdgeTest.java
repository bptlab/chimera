package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;

public class EdgeTest {
    private Document document = new DocumentImpl();
    private Element dataFlow;
    private Element controlFlow;

    // BEGIN: Set-Up
    @Before
    public void setUpDataFlow() {
        dataFlow = document.createElement("edge");
        dataFlow.appendChild(createProperty("label", ""));
        dataFlow.appendChild(createProperty("#id", "1536668063"));
        dataFlow.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.DataFlow"));
        dataFlow.appendChild(createProperty("#sourceNode", "88478777"));
        dataFlow.appendChild(createProperty("#targetNode", "1628237226"));
    }

    @Before
    public void setUpControlFlow() {
        controlFlow = document.createElement("edge");
        controlFlow.appendChild(createProperty("label", ""));
        controlFlow.appendChild(createProperty("#id", "1536668063"));
        controlFlow.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.SequenceFlow"));
        controlFlow.appendChild(createProperty("#sourceNode", "88478777"));
        controlFlow.appendChild(createProperty("#targetNode", "1628237226"));
    }
    // END: Set-Up

    // BEGIN: Tests
    @Test
    public void testDataFlowDeserialization() {
        Edge edge = new Edge();
        edge.initializeInstanceFromXML((org.w3c.dom.Node) dataFlow);
        Assert.assertEquals("The ID has not been set correctly", 1536668063, edge.getId());
        Assert.assertEquals("The Lable has not been set correctly", "", edge.getLabel());
        Assert.assertEquals("The type has not been set correctly",
                "net.frapu.code.visualization.bpmn.DataFlow",
                edge.getType());
        Assert.assertEquals("The SourceNodeId has not been set correctly", 88478777, edge.getSourceNodeId());
        Assert.assertEquals("The TargetNodeId has not been set correctly", 1628237226, edge.getTargetNodeId());
    }

    @Test
    public void testGetTarget() {
        Edge edge = new Edge();
        edge.initializeInstanceFromXML(dataFlow);
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        Node target = new Node();
        target.setId(1628237226);
        nodes.put(1628237226, target);
        edge.setControlNodes(nodes);
        Assert.assertEquals("The target id is not the same as the id of the target node",
                1628237226,
                edge.getTarget().getId());
        Assert.assertTrue("The target is not the node added as a target", edge.getTarget() == target);
    }

    @Test
    public void testGetSource() {
        Edge edge = new Edge();
        edge.initializeInstanceFromXML(dataFlow);
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        Node source = new Node();
        source.setId(88478777);
        nodes.put(88478777, source);
        edge.setControlNodes(nodes);
        Assert.assertEquals("The source id is not the same as the id of the source node",
                88478777,
                edge.getSource().getId());
        Assert.assertTrue("The target is not the node added as a source", edge.getSource() == source);
    }

    @Test
    public void testSequenceFlowDeserialization() {
        Edge edge = new Edge();
        edge.initializeInstanceFromXML((org.w3c.dom.Node) controlFlow);
        Assert.assertEquals("The ID has not been set correctly", 1536668063, edge.getId());
        Assert.assertEquals("The Lable has not been set correctly", "", edge.getLabel());
        Assert.assertEquals("The type has not been set correctly",
                "net.frapu.code.visualization.bpmn.SequenceFlow",
                edge.getType());
        Assert.assertEquals("The SourceNodeId has not been set correctly", 88478777, edge.getSourceNodeId());
        Assert.assertEquals("The TargetNodeId has not been set correctly", 1628237226, edge.getTargetNodeId());
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
