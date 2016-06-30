package de.uni_potsdam.hpi.bpt.bp2014.jconfiguration;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;

/**
 * This class executes configurations.
 */
public class Execution {

	/**
	 * This method marks the scenario as deleted if there are no running instances.
	 *
	 * @param scenarioId DatabaseID of the scenario that is supposed to get marked as deleted
	 * @return true if the scenario was deleted (else false)
	 * @throws Exception Running instances of the scenario exist.
	 */
	public boolean deleteScenario(int scenarioId) throws Exception {
		DbConfigurationConnection conn = new DbConfigurationConnection();
		conn.deleteScenario(scenarioId);
		EventDispatcher.unregisterCaseStartEvent(scenarioId);
		return true;
	}
}
