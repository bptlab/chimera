package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryActivityTransition;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryDataAttributeTransition;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbHistoryDataObjectTransition;

/**
 * This class provides an abstraction layer for logging,
 * so that the actually logic can be put to the database.
 */
public class Log {
	/**
	 * Database Connection objects.
	 */
	private DbHistoryActivityTransition dbHistoryActivityTransition =
			new DbHistoryActivityTransition();
	private DbHistoryDataObjectTransition dbHistoryDataObjectTransition =
			new DbHistoryDataObjectTransition();
	private DbHistoryDataAttributeTransition dbHistoryDataAttributeInstance =
			new DbHistoryDataAttributeTransition();

	/**
	 * This method delegates a log entry containing an activity state transition
	 * being saved into the database.
	 *
	 * @param id    the ID of the ActivityInstance that is changed.
	 * @param state the new state of the ActivityInstance.
	 */
	public void logStateTransition(int id, String state) {
		dbHistoryActivityTransition.createEntry(id, state);
	}

	/**
	 * This method delegates a log entry of a newly created ActivityInstance
	 * being saved into the database.
	 *
	 * @param id the ID of the ActivityInstance that is created.
	 */
	public void logActivityCreation(int id) {
		dbHistoryActivityTransition.createNewActivityEntry(id);
	}

	/**
	 * This method delegates a log entry containing a DataObjectInstance state transition
	 * being saved into the database.
	 *
	 * @param objectInstanceId the ID of the DataObjectInstance that is changed.
	 * @param stateId           the new state of the DataObjectInstance.
	 */
	public void logDataobjectStateTransition(int objectInstanceId, int stateId) {
		dbHistoryDataObjectTransition.createEntry(objectInstanceId, stateId);
	}

	/**
	 * This method delegates a log entry of a newly created DataObjectInstance
	 * being saved into the database.
	 *
	 * @param id the ID of the DataObjectInstance that is created.
	 */
	public void logDataObjectCreation(int id) {
		dbHistoryDataObjectTransition.createNewDataObjectInstanceEntry(id);
	}

	/**
	 * This method delegates a log entry containing a DataAttributeInstance value change
	 * being saved into the database.
	 *
	 * @param dataAttributeInstanceId the ID of the DataAttributeInstance that is changed.
	 * @param value                    the new value of the DataAttributeInstance.
	 */
	public void logDataAttributeTransition(int dataAttributeInstanceId, Object value) {
		dbHistoryDataAttributeInstance.createEntry(dataAttributeInstanceId, value);
	}

	/**
	 * This method delegates a log entry of a newly created DataAttributeInstance
	 * being saved into the database.
	 *
	 * @param dataAttributeInstanceId the ID of the DataAttributeInstance that is created.
	 */
	public void logDataAttributeCreation(int dataAttributeInstanceId) {
		dbHistoryDataAttributeInstance
				.createNewDataAttributeInstanceEntry(dataAttributeInstanceId);
	}
}
