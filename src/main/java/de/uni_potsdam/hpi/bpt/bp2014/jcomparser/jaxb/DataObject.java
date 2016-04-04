package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataClass;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataObject")
@XmlAccessorType(XmlAccessType.NONE)
public class DataObject {
    @XmlAttribute
    private String id;



    /**
     * A list of all dataNodes which represent the DataObject.
     */
    private List<DataNode> dataNodes = new ArrayList<>();

    /**
     * The database Id of the scenario.
     */
    private int scenarioId = -1;

    /**
     * The database ID of the data Object.
     */
    private int databaseId;
    /**
     * The database ID of the initial State.
     */
    private Integer initStateDatabaseId;
    /**
     * This is the dataClass belonging to this dataObject.
     */
    private DataClass dataClass;

    /**
     * Creates a new DataObject with a given dataClass.
     *
     * @param dataClass The dataClass the dataObject belongs to
     */
    public DataObject(final DataClass dataClass) {

        this.dataClass = dataClass;
    }

    /**
     * Adds a new dataNode to the dataObject.
     * If the state has not been written to the database it will be added
     *
     * @param dataNode the new node which will be added
     */
    public void addDataNode(final DataNode dataNode) {
        dataNodes.add(dataNode);
    }

    public int save() {
        if (0 >= scenarioId) {
            return -1;
        }

        Connector connector = new Connector();

        initStateDatabaseId = this.dataClass.getStateToDatabaseId().get("init");
        databaseId = connector.insertDataObjectIntoDatabase(
                this.dataClass.getDataClassName(), this.dataClass.getDataClassID(),
                scenarioId, initStateDatabaseId);
        saveDataNodes();
        return databaseId;
    }


    /**
     * Saves the data Nodes to the database.
     * Also the databaseID will be set for each node.
     */
    private void saveDataNodes() {
        initStateDatabaseId = this.dataClass.getStateToDatabaseId().get("init");
        for (DataNode dataNode : dataNodes) {
            dataNode.save(this);
        }
    }


    /**
     * Sets the scenario id.
     *
     * @param newScenarioId Should be the database Id of the Scenario.
     */
    public void setScenarioId(final int newScenarioId) {
        this.scenarioId = newScenarioId;
    }

    /**
     * Returns the list of dataNodes.
     * Be aware that it is no copy.
     * (It is more a composition than an aggregation.
     * This means, if you change the list you change the dataObject)
     *
     * @return the list of DataNodes inside the DataObject
     */
    public List<DataNode> getDataNodes() {
        return dataNodes;
    }

    /**
     * Returns the databaseID of the initial State.
     * (We assume that the initial State is ("init").
     *
     * @return the databaseId of the state "init".
     */
    public Integer getInitState() {
        return initStateDatabaseId;
    }

    /**
     * Returns the database id.
     *
     * @return the id (int) which is primary key inside the database.
     */
    public int getDatabaseId() {
        return databaseId;
    }

    public String getId() {
        return id;
    }

    public String getDataClassName() {
        return dataClass.getDataClassName();
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataClass getDataClass() {
        return dataClass;
    }

    public int getScenarioId() {
        return scenarioId;
    }

}
