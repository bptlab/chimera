package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.Map;


public class Edge implements IDeserialisable, IPersistable {
    /**
     * Maps the Node-Model-ID (from the XML) to the Node Object (might be either controlNOde or dataNode).
     */
    private Map<Long, Node> nodes;
    /**
     * The Model-XML-ID of the Edge.
     */
    private int id;
    /**
     * The Model-Node-ID (from the XML) of the source Node of the edge.
     */
    private long sourceNodeId;
    /**
     * The Model-Node-ID (from the XML) of the target Node of the edge.
     */
    private long targetNodeId;
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
        int targetDatabaseId = nodes.get(targetNodeId).getDatabaseID();
        int sourceDatabaseId = nodes.get(sourceNodeId).getDatabaseID();
        Connector connector = new Connector();
        if (type.contains("SequenceFlow")) {
            connector.insertControlFlowIntoDatabase(sourceDatabaseId,
                    targetDatabaseId,
                    label);
        } else if (type.contains("Association")) {
            Node controlNode = (nodes.get(sourceNodeId).isDataNode())
                    ? nodes.get(targetNodeId)
                    : nodes.get(sourceNodeId);
            connector.insertDataFlowIntoDatabase(
                    controlNode.getDatabaseID(),
                    setId,
                    !nodes.get(targetNodeId).isDataNode());
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
                if (label != null && label.equals("DEFAULT"))
                    break;
                label = value;
                break;
            case "#sourceNode":
                sourceNodeId = Long.parseLong(value);
                break;
            case "#targetNode":
                targetNodeId = Long.parseLong(value);
                break;
            case "sequence_type":
                if (value.equals("DEFAULT")) {
                    label = value;
                    break;
                }
                break;
            default:
                // This type is not supported by the JComparser
                break;
        }
    }

    // BEGIN: Getter & SETTER

    /**
     * Set the list of nodes inside the Edge.
     * This will be used to get the database ID.
     *
     * @param newNodes the map of new nodes
     */
    public void setNodes(final Map<Long, Node> newNodes) {
        this.nodes = newNodes;
    }

    /**
     * This is no copy. It is the the Map saved inside the Edge.
     * If you change the content of the Map,
     * you will change the state of the edge.
     *
     * @return all nodes
     */
    public Map<Long, Node> getNodes() {
        return nodes;
    }

    /**
     * Returns the ID of the source Node.
     * The Id is not the database but the xml id.
     *
     * @return the sourceNodeid
     */
    public long getSourceNodeId() {
        return sourceNodeId;
    }

    /**
     * Returns the ID of the target Node.
     * The Id is not the database but the xml id.
     *
     * @return the targetNodeId
     */
    public long getTargetNodeId() {
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
        return nodes.get(targetNodeId);
    }

    /**
     * Returns the ControlNode which is source of the edge.
     *
     * @return the source of the edge.
     */
    public Node getSource() {
        return nodes.get(sourceNodeId);
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
