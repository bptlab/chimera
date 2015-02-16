package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.Map;

/**
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


public class Edge implements IDeserialisable, IPersistable {
    /**
     * Maps the Model-Node-ID (from the XML) to the ControlNode Object.
     */
    private Map<Integer, Node> controlNodes;
    /**
     * The Model-XML-ID of the Edge.
     */
    private int id;
    /**
     * The Model-Node-ID (from the XML) of the source Node of the edge.
     */
    private int sourceNodeId;
    /**
     * The Model-Node-ID (from the XML) of the target Node of the edge.
     */
    private int targetNodeId;
    /**
     * The type of the edge.
     * Could be either "*Association" (DataFlow) or "*SequenceFlow".
     */
    private String type;
    /**
     * The label of the edge (Maybe the condition string of the flow).
     */
    private String label;
    /**
     * SetId is the database id of the corresponding I/O-Set,
     * If it is an association.
     */
    private int setId = -1;

    /**
     * This method instantiates a Edge from its XML representation.
     * It is important, that the xml is valid.
     *
     * @param element The XML-Node with tag name edge
     */
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {

        NodeList properties = element.getChildNodes();
        for (int i = 0; i < properties.getLength(); i++) {
            if (properties.item(i).getNodeName().equals("property")) {
                org.w3c.dom.Node property = properties.item(i);
                initializeField(property);
            }
        }
    }

    @Override
    public int save() {
        int targetDatabaseId = controlNodes.get(targetNodeId).getDatabaseID();
        int sourceDatabaseId = controlNodes.get(sourceNodeId).getDatabaseID();
        Connector connector = new Connector();
        if (type.contains("SequenceFlow")) {
            connector.insertControlFlowIntoDatabase(sourceDatabaseId,
                    targetDatabaseId,
                    label);
        } else if (type.contains("Association")) {
            Node controlNode = (controlNodes.get(sourceNodeId).isDataNode())
                    ? controlNodes.get(targetNodeId)
                    : controlNodes.get(sourceNodeId);
            connector.insertDataFlowIntoDatabase(
                    controlNode.getDatabaseID(),
                    setId,
                    !controlNodes.get(targetNodeId).isDataNode());
        }
        return 0;
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
            case "label":
                label = value;
                break;
            case "#sourceNode":
                sourceNodeId = Integer.parseInt(value);
                break;
            case "#targetNode":
                targetNodeId = Integer.parseInt(value);
                break;
            default:
                // This type is not supported by the JComparser
                break;
        }
    }

    // BEGIN: Getter & SETTER

    /**
     * Set the list of controlNodes inside the Edge.
     * This will be used to get the database ID.
     *
     * @param newControlNodes the map of new ControlNodes
     */
    public void setControlNodes(final Map<Integer, Node> newControlNodes) {
        this.controlNodes = newControlNodes;
    }

    /**
     * This is no copy. It is the the Map saved inside the Edge.
     * If you change the content of the Map,
     * you will change the state of the edge.
     *
     * @return the new controlNode
     */
    public Map<Integer, Node> getControlNodes() {
        return controlNodes;
    }

    /**
     * Returns the ID of the source Node.
     * The Id is not the database but the xml id.
     *
     * @return the sourceNodeid
     */
    public int getSourceNodeId() {
        return sourceNodeId;
    }

    /**
     * Returns the ID of the target Node.
     * The Id is not the database but the xml id.
     *
     * @return the targetNodeId
     */
    public int getTargetNodeId() {
        return targetNodeId;
    }

    /**
     * Returns the Type which is written inside the XML.
     *
     * @return the type (processEditor class)
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the label of the edge.
     * The Label is the text annotated.
     *
     * @return the label of the Edge.
     */
    public String getLabel() {
        return label;
    }

    /**
     * returns the Model-XML-ID of the edge.
     *
     * @return the database ID.
     */
    public int getId() {
        return id;
    }

    /**
     * The Control Node which is the target of the edge.
     *
     * @return the target of the edge.
     */
    public Node getTarget() {
        return controlNodes.get(targetNodeId);
    }

    /**
     * Returns the ControlNode which is source of the edge.
     *
     * @return the source of the edge.
     */
    public Node getSource() {
        return controlNodes.get(sourceNodeId);
    }

    /**
     * Sets the SetID.
     * The SetId represents the database Id of the Input or Output Set.
     *
     * @param newSetId the database ID of the corresponding DataSet (I/O).
     */
    public void setSetId(final int newSetId) {
        this.setId = newSetId;
    }
    // END: Getter & Setter
}
