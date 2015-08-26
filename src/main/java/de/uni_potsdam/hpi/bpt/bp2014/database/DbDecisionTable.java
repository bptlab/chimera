package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.util.List;

import org.apache.log4j.Logger;


public class DbDecisionTable extends DbObject {
    static Logger log = Logger.getLogger(DbControlNode.class.getName());


	public String getHitPolicy(int id){
		String sql = "SELECT hitPolicy FROM decisiontable WHERE id = " + id;
        return this.executeStatementReturnsString(sql, "hitPolicy");
		
	}
	
	public String getAggregation(int id){
		String sql = "SELECT aggregation FROM decisiontable WHERE id = " + id;
        return this.executeStatementReturnsString(sql, "aggregation");
		
	}

	public boolean getIsComplete(int id){
		String sql = "SELECT isComplete FROM decisiontable WHERE id = " + id;
        return this.executeStatementReturnsBoolean(sql, "isComplete");
		
	}
	
	public boolean getIsConsistent(int id){
		String sql = "SELECT isConsistent FROM decisiontable WHERE id = " + id;
        return this.executeStatementReturnsBoolean(sql, "isConsistent");
		
	}

	public List<Integer> getListOfRules(int id) {
		String sql = "SELECT id FROM rule WHERE decisiontable_id = " + id +" order by order_id";
        return this.executeStatementReturnsListInt(sql, "id");
	}
}
