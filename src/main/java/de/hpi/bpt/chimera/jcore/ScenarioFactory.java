package de.hpi.bpt.chimera.jcore;

import de.hpi.bpt.chimera.database.DbScenario;

/**
 * Factory for Scenarios. 
 */
public class ScenarioFactory {
  
  /**
   * Creates Scenarios only if they exist in the database.
   */
  public static Scenario createScenarioFromDatabase(int id) {
    DbScenario dbScenario = new DbScenario();
    if (dbScenario.existScenario(id)) {
      return new Scenario(id, dbScenario.getScenarioName(id));
    } else {
			// TODO replace with Null Object
      return null;
    }
  }

}
