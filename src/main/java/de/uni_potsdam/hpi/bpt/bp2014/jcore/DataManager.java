package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import com.sun.javaws.exceptions.InvalidArgumentException;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The data manager
 */
public class DataManager {
    private final ScenarioInstance scenarioInstance;


    private List<DataObjectInstance> dataObjectInstances;
    private List<DataObjectInstance> dataObjectInstancesOnChange;

    public DataManager(ScenarioInstance instance) {
        this.scenarioInstance = instance;
        initializeDataObjects();
    }


    public Boolean changeDataObjectInstanceState(int dataObjectId, int stateId) {
        Optional<DataObjectInstance> dataObjectInstance =
                this.scenarioInstance.getDataManager().getDataObjectInstances().stream()
                        .filter(x -> x.getDataObjectId() == dataObjectId).findFirst();
        if (dataObjectInstance.isPresent()) {
            dataObjectInstance.get().setState(stateId);
            Log scenarioLog = new Log();

            // TODO implement properly
            scenarioLog.logDataobjectStateTransition(0, 0);
            return true;
        }

        return false;

    }

    public DataObjectInstance getDataobjectInstanceForId(int id) {
        for (DataObjectInstance dataObjectInstance : this.dataObjectInstances) {
            if (dataObjectInstance.getDataObjectId() == id) {
                return dataObjectInstance;
            }
        }
        throw new IllegalArgumentException("Not a valid data object key");
    }

    /**
     * Initializes all data objects for the scenario instance.
     */
    private void initializeDataObjects() {
        DbDataObject dbDataObject = new DbDataObject();
        int scenarioId = this.scenarioInstance.getScenarioId();
        int scenarioInstanceId = this.scenarioInstance.getScenarioInstanceId();

        List<Integer> data = dbDataObject.getDataObjectsForScenario(scenarioId);
        for (Integer dataObject : data) {
            DataObjectInstance dataObjectInstance = new DataObjectInstance(
                    dataObject, scenarioId, scenarioInstanceId, scenarioInstance);

            //checks if dataObjectInstance is locked
            if (dataObjectInstance.getOnChange()) {
                dataObjectInstancesOnChange.add(dataObjectInstance);
            } else {
                dataObjectInstances.add(dataObjectInstance);
            }
        }
    }
    public List<DataObjectInstance> getDataObjectInstances() {
        return dataObjectInstances;
    }

    public List<DataObjectInstance> getDataObjectInstancesOnChange() {
        return dataObjectInstancesOnChange;
    }
}
