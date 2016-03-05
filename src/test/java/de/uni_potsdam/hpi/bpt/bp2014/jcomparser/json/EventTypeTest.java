package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class EventTypeTest {

    @After
    public void deleteEventType() {
        DbObject dbObject = new DbObject();
        String sql3 = "DELETE FROM EventType WHERE id = '56dabdeb02c19ae942450d85'";
        String sql4 = "DELETE FROM EventTypeAttribute WHERE type = 'Customer'";

        dbObject.executeUpdateStatement(sql3);
        dbObject.executeUpdateStatement(sql4);
    }

    @Test
    public void testEventType() {

        File file = new File("src/test/resources/EventScenarios/EventTypeScenario.json");
        try {
            String json = FileUtils.readFileToString(file);

            Scenario scenario = new Scenario();
            scenario.initializeInstanceFromJson(json);

            // Test saving of event type
            String sql = "SELECT * FROM EventType WHERE id = '56dabdeb02c19ae942450d85'";
            DbObject dbObject = new DbObject();
            String dbName = dbObject.executeStatementReturnsString(sql, "name");
            assertEquals(dbName, "Customer");

            // Test saving of attributes
            String sql2 = "SELECT * FROM EventTypeAttribute WHERE type = 'Customer'";
            String dbAttributeName = dbObject.executeStatementReturnsString(sql2, "name");
            assertEquals(dbAttributeName, "name");

        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
        }
    }

}
