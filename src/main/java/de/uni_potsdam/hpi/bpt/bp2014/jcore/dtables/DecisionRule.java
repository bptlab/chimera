package de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbRule;

public class DecisionRule {
	
	int databaseId;
	private int decisionTableId;
	private Map<String,String> inputs = new HashMap<String,String>();
	private String outputEntry;
	private String outputDefinition;
	
	private DbRule dbRule = new DbRule();
	
	
	public DecisionRule(int decisionTableID, Integer ruleId) {
		this.decisionTableId = decisionTableID;
		this.databaseId = ruleId;
		
		inputs = dbRule.getInputMap(databaseId);
		outputEntry = dbRule.getOutputEntry(databaseId);
		outputDefinition = dbRule.getOutputDefinition(databaseId);	
		
	}
	
	public Map<String, String> getInputs() {
		return inputs;
	}

	public int getDecisionTableId() {
		return decisionTableId;
	}


	public String getOutputEntry() {
		return outputEntry;
	}


	public String getOutputDefinition() {
		return outputDefinition;
	}

}
