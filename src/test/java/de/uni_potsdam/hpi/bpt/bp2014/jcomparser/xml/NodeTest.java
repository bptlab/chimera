package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 */
public class NodeTest {
    private Document document = new DocumentImpl(null);
    private Element dataNode;
    private Element activityGlobalNode;
    private Element activityLocalNode;
    private Element activitySendNode;
    private Element serviceTask;
    private Element startEventNode;
    private Element endEventNode;
    private Element xorNode;
    private Element andNode;


    /**
     *
     */
    @Before
    public void setUpDataObject() {
        dataNode = document.createElement("node");
        dataNode.appendChild(createProperty("text", "Teil"));
        dataNode.appendChild(createProperty("state", "transportbereit"));
        dataNode.appendChild(createProperty("#id", "706118804"));
        dataNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.DataObject"));
        dataNode.appendChild(createProperty("stereotype", ""));
    }

    /**
     *
     */
    @Before
    public void setUpGlobalTask() {
        activityGlobalNode = document.createElement("node");
        activityGlobalNode.appendChild(createProperty("text", "Teil transportieren"));
        activityGlobalNode.appendChild(createProperty("global", "1"));
        activityGlobalNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Task"));
        activityGlobalNode.appendChild(createProperty("#id", "368338489"));
        activityGlobalNode.appendChild(createProperty("stereotype", ""));
    }

    /**
     *
     */
    @Before
    public void setUpLocalTask() {
        activityLocalNode = document.createElement("node");
        activityLocalNode.appendChild(createProperty("text", "Teil kleben"));
        activityLocalNode.appendChild(createProperty("global", "0"));
        activityLocalNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Task"));
        activityLocalNode.appendChild(createProperty("#id", "368338489"));
        activityLocalNode.appendChild(createProperty("stereotype", ""));
    }

    /**
     *
     */
    @Before
    public void setUpSendTask(){
        activitySendNode = document.createElement("node");
        activitySendNode.appendChild(createProperty("text", "Email senden"));
        activitySendNode.appendChild(createProperty("global", "0"));
        activitySendNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Task"));
        activitySendNode.appendChild(createProperty("#id", "368338489"));
        activitySendNode.appendChild(createProperty("stereotype", "SEND"));
    }

    /**
     *
     */
    @Before
    public void setUpServiceTask(){
        serviceTask = document.createElement("node");
        serviceTask.appendChild(createProperty("text", "WebService ausfuehren"));
        serviceTask.appendChild(createProperty("global", "0"));
        serviceTask.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.Task"));
        serviceTask.appendChild(createProperty("#id", "368338489"));
        serviceTask.appendChild(createProperty("stereotype", "SERVICE"));
    }

    /**
     *
     */
    @Before
    public void setUpStartEventNode() {
        startEventNode = document.createElement("node");
        startEventNode.appendChild(createProperty("text", "Start"));
        startEventNode.appendChild(createProperty("#id", "368338489"));
        startEventNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.StartEvent"));
        startEventNode.appendChild(createProperty("stereotype", ""));
    }

    /**
     *
     */
    @Before
    public void setUpEndEventNode() {
        endEventNode = document.createElement("node");
        endEventNode.appendChild(createProperty("text", "End"));
        endEventNode.appendChild(createProperty("#id", "368338489"));
        endEventNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.EndEvent"));
        endEventNode.appendChild(createProperty("stereotype", ""));
    }

    /**
     *
     */
    @Before
    public void setUpXorNode() {
        xorNode = document.createElement("node");
        xorNode.appendChild(createProperty("text", "XOR"));
        xorNode.appendChild(createProperty("#id", "368338489"));
        xorNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.ExclusiveGateway"));
        xorNode.appendChild(createProperty("stereotype", ""));
    }

    /**
     *
     */
    @Before
    public void setUpAndNode() {
        andNode = document.createElement("node");
        andNode.appendChild(createProperty("text", "AND"));
        andNode.appendChild(createProperty("#id", "368338489"));
        andNode.appendChild(createProperty("#type", "net.frapu.code.visualization.bpmn.ParallelGateway"));
        andNode.appendChild(createProperty("stereotype", ""));
    }

    /**
     *
     * @param name
     * @param value
     * @return
     */
    private Element createProperty(String name, String value) {
        if (null == document) {
            return null;
        }
        Element property = document.createElement("property");
        property.setAttribute("name", name);
        property.setAttribute("value", value);
        return property;
    }

