package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbRule;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is only for debugging.
 */
public class Ausprobieren {

    /**
     * This main Methods catches a scenario from the server and parses it.
     * You may have to change the id of the model.
     *
     * @param args the command line arguments (will be ignored)
     * @throws ParserConfigurationException The XML seems to be invalid
     * @throws IOException                  The server returns an invalid file.
     * @throws SAXException                 The XML seems to be invalid.
     */
	
	private static boolean local = true;
	
    public static void main(String[] args) throws  	
            ParserConfigurationException,
            IOException,
            SAXException {

        Retrieval jRetrieval = new Retrieval();
        String scenarioXML = jRetrieval.getXMLWithAuth(
                "http://bp2014w1-dev:1205/",
                "http://bp2014w1-dev:1205/models/1225104276.pm");

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(scenarioXML));
        DocumentBuilder db = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder();
        Document doc = db.parse(is);
        Scenario scen = new Scenario("http://bp2014w1-dev:1205/");
        scen.initializeInstanceFromXML(doc.getDocumentElement());
        scen.save();

    	DbRule dbRule = new DbRule();
    	Map map = dbRule.getInputMap(1);
    	java.util.Set set = map.keySet();
    	
//    	String expression = "Schmutzgrad<=10";
//		System.out.println("Expression: " + expression);
//		Matcher m = Pattern.compile("(.*)(<=|<|>|>=)(.*)").matcher(expression);
//
//		if(m.find()){
//		String firstPart = m.group(1);
//		String secondPart = m.group(2);
//		String thirdPart = m.group(3);
//		}
		//System.out.println("second " +secondPart);
		
    	
//    	if(local){
//	
//    		Ausprobieren obj = new Ausprobieren();
//    		InputSource is = new InputSource();
//	        is.setCharacterStream(new StringReader(obj.getFileWithUtil("dtables/Testszenariodtables.xml")));
//	        DocumentBuilder db = DocumentBuilderFactory
//	                .newInstance()
//	                .newDocumentBuilder();
//	        Document doc = db.parse(is);
//	        Scenario scen = new Scenario("http://localhost:1205/");
//	        scen.initializeInstanceFromXML(doc.getDocumentElement());
//	        scen.save();
//    	}else{
//
//	        Retrieval jRetrieval = new Retrieval();
//	        String scenarioXML = jRetrieval.getHTMLwithAuth(
//	                "http://localhost:1205/",
//	                "http://localhost:1205/models/859462516.pm");
//	
//	        InputSource is = new InputSource();
//	        is.setCharacterStream(new StringReader(scenarioXML));
//	        DocumentBuilder db = DocumentBuilderFactory
//	                .newInstance()
//	                .newDocumentBuilder();
//	        Document doc = db.parse(is);
//	        Scenario scen = new Scenario("http://localhost:1205/");
//	        scen.initializeInstanceFromXML(doc.getDocumentElement());
//	        scen.save();
//            }
    }
    
    public String getFileWithUtil(String fileName) {
    	 
    	String result = "";
     
    	ClassLoader classLoader = getClass().getClassLoader();
    	try {
    	    result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
     
    	return result;
      }
     
    
}
