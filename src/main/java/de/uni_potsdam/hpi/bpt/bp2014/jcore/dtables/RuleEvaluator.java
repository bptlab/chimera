package de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables;

import java.util.List;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

public interface RuleEvaluator {

	public String evaluate(List<DecisionRule> rules, HitPolicy hitPolicy,
			BuiltInAggregator builtInAggregator,
			ScenarioInstance scenarioInstance);
}
