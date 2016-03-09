package de.uni_potsdam.hpi.bpt.bp2014;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * This class should be used to hold helper methods which are needed in tests
 * which are concerned with scenarios.
 */
public class ScenarioTestHelper {
    // The class contains only static methods so there should be no public default constructor
    private ScenarioTestHelper() {
    }

    /**
     * Loads a scenario Json from file, and returns the initialized scenario instance
     * @param path FilePath to an scenario json.
     * @return An initialized scenario instance
     * @throws IOException
     */
    public static ScenarioInstance createScenarioInstance(String path) throws IOException {
        File file = new File(path);
        String json = FileUtils.readFileToString(file);
        ScenarioData scenario = new ScenarioData(json);
        int databaseId = scenario.save();
        return new ScenarioInstance(databaseId);
    }
}
