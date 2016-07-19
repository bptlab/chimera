package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class InputOutputSetTest {
    Logger LOGGER = Logger.getLogger(InputOutputSetTest.class);

    private Fragment fragmentWithIOSets;
    private Fragment fragmentWithoutIOSets;
    private List<AbstractDataControlNode> activities;

    @Before
    public void setup() throws IOException, JAXBException {
        final int versionNumber = 0;
        final String fragmentName = "aDummyName";

        String path1 = "src/test/resources/fragments/InputOutputFragment.xml";
        File file1 = new File(path1);
        String xml1 = FileUtils.readFileToString(file1);
        fragmentWithIOSets = new Fragment(xml1, versionNumber, fragmentName);
        activities = fragmentWithIOSets.getAllActivities();
        assert(activities.size() == 3);

        String path2 = "src/test/resources/fragments/fragmentWithoutInputOutput.xml";
        File file2 = new File(path2);
        String xml2 = FileUtils.readFileToString(file2);
        fragmentWithoutIOSets = new Fragment(xml2, versionNumber, fragmentName);
    }

    @Test
    public void testInputSets() {
        assertEquals(5, fragmentWithIOSets.getInputSets().size());
    }

    @Test
    public void testSetsWhenNoDataObjectsExist() {
        assertEquals(0, fragmentWithoutIOSets.getInputSets().size());
    }

    @Test
    public void testGetOutputSets() {
        assertEquals(5, fragmentWithIOSets.getOutputSets().size());
    }

    @Test
    public void testGetOutputSetsForNode() {
        Map<String, DataNode> idToDataNode = new HashMap<>();
        for (DataNode dataNode : fragmentWithIOSets.getDataNodes()) {
            idToDataNode.put(dataNode.getId(), dataNode);
        }
        assertEquals(1, fragmentWithIOSets.getOutputSetsForNode(activities.get(0), idToDataNode).size());
        assertEquals(4, fragmentWithIOSets.getOutputSetsForNode(activities.get(1), idToDataNode).size());
        assertEquals(0, fragmentWithIOSets.getOutputSetsForNode(activities.get(2), idToDataNode).size());
    }


    @Test
    public void testGetInputSetsForNode() {
        assertEquals(0, fragmentWithIOSets.getInputSetsForNode(activities.get(0)).size());
        assertEquals(1, fragmentWithIOSets.getInputSetsForNode(activities.get(1)).size());
        assertEquals(4, fragmentWithIOSets.getInputSetsForNode(activities.get(2)).size());
    }

    @Test
    public void testNoIOSets() {
        assertEquals(0, fragmentWithoutIOSets.getInputSets().size());
    }

}
