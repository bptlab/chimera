package de.hpi.bpt.chimera.jconfiguration;

import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

/**
 * This class executes configurations.
 */
public class Execution {

	/**
	 * This method marks the scenario as deleted. Also unregisters Case start queries.
	 * This is also possible with running instances, although the user interface does not
	 * provide a functionality for it while there are instances running.
	 * It can still be called via REST for dev purposes.
	 *
	 * @param scenarioId Database Id of the scenario that is supposed to get marked as deleted
	 */
	public void deleteScenario(int scenarioId) {
		DbConfigurationConnection conn = new DbConfigurationConnection();
		conn.deleteScenario(scenarioId);
		EventDispatcher.unregisterCaseStartEvent(scenarioId);
	}
}
