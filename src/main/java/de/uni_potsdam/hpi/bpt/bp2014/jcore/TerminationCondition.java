package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbTerminationCondition;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class TerminationCondition {
    List<TerminationPart> terminationParts;

    public TerminationCondition(ScenarioInstance scenarioInstance) {
        DbTerminationCondition terminationCondition = new DbTerminationCondition();
        List<String> conditionSetKeys = terminationCondition.getConditionSetKeysForScenario(
                scenarioInstance.getScenarioId());
        this.terminationParts = conditionSetKeys.stream().
                map(TerminationPart::new).collect(Collectors.toList());
    }


    /**
     * Check the termination condition.
     * Get all termination condition and prove the condition for every condition set.
     *
     * @return true if the condition is true. false if not.
     */
    public boolean checkTerminationCondition(List<DataObjectInstance> dataObjectInstances) {
        for (TerminationPart terminationPart : this.terminationParts) {
            if (terminationPart.checkTermination(dataObjectInstances)) {
                return true;
            }
        }
        return false;
    }
}
