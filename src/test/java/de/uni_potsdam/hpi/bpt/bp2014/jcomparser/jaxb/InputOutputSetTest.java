package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class InputOutputSetTest {
    Logger LOGGER = Logger.getLogger(InputOutputSetTest.class);
    @Test
    public void testInputOutputSet() throws JAXBException {
        String path = "src/test/resources/fragments/InputOutputFragment.xml";
        File file = new File(path);
        try {
            String xml = FileUtils.readFileToString(file);
            int versionNumber = 0;
            String fragmentName = "aDummyName";
            int scenarioId = 0;
            String fragmentId = "aDummyId";
            Fragment fragment = new Fragment(xml, versionNumber, fragmentName, fragmentId);
            assertEquals(4, fragment.getInputSets().size());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
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
}
