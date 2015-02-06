package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by jaspar.mang on 02.02.15.
 */
public class ExecutionAcceptanceTest {

    //test AND
    @Test
    public void testScenario2(){
        System.out.println("\n ------------------ test Scenario 2 ------------------\n");
        int activity1 = 103;
        int activity2 = 104;
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(2);
        System.out.println("Start Scenario 2");
        System.out.println("enabled Activities: "+executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1, activity2}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity2}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity2}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 2
        System.out.println("do activity "+activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity1, activity2}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 2
        System.out.println("do activity "+activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity1}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity1}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(2, scenarioInstance);

        assertArrayEquals(new Integer[]{activity1}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity1, activity2}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

    }

    //test DataObjects
    @Test
    public void testScenario1(){
        System.out.println("\n ------------------ test Scenario 1 ------------------\n");
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(1);
        int activity1 = 2;
        int activity2 = 5;
        int activity3 = 6;
        int activity4 = 4;
        int activity5 = 10;
        int activity6 = 16;

        System.out.println("Start Scenario 1");
        System.out.println("enabled Activities: "+executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1, activity6}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity6}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity6, activity4, activity2}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 2
        System.out.println("do activity " + activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(1, scenarioInstance);
        assertArrayEquals(new Integer[]{activity4, activity6}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity2);
        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(1, scenarioInstance);
        assertArrayEquals(new Integer[]{activity4, activity5, activity6}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(1, scenarioInstance);
        assertArrayEquals(new Integer[]{activity4, activity5, activity6}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 4
        System.out.println("do activity " + activity4);
        executionService.beginActivity(scenarioInstance, activity4);
        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(1, scenarioInstance);
        assertArrayEquals(new Integer[]{ activity6}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity4);
        assertArrayEquals(new Integer[]{activity6, activity3}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 3
        System.out.println("do activity " + activity3);
        executionService.beginActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{activity6}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{activity6}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

    }

    //test Termination Condition
    @Test
    public void testScenario105(){
        System.out.println("\n ------------------ test Scenario 105 ------------------\n");
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(105);
        int activity1 = 125;
        int activity2 = 126;
        int activity3 = 128;
        int activity4 = 130;

        System.out.println("Start Scenario 105");
        System.out.println("enabled Activities: "+executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity4, activity2}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 2
        System.out.println("do activity " + activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity3}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 3
        System.out.println("do activity " + activity3);
        executionService.beginActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{activity1}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity4, activity2}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 4
        System.out.println("do activity " + activity4);
        executionService.beginActivity(scenarioInstance, activity4);
        assertArrayEquals(new Integer[]{}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        executionService.terminateActivity(scenarioInstance, activity4);
        assertArrayEquals(new Integer[]{}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertEquals(1, dbScenarioInstance.getTerminated(scenarioInstance));
    }

}
