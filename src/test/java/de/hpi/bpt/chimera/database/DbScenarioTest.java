package de.hpi.bpt.chimera.database;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DbScenarioTest {

    private static DbScenario dbScenario;
    private static Connector connector;

    private static int scenario1;
    private static int scenario2;

    @BeforeClass
    public static void clearAndInitialize() throws IOException, SQLException {
        dbScenario = new DbScenario();
        connector = new Connector();
        clearFragments();
        insertTestData();
    }

    @AfterClass
    public static void clearFragments() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    private static void insertTestData() {
        scenario1 = connector.insertScenario("scenario1", 1);
        scenario2 = connector.insertScenario("scenario2", 1);
    }

    @Test
    public void testGetScenarioIds() {
        List<Integer> scenarios = dbScenario.getScenarioIds();
        assertEquals(new Integer(scenario1), scenarios.get(0));
        assertEquals(new Integer(scenario2), scenarios.get(1));
    }

    @Test
    public void testExistScenario() {
        assertTrue(dbScenario.existScenario(1));
        assertTrue(dbScenario.existScenario(2));
        assertFalse(dbScenario.existScenario(3));
    }

    @Test
    public void testGetScenarioName(){
        assertEquals("scenario1", dbScenario.getScenarioName(scenario1));
        assertEquals("scenario2", dbScenario.getScenarioName(scenario2));
    }

    @Test
    public void testGetScenarios(){
        assertEquals(createScenarioMap(), dbScenario.getScenarios());
    }

    @Test
    public void testGetScenariosLike(){
        assertEquals(createScenarioMap(), dbScenario.getScenariosLike("scenario"));
    }

    @Test
    public void testGetScenarioDetails(){
        assertEquals(createScenarioDetailsMap(), dbScenario.getScenarioDetails(1));
    }

    private Map<Integer, String> createScenarioMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "scenario1");
        map.put(2, "scenario2");
        return map;
    }

    private Map<String, Object> createScenarioDetailsMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "scenario1");
        map.put("modelversion", 1);
        return map;
    }
}
