package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Fragment;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class InputOutputSetTest {
    @Test
    public void testInputOutputSet() {
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
            e.printStackTrace();
        }
    }
}
