package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 */
public class DbPathMappingTest {

    @After
    public void dropDatabase() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testMappingSaving() {
        try {
            ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance("src/test/resources/Scenarios/JsonPathWebserviceScenario.json");
            Map<Integer, String> expectedPathMap = buildExpectedPathMap();
            DbPathMapping dbPathMapping = new DbPathMapping();

            DbObject dbConn = new DbObject();
            int serviceTaskNodeId = dbConn.executeStatementReturnsInt(
                    String.format("SELECT * FROM controlnode WHERE model_id = '%s'", "ServiceTask_165z0yh"),
                    "id");

            assertEquals("The jsonpath mapping has not been saved correctly.",
                    expectedPathMap,
                    dbPathMapping.getPathsForAttributesOfControlNode(serviceTaskNodeId));
        } catch (IOException e) {
            fail("File could not be read.");
        }
    }

    private Map<Integer, String> buildExpectedPathMap() {
        DbObject dbConn = new DbObject();
        int attrId1 = dbConn.executeStatementReturnsInt(
                String.format("SELECT * FROM dataattribute WHERE name = '%s'", "attr1"),
                "id");
        int attrId2 = dbConn.executeStatementReturnsInt(
                String.format("SELECT * FROM dataattribute WHERE name = '%s'", "attr2"),
                "id");

        Map<Integer, String> pathMap = new HashMap<>();
        pathMap.put(attrId1, "$.foo");
        pathMap.put(attrId2, "$.bar['baz']");
        return pathMap;
    }

}
