package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Edge;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Node;
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
    private Edge dataFlowEdge;
    private Edge controlFlowEdge;
    private Node target;
    private Node source;

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

    @Before
    public void setUpEdges() {
        dataFlowEdge = new Edge();
        dataFlowEdge.initializeInstanceFromXML(dataFlow);
        controlFlowEdge = new Edge();
        controlFlowEdge.initializeInstanceFromXML(controlFlow);
        setUpTargetAndSource();
    }

    public void setUpTargetAndSource() {
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        target = new Node();
        target.setId(1628237226);
        nodes.put(1628237226, target);
        source = new Node();
        source.setId(88478777);
        nodes.put(88478777, source);
        dataFlowEdge.setControlNodes(nodes);
        controlFlowEdge.setControlNodes(nodes);
    }
    // END: Set-Up

    // BEGIN: Tests
    @Test
    public void testDataFlowDeserialization() {
        Assert.assertEquals("The ID has not been set correctly", 1536668063, dataFlowEdge.getId());
        Assert.assertEquals("The Lable has not been set correctly", "", dataFlowEdge.getLabel());
        Assert.assertEquals("The type has not been set correctly",
                "net.frapu.code.visualization.bpmn.DataFlow",
                dataFlowEdge.getType());
        Assert.assertEquals("The SourceNodeId has not been set correctly", 88478777, dataFlowEdge.getSourceNodeId());
        Assert.assertEquals("The TargetNodeId has not been set correctly", 1628237226, dataFlowEdge.getTargetNodeId());
    }

    @Test
    public void testGetTarget() {
        Assert.assertEquals("The target id is not the same as the id of the target node",
                1628237226,
                dataFlowEdge.getTarget().getId());
        Assert.assertTrue("The target is not the node added as a target", dataFlowEdge.getTarget() == target);
    }

    @Test
    public void testGetSource() {
        Assert.assertEquals("The source id is not the same as the id of the source node",
                88478777,
                dataFlowEdge.getSource().getId());
        Assert.assertTrue("The target is not the node added as a source", dataFlowEdge.getSource() == source);
    }

    @Test
    public void testSequenceFlowDeserialization() {
        Assert.assertEquals("The ID has not been set correctly", 1536668063, controlFlowEdge.getId());
        Assert.assertEquals("The Lable has not been set correctly", "", controlFlowEdge.getLabel());
        Assert.assertEquals("The type has not been set correctly",
                "net.frapu.code.visualization.bpmn.SequenceFlow",
                controlFlowEdge.getType());
        Assert.assertEquals("The SourceNodeId has not been set correctly", 88478777, controlFlowEdge.getSourceNodeId());
        Assert.assertEquals("The TargetNodeId has not been set correctly", 1628237226, controlFlowEdge.getTargetNodeId());
    }

    @Test
    public void testSaveDataFlow() {
        controlFlowEdge.getSource().setDatabaseID(424242);
        controlFlowEdge.getTarget().setDatabaseID(212121);
        dataFlowEdge.save();
    }

    @Test
    public void testSaveSequenceFlow() {
        controlFlowEdge.getSource().setDatabaseID(212121);
        controlFlowEdge.getTarget().setDatabaseID(424242);
        controlFlowEdge.save();
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
