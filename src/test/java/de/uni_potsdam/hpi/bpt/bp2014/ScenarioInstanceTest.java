package de.uni_potsdam.hpi.bpt.bp2014;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.junit.Test;

/**
 * Created by jaspar.mang on 05.01.15.
 */
public class ScenarioInstanceTest {
    @Test
    public void testScenarioID(){
        ScenarioInstance scenarioInstance = new ScenarioInstance(1);
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        int scenarioInstance_ID = dbScenarioInstance.getScenarioInstanceID(1);
        assert(dbScenarioInstance.existScenario(1, scenarioInstance_ID));
    }
}
