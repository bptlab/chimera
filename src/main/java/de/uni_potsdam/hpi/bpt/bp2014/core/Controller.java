package de.uni_potsdam.hpi.bpt.bp2014.core;

import de.uni_potsdam.hpi.bpt.bp2014.database.Operation;

import java.util.LinkedList;

public class Controller {

    public static String RetreiveItem(String type, int id) {
        return Operation.SelectSpecificRow(type, id);
    }

    public static LinkedList<Integer> RetreiveAllItems(String type) {
        return Operation.SelectAllRows(type);
    }

}
