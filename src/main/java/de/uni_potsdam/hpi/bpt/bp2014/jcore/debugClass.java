package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

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
//        String scenarioID = selectScenario();
//        String scenarioInstanceID = selectScenarioInstance();
//        ScenarioInstance scenarioInstance = new ScenarioInstance(new Integer(scenarioID), new Integer(scenarioInstanceID));

        ExecutionService executionService = new ExecutionService();
        int id = executionService.startNewScenarioInstance(new Integer(1));
        LinkedList<Integer> enabledActivitiesIDs = executionService.getEnabledActivitiesIDsForScenarioInstance(id);
        HashMap<Integer, String> labels = executionService.getActivityLabelsForScenarioInstance(id);
        for(int activityID: enabledActivitiesIDs) {
            System.out.println("ID: " + activityID + ", " + labels.get(activityID));
        }
       /* ExecutionService executionService = new ExecutionService();
        int id = executionService.startNewScenarioInstance(1);
        LinkedList<Integer> activitiesIDs= executionService.getEnabledActivitiesIDsForScenarioInstance(id);
        System.out.println("enabled Aktivität ID");
        for(int activityID: activitiesIDs){
            System.out.println(activityID);
        }*/


//        while(true){
//
//
//            ExecutionService executionService = new ExecutionService(scenarioInstance);
//            LinkedList<Integer> activitiesIDs= executionService.getEnabledActivitiesIDs();
//            System.out.println("enabled Aktivität ID");
//            for(int activityID: activitiesIDs){
//                System.out.println(activityID);
//            }
//            System.out.println("Select Activity");
//            executionService.startActivity(new Integer(readLine()));
//        }
    }
}
