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
}
