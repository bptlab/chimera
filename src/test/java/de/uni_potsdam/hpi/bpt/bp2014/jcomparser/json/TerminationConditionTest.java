package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class TerminationConditionTest {

    @Test
    public void testTerminationCondition() {

        try {
            File file = new File("src/test/resources/EventScenarios/TerminationConditionScenario.json");
            String json = FileUtils.readFileToString(file);

            Scenario scenario = new Scenario();
            scenario.initializeInstanceFromJson(json);

            ExecutionService service = ExecutionService.getInstance(scenario.getScenarioDbId());
            int instanceId = service.startNewScenarioInstance();

            // TODO find activity id
            int activityId = 0;


            //TODO assert termination condition not triggered
            assert(false);

            service.beginActivity(instanceId, activityId);
            service.terminateActivity(instanceId, activityId);

            //TODO assert termination condition triggered
            assert(false);

        } catch (IOException e) {
            e.printStackTrace();
            assert(false);
        }


    }

}
