package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;

public class DbScriptTask extends DbObject {

    public String getScriptTaskJar(int controlNodeId) {
        String sql = "SELECT scripttaskjar FROM scripttask " + "WHERE controlnode_id = " + controlNodeId;
        return this.executeStatementReturnsString(sql, "scripttaskjar");
    }

    public String getScriptTaskClassPath(int controlNodeId) {
        String sql = "SELECT scripttaskclasspath FROM scripttask " + "WHERE controlnode_id = " + controlNodeId;
        return this.executeStatementReturnsString(sql, "scripttaskclasspath");
    }

}
