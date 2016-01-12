package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.Edge;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.Fragment;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a DatabaseFragment of the XML-Model.
 * It implements the IDeserialisable interface,
 * which allows to initialize a fragment Object
 * from an XML and the IPersistable Interface,
 * which allows to save the Object to the Database.
 */
public class DatabaseFragment implements IPersistable {
	private static Logger log = Logger.getLogger(DatabaseFragment.class.getName());
	/**
	 * The url of the process Editor.
	 */
	private String processeditorServerUrl;
	/**
	 * The databaseID of the scenario.
	 */
	private int scenarioID = -1;
	/**
	 * The name of the DatabaseFragment.
	 */
	private String fragmentName;
	/**
	 * The XML of this fragment Model.
	 */
	private org.w3c.dom.Node fragmentXML;
	/**
	 * The Model-XML-ID of the DatabaseFragment.
	 */
	private long fragmentID;
	/**
	 * A Map which maps Model-XML-Element-IDs to nodes (either controlNodes or dataNodes).
	 */
	private Map<String, Node> nodes = new HashMap<>();
	/**
	 * The List of Edges created from the FragmentXML.
	 */
	private List<Edge> edges;
	/**
	 * The database ID of the fragment.
	 */
	private int databaseID;
	/**
	 * A list of all Inputs sets,
	 * which are used by any Activities inside this DatabaseFragment.
	 */
	private List<InputSet> inputSets;
	/**
	 * A List of all Outputs sets,
	 * which are used by any Activities inside this DatabaseFragment.
	 */
	private List<OutputSet> outputSets;
	/**
	 * The version of the current Scenario.
	 */
	private int versionNumber;

	/**
	 * Sets the processeditorServerUrl which is needed for connecting to the server
	 * in order to get the XML-files for the fragments.
	 *
	 * @param serverURL URL of the processEditorServer
	 */
	public DatabaseFragment(String serverURL) {
		processeditorServerUrl = serverURL;
	}

	/**
	 * This constructor is only used for testcases
	 * therefore, a connection to the server is not needed .
	 */
	public DatabaseFragment() {
	}


    /**
     *
     * @param xml xmlString of fragment which is valid BPMN standard
     */
   public void initialize(String xml, int versionNumber, String fragmentName,
                                  int fragmentID, Map<Long, DataClass> dataClasses) {
        Document doc = getXmlDocFromString(xml);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Fragment.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Fragment fragment = (Fragment) jaxbUnmarshaller.unmarshal(doc);

            this.fragmentName = fragmentName;
            this.versionNumber = versionNumber;
            this.fragmentID = fragmentID;
            this.inputSets = fragment.getInputSets();
            this.outputSets = fragment.getOutputSets();
            this.save();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private Document getXmlDocFromString(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }



	/**
	 * Sets the scenario Id.
	 * The scenario Id should be the primary key of the scenario
	 *
	 * @param id the primary key of the scenario
	 */
	public void setScenarioID(final int id) {
		this.scenarioID = id;
	}

	@Override public int save() {
		Connector conn = new Connector();
		this.databaseID = conn.insertFragmentIntoDatabase(this.fragmentName,
				this.scenarioID, this.fragmentID, this.versionNumber);
	    return databaseID;
	}


	/**
	 * Migrate fragmentinstances.
	 *
	 * @param scenarioDbID databaseId of the old scenario whose instances get migrated
	 */
	public void migrate(int scenarioDbID) {
		Connector connector = new Connector();
		int oldFragmentID = connector.getFragmentID(scenarioDbID, fragmentID);
		connector.migrateFragmentInstance(oldFragmentID, databaseID);
		// migrateDataAttributeInstances controlNodes
		for (Map.Entry<String, Node> node : nodes.entrySet()) {
			node.getValue().migrate(oldFragmentID);
		}
	}


	/**
	 * Returns a Map of Node-Ids (from XML) and their nodes.
	 * Any changes will manipulate the state of the DatabaseFragment.
	 *
	 * @return a Map<XML_ID, ControlNode>
	 */
	public Map<String, Node> getControlNodes() {
		return nodes;
	}

	/**
	 * The list of InputSets. Changes will alter the state of the fragment.
	 * Used only for test cases.
	 *
	 * @return List of InputSets
	 */
	public List<InputSet> getInputSets() {
		return inputSets;
	}

	/**
	 * The list of OutputSets. Changes will affect the state of the fragment.
	 * Used only for test cases.
	 *
	 * @return List of Output Sets
	 */
	public List<OutputSet> getOutputSets() {
		return outputSets;
	}

	/**
	 * Get a list of all Input- and OutputSets.
	 *
	 * @return List of Sets
	 */
	public List<Set> getSets() {
		List<Set> sets = new ArrayList<>();
		sets.addAll(outputSets);
		sets.addAll(inputSets);
		return sets;
	}

	/**
	 * Returns the name of the fragment.
	 *
	 * @return fragmentName
	 */
	public String getFragmentName() {
		return fragmentName;
	}

	/**
	 * Returns the Model-XML-ID of the DatabaseFragment.
	 *
	 * @return fragmentID
	 */
	public long getFragmentID() {
		return fragmentID;
	}

	/**
	 * Returns the Database-ID of the DatabaseFragment which is available after saving the fragment.
	 *
	 * @return DatabaseID
	 */
	public int getDatabaseID() {
		return databaseID;
	}

	/**
	 * Returns the model-version of the DatabaseFragment.
	 *
	 * @return versionNumber
	 */
	public int getVersion() {
		return versionNumber;
	}
}
