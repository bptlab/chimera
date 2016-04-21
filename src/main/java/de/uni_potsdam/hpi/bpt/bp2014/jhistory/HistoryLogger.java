package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryActivityTransition;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryDataAttributeTransition;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryDataObjectTransition;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryEvent;

/**
 * This class provides an abstraction layer for logging,
 * so that the actually logic can be put to the database.
 */
public class HistoryLogger {
	/**
	 * Database Connection objects.
	 */
	private DbHistoryActivityTransition dbHistoryActivityTransition =
			new DbHistoryActivityTransition();
	private DbHistoryDataObjectTransition dbHistoryDataObjectTransition =
			new DbHistoryDataObjectTransition();
	private DbHistoryDataAttributeTransition dbHistoryDataAttributeInstance =
			new DbHistoryDataAttributeTransition();

	public int logActivityTransition(int activityId, String newState, int scenarioInstanceId) {
        return dbHistoryActivityTransition.logActivityStateTransition(
                activityId, newState, scenarioInstanceId);
	}

	/**
	 * This method delegates a log entry of a newly created ActivityInstance
	 * being saved into the database.
	 *
	 * @param instanceId the ID of the ActivityInstance that is created.
	 */
	public void logActivityCreation(int instanceId) {
		dbHistoryActivityTransition.logActivityCreation(instanceId);
	}

    public void logEventRegistration(int eventInstanceId, int scenarioInstanceid) {
        new DbHistoryEvent().logEventRegistration(eventInstanceId, scenarioInstanceid);
    }


    public void logEventReceiving(int eventInstanceId, int scenarioInstanceid) {
        new DbHistoryEvent().logEventReceiving(eventInstanceId, scenarioInstanceid);
    }
	/**
	 * This method delegates a log entry containing a DataObjectInstance state transition
	 * being saved into the database.
	 *
	 * @param objectInstanceId the ID of the DataObjectInstance that is changed.
	 * @param stateId           the new state of the DataObjectInstance.
	 */
	public void logDataobjectStateTransition(
            int objectInstanceId, int stateId, int activityInstanceId) {
		dbHistoryDataObjectTransition.logStateTransition(
                objectInstanceId, stateId, activityInstanceId);
	}

	/**
	 * This method delegates a log entry of a newly created DataObjectInstance
	 * being saved into the database.
	 *
	 * @param instanceId the ID of the DataObjectInstance that is created.
	 */
	public void logDataObjectCreation(int instanceId) {
		dbHistoryDataObjectTransition.logDataObjectCreation(instanceId);
	}

	/**
	 * This method delegates a log entry containing a DataAttributeInstance value change
	 * being saved into the database.
	 *
	 * @param dataAttributeInstanceId the ID of the DataAttributeInstance that is changed.
	 * @param value                    the new value of the DataAttributeInstance.
	 * @param nodeInstanceId the id of the control node which caused the log entry.
     */
	public void logDataAttributeTransition(
            int dataAttributeInstanceId, Object value, int nodeInstanceId) {
		dbHistoryDataAttributeInstance.logDataAttributeTransition(
                dataAttributeInstanceId, value, nodeInstanceId);
	}

	/**
	 * This method delegates a log entry of a newly created DataAttributeInstance
	 * being saved into the database.
	 *
	 * @param dataAttributeInstanceId the ID of the DataAttributeInstance that is created.
	 */
	public void logDataAttributeCreation(int dataAttributeInstanceId) {
		dbHistoryDataAttributeInstance.logDataattributeCreation(dataAttributeInstanceId);
	}
}
