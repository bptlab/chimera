package de.uni_potsdam.hpi.bpt.bp2014.core;

import de.uni_potsdam.hpi.bpt.bp2014.database.Operation;

import java.util.List;
import java.util.Map;

public class Controller {

    public static int createNewRole(String roleName, String description, Integer adminId){
        return Operation.insertRoleRow(roleName, description, adminId);
    }

    public static int createNewUser(String username, Integer roleId, String description){
        return Operation.insertUserRow(username, roleId, description);
    }

    public static boolean updateRole(int roleID, String rolename, String description, int adminId) {
        return Operation.updateRoleRow(roleID, rolename, description, adminId);
    }

    public static boolean updateUser(int userID, String username, int roleId, String description) {
        return Operation.updateUserRow(userID, username, roleId, description);
    }

    public static List<Map<String,Object>> retrieveItem(String type, int id) {
        return Operation.selectSpecificRow(type, id);
    }

    public static List<Map<String,Object>> retrieveAllItems(String type) {
        return Operation.selectAllRows(type);
    }

    public static boolean updateUser(int id) {
        return Operation.deleteRow("user", id);
    }

    public static boolean updateRole(int id) {
        return Operation.deleteRow("role", id);
    }

}
