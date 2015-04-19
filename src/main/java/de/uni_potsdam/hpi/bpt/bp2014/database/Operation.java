package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Operation {

    /************************************************************************************
     Inserts
     */

    public static int InsertRoleRow(String rolename, String description, Integer admin_id) {
        String sql = "INSERT INTO `role` (`id` ,`rolename` ,`description` ,`admin_id`) VALUES (NULL , '"+rolename+"', '"+description+"', "+admin_id+");";
        return Helper.executeInsertStatement(sql);
    }

    public static int InsertUserRow(String username, Integer role_id, String description) {
        String sql = "INSERT INTO `user` (`id` ,`username` ,`role_id` ,`description`) VALUES (NULL , '"+username+"', "+role_id+", '"+description+"');";
        return Helper.executeInsertStatement(sql);
    }

    /************************************************************************************
     Update
     */

    public static int UpdateRoleRow(int id, String rolename, String description, Integer admin_id) {
        String sql = "UPDATE `role` SET rolename='"+rolename+"', description='"+description+"', admin_id="+admin_id+" WHERE id="+id+";";
        return Helper.executeInsertStatement(sql);
    }

    public static int UpdateUserRow(int id, String username, Integer role_id, String description) {
        String sql = "UPDATE `user` SET username='"+username+"', description='"+description+"', role_id="+role_id+" WHERE id="+id+";";
        return Helper.executeInsertStatement(sql);
    }


    /************************************************************************************
        SELECTs
     */
    public static LinkedList SelectAllColumnForType(String type, String column) {
        String sql = "SELECT " + column + " FROM " + type;
        return Helper.executeStatementReturnsListInt(sql, column);
    }

    public static String SelectSpecificColumnForType(String type, int id, String column) {
        String sql = "SELECT " + column + "  FROM " + type + "WHERE id = " + id;
        return Helper.executeStatementReturnsString(sql, column);
    }

    public static ArrayList<HashMap<String,Object>> SelectAllRows(String type) {
        String sql = "SELECT *  FROM " + type;
        return Helper.executeStatementReturnsHashMap(sql);
    }

    public static ArrayList<HashMap<String,Object>> SelectSpecificRow(String type, int id) {
        String sql = "SELECT * FROM " + type + " WHERE id = " + id;
        return Helper.executeStatementReturnsHashMap(sql);
    }
}
