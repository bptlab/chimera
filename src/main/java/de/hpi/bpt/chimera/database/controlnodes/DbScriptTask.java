package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;

/**
 * Represents the instance of a ScriptTask in the database.
 * Used in {@link de.hpi.bpt.chimera.jcore.executionbehaviors.ScriptTaskExecutionBehavior} to retrieve
 * data related to this instance.
 */
public class DbScriptTask extends DbObject {

    /**
     * Returns the relative path to the jar file which contains the script.
     *
     * @param controlNodeId The controlnode_id of the script task.
     * @return a String with the relative path.
     */
    public String getScriptTaskJar(int controlNodeId) {
        String sql = "SELECT scripttaskjar FROM scripttask " + "WHERE controlnode_id = " + controlNodeId;
        return this.executeStatementReturnsString(sql, "scripttaskjar");
    }

    /**
     * Updates the relative path to the jar file
     *
     * @param controlNodeId The controlnode_id of the script task.
     * @param scripttaskjar The new relative path to the jar file
     */
    public void updateScriptTaskJar(int controlNodeId, String scripttaskjar) {
        String sql = "UPDATE scripttask " + "SET scripttaskjar = '" + scripttaskjar + "' WHERE controlnode_id = " + controlNodeId;
        executeUpdateStatement(sql);
    }

    /**
     * Returns the classpath to the executable java file inside the jar.
     *
     * @param controlNodeId The controlnode_id of the script task.
     * @return a String with the classpath.
     */
    public String getScriptTaskClassPath(int controlNodeId) {
        String sql = "SELECT scripttaskclasspath FROM scripttask " + "WHERE controlnode_id = " + controlNodeId;
        return this.executeStatementReturnsString(sql, "scripttaskclasspath");
    }

    /**
     * Updates the classpath for the given script task.
     *
     * @param controlNodeId         The controlnode_id of the script task.
     * @param scripttaskclasspath   The new classpath to the executable java file inside the jar
     */
    public void updateScriptTaskClassPath(int controlNodeId, String scripttaskclasspath) {
        String sql = "UPDATE scripttask " + "SET scripttaskclasspath = '" + scripttaskclasspath + "' WHERE controlnode_id = " + controlNodeId;
        executeUpdateStatement(sql);
    }

}
