package de.hpi.bpt.chimera.database;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;

/**
 *
 */
public class DbSelectedDataObjectsTest extends AbstractDatabaseDependentTest{

    @Test
    public void testSaveDataObjectSeletion() {
        DbSelectedDataObjects selected = new DbSelectedDataObjects();
        int scenarioInstanceId = 1;
        int activityInstanceId = 1;
        List<Integer> dataobjectsIds = Arrays.asList(1, 2, 3);
        selected.saveDataObjectSelection(scenarioInstanceId, activityInstanceId, dataobjectsIds);
        List<Integer> retrievedObjectIds = selected.getDataObjectSelection(
                scenarioInstanceId, activityInstanceId);
        assertEquals(dataobjectsIds, retrievedObjectIds);
    }
}