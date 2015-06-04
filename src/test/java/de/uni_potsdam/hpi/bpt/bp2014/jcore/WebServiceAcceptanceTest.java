package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.Test;

import javax.ws.rs.core.Application;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;



/**
 * This class extends JerseyTest, because it's need the Rest Interface to check the Webservice Task functions.
 * The GET, PUT and POST Requests are send to the Rest Interface.
 */
public class WebServiceAcceptanceTest extends AbstractTest {

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2_schema.sql";

    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2_AcceptanceTests.sql";
    }

    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface.class);
    }

    @Test
    public void testGet() {
        System.out.println("\n ------------------ test Scenario 156 ------------------\n");
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(156);
        int activity1 = 524;

        System.out.println("Start Scenario 156");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        int activity1instance_id = executionService.getScenarioInstance(scenarioInstance).getRunningControlNodeInstances().getFirst().getControlNodeInstance_id();
        executionService.setDataAttributeValues(scenarioInstance, activity1instance_id, new HashMap<Integer, String>());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        Collection<DataAttributeInstance> dataAttributeInstances = executionService.getScenarioInstance(scenarioInstance).getDataAttributeInstances().values();
        String value12 = "";
        String value13 = "";
        for (DataAttributeInstance dataAttributeInstance : dataAttributeInstances) {
            if (dataAttributeInstance.getDataAttribute_id() == 12) {
                value12 = dataAttributeInstance.getValue().toString();
            } else if (dataAttributeInstance.getDataAttribute_id() == 13) {
                value13 = dataAttributeInstance.getValue().toString();
            }
        }
        assertEquals("[9450]", value12);
        assertEquals("Schmutzgrad pruefen", value13);
    }

    @Test
    public void testPOST() {
        System.out.println("\n ------------------ test Scenario 157 ------------------\n");
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(157);
        int activity1 = 528;

        System.out.println("Start Scenario 157");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        int activity1instance_id = executionService.getScenarioInstance(scenarioInstance).getRunningControlNodeInstances().getFirst().getControlNodeInstance_id();
        executionService.setDataAttributeValues(scenarioInstance, activity1instance_id, new HashMap<Integer, String>());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        Collection<DataAttributeInstance> dataAttributeInstances = executionService.getScenarioInstance(scenarioInstance).getDataAttributeInstances().values();
        String value14 = "";
        for (DataAttributeInstance dataAttributeInstance : dataAttributeInstances) {
            if (dataAttributeInstance.getDataAttribute_id() == 14) {
                value14 = dataAttributeInstance.getValue().toString();
            }
        }
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertEquals(dbScenarioInstance.getScenarioInstances(156).getLast().toString(), value14);
    }

    @Test
    public void testPUT() {
        System.out.println("\n ------------------ test Scenario 158 ------------------\n");
        ExecutionService executionService = new ExecutionService();
        int scenarioInstance = executionService.startNewScenarioInstance(158);
        int activity1 = 532;

        System.out.println("Start Scenario 158");
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());

        //do activity 1
        System.out.println("do activity " + activity1);
        executionService.beginActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        int activity1instance_id = executionService.getScenarioInstance(scenarioInstance).getRunningControlNodeInstances().getFirst().getControlNodeInstance_id();
        executionService.setDataAttributeValues(scenarioInstance, activity1instance_id, new HashMap<Integer, String>());
        executionService.terminateActivity(scenarioInstance, activity1);
        assertArrayEquals(new Integer[]{activity1}, executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toArray());
        System.out.println("enabled Activities: " + executionService.getEnabledActivitiesIDsForScenarioInstance(scenarioInstance).toString());

        Collection<DataAttributeInstance> dataAttributeInstances = executionService.getScenarioInstance(scenarioInstance).getDataAttributeInstances().values();
        String value16 = "";
        for (DataAttributeInstance dataAttributeInstance : dataAttributeInstances) {
            if (dataAttributeInstance.getDataAttribute_id() == 16) {
                value16 = dataAttributeInstance.getValue().toString();
            }
        }
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        assertEquals(dbScenarioInstance.getScenarioInstances(156).getLast().toString(), value16);
        assertEquals("new Instance", dbScenarioInstance.getInstanceMap(new Integer(value16)).get("name"));
    }
}
