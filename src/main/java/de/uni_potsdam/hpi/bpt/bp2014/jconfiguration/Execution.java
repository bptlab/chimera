package de.uni_potsdam.hpi.bpt.bp2014.jconfiguration;

import java.util.List;
/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

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
