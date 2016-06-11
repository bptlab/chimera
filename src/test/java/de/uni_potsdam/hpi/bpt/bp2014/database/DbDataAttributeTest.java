package de.uni_potsdam.hpi.bpt.bp2014.database;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataAttribute;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataClass;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class DbDataAttributeTest extends AbstractDatabaseDependentTest {
    private String dataclassString;

    @Before
    public void setupDataClass() {
        dataclassString = new JSONObject()
                .put("name", "Reise")
                .put("_id", "801101005")
                .put("attributes", new JSONArray()
                        .put(new JSONObject()
                                .put("name", "Beginn")
                                .put("datatype", "String")
                                .put("_id", "editorId1")
                        )
                        .put(new JSONObject()
                                .put("name", "Ende")
                                .put("datatype", "String")
                                .put("_id", "editorId2"))
                        .put(new JSONObject()
                                .put("name", "Gesamtkosten")
                                .put("datatype", "Float")
                                .put("_id", "editorId3"))
                ).toString();
    }

    @Test
    public void testAttributeSaving() {
        int testScenarioId = 1;
        DataClass dClass = new DataClass(dataclassString);
        dClass.save(testScenarioId);
        DataAttribute attribute = dClass.getAttributes().get(0);
        DbDataAttributeInstance instance = new DbDataAttributeInstance();
        String dbType = instance.getType(attribute.getAttributeDatabaseId());
        String dbName = instance.getName(attribute.getAttributeDatabaseId());
        assertEquals("The attributeName has not been saved correctly", "Beginn", dbName);
        assertEquals("The attributeType has not been saved correctly", "String", dbType);
    }
}
