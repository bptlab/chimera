package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import java.sql.*;

/*
As a part of the JComparser we need to seed the parsed information's into the JEngine Database.
 */

public class Connector {

    public int insertScenarioIntoDatabase(String name) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {

            String sql = "INSERT INTO scenario (scenario.name) VALUES ('" + name + "')";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public int insertFragmentIntoDatabase(String fragmentName, int scenarioID) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {

            String sql = "INSERT INTO fragment (fragment.name, scenario_id) VALUES ('" + fragmentName + "', " + scenarioID +")";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public int insertControlNodeIntoDatabase(String label, String type, int fragmentID) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {

            String sql = "INSERT INTO controlnode (label, controlnode.type, fragment_id) VALUES ('" + label + "', '" + type + "', " + fragmentID +")";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public void insertControlFlowIntoDatabase(int controlNodeID1, int controlNodeID2, String condition) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        if (conn == null) return;
        try {

            String sql = "INSERT INTO controlflow (controlnode_id1, controlnode_id2, condition) VALUES (" + controlNodeID1 +", " + controlNodeID2 + ", '" + condition + "')";
            stmt.executeUpdate(sql);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public int insertDataObjectIntoDatabase(String name, int dataClassID, int scenarioID, int startStateID) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {

            String sql = "INSERT INTO dataobject (dataobject.name, dataclass_id, scenario_id, start_state_id) VALUES ('" + name + "', " + dataClassID +", " + scenarioID +", " + startStateID +")";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public int insertStateIntoDatabase(String name, int olc_id) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {

            String sql = "INSERT INTO state (state.name, olc_id) VALUES ('" + name + "', " + olc_id +")";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public int insertDataClassIntoDatabase(String name) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {

            String sql = "INSERT INTO dataclass (dataclass.name) VALUES ('" + name + "')";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public int insertDataNodeIntoDatabase(int scenarioID, int stateID, int dataClassID, int dataObjectID) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {

            String sql = "INSERT INTO datanode (scenario_id, state_id, dataclass_id, dataobject_id) VALUES (" + scenarioID +", " + stateID +", " + dataClassID + ", " + dataObjectID +")";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public int insertDataSetIntoDatabase(Boolean input) {
        int inputAsInt;
        if (input){
            inputAsInt = 1;
        }else{
            inputAsInt = 0;
        }
        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        ResultSet rs = null;
        if (conn == null) return -1;
        int result = -1;
        try {

            String sql = "INSERT INTO dataset (dataset) VALUES (" + inputAsInt +")";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            rs.next();
            result = rs.getInt(1);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public void insertDataSetConsistOfDataNodeIntoDatabase(int dataSetID, int dataNodeID) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        if (conn == null) return;
        try {

            String sql = "INSERT INTO datasetconsistofdatanode (dataset_id, datanode_id) VALUES (" + dataSetID +", " + dataNodeID + ")";
            stmt.executeUpdate(sql);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void insertDataFlowIntoDatabase(int controlNodeID, int dataSetID, Boolean input) {
        int inputAsInt;
        if (input){
            inputAsInt = 1;
        }else{
            inputAsInt = 0;
        }
        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        if (conn == null) return;
        try {

            String sql = "INSERT INTO dataflow (controlnode_id, dataset_id, input) VALUES (" + controlNodeID +", " + dataSetID + ", " + inputAsInt + ")";
            stmt.executeUpdate(sql);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void insertReferenceIntoDatabase(int controlNodeID1, int controlNodeID2){
        insertReferenceOnSideIntoDatabase(controlNodeID1, controlNodeID2);
        insertReferenceOnSideIntoDatabase(controlNodeID2, controlNodeID1);
    }

    private void insertReferenceOnSideIntoDatabase(int controlNodeID1, int controlNodeID2) {

        java.sql.Connection conn = de.uni_potsdam.hpi.bpt.bp2014.database.Connection.getInstance().connect();
        Statement stmt = null;
        if (conn == null) return;
        try {

            String sql = "INSERT INTO reference (controlnode_id1, controlnode_id2) VALUES (" + controlNodeID1 +", " + controlNodeID2 + ")";
            stmt.executeUpdate(sql);
            //Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }



}
