package de.hpi.bpt.chimera.jcomparser.json;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.DbObject;

/**
 *
 */
public class EventTypeTest extends AbstractDatabaseDependentTest {

    @Test
    public void testEventType() throws JAXBException {
        File file = new File("src/test/resources/Scenarios/EventTypeScenario.json");
        try {
            String json = FileUtils.readFileToString(file);
            ScenarioData scenario = new ScenarioData(json);
            scenario.save();

            // Test saving of event type
            String sql = "SELECT * FROM dataclass WHERE id = 1 AND is_event = 1";
            DbObject dbObject = new DbObject();
            String dbName = dbObject.executeStatementReturnsString(sql, "name");
            assertEquals("Customer", dbName);

            // Test saving of attributes
            String sql2 = "SELECT * FROM dataattribute WHERE dataclass_id = 1";
            String dbAttributeName = dbObject.executeStatementReturnsString(sql2, "name");
            assertEquals(dbAttributeName, "Name");

        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
        }
    }

}
