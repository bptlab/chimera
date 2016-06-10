package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataAttribute;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataClass;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Optional;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataObjectReference")
@XmlAccessorType(XmlAccessType.NONE)
public class DataNode {
    @XmlAttribute
    private String id;
    /**
     * Id of a data object. This construct is used to express that a data object can
     * occur multiple times in an BPMN process.
     */
    @XmlAttribute
    private String dataObjectRef;
    /**
     * Name of the dataclass the dataobject/node refers to.
     */
    @XmlAttribute(name = "griffin:dataclass")
    private String name;
    /**
     * Current [state] of the datanode
     */
    @XmlAttribute(name = "griffin:state")
    private String state;
    /**
     * JsonObject that might contain a jsonpath expression for each data attribute.
     */
    @XmlAttribute(name = "griffin:jsonpath")
    private String jsonPath = "";

    private int databaseId;

    public void save(DataClass dataClass, int scenarioId) {
        Connector connector = new Connector();

        int stateDatabaseId = dataClass.getStateToDatabaseId().get(this.state);
        int nodeId = connector.insertDataNodeIntoDatabase(
                scenarioId, stateDatabaseId, dataClass.getDatabaseId());
        this.setDatabaseId(nodeId);
    }


    public void insertPathMappingIntoDatabase(DataClass dataClass) {
        if (jsonPath.isEmpty()) {
            return;
        }
        Connector connector = new Connector();
        String xmlEscapedPathObject = StringEscapeUtils.unescapeHtml4(jsonPath);
        JSONObject pathObject = new JSONObject(xmlEscapedPathObject);
        int precedingControlNode = new DbDataFlow().getPrecedingControlNode(this.databaseId);
        for (Object key : pathObject.keySet()) {
            Optional<DataAttribute> dataAttribute = dataClass.getDataAttributeByName(key.toString());
            if (dataAttribute.isPresent()) {
                int dataAttributeId = dataAttribute.get().getAttributeDatabaseId();
                String jsonPathString = pathObject.getString(key.toString());
                String escapedJsonPath = jsonPathString.replace("'", "''");
                connector.insertPathMappingIntoDatabase(
                        precedingControlNode, dataAttributeId, escapedJsonPath);
            }
        }
    }

    public String getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name of the data object which is referred by this data node
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataObjectRef() {
        return dataObjectRef;
    }

    public void setDataObjectRef(String dataObjectRef) {
        this.dataObjectRef = dataObjectRef;
    }

    public int getDatabaseId() {
        return databaseId;
    }


    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }
}
