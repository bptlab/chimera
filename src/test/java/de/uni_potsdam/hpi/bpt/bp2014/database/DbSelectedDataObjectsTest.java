package de.uni_potsdam.hpi.bpt.bp2014.database;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class DbSelectedDataObjectsTest {

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