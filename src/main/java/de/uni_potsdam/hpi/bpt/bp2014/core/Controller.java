package de.uni_potsdam.hpi.bpt.bp2014.core;

import de.uni_potsdam.hpi.bpt.bp2014.database.Operation;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

    public static int CreateNewRole(String rolename, String description, Integer admin_id){
        return Operation.InsertRoleRow(rolename, description, admin_id);
    }

    public static int CreateNewUser(String username, Integer role_id, String description){
        return Operation.InsertUserRow(username, role_id, description);
    }

    public static int UpdateRole(int roleID, String rolename, String description, int admin_id) {
        return Operation.UpdateRoleRow(roleID, rolename, description, admin_id);
    }

    public static int UpdateUser(int userID, String username, int role_id, String description) {
        return Operation.UpdateUserRow(userID, username, role_id, description);
    }

    public static ArrayList<HashMap<String,Object>> RetreiveItem(String type, int id) {
        return Operation.SelectSpecificRow(type, id);
    }

    public static ArrayList<HashMap<String,Object>> RetrieveAllItems(String type) {
        return Operation.SelectAllRows(type);
    }


}
