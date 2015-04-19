package de.uni_potsdam.hpi.bpt.bp2014.core;

import de.uni_potsdam.hpi.bpt.bp2014.database.Operation;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

    public static ArrayList<HashMap<String,Object>> RetreiveItem(String type, int id) {
        return Operation.SelectSpecificRow(type, id);
    }

    public static ArrayList<HashMap<String,Object>> RetrieveAllItems(String type) {
        return Operation.SelectAllRows(type);
    }

}
