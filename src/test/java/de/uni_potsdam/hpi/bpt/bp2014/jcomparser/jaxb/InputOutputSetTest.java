package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.DataClass;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.DatabaseFragment;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 */
public class InputOutputSetTest {
    @Test
    public void testCorrectInputOutput() {
        File file = new File("src/test/resources/fragments/InputOutputFragment.xml");
        try {
            String xml = FileUtils.readFileToString(file);
            DatabaseFragment frag = new DatabaseFragment();
            String fragmentName = "someName";
            int versionNumber  = 1;
            int fragmentId = 1;
            Map<Long, DataClass> dataClasses = new HashMap<>();
            frag.initialize(xml, versionNumber, fragmentName, fragmentId,
                    dataClasses);
            assertEquals(2, frag.getInputSets().size());
            assertEquals(2, frag.getOutputSets().size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
