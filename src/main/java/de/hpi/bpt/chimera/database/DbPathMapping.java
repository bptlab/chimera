package de.hpi.bpt.chimera.database;

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

    /**
     * Retrieves the JSONPath expression for each attribute of a data object.
     * @param controlNodeId The id of the control node writing the data values
     *                      to the data object.
     * @return A Map linking each data attribute id to its JSONPath expression.
     */
    public Map<Integer, String> getPathsForAttributesOfControlNode(int controlNodeId) {
        String retrievePaths = String.format(
                "SELECT * FROM pathmapping WHERE "
                        + "controlnode_id = %d;", controlNodeId);
        return executeStatementReturnsMap(retrievePaths, "dataattribute_id", "jsonpath");
    }

    /**
     * Retrieves the Ids of all data attributes, that can be found in
     * the data objects ingoing to the given control node.
     */
    public List<Integer> getAttributeIdsForControlNode(int controlNodeId) {
        String getAttrs = String.format(
                "SELECT * FROM pathmapping WHERE "
                        + "controlnode_id = %d", controlNodeId);
        return executeStatementReturnsListInt(getAttrs, "dataattribute_id");
    }
}
