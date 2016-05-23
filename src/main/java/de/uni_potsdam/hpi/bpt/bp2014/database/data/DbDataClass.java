package de.uni_potsdam.hpi.bpt.bp2014.database.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DbDataClass extends DbObject {
    private static Logger log = Logger.getLogger(DbDataClass.class);

    public String getName(int dataClassId) {
        String sql = "SELECT * FROM dataclass WHERE id = %d;";
        sql = String.format(sql, dataClassId);
        return this.executeStatementReturnsString(sql, "name");
    }

    public List<Integer> getDataAttributes(int classId) {
        Map<Integer, List<Integer>> classToAttributeIds = getDataAttributesPerClass();
        assert classToAttributeIds.containsKey(classId) : "Invalid dataclass requested";
        return classToAttributeIds.get(classId);
    }

    public Map<Integer, List<Integer>> getDataAttributesPerClass() {
        Map<Integer, List<Integer>> classToAttributeIds = new HashMap<>();

        String sql = "SELECT * FROM dataclass as dc, dataattribute as da" +
                "WHERE dc.id = da.dataclass_id;";
        java.sql.Connection con = Connection.getInstance().connect();
        try (Statement stat = con.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            while (rs.next()) {
                int classId = rs.getInt("dataclass_id");
                if (!classToAttributeIds.containsKey(classId)) {
                    classToAttributeIds.put(classId, new ArrayList<>());
                }
                int dataattributeId = rs.getInt("da.id");
                classToAttributeIds.get(classId).add(dataattributeId);
            }
        } catch (SQLException e) {
            log.error("Error loading dataclasses", e);
        }

        return classToAttributeIds;
    }
}
