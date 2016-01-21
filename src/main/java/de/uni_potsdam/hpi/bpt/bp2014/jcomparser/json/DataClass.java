package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.*;

/**
 * This class represents a DataClass.
 */
public class DataClass implements IDeserialisableJson, IPersistable {
	/**
	 * This is the modelID of the dataClass.
	 */
	private long dataClassModelID;
	/**
	 * This is the databaseID of the dataClass.
	 */
	private int dataClassID;
	/**
	 * This is the name of the dataClass.
	 */
	private String dataClassName;
	/**
	 * This boolean is used to identify the root element
	 * of the domainModel which is used to get the caseDataObject.
	 */
	private boolean isRootNode = false;
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

	/**
	 * This initializes the dataClass from the JSON.
	 *
	 * @param element The JSON String which will be used for deserialisation
	 */
	@Override public void initializeInstanceFromJson(final String element) {
		try {
			this.dataClassJson = new JSONObject(element);

			this.dataClassName = this.dataClassJson.getString("name");
			this.dataClassModelID = this.dataClassJson.getLong("_id");
			this.isRootNode = this.dataClassJson.getBoolean("is_root");
			generateDataAttributeList(this.dataClassJson.getJSONArray("attributes"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method saves the dataClass to the database.
	 *
	 * @return the databaseID of the dataClass.
	 */
	@Override public int save() {
		Connector conn = new Connector();
		int root;
		if (this.isRootNode) {
			root = 1;
		} else {
			root = 0;
		}
		this.dataClassID = conn.insertDataClassIntoDatabase(this.dataClassName, root);
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

	public boolean isRootNode() {
		return isRootNode;
	}

	public JSONObject getDataClassJson() {
		return dataClassJson;
	}

	public long getDataClassModelID() {
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
}
