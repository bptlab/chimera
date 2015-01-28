package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.Map;

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


public class Edge implements IDeserialisable, IPersistable {
    private Map<Integer, Node> controlNodes;
    private int id;
    private int sourceNodeId;
    private int targetNodeId;
    private String type;
    private String label;

    // Database specific values
    private int databaseId;


    @Override
    public void initializeInstanceFromXML(org.w3c.dom.Node element) {
        if (null == controlNodes) {
            return;
        }
        NodeList properties = element.getChildNodes();
        for (int i = 0; i < properties.getLength(); i++) {
            org.w3c.dom.Node property = properties.item(i);
            initializeField(property);
        }
    }

    @Override
    public int writeToDatabase() {
        int targetDatabaseId = controlNodes.get(targetNodeId).getDatabaseID();
        int sourceDatabaseId = controlNodes.get(sourceNodeId).getDatabaseID();
        Connector connector = new Connector();
        if (type.contains("SequnceFlow")) {
            connector.insertControlFlowIntoDatabase(sourceDatabaseId, targetDatabaseId, label);
        } // TODO: Association
        return 0;
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
            case "label" :
                label = value;
                break;
            case "#source" :
                sourceNodeId = Integer.parseInt(value);
                break;
            case "#target" :
                targetNodeId = Integer.parseInt(value);
                break;
        }
    }

    // BEGIN: Getter & SETTER
    public void setControlNodes(Map<Integer, Node> controlNodes) {
        this.controlNodes = controlNodes;
    }

    public Map<Integer, Node> getControlNodes() {
        return controlNodes;
    }

    public int getSourceNodeId() {
        return sourceNodeId;
    }

    public int getTargetNodeId() {
        return targetNodeId;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public int getId() {
        return id;
    }
    // END: Getter & Setter
}
