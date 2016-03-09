package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TerminationConditionTest {

    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testTerminationCondition() {
        try {
            String path = "src/test/resources/TerminationConditionScenario.json";
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            assertEquals(false, scenarioInstance.checkTerminationCondition());

            List<AbstractControlNodeInstance> controlNodeInstances =
                    scenarioInstance.getEnabledControlNodeInstances();
            ActivityInstance instance = (ActivityInstance) controlNodeInstances.get(0);
            instance.terminate();
            assertEquals(true, scenarioInstance.checkTerminationCondition());
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testMultiTerminationCondition() {
        try {
            String path = "src/test/resources/TerminationConditionScenario.json";
            ScenarioInstance scenarioInstance = ScenarioTestHelper.createScenarioInstance(path);
            assertEquals(false, scenarioInstance.checkTerminationCondition());

            List<AbstractControlNodeInstance> controlNodeInstances =
                    scenarioInstance.getEnabledControlNodeInstances();
            ActivityInstance instance = (ActivityInstance) controlNodeInstances.get(1);
            instance.terminate();
            assertEquals(true, scenarioInstance.checkTerminationCondition());
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testTerminationConditionString() {
        String terminationConditionStrings = "[\"A[end], B[another]\", \"A[approved]\"]";
        JSONArray array = new JSONArray(terminationConditionStrings);
        int scenarioId = 0;
        List<DataObject> dataObjects = createExampleDataobjects();

        Map<String, Integer> stateToDatabaseId = getExampleStates();
        List<TerminationCondition> terminationConditions = TerminationCondition.
                parseTerminationConditions(array, dataObjects, scenarioId, stateToDatabaseId);
        assertEquals(2, terminationConditions.size());
    }

    private Map<String, Integer> getExampleStates() {
        Map<String, Integer> exampleStates = new HashMap<>();
        exampleStates.put("end", 0);
        exampleStates.put("another", 1);
        exampleStates.put("approved", 2);
        return exampleStates;
    }

    private List<DataObject> createExampleDataobjects() {
        List<DataObject> dataObjects = new ArrayList<>();
        DataClass a = new DataClass("");
        a.setDataClassName("A");
        DataObject A = new DataObject(a);

        DataClass b = new DataClass("");
        b.setDataClassName("B");
        DataObject B = new DataObject(b);

        dataObjects.add(A);
        dataObjects.add(B);
        return dataObjects;
    }

}
