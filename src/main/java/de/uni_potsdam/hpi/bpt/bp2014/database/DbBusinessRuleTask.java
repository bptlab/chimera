package de.uni_potsdam.hpi.bpt.bp2014.database;

import org.apache.log4j.Logger;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.BusinessRuleTaskExecutionBehavior;

public class DbBusinessRuleTask extends DbObject {
	static Logger log = Logger.getLogger(BusinessRuleTaskExecutionBehavior.class.getName());

	public int getDecisionTableID(int controlNode_id) {
		String sql = "SELECT decisiontable_id FROM businessruletaskattribute WHERE controlnode_id = " + controlNode_id;
        return this.executeStatementReturnsInt(sql, "decisiontable_id");
	}
	
	public void createNewDecisionObject(int scenarioId, int controlNode_id,String result){
		
	}

	public int getDecisionClassId() {
		String sql = "SELECT id FROM dataclass WHERE name like \"Decision\"";
        return this.executeStatementReturnsInt(sql, "id");
	}
	
}
