package de.hpi.bpt.chimera.database;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.*;

/**
 * A generic Database Access Object utility, providing different
 * kinds of database requests/statements.
 */
public class DbObject {

    protected static Logger log = Logger.getLogger(DbObject.class);
    private static final String SQL_ERROR = "SQL Error: ";


    /**
     * Performs a sql insert statement.
     * This method contains an basic error handling. Resources will be closed.
     *
     * @param statement the statement to be executed.
     * @return the auto increment id of the newly created entry.
     */
    protected int performSQLInsertStatementWithAutoId(final String statement) {
        int result = -1;
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(statement, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            rs.close();
        } catch (SQLException se) {
            log.error("Error:", se);
        }
        return result;
    }

    /**
     * Executes the given select SQL statement and returns the result in List with Integer.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return List with Integer.
     */
    public List<Integer> executeStatementReturnsListInt(String sql, String columnLabel) {
        List<Integer> results = new ArrayList<>();
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(rs.getInt(columnLabel));
            }
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return results;
    }

    /**
     * Executes the given select SQL statement and returns the result in List with String.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return List with String.
     */
    public List<String> executeStatementReturnsListString(
            String sql, String columnLabel) {
        List<String> results = new ArrayList<>();
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(rs.getString(columnLabel));
            }
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return results;
    }

    /**
     * Executes the given select SQL statement and returns the result as String.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return String.
     */
    public String executeStatementReturnsString(String sql, String columnLabel) {
        String results = "";
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                results = rs.getString(columnLabel);
            }
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return results;
    }

    /**
     * Executes the given select SQL statement and returns the result as int.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return int.
     */
    public int executeStatementReturnsInt(String sql, String columnLabel) {
        int result = -1;
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                result = rs.getInt(columnLabel);
            }
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return result;
    }

    /**
     * Executes the given select SQL statement and returns the result as Obejct.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return Object.
     */
    public Object executeStatementReturnsObject(String sql, String columnLabel) {
        Object result = new Object();
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                result = rs.getObject(columnLabel);
            }
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return result;
    }

    /**
     * Executes the given select SQL statement and returns the result as boolean.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return boolean.
     */
    public boolean executeStatementReturnsBoolean(String sql, String columnLabel) {
        Boolean result = false;
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                result = rs.getBoolean(columnLabel);
            }
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return result;
    }

    /**
     * Executes the given select SQL statement.
     *
     * @param sql This is a given SQL Statement.
     * @return true if there is a result for the statement. false if not.
     */
    public boolean executeExistStatement(String sql) {
        boolean result = false;
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            result = rs.next();
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return result;
    }

    /**
     * Executes the given insert SQL statement.
     *
     * @param sql This is a given SQL Statement.
     * @return the generated key for the insert statement.
     */
    public int executeInsertStatement(String sql) {
        int result = -1;
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return result;
    }

    /**
     * Executes the given SQL Statement. Which should be a update or insert statement.
     *
     * @param sql This is a given SQL Statement.
     * @return 0.
     */
    public int executeUpdateStatement(String sql) {
        int rowId = 0;
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            rowId = stmt.executeUpdate(sql);
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return rowId;
    }

    /**
     * This method makes a sql Query and returns the keys and values in a Map.
     * We assume that every key is an integer.
     *
     * @param sql   The query string to be executed
     * @param key   The column name which will be key of the map
     * @param value The column name which will be the value of the map
     * @return A Map from the key Column to the Value Column
     */
    public Map<Integer, String> executeStatementReturnsMap(
            String sql, String key, String value) {
        Map<Integer, String> result = new HashMap<>();
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.put(rs.getInt(key), rs.getString(value));
            }
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return result;
    }

    /**
     * This method makes a sql Query and returns the keys and values in a Map.
     * We assume that every key is an integer.
     *
     * @param sql   The query string to be executed
     * @param key   The column name which will be key of the map
     * @param value The column name which will be the value of the map
     * @return A Map from the key Column to the Value Column
     */
    protected Map<Integer, Integer> executeStatementReturnsMapIntInt(
            String sql, String key, String value) {
        Map<Integer, Integer> result = new HashMap<>();
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.put(rs.getInt(key), rs.getInt(value));
            }
            rs.close();
        } catch (SQLException se) {
            log.error(SQL_ERROR, se);
        }
        return result;
    }


    /**
     * @param sql  The query string to be executed.
     * @param keys The column name which will be key of the map
     * @return a Map with IDs and Maps (with keys and values).
     */
    public Map<Integer, Map<String, Object>> executeStatementReturnsMapWithMapWithKeys(
            String sql, String... keys) {
        Map<Integer, Map<String, Object>> keysValues = new HashMap<>();
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                keysValues.put(rs.getInt("id"), new HashMap<>());
                for (String key : keys) {
                    (keysValues.get(rs.getInt("id"))).put(
                            key, rs.getObject(key));
                }
            }
            rs.close();
        } catch (SQLException e) {
            log.error(SQL_ERROR, e);
        }
        return keysValues;
    }

    /**
     * Creates a Map for the SQL-Result.
     * Each Column will be added as a key and the first result as
     * the result.
     *
     * @param sql  The SQl query to be executed.
     * @param keys The column names.
     * @return A Map of the results.
     */
    protected Map<String, Object> executeStatementReturnsMapWithKeys(
            String sql, String... keys) {
        Map<String, Object> keysValues = new HashMap<>();
        try (Connection conn = ConnectionWrapper.getInstance().connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                for (String key : keys) {
                    keysValues.put(key, rs.getObject(key));
                }
            }
            rs.close();
        } catch (SQLException e) {
            log.error(SQL_ERROR, e);
        }
        return keysValues;
    }
}
