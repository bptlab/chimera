package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataAttribute;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataObjectInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AttributeTypeValidationTest {

    List<Integer> attrDbIds = new ArrayList<>();

    @After
    public void clearDatabase() {
        DbObject dbObject = new DbObject();
        String sql1 = "DELETE FROM dataattribute WHERE id = %d";
        String sql2 = "DELETE FROM dataattributeinstance WHERE id = %d";
        for (int id : attrDbIds) {
            dbObject.executeUpdateStatement(String.format(sql1, id));
            dbObject.executeUpdateStatement(String.format(sql2, id));
        }
    }

    @Test
    public void testSettingOfCorrectValues() {
        DataAttributeInstance[] instances = mockInstances();

        try {
            instances[0].setValue("aString");
            instances[1].setValue(true);
            instances[2].setValue(1337);
            instances[3].setValue(3.14D);
            instances[4].setValue("anEnum");
            instances[5].setValue("aClass");
            instances[6].setValue("a:Da:te:00");
            assert(true);
        } catch (Exception e) {
            fail("Typecheck for data attribute values failed.");
        }
    }

    @Test
    public void testSettingOfWrongValues() {
        DataAttributeInstance[] instances = mockInstances();
        int count = 0;
        for(DataAttributeInstance instance : instances) {
            try {
                instance.setValue(new Object());
            } catch (IllegalArgumentException e) {
                count++;
            }
        }
        assertEquals("Typecheck for data attribute values allowed values "
                + "that should not be allowed.", 7, count);
    }


    private DataAttributeInstance[] mockInstances() {
        DataObjectInstance objectInstance = createNiceMock(DataObjectInstance.class);
        int dataObjectInstanceId = -1;

        DataAttribute attribute0 = new DataAttribute("aName", "String");
        int attributeDatabaseId0 = attribute0.save();
        attrDbIds.add(attributeDatabaseId0);
        DataAttributeInstance instance0 = new DataAttributeInstance(
                attributeDatabaseId0, dataObjectInstanceId, objectInstance);

        DataAttribute attribute1 = new DataAttribute("aName", "Boolean");
        int attributeDatabaseId1 = attribute1.save();
        attrDbIds.add(attributeDatabaseId1);
        DataAttributeInstance instance1 = new DataAttributeInstance(
                attributeDatabaseId1, dataObjectInstanceId, objectInstance);

        DataAttribute attribute2 = new DataAttribute("aName", "Integer");
        int attributeDatabaseId2 = attribute2.save();
        attrDbIds.add(attributeDatabaseId2);
        DataAttributeInstance instance2 = new DataAttributeInstance(
                attributeDatabaseId2, dataObjectInstanceId, objectInstance);

        DataAttribute attribute3 = new DataAttribute("aName", "Double");
        int attributeDatabaseId3 = attribute3.save();
        attrDbIds.add(attributeDatabaseId3);
        DataAttributeInstance instance3 = new DataAttributeInstance(
                attributeDatabaseId3, dataObjectInstanceId, objectInstance);

        DataAttribute attribute4 = new DataAttribute("aName", "Enum");
        int attributeDatabaseId4 = attribute4.save();
        attrDbIds.add(attributeDatabaseId4);
        DataAttributeInstance instance4 = new DataAttributeInstance(
                attributeDatabaseId4, dataObjectInstanceId, objectInstance);

        DataAttribute attribute5 = new DataAttribute("aName", "Class");
        int attributeDatabaseId5 = attribute5.save();
        attrDbIds.add(attributeDatabaseId5);
        DataAttributeInstance instance5 = new DataAttributeInstance(
                attributeDatabaseId5, dataObjectInstanceId, objectInstance);

        DataAttribute attribute6 = new DataAttribute("aName", "Date");
        int attributeDatabaseId6 = attribute6.save();
        attrDbIds.add(attributeDatabaseId6);
        DataAttributeInstance instance6 = new DataAttributeInstance(
                attributeDatabaseId6, dataObjectInstanceId, objectInstance);

        return new DataAttributeInstance[]{
                instance0,
                instance1,
                instance2,
                instance3,
                instance4,
                instance5,
                instance6
        };
    }
}
