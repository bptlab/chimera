package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * This class represents a Scenario-Model.
 * It can be parsed from an XML and written to a Database.
 */
public class Scenario implements IDeserialisableJson, IPersistable {
	private static Logger log = Logger.getLogger(Scenario.class.getName());
	/**
	 * The Name of the Scenario.
	 */
	private String scenarioName;

	/**
	 * The model Id of the scenario.
	 */
	private long scenarioID;
	/**
	 * The XML which holds all information from the model.
	 */
	private JSONObject scenarioJson;
	/**
	 * A List of fragments which are part of the scenario.
	 */
	private List<DatabaseFragment> fragments;
	/**
	 * The url of the process Editor.
	 */
	private String processeditorServerUrl;
	/**
	 * The database ID of the scenario.
	 */
	private int databaseID;
	/**
	 * A Map of the names of dataObjects and the objects themselves.
	 */
	private Map<String, DataObject> dataObjects;
	/**
	 * The version of the current Scenario.
	 */
	private int versionNumber;
	/**
	 * Marks if the scenario needs to be saved.
	 * If none of the fragments is changed and none is added or removed, and there is no newer
	 * version of the scenario, it does not need to be saved and
	 * the variable holds value false.
	 */
	private boolean needsToBeSaved;
	/**
	 * If the scenario contains new fragments that a older version in the database does not
	 * contain, all running instances are migrated to this scenario.
	 * Variable holds value true if such a migration is necessary.
	 */
	private boolean migrationNecessary;
	/**
	 * If migration is necessary, we need the databaseID of the scenario to migrate.
	 */
	private int migratingScenarioDbID = -1;
	/**
	 * This is a String containing the ProcessEditorServerID for the domainModel.
	 */
	private Long domainModelID;
	/**
	 * This Element contains the XML representation of the domainModel.
	 */
	private JSONObject domainModelJson;
	/**
	 * This is the domainModel Object belonging to this scenario.
	 */
	private DomainModel domainModel;
	/**
	 * If migration is necessary, this variable contains all the fragments that are new
	 * and not in the older version.
	 */
	private List<DatabaseFragment> newFragments;
	/**
	 * List<TCSets<Do, state>>
	 */
	private List<Map<DataObject, String>> terminationCondition;

