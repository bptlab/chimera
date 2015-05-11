package de.uni_potsdam.hpi.bpt.bp2014.database;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class DbObject {
    static Logger log = Logger.getLogger(DbObject.class.getName());

    /**
     * @param sql
     * @return
     */
    public static ArrayList<HashMap<String, Object>> executeStatementReturnsHashMap(String sql) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        try {
            //Execute a query
            stmt = conn.createStatement();

            //query
            ResultSet Result = null;
            boolean Returning_Rows = stmt.execute(sql);
            if (Returning_Rows)
                Result = stmt.getResultSet();
            else
                return new ArrayList<HashMap<String, Object>>();

            //get metadata
            ResultSetMetaData Meta = null;
            Meta = Result.getMetaData();

            //get column names
            int Col_Count = Meta.getColumnCount();
            ArrayList<String> Cols = new ArrayList<String>();
            for (int Index = 1; Index <= Col_Count; Index++)
                Cols.add(Meta.getColumnName(Index));

            //fetch out rows
            ArrayList<HashMap<String, Object>> Rows =
                    new ArrayList<HashMap<String, Object>>();

            while (Result.next()) {
                HashMap<String, Object> Row = new HashMap<String, Object>();
                for (String Col_Name : Cols) {
                    Object Val = Result.getObject(Col_Name);
                    Row.put(Col_Name, Val);
                }
                Rows.add(Row);
            }

            //close statement
            stmt.close();

            //pass back rows
            return Rows;
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
        }
        return null;
    }

    /**
     * Executes the given select SQL statement and returns the result in List with Integer.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return List with Integer.
     */
    public LinkedList<Integer> executeStatementReturnsListInt(String sql, String columnLabel) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Integer> results = new LinkedList<Integer>();
        if (conn == null) {
            return results;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(rs.getInt(columnLabel));
            }
            //Clean-up environment
            rs.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
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
    public LinkedList<String> executeStatementReturnsListString(String sql, String columnLabel) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<String> results = new LinkedList<>();
        if (conn == null) {
            return results;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(rs.getString(columnLabel));
            }
            //Clean-up environment
            rs.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
        }
        return results;
    }

    /**
     * Executes the given select SQL statement and returns the result in list with Long.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return List with Long.
     */
    public LinkedList<Long> executeStatementReturnsListLong(String sql, String columnLabel) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<Long> results = new LinkedList<Long>();
        if (conn == null) {
            return results;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.add(rs.getLong(columnLabel));
            }
            //Clean-up environment
            rs.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
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
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        String results = "";
        if (conn == null) {
            return results;
        }
        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                results = rs.getString(columnLabel);
            }
            //Clean-up environment
            rs.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
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
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        int results = -1;
        if (conn == null) {
            return results;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                results = rs.getInt(columnLabel);
            }
            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
        }
        return results;
    }

    /**
     * Executes the given select SQL statement and returns the result as Obejct.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return Object.
     */
    public Object executeStatementReturnsObject(String sql, String columnLabel) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        Object results = -1;
        if (conn == null) {
            return results;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                results = rs.getObject(columnLabel);
            }
            //Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
        }
        return results;
    }

    /**
     * Executes the given select SQL statement and returns the result as boolean.
     *
     * @param sql         This is a given select SQL Statement.
     * @param columnLabel This is the label of the column which is used as the result.
     * @return boolean.
     */

    public boolean executeStatementReturnsBoolean(String sql, String columnLabel) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean results = false;
        if (conn == null) {
            return results;
        }
        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                results = rs.getBoolean(columnLabel);
            }
            //Clean-up environment
            rs.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
        }
        return results;
    }

    /**
     * Executes the given select SQL statement.
     *
     * @param sql This is a given SQL Statement.
     * @return true if there is a result for the statement. false if not.
     */
    public boolean executeExistStatement(String sql) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) {
            return false;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return true;
            }
            //Clean-up environment
            rs.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
        }
        return false;
    }

    /**
     * Executes the given insert SQL statement.
     *
     * @param sql This is a given SQL Statement.
     * @return the generated key for the insert statement.
     */
    public int executeInsertStatement(String sql) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) {
            return -1;
        }
        int result = -1;
        try {
            //Execute a query
            stmt = conn.createStatement();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
        }
        return result;
    }

    /**
     * Executes the given SQL Statement. Which should be a update or insert statement.
     *
     * @param sql This is a given SQL Statement.
     */
    public int executeUpdateStatement(String sql) {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        int rowId = 0;
        ResultSet rs = null;
        if (conn == null) {
            return rowId;
        }
        try {
            //Execute a querystmt = conn.createStatement();
            stmt = conn.createStatement();
            rowId = stmt.executeUpdate(sql);
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                log.error("SQL Error!: ", se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error("SQL Error!: ", se);
            }
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
    public Map<Integer, String> executeStatementReturnsMap(String sql, String key, String value) {
        Map<Integer, String> result = new LinkedHashMap<>();

        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) {
            return result;
        }

        try {
            //Execute a query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.put(rs.getInt(key), rs.getString(value));
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            log.error("SQL Error!: ", se);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
        }
        return result;
    }

    public Map<Integer, Map<String, Object>> executeStatementReturnsMapWithMapWithKeys(String sql, String... keys) {
        java.sql.Connection conn = Connection.getInstance().connect();
        ResultSet results = null;
        Map<Integer, Map<String, Object>> keysValues = new HashMap<>();
        try {
            results = conn.prepareStatement(sql).executeQuery();
            while (results.next()) {
                keysValues.put(results.getInt("id"), new HashMap<String, Object>());
                for (String key : keys) {
                    (keysValues.get(results.getInt("id"))).put(key, results.getObject(key));
                }
            }
        } catch (SQLException e) {
            log.error("SQL Error!: ", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
            try {
                results.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
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
    protected Map<String, Object> executeStatementReturnsMapWithKeys(String sql, String... keys) {
        java.sql.Connection conn = Connection.getInstance().connect();
        ResultSet results = null;
        Map<String, Object> keysValues = new LinkedHashMap<>();
        try {
            results = conn.prepareStatement(sql).executeQuery();
            if (results.next()) {
                for (String key : keys) {
                    keysValues.put(key, results.getObject(key));
                }
            }
        } catch (SQLException e) {
            log.error("SQL Error!: ", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
            try {
                results.close();
            } catch (SQLException e) {
                log.error("SQL Error!: ", e);
            }
        }
        return keysValues;
    }
}
