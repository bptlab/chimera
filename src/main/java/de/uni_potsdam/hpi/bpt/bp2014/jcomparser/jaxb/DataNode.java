package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataObjectReference")
@XmlAccessorType(XmlAccessType.NONE)
public class DataNode {
    @XmlAttribute
    private String id;

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
     * Id of a data object. This construct is used to express that a data object can
     * occur multiple times in an BPMN process.
     */
    @XmlAttribute
    private String dataObjectRef;


    private int databaseId;

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
}
