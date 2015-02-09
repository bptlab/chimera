package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.HashMap;

/*
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

/**
 * A Class which represents a Node, (ControlNode and DataNodes) of thh model.
 * It can be created from an xml and saved to the database.
 */
public class Node implements IDeserialisable, IPersistable {


    // Attributes from the XML
    /**
     * The type of the Node. It will be extracted from the XML File.
     */
    private String type;
    /**
     * The ID of the node from the xml.
     */
    private int id;
    /**
     * The text (label) of the node.
     */
    private String text;
    /**
     * A flag which shows if a task is true or not.
     */
    private boolean global;
    /**
     * Saves the relation between the types used in the PE and database.
     */
    private HashMap<String, String> peTypeToDbType;
    // Database specific Attributes
    /**
     * The database id of the node.
     * Will be set as soon as the node is written to the database.
     */
    private int databaseID = -1;
    /**
     * The Database ID of the fragment which consists the node.
     */
    private int fragmentId = -1;
    /**
     * A string, which holds the state of the node.
     * It will only be set, if and only if the node is a data node.
     */
    private String state;


    /**
     * Creates a not initialized node.
     */
    public Node() {
        initializeTypeMap();
    }

    /**
     * Initializes the map of Types.
     * Each supported type of the process editor is mapped to
     * a type inside the database.
     */
    private void initializeTypeMap() {
        peTypeToDbType = new HashMap<String, String>();
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.Task",
                "Activity");
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.EndEvent",
                "Endevent");
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.StartEvent",
                "Startevent");
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.ParallelGateway",
                "AND");
        peTypeToDbType.put("net.frapu.code.visualization.bpmn.ExclusiveGateway",
                "XOR");
    }


    /**
     * Sets all its attributes from a given XML-Snippet.
     *
     * @param node The XML-Node
     */
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node node) {

        NodeList properties = node.getChildNodes();
        for (int i = 0; i < properties.getLength(); i++) {
            if (properties.item(i).getNodeName().equals("property")) {
                org.w3c.dom.Node property = properties.item(i);
                initializeField(property);
            }
        }
    }

    /**
     * If possible the field, which is described by the given property, is set.
     *
     * @param property the describing property
     */
    private void initializeField(final org.w3c.dom.Node property) {

        NamedNodeMap attributes = property.getAttributes();
        String name = attributes.getNamedItem("name").getTextContent();
        String value = attributes.getNamedItem("value").getTextContent();

        switch (name) {
            case "#type":
                type = value;
                break;
            case "#id":
                id = Integer.parseInt(value);
                break;
            case "text":
                text = value;
                break;
            case "global":
                global = value.equals("1");
                break;
            case "state":
                state = value;
                break;
            default:
                // Property will not be handled
                break;
        }
    }

    /**
     * Writes the information of the object to the database.
     *
     * @return int - the autoincrement id of the newly created row
     */
    @Override
    public int save() {
        if (fragmentId <= 0) {
            return -1;
        }
        Connector connector = new Connector();
        if (!type.contains("DataObject")) {
            // DataNodes will be done in DataObject
            databaseID = connector.insertControlNodeIntoDatabase(text,
                    peTypeToDbType.get(type),
                    fragmentId);
        }
        return databaseID;
    }

    // BEGIN: Getter & Setter

    /**
     * Returns the type of the node.
     * The Type is the one used inside the XML (ProcessEditor).
     *
     * @return the type of the node.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the ID of the node.
     * It is the id used inside the XML.
     *
     * @return the id extracted from the XML.
     */
    public int getId() {
        return id;
    }

    /**
     * This method returns the label of the node.
     * It will be null until it is initialized from the XML.
     *
     * @return the label/text of the node.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns true if the node is a global else it returns false.
     *
     * @return Node is globally defined (can be referenced)
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Returns the database id.
     *
     * @return the id which is primary key of the corresponding database entry.
     */
    public int getDatabaseID() {
        return databaseID;
    }

    /**
     * The state of the node.
     *
     * @return null if node is not a dataNode, else the state.
     */
    public String getState() {
        return state;
    }

    /**
     * Returns true if the node is a dataNode.
     *
     * @return Node == dataNode
     */
    public boolean isDataNode() {
        return null != type && type.contains("DataObject");
    }

    /**
     * Returns true if the node is a task.
     *
     * @return Node == task
     */
    public boolean isTask() {
        return null != type && type.contains("Task");
    }
    // END: Getter & Setter
}