	/**
	 * This method calls all needed methods to set up the domainModel.
	 *
	 * @param element The JSON String which will be used for deserialisation
	 */
	@Override public void initializeInstanceFromJson(final String element) {
		try {
			this.scenarioJson = new JSONObject(element);

			this.scenarioName = scenarioJson.getString("name");
			this.scenarioID = scenarioJson.getLong("_id");
			this.versionNumber = scenarioJson.getInt("revision");
			this.domainModelJson = scenarioJson.getJSONObject("domain_model");

			this.domainModel = createAndInitializeDomainModel();
			generateFragmentList();

			createDataObjects();
			setTerminationCondition();

			checkIfVersionAlreadyInDatabase();

			save();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override public void initializeInstanceFromXML(org.w3c.dom.Node element) {

	}

	/**
	 * This method takes the JSON and initializes the domainModel.
	 *
	 * @return the domainModel or null if there was no JSON given.
	 */
	private DomainModel createAndInitializeDomainModel() {
		if (this.domainModelJson != null) {
			DomainModel model = new DomainModel();
			model.initializeInstanceFromJson(domainModelJson.toString());
			return model;
		}
		return null;
	}

	/**
	 * Checks if the versions of all fragments and of this scenario are already in the database.
	 * If so, the scenario does not need to be saved once again
	 */
	private void checkIfVersionAlreadyInDatabase() {
		Connector connector = new Connector();
		int newestFragmentDatabaseVersion;
		int oldScenarioDbID = connector.getScenarioID(scenarioID);
		int scenarioDbVersion = connector.getScenarioVersion(oldScenarioDbID);
		boolean changesMade = false;
		needsToBeSaved = false;
		newFragments = new LinkedList<>();
		for (DatabaseFragment fragment : fragments) {
			newestFragmentDatabaseVersion =
					connector.getFragmentVersion(oldScenarioDbID,
					fragment.getFragmentID());
			// case 1: We don't have a fragment with this modelid in the database
			if (newestFragmentDatabaseVersion == -1) {
				needsToBeSaved = true;
				// ... this might have two reasons:
				// 1) the scenario is not in the database yet
				if (oldScenarioDbID == -1) {
					migrationNecessary = false;
				} else {
					// 2) a new fragment has been added
					migrationNecessary = true;
					newFragments.add(fragment);
					migratingScenarioDbID = oldScenarioDbID;
				}
			} else if (newestFragmentDatabaseVersion < fragment.getVersion()) {
				//case 2: an existing fragment has been modified:
				//we got a newer version of the fragment here
				needsToBeSaved = true;
				changesMade = true;
			}
		}
		// this evaluation is necessary as otherwise
		// the value of migrationNecessary is influenced by the
		// ordering of the fragments
		if (changesMade) {
			migrationNecessary = false;
		}
		// case 3: newer version of the scenario (e.g. terminationCondition changed)
		if (scenarioDbVersion < versionNumber) {
			needsToBeSaved = true;
		}
		if (migrationNecessary) {
			boolean fragmentFound;
			List<Long> fragmentDbIDs = connector.getFragmentModelIDs(
					migratingScenarioDbID);
			for (long fragmentModelDbID : fragmentDbIDs) {
				fragmentFound = false;
				for (DatabaseFragment fragment : fragments) {
					if (fragment.getFragmentID() == fragmentModelDbID) {
						fragmentFound = true;
						break;
					}
				}
				if (!fragmentFound) {
					needsToBeSaved = true;
					migrationNecessary = false;
					break;
				}
			}
		}
		checkDomainModelVersion(oldScenarioDbID);
		// in case old scenario is deleted...
		if (!needsToBeSaved) {
			int oldScenarioDeleted = connector.isScenarioDeleted(oldScenarioDbID);
			if (oldScenarioDeleted == 1) {
				needsToBeSaved = true;
			}
		}
	}

	/**
	 * Checks the version of the domainModel in the database
	 * and compares it with the version of this scenario.
	 * If the version in the database older as this one, migration won't be initiated.
	 *
	 * @param oldScenarioDbID The databaseID of the newest scenario in the database
	 */
	private void checkDomainModelVersion(int oldScenarioDbID) {
		Connector connector = new Connector();
		int oldDataModelVersion = connector.getDataModelVersion(oldScenarioDbID);
		// case 1: We have a newer version of the domainModel now
		if (domainModel.getVersionNumber() > oldDataModelVersion) {
			migrationNecessary = false;
			needsToBeSaved = true;
		} else {
			// case 2: There is another domainModel now
			long oldDataModelID = connector.getDataModelID(oldScenarioDbID);
			if (oldDataModelID != domainModel.getDomainModelModelID()) {
				migrationNecessary = false;
				needsToBeSaved = true;
			}
		}
	}

	/**
	 * Extracts the TerminationCondition from the XML.
	 * Corresponding values will be saved to the corresponding fields.
	 */
	private void setTerminationCondition() {
		this.terminationCondition = new LinkedList<>();
		JSONArray terminationConditions = scenarioJson.getJSONArray("termination_conditions");
		for (int i = 0; i < terminationConditions.length(); i++) {
			Map<DataObject, String> setMap = new HashMap<>();
			JSONObject terminationCondition = terminationConditions.getJSONObject(i);
			Iterator keys = terminationCondition.keys();
			while (keys.hasNext()) {
				String dataObject = keys.next().toString();
				String state = terminationCondition.getString(dataObject);
				setMap.put(this.dataObjects.get(dataObject), state);
			}
			this.terminationCondition.add(setMap);
		}
	}

	@Override public int save() {
		if (needsToBeSaved) {
			Connector conn = new Connector();
			this.databaseID = conn.insertScenarioIntoDatabase(
					this.scenarioName, scenarioID, versionNumber);
			saveFragments();
			domainModel.setScenarioID(this.databaseID);
			domainModel.save();
			saveDataObjects();
			saveConsistsOf();
			if (terminationCondition != null && terminationCondition.size() > 0) {
				saveTerminationCondition();
			}
			saveReferences();
			if (migrationNecessary) {
				migrateRunningInstances();
			}
			//Set flag in executionService to force a reload from the database
			ExecutionService.getInstance((int) scenarioID).setNewVersionAvailable(true);
			return this.databaseID;
		}
		return -1;
	}

	/**
	 * Migrate running instances with the modelId of this scenario
	 * and with the migratingScenarioVersion.
	 */
	private void migrateRunningInstances() {
		Connector connector = new Connector();
		// get the scenarioInstanceIDs of all running instances that need to be migrated
		// and migrate them (means changing their old reference
		// to the scenario to this scenario)
		connector.migrateScenarioInstance(migratingScenarioDbID, databaseID);
		//migrate FragmentInstances
		for (DatabaseFragment fragment : fragments) {
			// as there is no fragmentinstance for this new fragment
			// in the database so far, we don't need to change references
			if (!newFragments.contains(fragment)) {
				fragment.migrate(migratingScenarioDbID);
			}
		}
		migrateDataObjects();
		domainModel.migrateDataAttributeInstances(migratingScenarioDbID);
	}

	/**
	 * Migrate all dataObjectInstances of all scenarioInstances that are migrated.
	 */
	private void migrateDataObjects() {
		Connector connector = new Connector();
		int oldDataObjectDbID;
		for (Map.Entry<String, DataObject> dataObject : dataObjects.entrySet()) {
			oldDataObjectDbID = connector.getDataObjectID(migratingScenarioDbID,
					dataObject.getKey());
			if (oldDataObjectDbID > 0) {
				connector.migrateDataObjectInstance(oldDataObjectDbID,
						dataObject.getValue().getDatabaseId());
			}
		}
	}

	/**
	 * Save the referenced activities of the Scenario to the database.
	 * There fore search for all pairs of activities, that are referenced.
	 * And save these pairs inside the database. (Order is irrelevant,
	 * because the {@link de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector}
	 * does this for you.
	 */
	private void saveReferences() {
		/* Key is the ID used inside the model, value are a List of
         * all IDs used inside the database. */
		Map<String, List<Integer>> activities =
				getActivityDatabaseIDsForEachActivityModelID();
		Connector conn = new Connector();
		for (List<Integer> databaseIDs : activities.values()) {
			if (databaseIDs.size() > 1) {
				// The next two loops find all pairs of referenced activities.
				for (int i = 0; i < databaseIDs.size() - 1; i++) {
					for (int j = i + 1; j < databaseIDs.size(); j++) {
						conn.insertReferenceIntoDatabase(
								databaseIDs.get(i),
								databaseIDs.get(j));
					}
				}
			}
		}
	}

	/**
	 * Get all referenced activities with their model ID and databaseIDs.
	 * If two activities are referenced their model IDs are the same,
	 * but they have different IDs inside the database.
	 *
	 * @return A map of all activity-model-IDs to a list of their database IDs.
	 */
	private Map<String, List<Integer>> getActivityDatabaseIDsForEachActivityModelID() {
		Map<String, List<Integer>> result = new HashMap<>();
		for (DatabaseFragment fragment : fragments) {
			Map<String, Node> fragmentNodes = fragment.getControlNodes();
			for (Map.Entry<String, Node> node : fragmentNodes.entrySet()) {
				if (node.getValue().isTask()) {
					if (result.get(node.getKey()) == null) {
						List<Integer> activityDatabaseIDs
								= new ArrayList<>();
						activityDatabaseIDs.add(
								node.getValue().getDatabaseID());
						result.put(node.getKey(), activityDatabaseIDs);
					} else {
						result.get(node.getKey()).add(
								node.getValue().getDatabaseID());
					}
				}
			}
		}
		return result;
	}

	/**
	 * Saves the terminationCondition of the DatabaseFragment to the database.
	 */
	private void saveTerminationCondition() {
		Connector conn = new Connector();
		// first parameter currently irrelevant,
		// due to the restriction of the terminationCondition
		for (int i = 0; i < terminationCondition.size(); i++) {
			for (Map.Entry<DataObject, String> entry
					: terminationCondition.get(i).entrySet()) {
				int stateID = entry.getKey().getStates().get(entry.getValue());
				conn.insertTerminationConditionIntoDatabase(
						i + 1, entry.getKey().getDatabaseId(),
						stateID, databaseID);
			}
		}
	}

	/**
	 * Saves the Fragments of the Scenario to the Database.
	 * First the Scenarios have to be saved and
	 * the Fragments have to be initialized.
	 */
	private void saveFragments() {
		for (DatabaseFragment fragment : fragments) {
			fragment.setScenarioID(databaseID);
			fragment.save();
		}
	}

	/**
	 * Saves the DataObjects of the Scenario to the Database.
	 * First the Scenarios have to be saved and
	 * the DataObjects have to be initialized.
	 */
	private void saveDataObjects() {
		for (DataObject dataObject : dataObjects.values()) {
			dataObject.setScenarioId(databaseID);
			dataObject.save();
		}
	}

	/**
	 * Saves the relation of DataSets to DataNodes to the Database.
	 * Take care that all DtaNode and Input and Output nodes are implemented
	 */
	private void saveConsistsOf() {
		for (DatabaseFragment frag : fragments) {
			saveDataSetConsistOf(frag);
		}
	}

	/**
	 * Saves the relation of a dataSet to its dataNodes.
	 * Only the nodes of one DatabaseFragment will be represented.
	 *
	 * @param frag The fragment which contains the DataSet
	 */
	private void saveDataSetConsistOf(final DatabaseFragment frag) {
		Connector connector = new Connector();
		for (Set set : frag.getSets()) {
			for (Node dataNode : set.getDataNodes()) {
				connector.insertDataSetConsistOfDataNodeIntoDatabase(
						set.getDatabaseId(), dataNode.getDatabaseID());
			}
		}
	}

	private void createDataObjects() {
		//TODO read data classes of data objects from fragment xml
		dataObjects = new HashMap<>();
		for (DatabaseFragment fragment : fragments) {
			for (Node node : fragment.getControlNodes().values()) {
				if (node.isDataNode()) {
					if (null == dataObjects.get(node.getText())) {
						if (domainModel == null) {
							dataObjects.put(node.getText(), new DataObject());
							break;
						}
						// try to match dataClass over the name
						for (DataClass dC : domainModel.getDataClasses().
								values()) {
							if (dC.getDataClassName().equals(node.getText())) {
								dataObjects.put(node.getText(), new DataObject(dC));
								break;
							}
						}
						// no data class found
						dataObjects.put(node.getText(), new DataObject());
					}
					dataObjects.get(node.getText()).addDataNode(node);
				}
			}
		}
	}

	/**
	 * Generates a List of Fragments from the ScenarioXML.
	 */
	private void generateFragmentList() {
		JSONArray fragmentarray = this.scenarioJson.getJSONArray("fragments");
		this.fragments = new LinkedList<>();
		for (int i = 0; i < fragmentarray.length(); i++) {
			DatabaseFragment fragment = new DatabaseFragment();
			JSONObject fragmentjson = fragmentarray.getJSONObject(i);
            fragment.initialize(fragmentjson.getString("content"),
					fragmentjson.getInt("revision"),
					fragmentjson.getString("name"),
					fragmentjson.getInt("_id"),
					this.domainModel.getDataClasses());
			this.fragments.add(fragment);
		}
	}

	/**
	 * Casts a XML from its String Representation to a w3c Document.
	 *
	 * @param xml The String representation of the XML.
	 * @return The from String created Document.
	 */
	private Document stringToDocument(final String xml) {
		try {
			DocumentBuilder db =
					DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(xml)));
			doc.getDocumentElement().normalize();
			return doc;
		} catch (SAXException | IOException | ParserConfigurationException e) {
			log.error("Error:", e);
		}
		return null;
	}

