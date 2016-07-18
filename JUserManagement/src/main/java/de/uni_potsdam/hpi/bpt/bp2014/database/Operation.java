package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.*;

public class Operation {

    /************************************************************************************
     Inserts
     */

    public static int insertRoleRow(String roleName, String description, Integer adminId) {
        String sql = "INSERT INTO `role` (`id` ,`rolename` ,`description` ,`admin_id`) VALUES (NULL , '"+roleName+"', '"+description+"', "+adminId+");";
        return Helper.executeInsertStatement(sql);
    }

    public static int insertUserRow(String userName, Integer roleId, String description) {
        String sql = "INSERT INTO `user` (`id` ,`username` ,`role_id` ,`description`) VALUES (NULL , '"+userName+"', "+roleId+", '"+description+"');";
        return Helper.executeInsertStatement(sql);
    }

    /************************************************************************************
     Update
     */

    public static boolean updateRoleRow(int id, String roleName, String description, Integer adminId) {
        String sql = "UPDATE `role` SET `rolename` = '"+roleName+"', `description` = '"+description+"', `admin_id` = "+adminId+" WHERE id="+id+";";
        return Helper.executeUpdateStatement(sql);
    }

    public static boolean updateUserRow(int id, String userName, Integer roleId, String description) {
        String sql = "UPDATE `user` SET `username` = '"+userName+"', `description` = '"+description+"', `role_id` = "+roleId+" WHERE id="+id+";";
        return Helper.executeUpdateStatement(sql);
    }


    /************************************************************************************
        SELECTs
     */

    public static List selectAllColumnForType(String type, String column) {
        String sql = "SELECT " + column + " FROM " + type;
        return Helper.executeStatementReturnsListInt(sql, column);
    }

    public static String selectSpecificColumnForType(String type, int id, String column) {
        String sql = "SELECT " + column + "  FROM " + type + "WHERE id = " + id;
        return Helper.executeStatementReturnsString(sql, column);
    }

    public static List<Map<String,Object>> selectAllRows(String type) {
        String sql = "SELECT *  FROM " + type;
        return Helper.executeStatementReturnsMap(sql);
    }

    public static List<Map<String,Object>> selectSpecificRow(String type, int id) {
        String sql = "SELECT * FROM " + type + " WHERE id = " + id;
        return Helper.executeStatementReturnsMap(sql);
    }

    /************************************************************************************
     DELETEs
     */

    public static boolean deleteRow(String type, int id) {
        String sql = "DELETE FROM "+type+" WHERE id="+id+";";
        return Helper.executeUpdateStatement(sql);
    }
}
