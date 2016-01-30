package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 *
 */
public class DbBoundaryEvent extends DbObject {
    public int getControlNodeAttachedToEvent(int eventNodeId, int fragmentInstanceId) {
        String getModelId = "SELECT * FROM boundaryeventref where controlnode_id = "
                + eventNodeId + ";";
        String modelId = this.executeStatementReturnsString(getModelId, "attachedtoref");
        String getControlNodeForModelId = "SELECT * FROM controlnode WHERE modelid = "
                + modelId + ";";
        int controlNodeId = this.executeStatementReturnsInt(getControlNodeForModelId,
                "controlnode_id");
        DbControlNodeInstance controlNodeInstance = new DbControlNodeInstance();
        return controlNodeInstance.getControlNodeInstanceID(controlNodeId, fragmentInstanceId);
    }
}
