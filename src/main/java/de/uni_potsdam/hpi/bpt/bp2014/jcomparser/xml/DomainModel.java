package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
import java.util.List;
import java.util.Map;

/**
 * This class represents the DomainModel.
 */
public class DomainModel implements IDeserialisableJson, IPersistable {
	private static Logger log = Logger.getLogger(DomainModel.class.getName());

	/**
	 * The url of the process Editor.
	 */
	private String processeditorServerUrl;
	/**
	 * The modelID found in the JSON.
	 */
	private long domainModelModelID;
	/**
	 * The version number of the domainModel
	 */
	private int versionNumber;
	/**
	 * The databaseID of the corresponding scenario.
	 */
	private int scenarioID;
	/**
	 * A Mao of modelID's and corresponding dataClasses belonging to this domainModel.
	 */
	private Map<Long, DataClass> dataClasses;
	/**
	 * A List of all aggregation between the dataClasses belonging to this domainModel.
	 */
	private List<Aggregation> aggregations;
	/**
	 * The XML representation of a domainModel
	 */
	private JSONObject domainModelJson;

	/**
	 * The constructor.
	 *
	 * @param serverURL the severURL wher the JSON Objects are to be found.
	 */
	public DomainModel(String serverURL) {
		this.processeditorServerUrl = serverURL;
	}

	/**
	 * The constructor.
	 */
	public DomainModel() {

	}

	/**
	 * This method calls all needed methods to set up the domainModel.
	 *
	 * @param element The JSON String which will be used for deserialisation
	 */
	@Override public void initializeInstanceFromJson(final String element) {
		try {
			this.domainModelJson = new JSONObject(element);

			this.domainModelModelID = this.domainModelJson.getInt("_id");
			this.versionNumber = this.domainModelJson.getInt("revision");

			generateDataClasses();
			generateAggregations();

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	@Override public void initializeInstanceFromXML(final Node element) {

	}

	/**
	 * This sets the scenario ID needed for the update.
	 *
	 * @param id databaseID of the corresponding scenario.
	 */
	public void setScenarioID(final int id) {
		this.scenarioID = id;
	}

	/**
	 * This method generates a List of aggregates from the XML.
	*/
	private void generateAggregations() {
		try {
			JSONArray jsonAggregations = this.domainModelJson.getJSONArray("aggregations");
			int length = jsonAggregations.length();
			this.aggregations = new ArrayList<>(length);

			for (int i = 0; i < length; i++) {
				Aggregation currentAggregation = new Aggregation();
				currentAggregation.setDataClasses(dataClasses);
				currentAggregation.initializeInstanceFromJson(
						jsonAggregations.getJSONObject(i).toString()
				);
				this.aggregations.add(currentAggregation);
			}
		} catch (JSONException e) {
			log.error("Error: ", e);
		}
	}

	/**
	 * This method generates a Map of dataClasses with their modelID as keys.
	 */
	private void generateDataClasses() {
		try {
			JSONArray jsonDataClasses = this.domainModelJson.getJSONArray("dataclasses");
			int length = jsonDataClasses.length();
			this.dataClasses = new HashMap<>(length);

			for (int i = 0; i < length; i++) {
				DataClass currentClass = new DataClass();
				currentClass.initializeInstanceFromJson(
						jsonDataClasses.getJSONObject(i).toString());
				this.dataClasses.put(
						currentClass.getDataClassModelID(), currentClass);
			}
		} catch (JSONException e) {
			log.error("Error: ", e);
		}
	}

	/**
	 * This method saves the domainModel to the database
	 * as well as for the dataClasses and aggregations.
	 *
	 * @return the integer 1.
	 */
	@Override public int save() {
		Connector conn = new Connector();
		conn.insertDomainModelIntoDatabase(this.domainModelModelID, this.versionNumber,
				this.scenarioID);
		for (DataClass dataClass : dataClasses.values()) {
			dataClass.save();
		}
		for (Aggregation aggregation : aggregations) {
			aggregation.save();
		}
		return 1;
	}

	/**
	 * Migrate all dataAttributeInstances that are instances of all dataAttributes
	 * belonging to the instances of the old scenario.
	 *
	 * @param oldScenarioDbID DatabaseID of the old scenario
	 *                           whose dataAttributeInstances get migrated.
	 */
	public void migrateDataAttributeInstances(int oldScenarioDbID) {
		Map<Integer, Integer> mappedDataClassIDs = mapDataClassIDs(oldScenarioDbID);
		Map<Integer, Integer> mappedDataAttributeIDs = new HashMap<>();
		Connector connector = new Connector();
		for (Map.Entry<Integer, Integer> dataClassIDs : mappedDataClassIDs.entrySet()) {
			Map<Integer, String> oldDataAttributes = connector
					.getDataAttributes(dataClassIDs.getKey());
			Map<Integer, String> newDataAttributes = connector
					.getDataAttributes(dataClassIDs.getValue());
			for (Map.Entry<Integer, String> oldDataAttribute
					: oldDataAttributes.entrySet()) {
				for (Map.Entry<Integer, String> newDataAttribute
						: newDataAttributes.entrySet()) {
					if (oldDataAttribute.getValue().equals(
							newDataAttribute.getValue())) {
						mappedDataAttributeIDs.put(
								oldDataAttribute.getKey(),
								newDataAttribute.getKey());
						newDataAttributes.remove(newDataAttribute.getKey());
						break;
					}
				}
			}
		}
		for (Map.Entry<Integer, Integer> dataAttribute
				: mappedDataAttributeIDs.entrySet()) {
			connector.migrateDataAttributeInstance(
					dataAttribute.getKey(), dataAttribute.getValue());
		}
	}

	/**
	 * Map all dataClassIDs of the old scenario to its counterpart
	 * in the new scenario (= the scenario this domainModel belongs to);
	 *
	 * @param oldScenarioDbID DatabaseID of the old scenario.
	 */
	private Map<Integer, Integer> mapDataClassIDs(int oldScenarioDbID) {
		Connector connector = new Connector();
		List<Integer> oldDataClassIDs = connector.getDataClassIDs(oldScenarioDbID);
		List<Integer> newDataClassIDs = connector.getDataClassIDs(scenarioID);
		Map<Integer, Integer> mappedIDs = new HashMap<>();
		for (int oldID : oldDataClassIDs) {
			for (int newID : newDataClassIDs) {
				String oldName = connector.getDataClassName(oldID);
				String newName = connector.getDataClassName(newID);
				if (oldName.equals(newName)) {
					mappedIDs.put(oldID, newID);
					break;
				}
			}
		}
		return mappedIDs;
	}

	public List<Aggregation> getAggregations() {
		return aggregations;
	}

	public Map<Long, DataClass> getDataClasses() {
		return dataClasses;
	}

	public int getScenarioID() {
		return scenarioID;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public long getDomainModelModelID() {
		return domainModelModelID;
	}
}
