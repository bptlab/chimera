package de.uni_potsdam.hpi.bpt.bp2014;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.Scenario;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        Scenario scenario = new Scenario();
        String json = FileUtils.readFileToString(file);
        scenario.initializeInstanceFromJson(json);
        int databaseId = scenario.getScenarioDbId();
        return new ScenarioInstance(databaseId);
    }
}
