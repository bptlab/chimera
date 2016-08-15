package de.hpi.bpt.chimera.jcomparser.json;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcomparser.saving.IPersistable;
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
	 * The version number of the domainModel
	 */
	private int versionNumber;
	/**
	 * The databaseID of the corresponding scenario.
	 */
	private int scenarioId;

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
     * Creates a mapping from the name of the dataclass to the dataclass object itself.
     * @return map from name to dataclass
     */
    public Map<String, DataClass> getMapFromNameToDataClass() {
        Map<String, DataClass> nameToDataClass = new HashMap<>();
        for (DataClass dataClass : dataClasses) {
            nameToDataClass.put(dataClass.getName(), dataClass);
        }
        return nameToDataClass;
    }
	/**
	 * This sets the scenario ID needed for the update.
	 *
	 * @param id databaseID of the corresponding scenario.
	 */
	public void setScenarioId(final int id) {
		this.scenarioId = id;
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
		conn.insertDomainModel(this.versionNumber,
				this.scenarioId);
		dataClasses.forEach(x -> x.save(scenarioId));
		aggregations.forEach(Aggregation::save);
		return 1;
	}

	public List<Aggregation> getAggregations() {
		return aggregations;
	}

	public int getScenarioId() {
		return scenarioId;
	}

	public int getVersionNumber() {
		return versionNumber;
	}


	public List<DataClass> getDataClasses() {
		return dataClasses;
	}

	public Map<String, Olc> getOlcs() {
		return olcs;
	}

}
