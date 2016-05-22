package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataAttribute;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataClass;
import org.easymock.EasyMock;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 *
 */
public class MockProvider {
    public static List<DataAttribute> mockDataAttributes(
            List<String> names, List<String> editorIds, List<Integer> databaseIds) {
        assert names.size() == editorIds.size() : "names and ids must have the same length";

        List<DataAttribute> dataAttributes = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            DataAttribute dataAttribute = createNiceMock(DataAttribute.class);
            expect(dataAttribute.getDataAttributeName()).andReturn(names.get(i)).anyTimes();
            expect(dataAttribute.getEditorId()).andReturn(editorIds.get(i)).anyTimes();
            expect(dataAttribute.getDataAttributeID()).andReturn(databaseIds.get(i)).anyTimes();
            replay(dataAttribute);
            dataAttributes.add(dataAttribute);
        }

        return dataAttributes;
    }

    public static List<DataClass> mockDataClasses(
            List<String> names, List<List<DataAttribute>> dataAttributes) {
        assert names.size() == dataAttributes.size() : "names and attributes must have same length";
        List<DataClass> dataClasses = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            DataClass dataClass = createNiceMock(DataClass.class);
            expect(dataClass.getName()).andReturn(names.get(i)).anyTimes();
            expect(dataClass.getAttributes()).andReturn(dataAttributes.get(i)).anyTimes();
            expect(dataClass.getDatabaseId()).andReturn(i).anyTimes();
            for (DataAttribute dataAttribute : dataAttributes.get(i)) {
                String dataAttributeName = dataAttribute.getDataAttributeName();
                Optional<DataAttribute> dataAttributeOptional = Optional.of(dataAttribute);
                expect(dataClass.getDataAttributeByName(dataAttributeName))
                        .andReturn(dataAttributeOptional).anyTimes();
            }
            replay(dataClass);
            dataClass.setAttributes(dataAttributes.get(i));
            dataClasses.add(dataClass);
        }
        return dataClasses;
    }

    public static List<DataClass> mockDataClasses(List<String> names) {
        List<List<DataAttribute>> emptyAttributes = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            emptyAttributes.add(new ArrayList<>());
        }
        return mockDataClasses(names, emptyAttributes);
    }
}
