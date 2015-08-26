package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbRule extends DbObject {
	
	public String getInputEntry(int ruleId){
		String sql = "SELECT inputEntry FROM rulecondition WHERE ruleID = " + ruleId;
        return this.executeStatementReturnsString(sql, "inputEntry");
	}
	
	public String getInputExpression(int ruleId){
		String sql = "SELECT inputExpression FROM rulecondition WHERE ruleID = " + ruleId;
        return this.executeStatementReturnsString(sql, "inputExpression");
	}
	
	public String getOutputEntry(int ruleId){
		String sql = "SELECT outputEntry FROM ruleconclusion WHERE ruleID = " + ruleId;
        return this.executeStatementReturnsString(sql, "outputEntry");
	}
	
	public String getOutputDefinition(int ruleId){
		String sql = "SELECT outputDefinition FROM ruleconclusion WHERE ruleID = " + ruleId;
        return this.executeStatementReturnsString(sql, "outputDefinition");
	}

	public Map<String,String> getInputMap(int ruleId) {
		String sql = "SELECT inputExpression,inputEntry FROM rulecondition WHERE ruleID = " + ruleId;
		List<HashMap<String, Object>> list = new ArrayList<>();
		list = this.executeStatementReturnsHashMap(sql);

		Map map = new HashMap<String,String>();
		for (Map condition : list){
			map.put(condition.get("inputExpression"), condition.get("inputEntry"));
			
			
		}
		return map;
		
	}
}
