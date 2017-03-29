package de.hpi.bpt.chimera.database.data;

import de.hpi.bpt.chimera.database.ConnectionWrapper;
import de.hpi.bpt.chimera.database.DbObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database Access Object for Data Classes.
 * Used by the all classes working with data objects.
 */
public class DbDataClass extends DbObject {

	/**
	 * Retrieve the name of a data class specified via Id.
	 *
	 * @param id The data class Id.
	 * @return The name of the data class.
	 */
	public String getName(int id) {
		String sql = "SELECT * FROM dataclass WHERE id = %d;";
		sql = String.format(sql, id);
		return this.executeStatementReturnsString(sql, "name");
	}

	/**
	 * Retrieve the id of a data class, specified by the name in a given scenario.
	 *
	 * @param name       Name of the data class to be retrieved.
	 * @param scenarioId Id of the scenario containing the data class.
	 * @return Id of the data class.
	 */
	public int getId(String name, int scenarioId) {
		String sql = "SELECT dataclass.id as class_id FROM dataclass, datanode WHERE dataclass.name = '%s' " + "AND datanode.scenario_id = %d " + "AND dataclass.id = datanode.dataclass_id;";
		return this.executeStatementReturnsInt(String.format(sql, name, scenarioId), "class_id");
	}

	/**
	 * Retrieve all data attribute ids of a data class.
	 *
	 * @param classId Id of the data class.
	 * @return All data attribute ids for specified attributes in the data class.
	 */
	public List<Integer> getDataAttributes(int classId) {
		Map<Integer, List<Integer>> classToAttributeIds = getDataAttributesPerClass();
		assert classToAttributeIds.containsKey(classId) : "Invalid dataclass requested";
		return classToAttributeIds.get(classId);
	}

	/**
	 * Event types inherit from data classes. This method checks, whether a
	 * data class (specified by id) is an event type.
	 *
	 * @param classId Id of the data class.
	 * @return True, if the class is an event type. False otherwise.
	 */
	public boolean isEvent(int classId) {
		String sql = "SELECT is_event FROM dataclass WHERE id = %d;";
		return executeStatementReturnsBoolean(String.format(sql, classId), "is_event");
	}

	private Map<Integer, List<Integer>> getDataAttributesPerClass() {
		Map<Integer, List<Integer>> classToAttributeIds = new HashMap<>();
		List<Integer> dataClassIds = new DbObject().executeStatementReturnsListInt("SELECT id FROM dataclass;", "id");
		dataClassIds.forEach(id -> classToAttributeIds.put(id, new ArrayList<>()));

		String sql = "SELECT * FROM dataclass as dc, dataattribute as da " + "WHERE dc.id = da.dataclass_id;";
		java.sql.Connection con = ConnectionWrapper.getInstance().connect();
		try (Statement stat = con.createStatement(); ResultSet rs = stat.executeQuery(sql)) {
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
