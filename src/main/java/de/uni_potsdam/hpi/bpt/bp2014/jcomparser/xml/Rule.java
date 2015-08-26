package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

public class Rule implements IDeserialisable, IPersistable {

	static Logger log = Logger.getLogger(DecisionTable.class.getName());
	private int decisionTableID;
	private Node ruleXML;
	private String inputEntry;
	private String inputExpression;
	private String outputEntry;
	private String outputDefinition;
	private int databaseID;

	public Rule(int decisionTableID) {
		this.decisionTableID = decisionTableID;
	}

	@Override
	public int save() {
		Connector conn = new Connector();
        this.databaseID = conn.insertRuleIntoDatabase(decisionTableID);
        conn.insertRuleConditionIntoDatabase(databaseID, inputEntry, inputExpression);
        conn.insertRuleConclusionIntoDatabase(databaseID, outputEntry, outputDefinition);
        return databaseID;
	}

	@Override
	public void initializeInstanceFromXML(Node element) {
		this.ruleXML = element;
		//get all nodes from ruleXML
		NodeList properties = ruleXML.getChildNodes();
		for (int i = 0; i < properties.getLength(); i++) {
			if (properties.item(i).getNodeName().equals("property")) {
                org.w3c.dom.Node property = properties.item(i);
                initializeField(property);
            }
		    
		   
		}
		
	}

	private void initializeField(Node property) {
		NamedNodeMap attributes = property.getAttributes();
        String name = attributes.getNamedItem("name").getTextContent();
        String value = attributes.getNamedItem("value").getTextContent();
	    switch(name){
	        case "inputEntry":
	        	this.inputEntry = (String) value;
	        	break;
	        case "inputExpression":
	        	this.inputExpression = (String) value;
	        	break;
	        case "outputEntry":
	        	this.outputEntry = (String) value;
	        	break;
	        case "outputDefinition":
	        	this.outputDefinition = (String) value;
	        	break;
	        default:
	        	break;
	    }
	}

	public void setDecisionTableID(int databaseID) {
		this.decisionTableID = databaseID;
		
	}

}
