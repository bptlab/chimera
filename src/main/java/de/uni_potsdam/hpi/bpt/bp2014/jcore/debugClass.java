package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class debugClass {
    static Logger log = Logger.getLogger(debugClass.class.getName());

    public static String selectScenario() {

        System.out.print("Select Scenario: ");
        String scID = readLine();
        System.out.println("Scenario " + scID + " selected");
        return scID;
    }

    public static String selectScenarioInstance() {

        System.out.print("Select Scenario Instance: ");
        String scID = readLine();
        System.out.println("Scenario Instance " + scID + " selected");
        return scID;
    }

    public static String readLine() {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String back = null;
        try {
            back = br.readLine();
        } catch (IOException e) {
            System.out.print("ERROR: " + e);
            log.error("Error:", e);
        }

        return back;
    }

    public static void main(String args[]) {
        int scenarioID = new Integer(selectScenario());
        int scenarioInstanceID = new Integer(selectScenarioInstance());
        ExecutionService executionService = new ExecutionService();
        String scenarioName = executionService.getScenarioName(scenarioID);
        if (scenarioInstanceID == -1) {
            scenarioInstanceID = executionService.startNewScenarioInstance(scenarioID);
            System.out.println("neues Scenario " + scenarioName + " geöffnet, Scenario Instance ID: " + scenarioInstanceID);
        } else {
            if (executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID)) {
                System.out.println("Scenario Instance geöffnet vom Scenario " + scenarioName);
            } else {
                System.out.println("Scenario Instance existiert nicht");
                return;
            }
        }

        while (true) {
            Collection<ActivityInstance> acts = executionService.getEnabledActivities(scenarioInstanceID);

            System.out.println("\nenabled Aktivität ID");
            for (ActivityInstance activityInstance : acts) {
                System.out.println(activityInstance.getControlNodeInstance_id() + ", " + activityInstance.getLabel() + ", (" + activityInstance.getControlNode_id() + ")");
            }

            System.out.println("Select Activity ID");
            int read = new Integer(readLine());
            executionService.beginActivityInstance(scenarioInstanceID, read);
            executionService.setDataAttributeValues(scenarioInstanceID, read, new HashMap<Integer, String>());

            System.out.println("----------start activity-----------");
            System.out.println("enabled Aktivität ID");
            acts = executionService.getEnabledActivities(scenarioInstanceID);
            for (ActivityInstance activityInstance : acts) {
                System.out.println(activityInstance.getControlNodeInstance_id() + ", " + activityInstance.getLabel());
            }
            Map<Integer, Map<String, String>> outputs = executionService.getOutputSetsForActivityInstance(read);
            for (int key : outputs.keySet()) {
                System.out.println("---OUTPUTSET " + key + " ---");
                for (String k : outputs.get(key).keySet()) {
                    System.out.println(k + "  " + outputs.get(key).get(k));
                }
                System.out.println("");
            }
            //readLine();
            System.out.println("Select outPutSet, -1 for nothing");
            int read2 = new Integer(readLine());
            System.out.println("---------terminate activity------------");
            if (!executionService.terminateActivityInstance(scenarioInstanceID, read, read2)) {
                System.out.println("nicht terminiert");
            }
            if (executionService.checkTerminationForScenarioInstance(scenarioInstanceID))
                System.out.println("Scenario ist terminiert");
            executionService = null;
            executionService = new ExecutionService();
            executionService.openExistingScenarioInstance(scenarioID, scenarioInstanceID);
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
