package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbHistoryActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbHistoryDataObjectInstance;

/**
 * Created by Jan Selke on 16.03.2015.
 */
public class Log {

    private DbHistoryActivityInstance dbHistoryActivityInstance = new DbHistoryActivityInstance();
    private DbHistoryDataObjectInstance dbHistoryDataObjectInstance = new DbHistoryDataObjectInstance();

    public void newActivityInstanceState(int id, String state) {
        dbHistoryActivityInstance.createEntry(id, state);
    }

    public void newActivityInstance(int id){
        dbHistoryActivityInstance.createNewActivityEntry(id);
    }

    public void newDataObjectInstanceState(int object_instance_id, int state_id){
        dbHistoryDataObjectInstance.createEntry(object_instance_id,state_id);
    }

    public void newDataObjectInstance(int id){
        dbHistoryDataObjectInstance.createNewDataObjectInstanceEntry(id);
    }
}
