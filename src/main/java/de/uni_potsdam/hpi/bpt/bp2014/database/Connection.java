package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;
import org.apache.log4j.Logger;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used to build a connection to the database. It reads database credentials from the
 * configuration file and initializes the JDBC Driver accordingly.
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
  public static synchronized Connection getInstance() {
    if (instance == null) {
      instance = new Connection();
      instance.initializeDatabaseConfiguration();
      instance.schemaVersionOutdated();
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
      // Register JDBC driver
      Class.forName(JDBC_DRIVER);
      // Open a connection
      conn = DriverManager.getConnection(url, username, password);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      // Handle errors for Class.forName
      log.error("MySQL Connection Error:", e);
    }

    return conn;
  }

  /**
   * Checks whether a schema update is necessary. Returns {@literal true} if the schema version in
   * the database it outdated.
   */
  private Boolean schemaVersionOutdated() {
    int dbVersion;
    try {
      dbVersion = getDatabaseVersion();
    } catch (SQLException e) {
      log.error("Could not read schema version used in database. Will update the schema.", e);
      return true;
    }
    int schemaVersion;
    try {
      InputStream is = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream("schemaversion");
      String line = (new BufferedReader(new InputStreamReader(is))).readLine();
      schemaVersion = Integer.parseInt(line);
    } catch (IOException e) {
      log.error(
          "Could no read schema version used by this build of the engine (should be stored in a file called 'schemaversion').\n Will NOT update schema.",
          e);
      return false;
    }
    log.info(String.format("Version in DB: %d,\nVersion of schema: %d", dbVersion, schemaVersion));
    if (schemaVersion > dbVersion) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Reads the schema version used in the database from table 'version'. Throws a SQLException if
   * that table does not exist or is empty. Otherwise returns the version number stored in the
   * database.
   * 
   * @return int - schema version used in database
   * @throws SQLException
   */
  private int getDatabaseVersion() throws SQLException {
    java.sql.Connection conn = this.connect();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'version'");
    // if version tables not exists => update schema
    if (!rs.next()) {
      throw new SQLException("Table 'version' does not exist.");
    }
    rs = stmt.executeQuery("SELECT * FROM version");
    // if empty ResultSet => update schema
    if (!rs.next()) {
      throw new SQLException("Table 'version' is empty.");
    }
    int dbVersion = rs.getInt(0);
    conn.close();
    return dbVersion;
  }

  /**
   * Updates the schema. It drops the existing schema, then creates a new schema with the same name
   * and reads the table structure from the sql script.
   */
  private void updateSchema() {
    java.sql.Connection conn = this.connect();
    try {
      Statement stmt = conn.createStatement();
      String schemaName = PropertyLoader.getProperty("mysql.schema");
      stmt.executeQuery("DROP SCHEMA " + schemaName);
      stmt.executeQuery("CREATE SCHEMA " + schemaName);
      ScriptRunner runner = new ScriptRunner(conn, false, false);
      runner.runScript(new FileReader("JEngineV2_schema.sql"));
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private void initializeDatabaseConfiguration() {
    username = PropertyLoader.getProperty("mysql.username");
    password = PropertyLoader.getProperty("mysql.password");
    url = PropertyLoader.getProperty("mysql.url");
  }

}