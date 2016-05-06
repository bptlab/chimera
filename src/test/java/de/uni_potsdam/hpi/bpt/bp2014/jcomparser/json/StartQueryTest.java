package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbStartQuery;
import org.easymock.EasyMock;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

/**
 *
 */
public class StartQueryTest {

    static JSONObject startQueryJson;

    @BeforeClass
    public static void setUpStartQuery() {
        startQueryJson = new JSONObject();
        startQueryJson.put("query", "SELECT * FROM Event");
        JSONObject pathMappings = new JSONObject();
        pathMappings.put("anId", "$a.b");
        pathMappings.put("anotherId", "$foo.bar");
        startQueryJson.put("mappings", pathMappings);
    }

    @Test
    public void testStartQueryCreation() {
        StartQuery startQuery = new StartQuery(startQueryJson);
        assertEquals(2, startQuery.getAttributeToJsonPath().size());
        assertEquals("SELECT * FROM Event", startQuery.getQuery());
    }

    @Test
    public void testStartQuerySaving() {
        StartQuery startQuery = new StartQuery(startQueryJson);
        List<DataAttribute> attributes = createDataAttributeMocks();
        startQuery.save(attributes, 123451337);

        DbStartQuery dbStartQuery = new DbStartQuery();
        assertEquals("SELECT * FROM Event", dbStartQuery.getStartQuery(123451337));
        Map<Integer, String> pathMapping = dbStartQuery.getPathMappings(123451337);
        assertEquals(2, pathMapping.size());
        assertEquals("$a.b", pathMapping.get(1));
        assertEquals("$foo.bar", pathMapping.get(2));
    }

    @Test
    public void testStartQueryScenarioIntegration() {
        Assert.fail();
    }

    private List<DataAttribute> createDataAttributeMocks() {
        DataAttribute attribute1 = EasyMock.createNiceMock(DataAttribute.class);
        DataAttribute attribute2 = EasyMock.createNiceMock(DataAttribute.class);
        expect(attribute1.getEditorId()).andReturn("anId").anyTimes();
        expect(attribute1.getDataAttributeID()).andReturn(1).anyTimes();

        expect(attribute2.getEditorId()).andReturn("anotherId").anyTimes();
        expect(attribute2.getDataAttributeID()).andReturn(2).anyTimes();
        replay(attribute1, attribute2);
        return Arrays.asList(attribute1, attribute2);
    }
}