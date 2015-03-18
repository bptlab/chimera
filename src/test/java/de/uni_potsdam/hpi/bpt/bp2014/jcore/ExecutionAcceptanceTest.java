package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class ExecutionAcceptanceTest {

    /**
     * This test run scenario 2 from the database. It checks AND gateways and consistency.
     */
    //test AND1
    @Test
    public void testScenario2() {
        System.out.println("\n ------------------ test Scenario 2 ------------------\n");
        int activity1 = 103;
        int activity2 = 104;
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(2);
        System.out.println("Start Scenario 2");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1, activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 2
        System.out.println("do activity " + activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity1, activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 2
        System.out.println("do activity " + activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(2, scenarioInstance);

        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity1, activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

    }

    /**
     * This test run scenario 118 from the database.
     * It checks that after an AND gateway could be directly another AND gateway.
     */
    //test AND2
    @Test
    public void testScenario118() {
        System.out.println("\n ------------------ test Scenario 118 ------------------\n");
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(118);
        int activity246 = 246;
        int activity243 = 243;
        int activity245 = 245;


        System.out.println("Start Scenario 118");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity246, activity243, activity245}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());


    }

    /**
     * This test run scenario 1 from the database. It checks data objects and consistency.
     */
    //test DataObjects
    @Test
    public void testScenario1() {
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
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1, activity6}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity6}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity6, activity4, activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 2
        System.out.println("do activity " + activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(1, scenarioInstance);
        assertArrayEquals(new Integer[]{activity4, activity6}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity2);
        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(1, scenarioInstance);
        assertArrayEquals(new Integer[]{activity4, activity5, activity6}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(1, scenarioInstance);
        assertArrayEquals(new Integer[]{activity4, activity5, activity6}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 4
        System.out.println("do activity " + activity4);
        executionService.beginActivity(scenarioInstance, activity4);
        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(1, scenarioInstance);
        assertArrayEquals(new Integer[]{activity6}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity4);
        assertArrayEquals(new Integer[]{activity6, activity3}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 3
        System.out.println("do activity " + activity3);
        executionService.beginActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{activity6}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{activity6}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

    }

    /**
     *  This test run scenario 105 from the database. It checks the termination condition and consistency.
     */
    //test Termination Condition
    @Test
    public void testScenario105() {
        System.out.println("\n ------------------ test Scenario 105 ------------------\n");
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(105);
        int activity1 = 125;
        int activity2 = 126;
        int activity3 = 128;
        int activity4 = 130;

        System.out.println("Start Scenario 105");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity4, activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 2
        System.out.println("do activity " + activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity3}, ((LinkedList<Integer>) executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance)).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 3
        System.out.println("do activity " + activity3);
        executionService.beginActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity3);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity4, activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 4
        System.out.println("do activity " + activity4);
        executionService.beginActivity(scenarioInstance, activity4);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity4);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //check termination in database
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertEquals(1, dbScenarioInstance.getTerminated(scenarioInstance));
    }

    /**
     * This test run scenario 111 from the database. It checks referential activities and consistency.
     */
    //test References1
    @Test
    public void testScenario111() {
        System.out.println("\n ------------------ test Scenario 111 ------------------\n");
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(111);
        int activity1 = 183;
        int activity2 = 184;
        int activity3 = 191;
        int activity4 = 193;
        int activity5 = 187;
        int activity6 = 189;

        System.out.println("Start Scenario 111");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity3, activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());


        int activity3instance_id = executionService.getScenarioInstance(scenarioInstance).getEnabledControlNodeInstances().getFirst().getControlNodeInstance_id();
        int activity2instance_id = executionService.getScenarioInstance(scenarioInstance).getEnabledControlNodeInstances().get(1).getControlNodeInstance_id();


        //do activity 2
        System.out.println("do activity " + activity2);
        executionService.beginActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        assertEquals("referentialRunning", dbActivityInstance.getState(activity3instance_id));
        executionService.terminateActivity(scenarioInstance, activity2);
        assertArrayEquals(new Integer[]{activity6, activity4, activity5}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());


        assertEquals("terminated", dbActivityInstance.getState(activity2instance_id));
        assertEquals("terminated", dbActivityInstance.getState(activity3instance_id));
    }

    /**
     * This test run scenario 113 from the database. It checks referential activities and consistency.
     */
    //test References2
    @Test
    public void testScenario113() {
        System.out.println("\n ------------------ test Scenario 113 ------------------\n");
        DbActivityInstance dbActivityInstance = new DbActivityInstance();
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(113);
        int activity1 = 207;
        int activity2 = 208;
        //int activity3 = 202;
        int activity4 = 203;


        System.out.println("Start Scenario 111");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        int activity2instance_id = executionService.getScenarioInstance(scenarioInstance).getControlFlowEnabledControlNodeInstances().getFirst().getControlNodeInstance_id();
        int activity1instance_id = executionService.getScenarioInstance(scenarioInstance).getControlFlowEnabledControlNodeInstances().get(1).getControlNodeInstance_id();


        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        assertEquals("referentialRunning", dbActivityInstance.getState(activity2instance_id));

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(113, scenarioInstance);

        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity4, activity2}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());


        assertEquals("terminated", dbActivityInstance.getState(activity1instance_id));
        assertEquals("terminated", dbActivityInstance.getState(activity2instance_id));
    }

    /**
     * This test run scenario 114 from the database. It checks referential activities and consistency.
     */
    //test References3
    @Test
    public void testScenario114() {
        System.out.println("\n ------------------ test Scenario 114 ------------------\n");
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(114);
        int activity218 = 218;
        int activity220 = 220;
        int activity216 = 216;
        int activity214 = 214;
        int activity210 = 210;
        int activity211 = 211;


        System.out.println("Start Scenario 114");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity210, activity216, activity218}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 216
        System.out.println("do activity " + activity216);
        executionService.beginActivity(scenarioInstance, activity216);
        assertArrayEquals(new Integer[]{activity218}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(114, scenarioInstance);

        assertArrayEquals(new Integer[]{activity218}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        executionService.terminateActivity(scenarioInstance, activity216);
        assertArrayEquals(new Integer[]{activity218, activity211, activity214}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 218
        System.out.println("do activity " + activity218);
        executionService.beginActivity(scenarioInstance, activity218);
        assertArrayEquals(new Integer[]{activity211, activity214}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity218);
        assertArrayEquals(new Integer[]{activity211, activity214, activity220}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 220
        System.out.println("do activity " + activity220);
        executionService.beginActivity(scenarioInstance, activity220);
        assertArrayEquals(new Integer[]{activity211, activity214}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity220);
        assertArrayEquals(new Integer[]{activity211, activity214, activity218}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 214
        System.out.println("do activity " + activity214);
        executionService.beginActivity(scenarioInstance, activity214);
        assertArrayEquals(new Integer[]{activity211, activity218}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity214);
        assertArrayEquals(new Integer[]{activity211, activity218, activity216}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 218
        System.out.println("do activity " + activity218);
        executionService.beginActivity(scenarioInstance, activity218);
        assertArrayEquals(new Integer[]{activity211, activity216}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity218);
        assertArrayEquals(new Integer[]{activity211, activity216, activity220}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 211
        System.out.println("do activity " + activity211);
        executionService.beginActivity(scenarioInstance, activity211);
        assertArrayEquals(new Integer[]{activity216, activity220}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity211);
        assertArrayEquals(new Integer[]{activity216, activity220, activity210}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        //do activity 220
        System.out.println("do activity " + activity220);
        executionService.beginActivity(scenarioInstance, activity220);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity220);
        assertArrayEquals(new Integer[]{activity211, activity218}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
    }

    //Email Test Scenario 145, XOR test
    @Test
    public void testScenario145() {
        ExecutionService executionService = new ExecutionService();
        int scenarioID = 145;
        int scenarioInstance = executionService.startNewScenarioInstance(scenarioID);
        int activity389 = 389;
        int activity396 = 396;
        int activity399 = 399;
        int activity407 = 407;
        int activity393 = 393;
        int activity397 = 397;
        int activity401 = 401;
        int activity404 = 404;
        int activity408 = 408;
        int activity411 = 411;
        int activity412 = 412;
        System.out.println("Start Scenario 145");

        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity389, activity396, activity399, activity407}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 399
        System.out.println("do activity " + activity399);
        executionService.beginActivity(scenarioInstance, activity399);
        assertArrayEquals(new Integer[]{activity389, activity396, activity407}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity399);
        assertArrayEquals(new Integer[]{activity389, activity396, activity407, activity401}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);


        //do activity 389
        System.out.println("do activity " + activity389);
        executionService.beginActivity(scenarioInstance, activity389);
        assertArrayEquals(new Integer[]{activity396, activity401, activity407}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity389);
        assertArrayEquals(new Integer[]{activity396, activity401, activity407, activity404}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);


        //do activity 404
        System.out.println("do activity " + activity404);
        executionService.beginActivity(scenarioInstance, activity404);
        assertArrayEquals(new Integer[]{activity396, activity407}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity404);
        assertArrayEquals(new Integer[]{activity396, activity407, activity389}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);


        //do activity 407
        System.out.println("do activity " + activity407);
        executionService.beginActivity(scenarioInstance, activity407);
        assertArrayEquals(new Integer[]{activity389, activity396}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity407);
        assertArrayEquals(new Integer[]{activity389, activity396, activity408, activity411, activity412}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());


        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);


        //do activity 396
        System.out.println("do activity " + activity396);
        executionService.beginActivity(scenarioInstance, activity396);
        assertArrayEquals(new Integer[]{activity389, activity408, activity411, activity412}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity396);
        assertArrayEquals(new Integer[]{activity389, activity408, activity411, activity412, activity393, activity397}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);


        //do activity 408
        System.out.println("do activity " + activity408);
        executionService.beginActivity(scenarioInstance, activity408);
        assertArrayEquals(new Integer[]{activity389, activity393, activity397}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity408);
        assertArrayEquals(new Integer[]{activity389, activity393, activity397, activity407}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);


        //do activity 407
        System.out.println("do activity " + activity407);
        executionService.beginActivity(scenarioInstance, activity407);
        assertArrayEquals(new Integer[]{activity389, activity393, activity397}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity407);
        assertArrayEquals(new Integer[]{activity389, activity393, activity397, activity408, activity411, activity412}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);


        //do activity 412
        System.out.println("do activity " + activity412);
        executionService.beginActivity(scenarioInstance, activity412);
        assertArrayEquals(new Integer[]{activity389, activity393, activity397}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity412);
        assertArrayEquals(new Integer[]{activity389, activity393, activity397, activity407}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
    }

    //test scenario 144, xor test
    @Test
    public void testScenario144() {
        ExecutionService executionService = new ExecutionService();
        int scenarioID = 144;
        int scenarioInstance = executionService.startNewScenarioInstance(scenarioID);
        int activity375 = 375;
        int activity377 = 377;
        int activity376 = 376;
        int activity378 = 378;
        int activity387 = 387;
        int activity384 = 384;
        int activity379 = 379;
        System.out.println("Start Scenario 144");

        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity375, activity387}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 375
        System.out.println("do activity " + activity375);
        executionService.beginActivity(scenarioInstance, activity375);
        assertArrayEquals(new Integer[]{activity387}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity375);
        assertArrayEquals(new Integer[]{activity387, activity377, activity378, activity376, activity384}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);

        //do activity 377
        System.out.println("do activity " + activity377);
        executionService.beginActivity(scenarioInstance, activity377);
        assertArrayEquals(new Integer[]{activity378, activity387}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity377);
        assertArrayEquals(new Integer[]{activity378, activity387, activity379}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
    }

    //test scenario 144_2, xor test
    @Test
    public void testScenario144_2() {
        ExecutionService executionService = new ExecutionService();
        int scenarioID = 144;
        int scenarioInstance = executionService.startNewScenarioInstance(scenarioID);
        int activity375 = 375;
        int activity377 = 377;
        int activity376 = 376;
        int activity378 = 378;
        int activity387 = 387;
        int activity384 = 384;
        int activity383 = 383;
        System.out.println("Start Scenario 144");

        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity375, activity387}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 375
        System.out.println("do activity " + activity375);
        executionService.beginActivity(scenarioInstance, activity375);
        assertArrayEquals(new Integer[]{activity387}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity375);
        assertArrayEquals(new Integer[]{activity387, activity377, activity378, activity376, activity384}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        System.out.println("--- restart Service ---");
        executionService = null;
        executionService = new ExecutionService();
        executionService.openExistingScenarioInstance(scenarioID, scenarioInstance);

        //do activity 376
        System.out.println("do activity " + activity376);
        executionService.beginActivity(scenarioInstance, activity376);
        assertArrayEquals(new Integer[]{activity387}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        executionService.terminateActivity(scenarioInstance, activity376);
        assertArrayEquals(new Integer[]{activity387, activity383}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
    }
}