	// Getters - Currently used for Tests only

	/**
	 * Returns the name of the Scenario.
	 *
	 * @return The Name of the Scenario
	 */
	public String getScenarioName() {
		return scenarioName;
	}

	/**
	 * Returns the ID of the scenario used in the XML.
	 *
	 * @return The id from the XML.
	 */
	public long getScenarioID() {
		return scenarioID;
	}

	/**
	 * Returns the Node Object, which represents the XML.
	 *
	 * @return the XML of representing the Scenario.
	 */
	public JSONObject getScenarioJson() {
		return scenarioJson;
	}

	/**
	 * Returns a List of all Fragments.
	 * Be aware, that changes to this List
	 * will change the state of the scenario.
	 *
	 * @return the List of fragments.
	 */
	public List<DatabaseFragment> getFragments() {
		return fragments;
	}

	/**
	 * The URL of the ProcessEditor Server.
	 *
	 * @return The PE-Server URL.
	 */
	public String getProcesseditorServerUrl() {
		return processeditorServerUrl;
	}

	/**
	 * Returns the Database ID of the Scenario.
	 * Returns -1 if Scenario is not saved to the database.
	 *
	 * @return The ID used inside the database.
	 */
	public int getDatabaseID() {
		return databaseID;
	}

	/**
	 * Returns a List of all DataObjects of the Scenario.
	 * Changes will affect the scenario directly.
	 *
	 * @return the DataObjects of the scenario.
	 */
	public Map<String, DataObject> getDataObjects() {
		return dataObjects;
	}

	/**
	 * Returns an integer Representing the version of the Scenario.
	 *
	 * @return the Version of the Scenario.
	 */
	public int getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Returns a boolean which marks if migration is necessary.
	 * Used for testCases only so far.
	 *
	 * @return 1 if migration is necessary (0 otherwise)
	 */
	public boolean isMigrationNecessary() {
		return migrationNecessary;
	}

	public Long getDomainModelID() {
		return domainModelID;
	}

	public JSONObject getDomainModelJson() {
		return domainModelJson;
	}

	public DomainModel getDomainModel() {
		return domainModel;
	}

	public List<Map<DataObject, String>> getTerminationCondition() {
		return terminationCondition;
	}
}
