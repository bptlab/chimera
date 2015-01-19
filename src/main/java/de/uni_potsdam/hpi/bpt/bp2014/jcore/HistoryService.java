package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;

import java.util.LinkedList;

/**
 * Created by jaspar.mang on 19.01.15.
 */
public class HistoryService {
    private DbActivityInstance dbActivityInstance = new DbActivityInstance();

    public LinkedList<Integer> getTerminatedActivitysForScenarioInstance(int scenarioInstance_id){
        LinkedList<Integer> ids = dbActivityInstance.getTerminatedActivitiesForScenarioInstance(scenarioInstance_id);
        return  ids;
    }
}
