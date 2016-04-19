package de.uni_potsdam.hpi.bpt.bp2014;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * This class should be used to hold helper methods which are needed in tests
 * which are concerned with fragments.
 */
public class FragmentTestHelper {
    // The class contains only static methods so there should be no public default constructor
    private FragmentTestHelper() {
    }

    /**
     * Loads a fragment xml from a file and returns it.
     * @param path FilePath to a fragment xml.
     * @return the respective fragment.
     * @throws IOException
     */
    public static Fragment createFragment(String path) throws IOException {
        File file = new File(path);
        try {
            String xml = FileUtils.readFileToString(file);
            int versionNumber = 0;
            String fragmentName = "aDummyName";
            String fragmentId = "aDummyId";
            return new Fragment(xml, versionNumber, fragmentName, fragmentId);

        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

}
