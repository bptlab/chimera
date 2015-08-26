package de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataObjectInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

public class SimpleEvaluator implements RuleEvaluator {
	private ScenarioInstance scenInstance;

	@Override
	public String evaluate(List<DecisionRule> rules, HitPolicy hitPolicy,
			BuiltInAggregator builtInAggregator, ScenarioInstance scenarioInstance) {
		
		scenInstance = scenarioInstance;
		String result = null;
		for ( DecisionRule rule : rules){
			System.out.println("rules: "+rules.size());
			
			boolean condition = true; //gets false if any condition fails
			for(String inputExpression : rule.getInputs().keySet()){
				String inputExpressionAttribute = "", inputExpressionObject;
				String[] split = inputExpression.split("\\.");
				if (split.length > 1){
					inputExpressionObject = split[0];
					inputExpressionAttribute = split[1];
				}else{
					inputExpressionObject = inputExpression;
				}
				DataObjectInstance inputExpressionData = getDataObjectInstanceForInputExpression(inputExpressionObject);
			
			
			
				if (inputExpressionData == null) {
					condition = false;
					break;
				}
				String entry = rule.getInputs().get(inputExpression);
				
				Matcher m = Pattern.compile("(<=|<|>|>=)(.*)").matcher(entry);
				String comparator="",checkValue="";
				if (m.find()){
					comparator= m.group(1);
					checkValue = m.group(2);
				}
				System.out.println("second " +checkValue);
				
				
				for( DataAttributeInstance attribute : inputExpressionData.getDataAttributeInstances()){
					System.out.println("data atrribute: "+attribute.getName());
					if(attribute.getName().equals(inputExpressionAttribute)){
						condition = checkCondition(attribute, comparator, checkValue);
					}
				}
				if (condition == false)break;
			}
			
			System.out.println("condition: "+condition);
			if(condition){
				result = rule.getOutputEntry();
			}
			System.out.println(result);
		}
		return result;
	}

	private boolean checkCondition(DataAttributeInstance attribute,
			String comparator, String checkVal) {
		System.out.println(comparator + " " + checkVal);
		boolean condition = false;
		Integer value = Integer.parseInt((String) attribute.getValue());
		Integer checkValue = Integer.parseInt(checkVal);
		switch(comparator){
			case "<":
				condition = value<checkValue;
				break;
			case "<=":
				condition = value<=checkValue;
				break;
			case ">":
				condition = value > checkValue;
				break;
			case ">=":
				condition = value >= checkValue;
				break;
			
		}
		return condition;
	}

	private DataObjectInstance getDataObjectInstanceForInputExpression(String inputExpression) {
		String[] split = inputExpression.split("\\.");
		if (split.length > 1)inputExpression = split[0];
		for (DataObjectInstance object : scenInstance.getDataObjectInstances() ){

			if(object.getName().equalsIgnoreCase(inputExpression)){
				System.out.println("object found");
				return object;
			}
		}
		return null;
	}

	


}
