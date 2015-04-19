package de.uni_potsdam.hpi.bpt.bp2014.core;

import de.uni_potsdam.hpi.bpt.bp2014.database;
import de.uni_potsdam.hpi.bpt.bp2014.database.Operation;
import de.uni_potsdam.hpi.bpt.bp2014.rest;

import java.util.LinkedList;

public class Controller {

    public boolean class CreateNew(String type, Object object) {
        //initialize database
        if(type == "user"){
            // create new user
        } else if(type == "role") {
            //create new role
        }
        return false;
    }

    public boolean  RetreiveItem(String type, String id) {
        return true;
    }

    public boolean  RetreiveAllItems(String type) {

        LinkedList<Integer> result = Operation.SelectAllRows(type);

    }

    public boolean class EditEntry(Integer id) {
        return true;
    }

}
