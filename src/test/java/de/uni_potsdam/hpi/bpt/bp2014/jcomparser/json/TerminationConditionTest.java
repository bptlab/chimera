package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TerminationConditionTest {

    @Test
    public void testTerminationCondition() {

        try {
            File file = new File("src/test/resources/EventScenarios/TerminationConditionScenario.json");
            String json = FileUtils.readFileToString(file);

            Scenario scenario = new Scenario();
            scenario.initializeInstanceFromJson(json);

            ExecutionService service = ExecutionService.getInstance(scenario.getScenarioDbId());
            int instanceId = service.startNewScenarioInstance();

            // TODO find activity id
            int activityId = 0;


            //TODO assert termination condition not triggered
            assert(false);

            service.beginActivity(instanceId, activityId);
            service.terminateActivity(instanceId, activityId);

            //TODO assert termination condition triggered
            assert(false);

        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
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
        DataClass a = new DataClass();
        a.setDataClassName("A");
        DataObject A = new DataObject(a);

        DataClass b = new DataClass();
        b.setDataClassName("B");
        DataObject B = new DataObject(b);

        dataObjects.add(A);
        dataObjects.add(B);
        return dataObjects;
    }

}
