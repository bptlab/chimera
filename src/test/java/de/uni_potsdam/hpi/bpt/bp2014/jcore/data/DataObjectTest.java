package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 */
public class DataObjectTest {
    @Test
    public void testNoDataObjectsAtStart() throws IOException {
        ScenarioTestHelper.createScenarioInstance("src/test/resources/Scenarios/DataObjectTestScenario.json");
        assertEquals(0, getDataObjectCount());
    }

    @Test
    public void testObjectCreationAndUpdate() throws IOException {
        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance("src/test/resources/Scenarios/DataObjectTestScenario.json");
        ScenarioTestHelper.executeActivityByName("create Old Class", instance);
        assertEquals(1, getDataObjectCount());
        ScenarioTestHelper.executeActivityByName("create New Class", instance);
        assertEquals(2, getDataObjectCount());
    }

    @Test
    public void testMultiObjectCreation() {
        Assert.fail();
    }

    @Test
    public void testInitializationViaCaseStart() {
        Assert.fail();
    }

    private int getDataObjectCount() {
        return new DbDataObject().executeStatementReturnsInt(
                "SELECT COUNT(*) as count FROM dataobject", "count");
    }

}