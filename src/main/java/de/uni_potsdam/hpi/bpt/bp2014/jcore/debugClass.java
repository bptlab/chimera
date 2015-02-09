package de.uni_potsdam.hpi.bpt.bp2014.jcore;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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



public class debugClass {
    public static String selectScenario(){

        System.out.print("Select Scenario: ");
        String scID = readLine();
        System.out.println("Scenario " + scID + " selected");
        return scID;
    }

    public static String selectScenarioInstance(){

        System.out.print("Select Scenario Instance: ");
        String scID = readLine();
        System.out.println("Scenario Instance " + scID + " selected");
        return scID;
    }

    public static String readLine(){
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String back = null;
        try {
            back = br.readLine();
        } catch (IOException e) {
            System.out.print("ERROR: "+e);
            e.printStackTrace();
        }

        return back;
    }

    public static void main(String args[]){

        int scenarioID = new Integer(selectScenario());
        int scenarioInstanceID = new Integer(selectScenarioInstance());
        ExecutionService executionService = new ExecutionService();
        if (scenarioInstanceID == -1) {
            scenarioInstanceID = executionService.startNewScenarioInstance(scenarioID);
            System.out.println("neues Scenario geöffnet, Scenario Instance ID: " + scenarioInstanceID);
        }else {
            if(executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID)){
                System.out.println("Scenario Instance geöffnet");
            }else{
                System.out.println("Scenario Instance existiert nicht");
            }
        }

        while(true){
            LinkedList<Integer> activitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstanceID);
            HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(scenarioInstanceID);
            System.out.println("enabled Aktivität ID");
            for(int activityID: activitiesIDs){
                System.out.println(activityID + ", " + labels.get(activityID));
            }

            System.out.println("Select Activity ID");
            int read = new Integer(readLine());
            executionService.beginActivity(scenarioInstanceID, read);
            System.out.println("----------start activity-----------");
            System.out.println("enabled Aktivität ID");
            activitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstanceID);
            labels = executionService.getEnabledActivityLabelsForScenarioInstance(scenarioInstanceID);
            for(int activityID: activitiesIDs){
                System.out.println(activityID + ", " + labels.get(activityID));
            }
            //readLine();
            System.out.println("---------terminate activity------------");
            executionService.terminateActivity(scenarioInstanceID, read);
            if (executionService.checkTerminationForScenarioInstance(scenarioInstanceID)) System.out.println("Scenario ist terminiert");
        }

        /*        int id = executionService.startNewScenarioInstance(new Integer(1));
        LinkedList<Integer> enabledActivitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(id);
        HashMap<Integer, String> labels = executionService.getEnabledActivityLabelsForScenarioInstance(id);
        for(int activityID: enabledActivitiesIDs) {
            System.out.println("ID: " + activityID + ", " + labels.get(activityID));
        }
        System.out.println(" -  -- -- -- -- -- - -- -- -- -- -");
        HistoryService historyService = new HistoryService();
        LinkedList<Integer> terminatedActivities = historyService.getTerminatedActivitysForScenarioInstance(223);
        HashMap<Integer, String> labels2 = historyService.getTerminatedActivityLabelsForScenarioInstance(223);
        for(int activityID: terminatedActivities) {
            System.out.println("ID: " + activityID + ", " + labels2.get(activityID));
        }*/
       /* ExecutionService executionService = new ExecutionService();
        int id = executionService.startNewScenarioInstance(1);
        LinkedList<Integer> activitiesIDs= executionService.getEnabledActivitiesIDsForScenarioInstance(id);
        System.out.println("enabled Aktivität ID");
        for(int activityID: activitiesIDs){
            System.out.println(activityID);
        }*/
    }
}
