package de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDecisionTable;

public class DecisionTable extends Expression {

	int databaseID;
	HitPolicy hitPolicy = HitPolicy.UNIQUE;
	BuiltInAggregator aggregation = BuiltInAggregator.COLLECT;
	DecisionTableOrientation preferedOrientation;
	boolean isComplete = false;
	boolean isConsistent = false;
	List<DecisionRule> rules = new ArrayList<DecisionRule>();
	Set<String> inputColumns;

	private final DbDecisionTable dbDecisionTable = new DbDecisionTable();

	public DecisionTable(int decisionTableID) {
		databaseID = decisionTableID;
		hitPolicy = getDbHitPolicy();
		aggregation = getDbAggregation();
		isComplete = dbDecisionTable.getIsComplete(databaseID);
		isConsistent = dbDecisionTable.getIsConsistent(databaseID);
		rules = getDecisionRuleList();
		inputColumns = computeInputColumns();
	}

	private Set<String> computeInputColumns() {
		Set<String> inputSet =  new HashSet<String>();
		for (DecisionRule rule : rules){
			inputSet.addAll(rule.getInputs().keySet());
		}
		return inputSet;
	}

	private List<DecisionRule> getDecisionRuleList() {
		List<Integer> list = dbDecisionTable.getListOfRules(databaseID);
		List<DecisionRule> rulesList = new ArrayList<DecisionRule>();
		
		for ( Integer ruleId : list){
			DecisionRule rule = new DecisionRule(databaseID,ruleId);
			rulesList.add(rule);
		}
		return rulesList;
	}

	private BuiltInAggregator getDbAggregation() {
		BuiltInAggregator aggregation = null;
		String agg = dbDecisionTable.getAggregation(databaseID);
		switch (agg) {
		case "AVERAGE":
			aggregation = BuiltInAggregator.AVERAGE;
			break;
		case "COLLECT":
			aggregation = BuiltInAggregator.COLLECT;
			break;
		case "COUNT":
			aggregation = BuiltInAggregator.COUNT;
			break;
		case "MAX":
			aggregation = BuiltInAggregator.MAX;
			break;
		case "MIN":
			aggregation = BuiltInAggregator.MIN;
			break;
		case "SUM":
			aggregation = BuiltInAggregator.SUM;
			break;

		}
		return aggregation;
	}


	private HitPolicy getDbHitPolicy() {
		HitPolicy hPolicy = null;
		String policy = dbDecisionTable.getHitPolicy(databaseID);
		switch (policy) {
		case "UNIQUE":
			hPolicy = HitPolicy.UNIQUE;
			break;
		case "ANY":
			hPolicy = HitPolicy.ANY;
			break;
		case "FIRST":
			hPolicy = HitPolicy.FIRST;
			break;
		case "OUTPUT_ORDER":
			hPolicy = HitPolicy.OUTPUT_ORDER;
			break;
		case "PRIORITY":
			hPolicy = HitPolicy.PRIORITY;
			break;
		case "RULE_ORDER":
			hPolicy = HitPolicy.RULE_ORDER;
			break;
		case "UNORDERED":
			hPolicy = HitPolicy.UNORDERED;
			break;

		}
		return hPolicy;
	}
	 
	public List<DecisionRule> getRules(){
		return rules;
	}

	public HitPolicy getHitPolicy() {
		return hitPolicy;
	}

	public BuiltInAggregator getAggregation() {
		return aggregation;
	}
}
