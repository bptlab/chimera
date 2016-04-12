package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.List;
import java.util.Map;

/**
 * Class for database access to the mapping from
 * data objects and their attributes
 * to a specified json path.
 *
 * This is used for events and webservice tasks, for specifying
 * which returned value should be saved into the data attribute.
 */
public class DbPathMapping extends DbObject {

    public String getPathForNodeAndAttribute(int controlNodeId, int dataAttributeId) {
        String retrievePath = String.format(
                "SELECT * FROM pathmapping WHERE "
                        + "controlnode_id = %d "
                        + "AND dataattribute_id = %d;", controlNodeId, dataAttributeId);
        return executeStatementReturnsString(retrievePath, "jsonpath");
    }

    public Map<Integer, String> getPathsForAttributesOfControlNode(int controlNodeId) {
        String retrievePaths = String.format(
                "SELECT * FROM pathmapping WHERE "
                        + "controlnode_id = %d;", controlNodeId);
        return executeStatementReturnsMap(retrievePaths, "dataattribute_id", "jsonpath");
    }

    public List<Integer> getAttributeIdsForControlNode(int controlNodeId) {
        String getAttrs = String.format(
                "SELECT * FROM pathmapping WHERE "
                        + "controlnode_id = %d", controlNodeId);
        return executeStatementReturnsListInt(getAttrs, "dataattribute_id");
    }
}
