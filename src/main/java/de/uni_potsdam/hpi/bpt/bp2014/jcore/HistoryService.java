package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by jaspar.mang on 19.01.15.
 */
public class HistoryService {
    private DbActivityInstance dbActivityInstance = new DbActivityInstance();
    private DbControlNode dbControlNode = new DbControlNode();

    public LinkedList<Integer> getTerminatedActivitysForScenarioInstance(int scenarioInstance_id){
        LinkedList<Integer> ids = dbActivityInstance.getTerminatedActivitiesForScenarioInstance(scenarioInstance_id);
        return  ids;
    }
    public HashMap<Integer, String> getTerminatedActivityLabelsForScenarioInstance(int scenarioInstance_id){
        LinkedList<Integer> ids = dbActivityInstance.getTerminatedActivitiesForScenarioInstance(scenarioInstance_id);
        HashMap<Integer, String> labels = new HashMap<Integer, String>();
        for (int id: ids){
            labels.put(id, dbControlNode.getLabel(id));
        }
        return  labels;
    }
}
