package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

public class DecisionTable implements IDeserialisable, IPersistable {

	static Logger log = Logger.getLogger(DecisionTable.class.getName());
	
	private String processeditorServerUrl;

	private Node decisionTableXML;

	private String decisionTableName;

	private int decisionTableID;

	private HashMap<Long, Node> ruleNodes;

	private ArrayList<Rule> rules;

	private boolean isConsistent;

	private boolean isComplete;

	private String preferedOrientation;

	private String aggregation;

	private String hitPolicy;

	private int scenarioID;

	private int databaseID;
	
	/**
     * Sets the processeditorServerUrl which is needed for connecting to the server
     * in order to get the XML-files for the decision tables.
     *
     * @param serverURL URL of the processEditorServer
     */
    public DecisionTable(String serverURL) {
        processeditorServerUrl = serverURL;
    }
	@Override
	public int save() {
		Connector conn = new Connector();
        this.databaseID = conn.insertDecisionTableIntoDatabase(this.decisionTableName, this.hitPolicy, this.aggregation, this.preferedOrientation, this.isComplete, this.isConsistent);
        for (Rule rule : rules) {
            rule.setDecisionTableID(databaseID);
            rule.save();
        }
        return databaseID;
	}

	@Override
	public void initializeInstanceFromXML(Node element) {
		this.decisionTableXML = element;
		setDecisionTableName();
        setDecisionTableID();
        setDecisionTableProperties();
        generateRules();
		
	}
	private void setDecisionTableProperties() {
		try {
            //get all nodes from fragmentXML
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathQuery = "/model/properties/property";
            NodeList properties = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(this.decisionTableXML, XPathConstants.NODESET);
            for (int i = 0; i < properties.getLength(); i++) {
            	if (properties.item(i).getNodeName().equals("property")) {
                    org.w3c.dom.Node property = properties.item(i);
                    NamedNodeMap attributes = property.getAttributes();
                    String name = attributes.getNamedItem("name").getTextContent();
                    String value = attributes.getNamedItem("value").getTextContent();
       
	                switch(name){
		                case "hitPolicy":
		                	this.hitPolicy = (String) value;
		                	break;
		                case "aggregation":
		                	this.aggregation = (String) value;
		                	break;
		                case "preferedOrientation":
		                	this.preferedOrientation = (String) value;
		                	break;
		                case "isComplete":
		                	this.isComplete = value.equals("0")?false:true;
		                	break;
		                case "isConsistent":
		                	this.isConsistent = value.equals("0")?false:true;
		                	break;
		                default:
		                	break;
	                }
            	}
                	
            }
        } catch (XPathExpressionException e) {
            log.error("Error:", e);
        }
		
	}
	private void generateRules() {
		 try {
	            //get all nodes from fragmentXML
	            XPath xPath = XPathFactory.newInstance().newXPath();
	            String xPathQuery = "/model/nodes/node";
	            NodeList nodes = (NodeList) xPath
	                    .compile(xPathQuery)
	                    .evaluate(this.decisionTableXML, XPathConstants.NODESET);
	            
	            this.rules = new ArrayList<Rule>(nodes.getLength());

	            for (int i = 0; i < nodes.getLength(); i++) {
	                Rule currentRule = new Rule(this.decisionTableID);
	                currentRule.initializeInstanceFromXML(nodes.item(i));
	                this.rules.add(currentRule);
	            }
	        } catch (XPathExpressionException e) {
	            log.error("Error:", e);
	        }
	}
	private void setDecisionTableID() {
		XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@id";
        try {
            this.decisionTableID = Integer.parseInt(xPath
                    .compile(xPathQuery)
                    .evaluate(this.decisionTableXML));
        } catch (XPathExpressionException e) {
            log.error("Error:", e);
        }
	}
	private void setDecisionTableName() {
		XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@name";
        try {
            this.decisionTableName = xPath
                    .compile(xPathQuery)
                    .evaluate(this.decisionTableXML);
        } catch (XPathExpressionException e) {
            log.error("Error:", e);
        }
		
	}
	public void setScenarioID(int szenarioID) {
		this.scenarioID = szenarioID;
		
	}

}
