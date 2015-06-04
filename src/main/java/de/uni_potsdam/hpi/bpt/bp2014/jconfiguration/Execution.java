package de.uni_potsdam.hpi.bpt.bp2014.jconfiguration;

import java.util.List;


/**
 * This class executes configurations.
 */
public class Execution {

    /**
     * This method marks the scenario as deleted as far as there are no running instances thereof.
     *
     * @param scenarioID DatabaseID of the scenario that is supposed to get marked as deleted
     * @throws Exception Running instances of the scenario exist.
     */
    public boolean deleteScenario(int scenarioID) throws Exception {
        DbConfigurationConnection conn = new DbConfigurationConnection();
        List<Integer> runningInstances = conn.getRunningScenarioInstances(scenarioID);
        if (runningInstances.size() > 0) {
            return false;
        } else {
            conn.deleteScenario(scenarioID);
            return true;
        }
    }
}
