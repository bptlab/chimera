package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbHistoryActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbHistoryDataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbHistoryDataObjectInstance;


/**
 * This class provides an abstraction layer for logging, so that the actually logic can be put to the database.
 */
public class Log {
    /**
     * Database Connection objects.
     */
    private DbHistoryActivityInstance dbHistoryActivityInstance = new DbHistoryActivityInstance();
    private DbHistoryDataObjectInstance dbHistoryDataObjectInstance = new DbHistoryDataObjectInstance();
    private DbHistoryDataAttributeInstance dbHistoryDataAttributeInstance = new DbHistoryDataAttributeInstance();

    /**
     * This method delegates a log entry containing an activity state transition being saved into the database.
     *
     * @param id    the ID of the ActivityInstance that is changed.
     * @param state the new state of the ActivityInstance.
     */
    public void newActivityInstanceState(int id, String state) {
        dbHistoryActivityInstance.createEntry(id, state);
    }

    /**
     * This method delegates a log entry of a newly created ActivityInstance being saved into the database.
     *
     * @param id the ID of the ActivityInstance that is created.
     */
    public void newActivityInstance(int id) {
        dbHistoryActivityInstance.createNewActivityEntry(id);
    }

    /**
     * This method delegates a log entry containing a DataObjectInstance state transition being saved into the database.
     *
     * @param object_instance_id the ID of the DataObjectInstance that is changed.
     * @param state_id           the new state of the DataObjectInstance.
     */
    public void newDataObjectInstanceState(int object_instance_id, int state_id) {
        dbHistoryDataObjectInstance.createEntry(object_instance_id, state_id);
    }

    /**
     * This method delegates a log entry of a newly created DataObjectInstance being saved into the database.
     *
     * @param id the ID of the DataObjectInstance that is created.
     */
    public void newDataObjectInstance(int id) {
        dbHistoryDataObjectInstance.createNewDataObjectInstanceEntry(id);
    }

    /**
     * This method delegates a log entry containing a DataAttributeInstance value change being saved into the database.
     *
     * @param dataattributeinstance_id the ID of the DataAttributeInstance that is changed.
     * @param value                    the new value of the DataAttributeInstance.
     */
    public void newDataAttributeInstanceValue(int dataattributeinstance_id, Object value) {
        dbHistoryDataAttributeInstance.createEntry(dataattributeinstance_id, value);
    }

    /**
     * This method delegates a log entry of a newly created DataAttributeInstance being saved into the database.
     *
     * @param dataattributeinstance_id the ID of the DataAttributeInstance that is created.
     */
    public void newDataAttributeInstance(int dataattributeinstance_id) {
        dbHistoryDataAttributeInstance.createNewDataAttributeInstanceEntry(dataattributeinstance_id);
    }
}
