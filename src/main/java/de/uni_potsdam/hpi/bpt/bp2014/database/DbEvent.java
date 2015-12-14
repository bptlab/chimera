package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 *
 */
public class DbEvent extends DbObject{
    /**
     *
     * @return Searches Event in the database and returns control Node Id
     */
    public int getControlNodeIdForEvent(String id, int fragmentId) {
        String sql = "SELECT * FROM event WHERE model_id = '" + id + "';";
        int controlNodeId = this.executeStatementReturnsInt(sql, "controlnode_id");

        return 0;
    }
}
