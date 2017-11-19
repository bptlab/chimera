
package de.hpi.bpt.chimera.jcore;

import java.util.List;

import de.hpi.bpt.chimera.database.DbScenario;
import de.hpi.bpt.chimera.database.DbScenarioInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

/**
 * Represents a scenario. 
 * TODO: Extend this with termination condition etc.
 * @author Marcin.Hewelt
 */
public class Scenario {
  
  private final int scenarioId;
  private final String name;
  
  /**
   * Constructor, reads scenario from database.
   */
  Scenario(int id, String name) {
    this.scenarioId = id;
    this.name = name;
  }
  
  /**
   * Is this Scenario in the database and not marked 'deleted'?
   * @return true, if scenario exists
   */
  public Boolean exists() {
    return new DbScenario().existScenario(scenarioId);
  }
  
  /** 
   * Deletes this scenario, running cases, and unsubscribes case start queries. 
   * TODO: A return value, indicating whether this succeeded would be nice extension.
   */
  public void delete() {
    new DbScenario().deleteScenario(scenarioId);
    deleteCases();
		EventDispatcher.unregisterCaseStartEvent(scenarioId);
  }

  /**
   * Deletes cases of this scenario.
   */
  private void deleteCases() {
    List<Integer> caseIds = new DbScenarioInstance().getScenarioInstances(scenarioId);
    DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    for (Integer caseId : caseIds) {
      dbScenarioInstance.delete(caseId);
    }
    
  }

}
