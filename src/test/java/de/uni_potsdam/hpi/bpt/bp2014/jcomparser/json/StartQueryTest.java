package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbStartQuery;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.MockProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 *
 */
public class StartQueryTest {

    static JSONArray startQueriesArray;

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @BeforeClass
    public static void setUpStartQuery() {
        startQueriesArray = new JSONArray();
        JSONObject first = getSinglePathMapping("Data", "Name", "$a.b");
        JSONObject second = getSinglePathMapping("Data", "Age", "$foo.bar");
        startQueriesArray.put(getSingleStartQuery("SELECT * FROM anEvent", Arrays.asList(first, second)));
        startQueriesArray.put(getSingleStartQuery("SELECT * FROM anotherEvent", Arrays.asList(first)));
    }

    private static JSONObject getSingleStartQuery(String condition, List<JSONObject> pathMappings) {
        JSONObject startQuery = new JSONObject();
        startQuery.put("condition", condition);
        JSONArray pathMappingsAsJson = new JSONArray();
        pathMappings.forEach(pathMappingsAsJson::put);
        startQuery.put("mapping", pathMappings);
        return startQuery;
    }

    private static JSONObject getSinglePathMapping(String clazz, String attr, String path) {
        JSONObject pathMapping = new JSONObject();
        pathMapping.put("classname", clazz);
        pathMapping.put("attr", attr);
        pathMapping.put("path", path);
        return pathMapping;
    }

    @Test
    public void testStartQueryCreation() {
        StartQuery startQuery = createTestStartQuery();
        assertEquals(2, startQuery.getAttributeToJsonPath().size());
        assertEquals("SELECT * FROM anEvent", startQuery.getQuery());
    }

    private StartQuery createTestStartQuery() {
        JSONObject first = getSinglePathMapping("Data", "Name", "$a.b");
        JSONObject second = getSinglePathMapping("Data", "Age", "$foo.bar");
        JSONObject startQueryJson = getSingleStartQuery(
                "SELECT * FROM anEvent", Arrays.asList(first, second));
        List<DataClass> dataClasses = getTestDataclasses();
        return new StartQuery(startQueryJson, dataClasses);
    }

    private List<DataClass> getTestDataclasses() {
        List<DataAttribute> dataAttributes = MockProvider.mockDataAttributes(
                Arrays.asList("Name", "Age"), Arrays.asList("1", "2"), Arrays.asList(1, 2));
        return MockProvider.mockDataClasses(
                Arrays.asList("Data"), Arrays.asList(dataAttributes));
    }

    @Test
    public void testStartQuerySaving() {
        StartQuery startQuery = createTestStartQuery();
        int scenarioId = 123451337;
        startQuery.save(scenarioId);
        DbStartQuery dbStartQuery = new DbStartQuery();
        assertEquals(Arrays.asList("SELECT * FROM anEvent"), dbStartQuery.getStartQueries(123451337));
        Map<String, Map<Integer, String>> pathMappings = dbStartQuery.getPathMappings(123451337);
        assertEquals(1, pathMappings.size());
        String queryId = pathMappings.keySet().iterator().next();
        Map<Integer, String> pathMapping = pathMappings.get(queryId);
        assertEquals(2, pathMapping.size());
        assertEquals("$a.b", pathMapping.get(1));
        assertEquals("$foo.bar", pathMapping.get(2));
    }

    @Test
    public void testMultipleStartQueriesSaving() {
        List<DataClass> dataClasses = getTestDataclasses();
        List<StartQuery> startQueries = StartQuery.parseStartQueries(startQueriesArray, dataClasses);
        assertEquals(2, startQueries.size());
        int scenarioId = 12345;
        startQueries.forEach(x -> x.save(scenarioId));

        DbStartQuery dbStartQuery = new DbStartQuery();
        List<String> expected = Arrays.asList("SELECT * FROM anEvent", "SELECT * FROM anotherEvent");
        assertTrue(expected.containsAll(dbStartQuery.getStartQueries(12345))
                && dbStartQuery.getStartQueries(12345).containsAll(expected));

        // TODO also assert values
        Map<String, Map<Integer, String>> pathMappings = dbStartQuery.getPathMappings(12345);
        assertEquals(2, pathMappings.size());
    }

    @Test
    public void testStartQueryScenarioIntegration() {
        Assert.fail();
    }
}