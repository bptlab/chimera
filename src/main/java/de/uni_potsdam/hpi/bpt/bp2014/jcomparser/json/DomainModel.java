package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.IPersistable;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the DomainModel.
 */
public class DomainModel implements IPersistable {
	private static Logger log = Logger.getLogger(DomainModel.class);

	/**
	 * The modelID found in the JSON.
	 */
	private String domainModelEditorId;
	/**
	 * The version number of the domainModel
	 */
	private int versionNumber;
	/**
	 * The databaseID of the corresponding scenario.
	 */
	private int scenarioID;

    /**
	 * A List of all aggregation between the dataClasses belonging to this domainModel.
	 */
	private List<Aggregation> aggregations = new ArrayList<>();
	/**
	 * The XML representation of a domainModel
	 */
	private JSONObject domainModelJson;

    private List<DataClass> dataClasses = new ArrayList<>();
	/**
	 * A map with all known OLCs for DataClasses (identified by name).
	 */
	private Map<String, Olc> olcs = new HashMap<>();


    public DomainModel(final String element) {
        try {
            this.domainModelJson = new JSONObject(element);
            this.versionNumber = this.domainModelJson.getInt("revision");
            generateDataClassesAndEventTypes();
            // TODO this.aggregations = generateAggregations();
        } catch (JSONException e) {
            log.error(e);
            throw new JSONException("Illegal Domain Model JSON");
        }
	}

    /**
     * Creates a mapping from the Id of the dataclass to the dataclass object itself.
     * @return map from Id to dataclass
     */
    public Map<String, DataClass> getMapFromIdToDataclass() {
        Map<String, DataClass> idToDataclass = new HashMap<>();
        for (DataClass dataClass : dataClasses) {
            idToDataclass.put(dataClass.getModelId(), dataClass);
        }
        return idToDataclass;
    }

    /**
     * Creates a mapping from the name of the dataclass to the dataclass object itself.
     * @return map from name to dataclass
     */
    public Map<String, DataClass> getMapFromNameToDataclass() {
        Map<String, DataClass> nameToDataclass = new HashMap<>();
        for (DataClass dataClass : dataClasses) {
            nameToDataclass.put(dataClass.getName(), dataClass);
        }
        return nameToDataclass;
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
	private List<Aggregation> generateAggregations() {
		// TODO check every attribute if its name equals one of the data classes' name
		throw new NotImplementedException("Aggregations cannot be parsed.");
	}

	/**
	 * This method generates a Map of dataClasses with their modelID as keys
	 * and a Map of EventTypes with their modelID as keys.
	 */
	private void generateDataClassesAndEventTypes() {
        try {
			JSONArray jsonDataClasses = this.domainModelJson.getJSONArray("dataclasses");
			int length = jsonDataClasses.length();

			for (int i = 0; i < length; i++) {
				JSONObject currentObject = jsonDataClasses.getJSONObject(i);
				if (currentObject.getBoolean("is_event")) {
					EventType currentET = new EventType(currentObject.toString());
					dataClasses.add(currentET);
				} else {
					DataClass currentClass = new DataClass(currentObject.toString());
					dataClasses.add(currentClass);
					//In case we need a list of all OLCs we might as well create
					//it on the fly by iterating over all data classes
					//(basically like here)
					if (currentClass.getOlc() != null) {
						olcs.put(currentClass.getName(),
								currentClass.getOlc());
					}
				}
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
		conn.insertDomainModelIntoDatabase(this.domainModelEditorId, this.versionNumber,
				this.scenarioID);
		dataClasses.forEach(DataClass::save);
		aggregations.forEach(Aggregation::save);
		return 1;
	}

	public List<Aggregation> getAggregations() {
		return aggregations;
	}

	public int getScenarioID() {
		return scenarioID;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public String getDomainModelEditorId() {
		return domainModelEditorId;
	}

	public List<DataClass> getDataClasses() {
		return dataClasses;
	}

	public Map<String, Olc> getOlcs() {
		return olcs;
	}

}
