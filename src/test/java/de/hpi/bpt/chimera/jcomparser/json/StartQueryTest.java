package de.hpi.bpt.chimera.jcomparser.json;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.DbCaseStart;
import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.database.controlnodes.events.DbStartQuery;
import de.hpi.bpt.chimera.jcore.eventhandling.StartQueryPart;
import de.hpi.bpt.chimera.jcore.MockProvider;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class StartQueryTest {

    JSONArray startQueriesArray;

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Before
    public void setup() throws IOException {
        String path = "src/test/resources/json/exampleStartQuery.json";
        String startQueryJson = FileUtils.readFileToString(new File(path));
        JSONObject startQuery = new JSONObject(startQueryJson);
        startQueriesArray = startQuery.getJSONArray("startconditions");
    }

    private static JSONObject getSingleStartQuery(String query, List<JSONObject> pathMappings) {
        JSONObject startQuery = new JSONObject();
        startQuery.put("query", query);
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
        StartQuery startQuery = new StartQuery(startQueriesArray.getJSONObject(0));
        assertEquals(1, startQuery.getQueryParts().size());
        assertEquals("SELECT * FROM anEvent", startQuery.getQuery());
    }

    private List<DataClass> getTestDataclasses() {
        List<DataAttribute> dataAttributes = MockProvider.mockDataAttributes(
                Arrays.asList("Name", "Age"), Arrays.asList("1", "2"), Arrays.asList(1, 2));
        List<List<String>> states = Arrays.asList(Arrays.asList("init"));
        return MockProvider.mockDataClasses(
                Arrays.asList("Data"), Arrays.asList(dataAttributes), states);
    }

    @Test
    public void testStartQuerySaving() {
        StartQuery startQuery = new StartQuery(startQueriesArray.getJSONObject(0));
        int scenarioId = 123451337;
        List<DataClass> dataClasses = getTestDataclasses();
        startQuery.save(scenarioId, dataClasses);
        DbStartQuery dbStartQuery = new DbStartQuery();
        assertEquals(Arrays.asList("SELECT * FROM anEvent"), dbStartQuery.getStartQueries(123451337));
        List<String> queryIds = getQueryIds(scenarioId);
        assertEquals(2, queryIds.size());
        List<StartQueryPart> pathMappings = dbStartQuery.loadStartQueryParts(queryIds.get(0),
                123451337);
        assertEquals(1, pathMappings.size());
        // HERE assert values of start query parts
    }

    private List<String> getQueryIds(int scenarioId) {
        String getQueryIds = "SELECT * FROM startquery WHERE scenario_id = %d;";
        getQueryIds = String.format(getQueryIds, scenarioId);
        return new DbObject().executeStatementReturnsListString(getQueryIds, "id");
    }

    @Test
    public void testMultipleStartQueriesSaving() {
        List<DataClass> dataClasses = getTestDataclasses();
        List<StartQuery> startQueries = StartQuery.parseStartQueries(startQueriesArray);
        assertEquals(2, startQueries.size());
        int scenarioId = 12345;
        startQueries.forEach(x -> x.save(scenarioId, dataClasses));

        DbStartQuery dbStartQuery = new DbStartQuery();
        List<String> expected = Arrays.asList("SELECT * FROM anEvent", "SELECT * FROM anotherEvent");
        assertTrue(expected.containsAll(dbStartQuery.getStartQueries(12345))
                && dbStartQuery.getStartQueries(12345).containsAll(expected));

        // TODO also assert values
    }

    @Test
    public void testStartQueryFromScenario() throws IOException, JAXBException {
        String path = "src/test/resources/Scenarios/StartScenario.json";
        String json = FileUtils.readFileToString(new File(path));
        ScenarioData scenarioData = new ScenarioData(json);
        scenarioData.save();

        DbCaseStart caseStart = new DbCaseStart();
        List<String> requestKeys =  caseStart.getRequestKeys(scenarioData.getId());
        assertEquals(2, requestKeys.size());
    }
}