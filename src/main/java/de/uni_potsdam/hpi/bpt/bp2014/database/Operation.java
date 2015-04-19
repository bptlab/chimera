package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Operation {

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
        String sql = "SELECT *  FROM " + type + "WHERE id = " + id;
        return Helper.executeStatementReturnsHashMap(sql);
    }
}