    /**
     *
     */
    @Test
    public void testGlobalActivityDeserialization() {
        Node activity = new Node();
        activity.initializeInstanceFromXML(activityGlobalNode);
        Assert.assertEquals("Id has not been set correctly", 368338489L, activity.getId());
        Assert.assertEquals("Text has not been set correctly", "Teil transportieren", activity.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.Task", activity.getType());
        Assert.assertEquals("Global has not been set correctly", true, activity.isGlobal());
        Assert.assertTrue("The Node is a Task but isTask returns false", activity.isTask());
        Assert.assertFalse("The Node is a Task but isDataNode returns true", activity.isDataNode());
        Assert.assertEquals("The stereotype is not set correctly", "", activity.getStereotype());
    }

    /**
     *
     */
    @Test
    public void testLocalActivityDeserialization() {
        Node activity = new Node();
        activity.initializeInstanceFromXML(activityLocalNode);
        Assert.assertEquals("Id has not been set correctly", 368338489L, activity.getId());
        Assert.assertEquals("Text has not been set correctly", "Teil kleben", activity.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.Task", activity.getType());
        Assert.assertEquals("Global has not been set correctly", false, activity.isGlobal());
        Assert.assertTrue("The Node is a Task but isTask returns false", activity.isTask());
        Assert.assertFalse("The Node is a Task but isDataNode returns true", activity.isDataNode());
        Assert.assertEquals("The stereotype is not set correctly", "", activity.getStereotype());
    }

    /**
     *
     */
    @Test
    public void testSendActivityDeserialization() {
        Node activity = new Node();
        activity.initializeInstanceFromXML(activitySendNode);
        Assert.assertEquals("Id has not been set correctly", 368338489L, activity.getId());
        Assert.assertEquals("Text has not been set correctly", "Email senden", activity.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.Task", activity.getType());
        Assert.assertEquals("Global has not been set correctly", false, activity.isGlobal());
        Assert.assertTrue("The Node is a Task but isTask returns false", activity.isTask());
        Assert.assertFalse("The Node is a Task but isDataNode returns true", activity.isDataNode());
        Assert.assertEquals("The stereotype is not set correctly", "SEND", activity.getStereotype());
    }

    /**
     *
     */
    @Test
    public void testServiceTaskDeserialization() {
        Node activity = new Node();
        activity.initializeInstanceFromXML(serviceTask);
        Assert.assertEquals("Id has not been set correctly", 368338489L, activity.getId());
        Assert.assertEquals("Text has not been set correctly", "WebService ausfuehren", activity.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.Task", activity.getType());
        Assert.assertEquals("Global has not been set correctly", false, activity.isGlobal());
        Assert.assertTrue("The Node is a Task but isTask returns false", activity.isTask());
        Assert.assertFalse("The Node is a Task but isDataNode returns true", activity.isDataNode());
        Assert.assertEquals("The stereotype is not set correctly", "SERVICE", activity.getStereotype());
    }

    /**
     *
     */
    @Test
    public void testStartEventyDeserialization() {
        Node startEvent = new Node();
        startEvent.initializeInstanceFromXML(startEventNode);
        Assert.assertEquals("Id has not been set correctly", 368338489L, startEvent.getId());
        Assert.assertEquals("Text has not been set correctly", "Start", startEvent.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.StartEvent", startEvent.getType());
        Assert.assertFalse("The Node is a StartEvent but isTask returns true", startEvent.isTask());
        Assert.assertFalse("The Node is a StartEvent but isDataNode returns true", startEvent.isDataNode());
        Assert.assertEquals("The stereotype is not set correctly", "", startEvent.getStereotype());
    }

    /**
     *
     */
    @Test
    public void testEndEventDeserialization() {
        Node endEvent = new Node();
        endEvent.initializeInstanceFromXML(endEventNode);
        Assert.assertEquals("Id has not been set correctly", 368338489L, endEvent.getId());
        Assert.assertEquals("Text has not been set correctly", "End", endEvent.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.EndEvent", endEvent.getType());
        Assert.assertFalse("The Node is a endEvent but isTask returns true", endEvent.isTask());
        Assert.assertFalse("The Node is a endEvent but isDataNode returns true", endEvent.isDataNode());
        Assert.assertEquals("The stereotype is not set correctly", "", endEvent.getStereotype());
    }

    /**
     *
     */
    @Test
    public void testXorDeserialization() {
        Node xor = new Node();
        xor.initializeInstanceFromXML(xorNode);
        Assert.assertEquals("Id has not been set correctly", 368338489L, xor.getId());
        Assert.assertEquals("Text has not been set correctly", "XOR", xor.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.ExclusiveGateway", xor.getType());
        Assert.assertFalse("The Node is a xorGateway but isTask returns true", xor.isTask());
        Assert.assertFalse("The Node is a xorGateway but isDataNode returns true", xor.isDataNode());
        Assert.assertEquals("The stereotype is not set correctly", "", xor.getStereotype());
    }

    /**
     *
     */
    @Test
    public void testAndDeserialization() {
        Node and = new Node();
        and.initializeInstanceFromXML(andNode);
        Assert.assertEquals("Id has not been set correctly", 368338489L, and.getId());
        Assert.assertEquals("Text has not been set correctly", "AND", and.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.ParallelGateway", and.getType());
        Assert.assertFalse("The Node is a andGateway but isTask returns true", and.isTask());
        Assert.assertFalse("The Node is a andGateway but isDataNode returns true", and.isDataNode());
        Assert.assertEquals("The stereotype is not set correctly", "", and.getStereotype());
    }

    /**
     *
     */
    @Test
    public void testDataObject() {
        Node data = new Node();
        data.initializeInstanceFromXML(dataNode);
        Assert.assertEquals("Id has not beens set correctly", 706118804L, data.getId());
        Assert.assertEquals("Text has not been set correctly", "Teil", data.getText());
        Assert.assertEquals("Type has not been set correctly", "net.frapu.code.visualization.bpmn.DataObject", data.getType());
        Assert.assertFalse("The Node is a dataObject, but isTask returns true", data.isTask());
        Assert.assertTrue("The Node is a dataobject, but isDataNode returns false", data.isDataNode());
        Assert.assertEquals("The stereotype is not set correctly", "", data.getStereotype());
    }

    /**
     * Actually the save operation does not write DataObjects to the Database.
     * We are testing that no exception occurs
     */
    @Test
    public void testSaveDataObject() {
        Node data = new Node();
        data.initializeInstanceFromXML(dataNode);
        data.save();
    }

    /**
     * Actually we are only testing that no exception occurs and that the databaseId will be valid afterwards.
     */
    @Test
    public void testControlNode() {
        Node task = new Node();
        task.initializeInstanceFromXML(activityGlobalNode);
        task.setFragmentId(424242);
        task.save();
        Assert.assertFalse("The database Id is invalid (0 or less)", 1 > task.getDatabaseID());
    }
}
