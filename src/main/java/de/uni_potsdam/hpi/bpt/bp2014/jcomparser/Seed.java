package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 */
public class Seed {
	private static Logger log = Logger.getLogger(Seed.class);

	/**
	 * @param id some some id.
	 * @return Array list
	 */
	public ArrayList<Integer> getAllActivityIDByFragmentID(int id) {

		Connection conn = de.uni_potsdam.hpi.bpt.bp2014
				.database.Connection.getInstance().connect();

		Statement stmt = null;
		ResultSet rs;
		ArrayList<Integer> results = new ArrayList<>();
		if (conn == null) {
			return results;
		}

		try {
			//Execute a query
			stmt = conn.createStatement();
			String sql =
					"SELECT id FROM ProcessElement WHERE type = 'Activity' "
							+ "AND fragment_id = " + id
							+ " ORDER BY id";

			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				results.add(rs.getInt("id"));
			}

			//Clean-up environment
			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) {
			//Handle errors for JDBC
			log.error("SQL Error!:", se);
		} finally {
			//finally block used to close resources
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException ignored) {
			} // nothing we can do
			try {
				conn.close();
			} catch (SQLException se) {
				log.error("SQL Error!:", se);
			}
		}

		return results;
	}
}
