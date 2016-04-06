package de.uni_potsdam.hpi.bpt.bp2014;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.ScenarioData;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        try {
            ScenarioData scenario  = new ScenarioData(json);
            int databaseId = scenario.save();
            return new ScenarioInstance(databaseId);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * Finds an activity instance by name and terminates the activity instance
     * in the given scenario instance.
     * @param activityName name of the activity belonging to the activity instance.
     * @param scenarioInstance the scenario instance containing the activity instance.
     */
    public static void terminateActivityInstanceByName(String activityName, ScenarioInstance scenarioInstance) {
        List<AbstractControlNodeInstance> controlNodeInstances =
                scenarioInstance.getEnabledControlNodeInstances();

        ActivityInstance instance = (ActivityInstance)
                controlNodeInstances.stream()
                .filter(a -> ((ActivityInstance) a).getLabel().equals(activityName))
                .findFirst().get();

        instance.terminate();
    }

    /**
     *
     */
    public static boolean isActivityActivated(String activityName, ScenarioInstance scenarioInstance) {
        Optional<AbstractControlNodeInstance> activity = findActivityInstanceByName(
                activityName, scenarioInstance);
        return activity.isPresent();
    }

    public static Optional<AbstractControlNodeInstance> findActivityInstanceByName(
            String name, ScenarioInstance scenarioInstance) {
        List<AbstractControlNodeInstance> controlNodeInstances =
                scenarioInstance.getEnabledControlNodeInstances();

        return controlNodeInstances.stream()
                .filter(ActivityInstance.class::isInstance)
                .filter(a -> ((ActivityInstance) a).getLabel().equals(name))
                .findFirst();
    }

    public static void beginActivityByName(String name, ScenarioInstance scenarioInstance) {
        Optional<AbstractControlNodeInstance> activity = findActivityInstanceByName(
                name, scenarioInstance);
        if (activity.isPresent()) {
            ActivityInstance casted = (ActivityInstance) activity.get();
            casted.begin();
        }
    }
}
