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
     * The name is given at the moment as name [state]
     */
    @XmlAttribute
    private String name;

    /**
     * Id of an data object. This construct is used to express that, an data object can
     * occur multiple times in an BPMN process.
     */
    @XmlAttribute
    private String dataObjectRef;


    private int databaseId;

    public String getState() {
        String[] splittedName = this.name.split("\\s+");
        // State
        return splittedName[1].substring(1, splittedName[1].length() - 1);
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
        String[] splittedName = this.name.split("\\s+");
        return splittedName[0];
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
