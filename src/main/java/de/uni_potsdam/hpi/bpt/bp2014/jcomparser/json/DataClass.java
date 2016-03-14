package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.IPersistable;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.*;

/**
 * This class represents a DataClass.
 */
public class DataClass implements IPersistable {
    private static Logger logger = Logger.getLogger(EventType.class);

    /**
	 * This is the modelID of the dataClass.
	 */
	private String dataClassModelID;
	/**
	 * This is the databaseID of the dataClass.
	 */
	private int dataClassID;

    /**
	 * This is the name of the dataClass.
	 */
	private String dataClassName;
	/**
	 * This is a list containing all dataAttributes belonging to this dataClass.
	 */
	private List<DataAttribute> dataAttributes = new LinkedList<>();
	/**
	 * This contains the XML representation of the dataClass.
	 */
	private JSONObject dataClassJson;

    private Map<String, Integer> stateToDatabaseId = new HashMap<>();

    private List<String> states = new ArrayList<>();

	public DataClass(final String element) {
        try {
            this.dataClassJson = new JSONObject(element);
            this.dataClassName = this.dataClassJson.getString("name");
            this.dataClassModelID = this.dataClassJson.getString("_id");
            this.dataClassJson = new JSONObject(element);
            generateDataAttributeList(this.dataClassJson.getJSONArray("attributes"));
        } catch (JSONException e) {
            logger.trace(e);
            throw new JSONException("Invalid class json");
        }
    }

	/**
	 * This method saves the dataClass to the database.
	 *
	 * @return the databaseID of the dataClass.
	 */
	@Override public int save() {
		Connector conn = new Connector();
		this.dataClassID = conn.insertDataClassIntoDatabase(this.dataClassName);
		saveDataAttributes();
        Connector connector = new Connector();
        for (String state : this.states) {
            int stateID = connector.insertStateIntoDatabase(state, this.dataClassID);
            stateToDatabaseId.put(state, stateID);
        }

        return dataClassID;
	}

	/**
	 * This method gets all the dataAttributes from the JSON.
	 * DataAttributes can only be alphanumerical.
	 *
	 * @param jsonAttributes This JSONArray contains all dataAttributes from the JSON.
	 */
	private void generateDataAttributeList(JSONArray jsonAttributes) {
		int length = jsonAttributes.length();
		for (int i = 0; i < length; i++) {
			DataAttribute dataAttribute = new DataAttribute(
					jsonAttributes.getJSONObject(i).getString("name"),
					jsonAttributes.getJSONObject(i).getString("datatype")
			);
			this.dataAttributes.add(dataAttribute);
		}
	}

	/**
	 * This method iterates through all dataAttributes and sets
	 * the dataClass for them as well as calling the save method.
	 */
	private void saveDataAttributes() {
		for (DataAttribute dataAttribute : dataAttributes) {
            dataAttribute.setDataClassID(dataClassID);
			dataAttribute.save();
		}
	}

	public int getDataClassID() {
		return dataClassID;
	}

	public String getDataClassName() {
		return dataClassName;
	}

	public List<DataAttribute> getDataAttributes() {
		return dataAttributes;
	}

	public JSONObject getDataClassJson() {
		return dataClassJson;
	}

	public String getDataClassModelID() {
		return dataClassModelID;
	}

    public Map<String, Integer> getStateToDatabaseId() {
        return stateToDatabaseId;
    }

    public void setStateToDatabaseId(Map<String, Integer> stateToDatabaseId) {
        this.stateToDatabaseId = stateToDatabaseId;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public void setDataClassName(String dataClassName) {
        this.dataClassName = dataClassName;
    }
}
