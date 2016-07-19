package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataAttribute;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataClass;
import org.easymock.EasyMock;

import java.util.*;

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
            expect(dataAttribute.getName()).andReturn(names.get(i)).anyTimes();
            expect(dataAttribute.getEditorId()).andReturn(editorIds.get(i)).anyTimes();
            expect(dataAttribute.getId()).andReturn(databaseIds.get(i)).anyTimes();
            replay(dataAttribute);
            dataAttributes.add(dataAttribute);
        }

        return dataAttributes;
    }

    public static List<DataClass> mockDataClasses(
            List<String> names, List<List<DataAttribute>> dataAttributes,
            List<List<String>> states) {
        assert names.size() == dataAttributes.size() : "names and attributes must have same length";
        List<DataClass> dataClasses = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            DataClass dataClass = createNiceMock(DataClass.class);
            expect(dataClass.getName()).andReturn(names.get(i)).anyTimes();
            expect(dataClass.getAttributes()).andReturn(dataAttributes.get(i)).anyTimes();
            expect(dataClass.getDatabaseId()).andReturn(i + 1).anyTimes();

            for (DataAttribute dataAttribute : dataAttributes.get(i)) {
                String dataAttributeName = dataAttribute.getName();
                Optional<DataAttribute> dataAttributeOptional = Optional.of(dataAttribute);
                expect(dataClass.getDataAttributeByName(dataAttributeName))
                        .andReturn(dataAttributeOptional).anyTimes();
            }

            Map<String, Integer> stateToDatabaseId = new HashMap<>();
            List<String> statesOfDataclass = states.get(i);
            for (int j = 0; j < statesOfDataclass.size(); j++) {
                stateToDatabaseId.put(statesOfDataclass.get(j), j + 1);
            }
            expect(dataClass.getStateToDatabaseId()).andReturn(stateToDatabaseId).anyTimes();

            replay(dataClass);
            dataClass.setAttributes(dataAttributes.get(i));
            dataClasses.add(dataClass);
        }
        return dataClasses;
    }



    public static List<DataClass> mockDataClasses(List<String> names) {
        List<List<DataAttribute>> emptyAttributes = new ArrayList<>();
        List<List<String>> emptyStates = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            emptyAttributes.add(new ArrayList<>());
            emptyStates.add(new ArrayList<>());
        }
        return mockDataClasses(names, emptyAttributes, emptyStates);
    }

    public static List<DataNode> mockDataNodes(List<String> dataclassNames, List<String> states) {
        List<DataNode> dataNodes = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            DataNode dataNode = EasyMock.createNiceMock(DataNode.class);
            expect(dataNode.getState()).andReturn(states.get(i)).anyTimes();
            expect(dataNode.getName()).andReturn(dataclassNames.get(i)).anyTimes();
            replay(dataNode);
            dataNodes.add(dataNode);
        }
        return dataNodes;
    }
}
