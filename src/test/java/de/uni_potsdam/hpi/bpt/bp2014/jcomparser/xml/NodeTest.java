package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;


import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NodeTest {
    private Document document = new DocumentImpl(null);
    private Element activityNode;
    private Element startEventNode;
    private Element endEventNode;
    private Element xorNode;
    private Element andNode;

    @Before
    public void setUpGlobalTask() {
        activityNode = document.createElement("node");
        activityNode.appendChild(createProperty("text", "Teil transportieren"));
        activityNode.appendChild(createProperty("global", "1"));
        activityNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Task"));
        activityNode.appendChild(createProperty("#id", "368338489"));

    }

    @Before
    public void setUpStartEventNode() {
        startEventNode = document.createElement("node");
        startEventNode.appendChild(createProperty("text", "Start"));
        startEventNode.appendChild(createProperty("#id", "368338489"));
        startEventNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.StartEvent"));
    }

    @Before
    public void setUpEndEventNode() {
        endEventNode = document.createElement("node");
        endEventNode.appendChild(createProperty("text", "End"));
        endEventNode.appendChild(createProperty("#id", "368338489"));
        endEventNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.EndEvent"));
    }

    @Before
    public void setUpXorNode() {
        xorNode = document.createElement("node");
        xorNode.appendChild(createProperty("text", "XOR"));
        xorNode.appendChild(createProperty("#id", "368338489"));
        xorNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.ExclusiveGateway"));
    }

    @Before
    public void setUpAndNode() {
        andNode = document.createElement("node");
        andNode.appendChild(createProperty("text", "AND"));
        andNode.appendChild(createProperty("#id", "368338489"));
        andNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.ParallelGateway"));
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

    @Test
    public void testGlobalActivityDeserialization() {
        Node activity = new Node();
        activity.initializeInstanceFromXML(activityNode);
        Assert.assertEquals("Id has not been set correctly", 368338489, activity.getId());
        Assert.assertEquals("Text has not been set correctly", "Teil transportieren", activity.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.Task", activity.getType());
        Assert.assertEquals("Global has not been set correctly", true, activity.isGlobal());
    }

    @Test
    public void testStartEventyDeserialization() {
        Node startEvent = new Node();
        startEvent.initializeInstanceFromXML(startEventNode);
        Assert.assertEquals("Id has not been set correctly", 368338489, startEvent.getId());
        Assert.assertEquals("Text has not been set correctly", "Start", startEvent.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.StartEvent", startEvent.getType());
    }

    @Test
    public void testEndEventDeserialization() {
        Node endEvent = new Node();
        endEvent.initializeInstanceFromXML(endEventNode);
        Assert.assertEquals("Id has not been set correctly", 368338489, endEvent.getId());
        Assert.assertEquals("Text has not been set correctly", "End", endEvent.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.EndEvent", endEvent.getType());
    }

    @Test
    public void testXorDeserialization() {
        Node xor = new Node();
        xor.initializeInstanceFromXML(xorNode);
        Assert.assertEquals("Id has not been set correctly", 368338489, xor.getId());
        Assert.assertEquals("Text has not been set correctly", "XOR", xor.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.ExclusiveGateway", xor.getType());
    }

    @Test
    public void testAndDeserialization() {
        Node xor = new Node();
        xor.initializeInstanceFromXML(andNode);
        Assert.assertEquals("Id has not been set correctly", 368338489, xor.getId());
        Assert.assertEquals("Text has not been set correctly", "AND", xor.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.ParallelGateway", xor.getType());
    }
}
