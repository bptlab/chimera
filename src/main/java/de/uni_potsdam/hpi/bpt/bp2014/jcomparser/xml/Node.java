package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.HashMap;


/**
 * A Class which represents a Node, (ControlNode and DataNodes) of thh model.
 * It can be created from an xml and saved to the database.
 */
public class Node implements IDeserialisable, IPersistable {

    /**
     * The type of the Node. It will be extracted from the XML File.
     */
    private String type;
    /**
     * The ID of the node from the xml.
     */
    private long id;
    /**
     * The text (label) of the node.
     */
    private String text;
    /**
     * A flag which shows if a task is global or not.
     */
    private boolean global;

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
     * A string, which holds the stereotype of the node (e.g. "SEND" for EmailTask).
     */
    private String stereotype;

    /**
     * Each supported type of the process editor is mapped to
     * a type inside the database.
     * @param type The type of the process element from the xml
     * @return the type as String that is written to the database
     */
    private String getDbTypeByPeType(String type) {
        if (type.contains("Task"))
            return "Activity";
        if (type.contains("EndEvent"))
            return "Endevent";
        if (type.contains("StartEvent"))
            return "Startevent";
        if (type.contains("ParallelGateway"))
            return "AND";
        if (type.contains("ExclusiveGateway"))
            return "XOR";
        if (type.contains("SEND"))
            return "EmailTask";
        if (type.contains("SERVICE"))
            return "WebServiceTask";
        else
            return "";
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
                id = Long.parseLong(value);
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
            case "stereotype":
                stereotype = value;
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
            if (!stereotype.isEmpty()) {
                // we identify mailtasks that need to be marked in the database by their stereotype
                databaseID = connector.insertControlNodeIntoDatabase(text,
                        getDbTypeByPeType(stereotype),
                        fragmentId,
                        id);
                if (stereotype.equals("SEND"))
                    connector.createEMailTemplate(databaseID);

            } else {
                // DataNodes will be done in DataObject
                databaseID = connector.insertControlNodeIntoDatabase(text,
                        getDbTypeByPeType(type),
                        fragmentId,
                        id);
            }
        }
        return databaseID;
    }

    /**
     * Migrate datanode- or controlnodeInstances.
     *
     * @param oldFragmentID databaseId of the old fragment whose instances get migrated and this node belongs to
     */
    public void migrate(int oldFragmentID) {
        Connector connector = new Connector();
        int oldControlNodeID = connector.getControlNodeID(oldFragmentID, id);
        connector.migrateControlNodeInstance(oldControlNodeID, databaseID);
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
    public long getId() {
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

    /**
     * Returns the stereotype.
     *
     * @return the stereotype as a String.
     */
    public String getStereotype() {
        return stereotype;
    }

    /**
     * Sets the fragment id.
     *
     * @param newFragmentId should be the database ID of a fragment.
     */
    public void setFragmentId(final int newFragmentId) {
        this.fragmentId = newFragmentId;
    }

    /**
     * Sets the database id for an Node.
     * Used for DataNodes.
     *
     * @param newDatabaseID the new database ID
     */
    public void setDatabaseID(final int newDatabaseID) {
        this.databaseID = newDatabaseID;
    }

    /**
     * Sets the Id of the Node.
     * Currently only used by tests.
     *
     * @param newId the new id of the data Node.
     */
    protected void setId(final long newId) {
        this.id = newId;
    }

    /**
     * Sets the Label of the Node.
     * Currently only used by tests.
     *
     * @param newLabel the new label of the data Node.
     */
    protected void setText(final String newLabel) {
        this.text = newLabel;
    }
    // END: Getter & Setter
}
