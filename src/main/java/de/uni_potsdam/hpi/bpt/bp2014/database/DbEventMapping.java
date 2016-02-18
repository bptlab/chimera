package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.List;

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
            eventControlNodeId) {
        String saveMappingQuery = "INSERT into eventmapping (fragmentInstanceId, "
                + "eventKey, eventcontrolnodeid) VALUES (" + fragmentInstanceId + ", '"
                + eventKey + "', " + eventControlNodeId + ");";
        this.executeInsertStatement(saveMappingQuery);
    }

    /**
     * Searches for the event control node Id which was saved with the given key.
     * @param fragmentInstanceId The if of scenario instance, which contains the event
     * @param eventKey The request key which was generated when registering the event.
     * @return Return id of the event control node which registered.
     */
    public int getEventControlNodeId(int fragmentInstanceId, String eventKey) {
        String retrieveEventControlNodeId = "SELECT * FROM eventmapping WHERE eventKey = '"
                + eventKey + "' AND fragmentInstanceId = " + fragmentInstanceId + ";";
        return this.executeStatementReturnsInt(retrieveEventControlNodeId, "eventcontrolnodeid");
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
}
