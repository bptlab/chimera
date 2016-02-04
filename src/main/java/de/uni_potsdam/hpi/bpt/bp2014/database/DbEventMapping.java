package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 * This class is used by the
 * {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher} to store the
 * connection between the registration of an event at Unicorn and the corresponding event
 * control node.
 */
public class DbEventMapping extends DbObject {
    /**
     * Saves mapping between registration key and event control Node Id.
     * @param scenarioInstanceId The if of scenario instance, which contains the event
     * @param eventKey The request key which was generated when registering the event.
     * @param eventControlNodeId The Id of the event control node
     */
    public void saveMappingToDatabase(int scenarioInstanceId, String eventKey, int
            eventControlNodeId) {
        String saveMappingQuery = "INSERT into EventMapping (scenarioInstanceId, "
                + "eventKey, eventControlNode) VALUES (" + scenarioInstanceId + ", '"
                + eventKey + "', " + eventControlNodeId + ");";
        this.executeInsertStatement(saveMappingQuery);
    }

    /**
     * Searches for the event control node Id which was saved with the given key.
     * @param scenarioInstanceId The if of scenario instance, which contains the event
     * @param eventKey The request key which was generated when registering the event.
     * @return Return id of the event control node which registered.
     */
    public int getEventControlNodeId(int scenarioInstanceId, String eventKey) {
        String retrieveEventControlNodeId = "SELECT * FROM EventMapping WHERE eventKey = '"
                + eventKey + "' AND scenarioInstanceId = " + scenarioInstanceId + ";";
        return this.executeStatementReturnsInt(retrieveEventControlNodeId, "controlNodeId");
    }
}
