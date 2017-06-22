package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;

public class DbScriptTask extends DbObject {

    public String getScriptFilePath(int controlNodeId) {
        String sql = "SELECT scripttaskfilepath FROM scripttask " + "WHERE controlnode_id = " + controlNodeId;
        return this.executeStatementReturnsString(sql, "url");
    }

}
