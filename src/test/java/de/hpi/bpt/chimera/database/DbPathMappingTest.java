package de.hpi.bpt.chimera.database;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;

/**
 *
 */
public class DbPathMappingTest extends AbstractDatabaseDependentTest {

    @Test
    public void testMappingSaving() throws IOException {
        ScenarioTestHelper.createScenarioInstance("src/test/resources/Scenarios/JsonPathWebserviceScenarioGet.json");
        Map<Integer, String> expectedPathMap = buildExpectedPathMap();
		DbPathMapping dbPathMapping = new DbPathMapping();

		DbObject dbConn = new DbObject();
		int serviceTaskNodeId = dbConn.executeStatementReturnsInt(
		        String.format("SELECT * FROM controlnode WHERE modelid = '%s'",
		                "ServiceTask_165z0yh"), "id");

		assertEquals("The jsonpath mapping has not been saved correctly.",
		        expectedPathMap,
		        dbPathMapping.getPathsForAttributesOfControlNode(serviceTaskNodeId));
    }

    private Map<Integer, String> buildExpectedPathMap() {
        DbObject dbConn = new DbObject();
        int attrId1 = dbConn.executeStatementReturnsInt(
                String.format("SELECT * FROM dataattribute WHERE name = '%s'", "attr1"), "id");

        int attrId2 = dbConn.executeStatementReturnsInt(
                String.format("SELECT * FROM dataattribute WHERE name = '%s'", "attr2"), "id");
        Map<Integer, String> pathMap = new HashMap<>();
        pathMap.put(attrId1, "$.d");
        pathMap.put(attrId2, "$.a[0].b.prop1");
        return pathMap;
    }

}
