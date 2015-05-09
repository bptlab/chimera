package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */


public class HistoryService {
    /**
     * Database Connection objects
     */
    private final DbActivityInstance dbActivityInstance = new DbActivityInstance();
    private final DbControlNode dbControlNode = new DbControlNode();


    /**
     * Gives all ids of terminated activities for a scenario instance id.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return a list of int ids of the activities.
     */
    public LinkedList<Integer> getTerminatedActivitiesForScenarioInstance(int scenarioInstance_id) {
        return dbActivityInstance.getTerminatedActivitiesForScenarioInstance(scenarioInstance_id);
    }

    /**
     * Returns the Labels of terminated activities for a scenario instance id.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return a Map. Keys are the activity ids. Values are the labels of the activities.
     */
    public HashMap<Integer, String> getTerminatedActivityLabelsForScenarioInstance(int scenarioInstance_id) {
        LinkedList<Integer> ids = dbActivityInstance.getTerminatedActivitiesForScenarioInstance(scenarioInstance_id);
        HashMap<Integer, String> labels = new HashMap<Integer, String>();
        for (int id : ids) {
            labels.put(id, dbControlNode.getLabel(id));
        }
        return labels;
    }

    public Map<Integer, Map<String, Object>> getDataObjectLogEntriesForScenarioInstance(int scenarioInstance_id) {
        DbHistoryDataObjectInstance dbHistoryDataObjectInstance = new DbHistoryDataObjectInstance();
        return dbHistoryDataObjectInstance.getLogEntriesForScenarioInstance(scenarioInstance_id);
    }

    public Map<Integer, Map<String, Object>> getActivityInstanceLogEntriesForScenarioInstance(int scenarioInstance_id) {
        DbHistoryActivityInstance dbHistoryActivityInstance = new DbHistoryActivityInstance();
        return dbHistoryActivityInstance.getLogEntriesForScenarioInstance(scenarioInstance_id);
    }

    public Map<Integer, Map<String, Object>> getSelectedActivityInstanceLogEntriesForScenarioInstance(int scenarioInstance_id) {
        DbHistoryActivityInstance dbHistoryActivityInstance = new DbHistoryActivityInstance();
        return dbHistoryActivityInstance.getterminatedLogEntriesForScenarioInstance(scenarioInstance_id);
    }

    public Map<Integer, Map<String, Object>> getDataAttributeInstanceLogEntriesForScenarioInstance(int scenarioInstance_id) {
        DbHistoryDataAttributeInstance dbHistoryDataAttributeInstance = new DbHistoryDataAttributeInstance();
        return dbHistoryDataAttributeInstance.getLogEntriesForScenarioInstance(scenarioInstance_id);
    }

    public static void main(String args[]) {
        HistoryService h = new HistoryService();
        System.out.print(h.getDataAttributeInstanceLogEntriesForScenarioInstance(1330).toString());

    }
}
