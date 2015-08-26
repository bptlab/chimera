package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbBusinessRuleTask;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables.DecisionTable;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables.RuleEvaluator;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables.SimpleEvaluator;

public class BusinessRuleTaskExecutionBehavior extends TaskExecutionBehavior {

	/**
     * DB Connection class.
     */
	DbBusinessRuleTask dbBusinessRuleTask = new DbBusinessRuleTask();
	
	RuleEvaluator evaluator = new SimpleEvaluator();
	
	
	
	public BusinessRuleTaskExecutionBehavior(int activityInstance_id,
			ScenarioInstance scenarioInstance,
			ControlNodeInstance controlNodeInstance) {
		super(activityInstance_id, scenarioInstance, controlNodeInstance);
		
	}
	
	@Override
    public void execute() {
		System.out.println("Start BusinessRuleTaskExecution");
		int decisionTableId = dbBusinessRuleTask.getDecisionTableID(controlNodeInstance.getControlNode_id());
		DecisionTable table = new DecisionTable(decisionTableId);
		
        String result = evaluator.evaluate(table.getRules(), table.getHitPolicy(),table.getAggregation(),scenarioInstance);
        System.out.println("table result: "+result);
        int decisionClassId = dbBusinessRuleTask.getDecisionClassId();
        int scenario_id = this.scenarioInstance.getScenario_id();
        int scenarioInstance_id = this.scenarioInstance.getScenarioInstance_id();
   
        Connector conn = new Connector();
        int dataObjectId = conn.getDataObjectID(scenario_id, "Decision");
    		
        if(dataObjectId == -1){
        	dataObjectId = conn.insertDataObjectIntoDatabase("Decision", decisionClassId, scenario_id, 1);
        } 
        
        
        DataObjectInstance dataObjectInstance = new DataObjectInstance(dataObjectId, scenario_id, scenarioInstance_id, this.scenarioInstance);
        //checks if dataObjectInstance is locked
        LinkedList<DataAttributeInstance> attributes = dataObjectInstance.getDataAttributeInstances();
        for(DataAttributeInstance attribute: attributes){
        	if(attribute.name.equals("Result")){
        		attribute.setValue(result);
        	}
        }
        if (dataObjectInstance.getOnChange()) {
            this.scenarioInstance.getDataObjectInstancesOnChange().add(dataObjectInstance);
        } else {
            this.scenarioInstance.getDataObjectInstances().add(dataObjectInstance);
        }
        System.out.println("decision object created");
        
        this.setCanTerminate(true);
    }



}
