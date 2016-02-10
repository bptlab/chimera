package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;
import org.apache.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used to build a connection to the database.
 * It initializes the JDBC Driver with the needed configuration to connect to the database.
 */
public final class Connection {
	private static Connection instance = null;
	private static String username;
	private static String password;
	private static String url;
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static Logger log = Logger.getLogger(Connection.class.getName());

	/**
	 * constructor
	 */
	private Connection() {
	}

	/**
	 * This method builds a connection for the database with a default path.
	 *
	 * @return the instance of a new Connection.
	 */
	public static Connection getInstance() {
		if (instance == null) {
			instance = new Connection();
			instance.initializeDatabaseConfiguration();
		}
		return instance;
	}

	/**
	 * This connection is used to connect to the database.
	 *
	 * @return the open connection.
	 */
	public java.sql.Connection connect() {
		java.sql.Connection conn = null;


		try {
			//Register JDBC driver
			Class.forName(JDBC_DRIVER);
			//Open a connection
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			//Handle errors for Class.forName
			log.error("MySQL Connection Error:", e);
		}

		return conn;
	}

	private void initializeDatabaseConfiguration() {
		username = PropertyLoader.getProperty("mysql.username");
		password = PropertyLoader.getProperty("mysql.password");
		url = PropertyLoader.getProperty("mysql.url");
	}

}