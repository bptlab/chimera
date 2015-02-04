package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;

/***********************************************************************************
 *
 *   _________ _______  _        _______ _________ _        _______
 *   \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 *      )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 *      |  |  | (__    |   \ | || |         | |   |   \ | || (__
 *      |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 *      |  |  | (      | | \   || | \_  )   | |   | | \   || (
 *   |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 *   (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 *
 *******************************************************************
 *
 *   Copyright © All Rights Reserved 2014 - 2015
 *
 *   Please be aware of the License. You may found it in the root directory.
 *
 ************************************************************************************/
public class Node implements IDeserialisable, IPersistable {

    // Attributes from the XML
    private String type;
    private int id;
    private String text;
    private boolean global;
    // Saves the relation between the types used in the processeditor end the one used in the database of the JEngine
    private HashMap<String, String> peTypeToDbType;
    // Database specific Attributes
    private int databaseID = -1;
    // The Database ID of the fragment which consists the node
    private int fragmentId = -1;
    private String state;
    private int classId;


    public Node() {
        initializeTypeMap();
    }

    private void initializeTypeMap() {
        peTypeToDbType = new HashMap<String, String>();
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.Task", "Activity");
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.EndEvent", "Endevent");
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.StartEvent", "Startevent");
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.ParallelGateway", "AND");
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.ExclusiveGateway", "XOR");
    }


    /**
     * Sets all its attributes from a given XML-Snippet
     * @param node The XML-Node
     */
    @Override
    public void initializeInstanceFromXML(org.w3c.dom.Node node) {
//        try {
//            XPath xPath =  XPathFactory.newInstance().newXPath();
//             String xPathQuery = "/node/property";
//           NodeList properties = (NodeList) xPath.compile(xPathQuery).evaluate(node, XPathConstants.NODESET);

            NodeList properties = node.getChildNodes();
            for (int i = 0; i < properties.getLength(); i++) {
                if (properties.item(i).getNodeName().equals("property")) {
                    org.w3c.dom.Node property = properties.item(i);
                    initializeField(property);
                }
            }
//        } catch (XPathExpressionException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * If possible the field, which is described by the given property, is set.
     * @param property the describing property
     */
    private void initializeField(org.w3c.dom.Node property) {

        NamedNodeMap attributes = property.getAttributes();
        String name = attributes.getNamedItem("name").getTextContent();
        String value = attributes.getNamedItem("value").getTextContent();

        switch (name) {
            case "#type" :
                type = value;
                break;
            case "#id" :
                id = Integer.parseInt(value);
                break;
            case "text" :
                text = value;
                break;
            case "global" :
                global = value.equals("1") ? true : false;
                break;
            case "state" :
                state = value;
                break;
        }
    }

    /**
     * Writes the information of the object to the database
     * @return int - the autoincrement id of the newly created row
     */
    @Override
    public int save() {
        if (fragmentId <= 0) {
            return -1;
        }
        Connector connector = new Connector();
        if (type.contains("DataObject")) {
            // DataObject (Which mean DataNodes) will be done in DataObject
        } else {
            databaseID = connector.insertControlNodeIntoDatabase(text, peTypeToDbType.get(type), fragmentId);
        }
        return databaseID;
    }

    // BEGIN: Getter & Setter
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public HashMap<String, String> getPeTypeToDbType() {
        return peTypeToDbType;
    }

    public void setPeTypeToDbType(HashMap<String, String> peTypeToDbType) {
        this.peTypeToDbType = peTypeToDbType;
    }

    public int getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(int databaseID) {
        this.databaseID = databaseID;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getState() {
        return state;
    }

    public boolean isDataNode() {
        return null != type && type.contains("DataObject");
    }

    public int getClassId() {
        return classId;
    }

    public boolean isTask() {
        return null != type && type.contains("Task");
    }
    // END: Getter & Setter
}
