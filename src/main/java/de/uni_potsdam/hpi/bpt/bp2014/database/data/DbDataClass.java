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

    public String getName(int id) {
        String sql = "SELECT * FROM dataclass WHERE id = %d;";
        sql = String.format(sql, id);
        return this.executeStatementReturnsString(sql, "name");
    }

    public int getId(String name, int scenarioId) {
        String sql = "SELECT dataclass.id as class_id FROM dataclass, datanode WHERE dataclass.name = '%s' "
                + "AND datanode.scenario_id = %d "
                + "AND dataclass.id = datanode.dataclass_id;";
        return this.executeStatementReturnsInt(
                String.format(sql, name, scenarioId), "class_id");
    }

    public List<Integer> getDataAttributes(int classId) {
        Map<Integer, List<Integer>> classToAttributeIds = getDataAttributesPerClass();
        assert classToAttributeIds.containsKey(classId) : "Invalid dataclass requested";
        return classToAttributeIds.get(classId);
    }

    public boolean isEvent(int classId) {
        String sql = "SELECT is_event FROM dataclass WHERE id = %d;";
        return executeStatementReturnsBoolean(
                String.format(sql, classId),
                "is_event");
    }

    public Map<Integer, List<Integer>> getDataAttributesPerClass() {
        Map<Integer, List<Integer>> classToAttributeIds = new HashMap<>();
        List<Integer> dataClassIds = new DbObject()
                .executeStatementReturnsListInt(
                        "SELECT id FROM dataclass;", "id");
        dataClassIds.forEach(id -> classToAttributeIds.put(id, new ArrayList<>()));

        String sql = "SELECT * FROM dataclass as dc, dataattribute as da " +
                "WHERE dc.id = da.dataclass_id;";
        java.sql.Connection con = Connection.getInstance().connect();
        try (Statement stat = con.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            while (rs.next()) {
                int classId = rs.getInt("da.dataclass_id");
                int dataattributeId = rs.getInt("da.id");
                classToAttributeIds.get(classId).add(dataattributeId);
            }
        } catch (SQLException e) {
            log.error("Error loading dataclasses", e);
        }

        return classToAttributeIds;
    }
}
