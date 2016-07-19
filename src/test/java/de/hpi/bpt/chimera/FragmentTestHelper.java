package de.hpi.bpt.chimera;

import de.hpi.bpt.chimera.jcomparser.saving.Fragment;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

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
            return new Fragment(xml, versionNumber, fragmentName);

        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

}
