package de.hpi.bpt.chimera.jcomparser.json;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.data.DbTerminationCondition;
import de.hpi.bpt.chimera.jcore.MockProvider;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ParsingTerminationConditionTest {
    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testTerminationConditionString() {
        String terminationConditionStrings = "[\"A[end], B[another]\", \"A[approved]\"]";
        JSONArray array = new JSONArray(terminationConditionStrings);
        int scenarioId = 0;
        List<DataClass> dataClasses = MockProvider.mockDataClasses(Arrays.asList("A", "B"));
        Map<String, Integer> stateToDatabaseId = getExampleStates();
        List<TerminationCondition> terminationConditions = TerminationCondition.
                parseTerminationConditions(array, dataClasses, scenarioId, stateToDatabaseId);


        assertEquals(2, terminationConditions.size());
    }

    @Test
    public void testSavingOfTerminationCondition() {
        String terminationConditionStrings = "[\"A[end], B[another]\", \"A[approved]\"]";
        JSONArray array = new JSONArray(terminationConditionStrings);
        int scenarioId = 0;
        List<DataClass> dataClasses = MockProvider.mockDataClasses(Arrays.asList("A", "B"));
        Map<String, Integer> stateToDatabaseId = getExampleStates();
        List<TerminationCondition> terminationConditions = TerminationCondition.
                parseTerminationConditions(array, dataClasses, scenarioId, stateToDatabaseId);
        terminationConditions.forEach(TerminationCondition::save);
        DbTerminationCondition dbTerminationCondition = new DbTerminationCondition();
        Set<String> conditionIds = dbTerminationCondition
                .getConditionSetKeysForScenario(scenarioId);
        List<Map<Integer, Integer>> conditions = conditionIds.stream().map(
                dbTerminationCondition::getDataClassToState).collect(Collectors.toList());
        assertEquals(2, conditions.size());
    }

    private Map<String, Integer> getExampleStates() {
        Map<String, Integer> exampleStates = new HashMap<>();
        exampleStates.put("end", 0);
        exampleStates.put("another", 1);
        exampleStates.put("approved", 2);
        return exampleStates;
    }
}
