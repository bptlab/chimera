package de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractEvent;

import java.util.List;
import java.util.UUID;

/**
 * This class is used by the
 * {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher} to store the
 * connection between the registration of an event at Unicorn and the corresponding event
 * control node.
 */
public class DbEventMapping extends DbObject {
    /**
     * Saves mapping between registration key and event control Node Id.
     * @param fragmentInstanceId The if of fragment instance, which contains the event
     * @param eventKey The request key which was generated when registering the event.
     * @param eventControlNodeId The Id of the event control node
     */
    public void saveMappingToDatabase(int fragmentInstanceId, String eventKey, int
            eventControlNodeId, String notificationRuleId) {
        String saveMappingQuery = "INSERT into eventmapping (fragmentInstanceId, "
                + "eventKey, eventControlNodeId, notificationRuleId) VALUES (" + fragmentInstanceId + ", '"
                + eventKey + "', " + eventControlNodeId + ", '" + notificationRuleId + "');";
        this.executeInsertStatement(saveMappingQuery);
    }

    /**
     * Searches for the event control node Id which was saved with the given key.
     * @param eventKey The request key which was generated when registering the event.
     * @return Return id of the event control node which registered.
     */
    public int getEventControlNodeId(String eventKey) {
        String retrieveEventControlNodeId = "SELECT * FROM eventmapping WHERE eventKey = '"
                + eventKey + "' ;";
        return this.executeStatementReturnsInt(retrieveEventControlNodeId, "eventcontrolnodeid");
    }

    /**
     * Searches for the fragment instance Id with was saved with the given key.
     * @param eventKey The request key which was generated when registering the event.
     * @return Return id of the event control node which registered.
     */
    public int getFragmentInstanceId(String eventKey) {
        String retrieveFragmentInstanceId = "SELECT * FROM eventmapping WHERE eventkey = '"
                + eventKey + "';";
        return this.executeStatementReturnsInt(retrieveFragmentInstanceId, "fragmentInstanceId");
    }

    public List<Integer> getRegisteredEventsForFragment(int fragmentInstanceId) {
        String retrieveEventControlNodeId = "SELECT * FROM eventmapping WHERE "
                + "fragmentInstanceId = " + fragmentInstanceId + ";";
        return this.executeStatementReturnsListInt(retrieveEventControlNodeId, "eventcontrolnodeid");
    }

    public List<String> getRequestKeysForFragment(int fragmentInstanceId) {
        String retrieveRequestKeys = "SELECT * FROM eventmapping WHERE "
                + "fragmentInstanceId = " + fragmentInstanceId + ";";
        return this.executeStatementReturnsListString(retrieveRequestKeys, "eventkey");
    }

    public void removeEventMapping(int fragmentInstanceId, int eventControlNodeId) {
        String deleteMapping = "DELETE FROM eventmapping WHERE "
                + "fragmentInstanceId = " + fragmentInstanceId + " AND eventcontrolnodeid = "
                + eventControlNodeId + " ;";
        this.executeUpdateStatement(deleteMapping);
    }


    public void saveAlternativeEvents(List<AbstractEvent> events) {
        if (events.size() == 0) {
            return;
        }
        final String mappingId = UUID.randomUUID().toString().replaceAll("\\-", "");
        int fragementInstanceId = events.get(0).getFragmentInstanceId();
        int scenarioInstanceId = events.get(0).getScenarioInstance().getId();
        String insertMapping = "INSERT INTO ExclusiveEvents (MappingKey, FragmentInstanceId,"
                + " ScenarioInstanceId, EventControlNodeId) Values ('%s', %d, %d, %d);";
        for (AbstractEvent event : events) {
            String insertEvent = String.format(insertMapping, mappingId, fragementInstanceId,
                    scenarioInstanceId, event.getControlNodeId());
            this.executeInsertStatement(insertEvent);
        }
    }

    public boolean isAlternativeEvent(AbstractEvent event) {
        String mappingKey = getMappingKeyForEvent(event);
        return !"".equals(mappingKey);
    }

    public List<Integer> getAlternativeEventsIds(AbstractEvent event) {
        String mappingKey = getMappingKeyForEvent(event);
        return getEventIdsForMappingKey(mappingKey);
    }

    private String getMappingKeyForEvent(AbstractEvent event) {
        int fragmentInstanceId = event.getFragmentInstanceId();
        int scenarioInstanceId = event.getScenarioInstance().getId();
        int eventControlNodeId = event.getControlNodeId();
        String retrieveMappingKey = "SELECT * FROM ExclusiveEvents "
                + "WHERE FragmentInstanceId = '%s'"
                + "AND ScenarioInstanceId = %d AND EventControlNodeId = %d;";
        String retrieveAlternativeEventsQuery = String.format(retrieveMappingKey,
                fragmentInstanceId, scenarioInstanceId, eventControlNodeId);
        return this.executeStatementReturnsString(retrieveAlternativeEventsQuery,
                "MappingKey");
    }

    public List<Integer> getEventIdsForMappingKey(String mappingKey) {
        String checkExclusiveEvents = "SELECT * FROM ExclusiveEvents WHERE MappingKey = '%s';";
        List<Integer> alternativeEvents = this.executeStatementReturnsListInt(
                String.format(checkExclusiveEvents, mappingKey), "EventControlNodeId");
        return alternativeEvents;
    }

    public String getNotificationRuleId(int eventControlNodeId) {
        String findNotificationRuleId = "SELECT * FROM eventmapping WHERE eventControlNodeId = "
                + eventControlNodeId + ";";
        return this.executeStatementReturnsString(findNotificationRuleId, "notificationRuleId");
    }

}
