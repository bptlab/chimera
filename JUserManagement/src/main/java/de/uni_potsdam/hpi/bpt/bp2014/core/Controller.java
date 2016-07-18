package de.uni_potsdam.hpi.bpt.bp2014.core;

import de.uni_potsdam.hpi.bpt.bp2014.database.Operation;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

    /**
     *
     * @param rolename
     * @param description
     * @param admin_id
     * @return
     */
    public static int CreateNewRole(String rolename, String description, Integer admin_id){
        return Operation.InsertRoleRow(rolename, description, admin_id);
    }

    /**
     *
     * @param username
     * @param role_id
     * @param description
     * @return
     */
    public static int CreateNewUser(String username, Integer role_id, String description){
        return Operation.InsertUserRow(username, role_id, description);
    }

    /**
     *
     * @param roleID
     * @param rolename
     * @param description
     * @param admin_id
     * @return
     */
    public static boolean UpdateRole(int roleID, String rolename, String description, int admin_id) {
        return Operation.UpdateRoleRow(roleID, rolename, description, admin_id);
    }

    /**
     *
     * @param userID
     * @param username
     * @param role_id
     * @param description
     * @return
     */
    public static boolean UpdateUser(int userID, String username, int role_id, String description) {
        return Operation.UpdateUserRow(userID, username, role_id, description);
    }

    /**
     *
     * @param type
     * @param id
     * @return
     */
    public static ArrayList<HashMap<String,Object>> RetrieveItem(String type, int id) {
        return Operation.SelectSpecificRow(type, id);
    }

    /**
     *
     * @param type
     * @return
     */
    public static ArrayList<HashMap<String,Object>> RetrieveAllItems(String type) {
        return Operation.SelectAllRows(type);
    }

    /**
     *
     * @param id
     * @return
     */
    public static boolean UpdateUser(int id) {
        return Operation.DeleteRow("user", id);
    }

    /**
     *
     * @param id
     * @return
     */
    public static boolean UpdateRole(int id) {
        return Operation.DeleteRow("role", id);
    }

}
