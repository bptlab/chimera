package de.hpi.bpt.chimera.database.controlnodes.events;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

import java.util.List;
import java.util.UUID;

/**
 * This class is used by the {@link EventDispatcher} to store the
 * connection between the registration of an event in Unicorn
 * and the corresponding event control node.
 */
public class DbEventMapping extends DbObject {
	/**
	 * Saves mapping between registration key and event control node Id.
	 *
	 * @param fragmentInstanceId The id of fragment instance containing the event node.
	 * @param eventKey           The request key which was generated when registering the event.
	 * @param eventControlNodeId The Id of the event control node.
	 */
	public void saveMappingToDatabase(int fragmentInstanceId, String eventKey, int eventControlNodeId, String notificationRuleId) {
		String saveMappingQuery = "INSERT into eventmapping (fragmentInstanceId, " + "eventKey, eventControlNodeId, notificationRuleId) VALUES (" + fragmentInstanceId + ", '" + eventKey + "', " + eventControlNodeId + ", '" + notificationRuleId + "');";
		this.executeInsertStatement(saveMappingQuery);
	}

	/**
	 * Searches for the event control node Id which was saved with the given key.
	 *
	 * @param eventKey The request key which was generated when registering the event.
	 * @return The Id of the event control node which registered.
	 */
	public int getEventControlNodeId(String eventKey) {
		String retrieveEventControlNodeId = "SELECT * FROM eventmapping WHERE eventKey = '" + eventKey + "' ;";
		return this.executeStatementReturnsInt(retrieveEventControlNodeId, "eventcontrolnodeid");
	}

	/**
	 * Searches for the fragment instance Id with was saved with the given key.
	 *
	 * @param eventKey The request key which was generated when registering the event.
	 * @return The Id of the event control node which registered.
	 */
	public int getFragmentInstanceId(String eventKey) {
		String retrieveFragmentInstanceId = "SELECT * FROM eventmapping WHERE eventkey = '" + eventKey + "';";
		return this.executeStatementReturnsInt(retrieveFragmentInstanceId, "fragmentInstanceId");
	}

	/**
	 * Retrieves all event control node ids for a given fragment instance.
	 *
	 * @param fragmentInstanceId Id of the fragment instance.
	 * @return A List of event control node ids.
	 */
	public List<Integer> getRegisteredEventsForFragment(int fragmentInstanceId) {
		String retrieveEventControlNodeId = "SELECT * FROM eventmapping WHERE " + "fragmentInstanceId = " + fragmentInstanceId + ";";
		return this.executeStatementReturnsListInt(retrieveEventControlNodeId, "eventcontrolnodeid");
	}

	/**
	 * Retrieves all event keys for a given fragment instance.
	 *
	 * @param fragmentInstanceId Id of the fragment instance.
	 * @return A List of event request keys.
	 */
	public List<String> getRequestKeysForFragment(int fragmentInstanceId) {
		String retrieveRequestKeys = "SELECT * FROM eventmapping WHERE " + "fragmentInstanceId = " + fragmentInstanceId + ";";
		return this.executeStatementReturnsListString(retrieveRequestKeys, "eventkey");
	}

	/**
	 * Removes a mapping of event node to request from the database.
	 *
	 * @param fragmentInstanceId Id of the fragment instance that contains the event node.
	 * @param eventControlNodeId Id of the event node.
	 */
	public void removeEventMapping(int fragmentInstanceId, int eventControlNodeId) {
		String deleteMapping = "DELETE FROM eventmapping WHERE " + "fragmentInstanceId = " + fragmentInstanceId + " AND eventcontrolnodeid = " + eventControlNodeId + " ;";
		this.executeUpdateStatement(deleteMapping);
	}


	/**
	 * Saves events coming after an event based gateway as exclusive to each other.
	 *
	 * @param events A list of alternative events.
	 */
	public void saveAlternativeEvents(List<AbstractEvent> events) {
		if (events.size() == 0) {
			return;
		}
		final String mappingId = UUID.randomUUID().toString().replaceAll("\\-", "");
		int fragementInstanceId = events.get(0).getFragmentInstanceId();
		int scenarioInstanceId = events.get(0).getScenarioInstance().getId();
		String insertMapping = "INSERT INTO ExclusiveEvents (MappingKey, FragmentInstanceId," + " ScenarioInstanceId, EventControlNodeId) Values ('%s', %d, %d, %d);";
		for (AbstractEvent event : events) {
			String insertEvent = String.format(insertMapping, mappingId, fragementInstanceId, scenarioInstanceId, event.getControlNodeId());
			this.executeInsertStatement(insertEvent);
		}
	}

	/**
	 * Checks if there are events exclusive to a given event, coming after an event
	 * based gateway.
	 *
	 * @param event The event to check.
	 * @return Whether the event comes after an event based gateway.
	 */
	public boolean isAlternativeEvent(AbstractEvent event) {
		String mappingKey = getMappingKeyForEvent(event);
		return !"".equals(mappingKey);
	}

	/**
	 * Retrieves the event control node ids for events exclusive to a given event.
	 *
	 * @param event The event to check.
	 * @return Ids of all events exclusive to the given one.
	 */
	public List<Integer> getAlternativeEventsIds(AbstractEvent event) {
		String mappingKey = getMappingKeyForEvent(event);
		return getEventIdsForMappingKey(mappingKey);
	}

	/**
	 * Retrieves the Id of the notification rule for a given event node id.
	 *
	 * @param eventControlNodeId Id of the event control node.
	 * @return The Id of the notification rule in unicorn, that is mapped to this event node.
	 */
	public String getNotificationRuleId(int eventControlNodeId) {
		String findNotificationRuleId = "SELECT * FROM eventmapping WHERE eventControlNodeId = " + eventControlNodeId + ";";
		return this.executeStatementReturnsString(findNotificationRuleId, "notificationRuleId");
	}

	private String getMappingKeyForEvent(AbstractEvent event) {
		int fragmentInstanceId = event.getFragmentInstanceId();
		int scenarioInstanceId = event.getScenarioInstance().getId();
		int eventControlNodeId = event.getControlNodeId();
		String retrieveMappingKey = "SELECT * FROM ExclusiveEvents " + "WHERE FragmentInstanceId = '%s'" + "AND ScenarioInstanceId = %d AND EventControlNodeId = %d;";
		String retrieveAlternativeEventsQuery = String.format(retrieveMappingKey, fragmentInstanceId, scenarioInstanceId, eventControlNodeId);
		return this.executeStatementReturnsString(retrieveAlternativeEventsQuery, "MappingKey");
	}

	private List<Integer> getEventIdsForMappingKey(String mappingKey) {
		String checkExclusiveEvents = "SELECT * FROM ExclusiveEvents WHERE MappingKey = '%s';";
		return this.executeStatementReturnsListInt(String.format(checkExclusiveEvents, mappingKey), "EventControlNodeId");
	}

}
