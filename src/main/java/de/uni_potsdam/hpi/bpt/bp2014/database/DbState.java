package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 * Creates and executes a sql statement to get the name of a corresponding state ID.
 */
public class DbState extends DbObject {
    /**
     * This creates and executes a sql statement to get the state name of a dataObject.
     *
     * @param id This is the database ID of a state.
     * @return the name of the state as a String.
     */
    public String getStateName(int id) {
        String sql = "SELECT name FROM state WHERE id = " + id;
        return this.executeStatementReturnsString(sql, "name");
    }
}
