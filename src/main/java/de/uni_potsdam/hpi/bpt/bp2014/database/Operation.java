package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.LinkedList;

public class Operation {

    public static LinkedList<Integer> SelectAllRows(String type) {
        String sql = "SELECT * FROM " + type;
        return Helper.executeStatementReturnsListInt(sql, "*");
    }

    public static String SelectSpecificRow(String type, int id) {
        String sql = "SELECT * FROM " + type + "WHERE id = " + id;
        return Helper.executeStatementReturnsString(sql, "*");
    }



}
