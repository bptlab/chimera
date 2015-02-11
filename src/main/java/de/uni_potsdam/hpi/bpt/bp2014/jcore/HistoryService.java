package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;

import java.util.HashMap;
import java.util.LinkedList;


/***********************************************************************************
*   
*   _________ _______  _        _______ _________ _        _______ 
*   \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
*      )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
*      |  |  | (__    |   \ | || |         | |   |   \ | || (__    
*      |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)   
*      |  |  | (      | | \   || | \_  )   | |   | | \   || (      
*   |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
*   (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
*
*******************************************************************
*
*   Copyright © All Rights Reserved 2014 - 2015
*
*   Please be aware of the License. You may found it in the root directory.
*
************************************************************************************/


public class HistoryService {
    //Database Connection objects
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
