package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class InputOutputSetTest {
    Logger LOGGER = Logger.getLogger(InputOutputSetTest.class);

    private Fragment fragmentWithIOSets;

    @Before
    public void setup() throws IOException, JAXBException {
        String path = "src/test/resources/fragments/InputOutputFragment.xml";
        File file = new File(path);
        String xml = FileUtils.readFileToString(file);
        int versionNumber = 0;
        String fragmentName = "aDummyName";
        String fragmentId = "aDummyId";
        fragmentWithIOSets = new Fragment(xml, versionNumber, fragmentName, fragmentId);
    }

    @Test
    public void testInputSets() throws JAXBException, IOException {
        assertEquals(4, fragmentWithIOSets.getInputSets().size());
    }

    @Test
    public void testSetsWhenNoDataObjectsExist() throws JAXBException {
        String path = "src/test/resources/fragments/fragmentWithoutInputOutput.xml";
        File file = new File(path);
        try{
            String xml = FileUtils.readFileToString(file);
            int versionNumber = 0;
            String fragmentName = "aDummyName";
            int scenarioId = 0;
            String fragmentId = "aDummyId";
            Fragment fragment = new Fragment(xml, versionNumber, fragmentName, fragmentId);
            assertEquals(0, fragment.getInputSets().size());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void testGetOutputSets() {
        // TODO(Maarten) model fragment which also has output sets
        assertEquals(4, fragmentWithIOSets.getOutputSets().size());
    }

    @Test
    public void testGetOutputSetsForNode() throws JAXBException, IOException {
        // TODO(Maarten) model fragment which also has output sets
        Assert.fail();
    }


    @Test
    public void testGetInputSetsForNode() throws IOException, JAXBException {
        List<AbstractDataControlNode> activities = fragmentWithIOSets.getAllActivities();
        assertEquals(1, activities.size());
        assertEquals(4, fragmentWithIOSets.getInputSetsForNode(
                activities.get(0)).size());
    }

    @Test
    public void testNoIOSets() throws JAXBException, IOException {
        String path = "src/test/resources/fragments/fragmentWithoutInputOutput.xml";
        File file = new File(path);
        String xml = FileUtils.readFileToString(file);
        int versionNumber = 0;
        String fragmentName = "aDummyName";
        int scenarioId = 0;
        String fragmentId = "aDummyId";
        Fragment fragment = new Fragment(xml, versionNumber, fragmentName, fragmentId);
        assertEquals(0, fragment.getInputSets().size());
    }

}
