package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A transportation bean for the new Creation of DataObjects out of DataNodes
 * and transition of States of DataObjects as well as the transition of
 * DataAttribute values.
 */
public class DataManagerBean {
	/**
	 * Ids of DataNodes that should be new created.
	 */
	private List<String> newCreations = new ArrayList<>();
	/**
	 * Ids of DataNodes to Map of Id of DataAttribute to new value. For new
	 * creation.
	 */
	private Map<String, Map<String, Object>> dataNodeAttributeValues = new HashMap<>();
	/**
	 * Map from DataObjectId to new Id of new DataNode.
	 */
	private Map<String, String> transitions = new HashMap<>();
	/**
	 * Ids of DataObject to Map of Id of DataAttributeInstance to new Value. For
	 * edit.
	 */
	private Map<String, Map<String, Object>> dataObjectAttributeValues = new HashMap<>();

	public DataManagerBean(JSONObject post) {
		if (validPost(post)) {
			resolveDataNodes(post);
			resolveDataObjects(post);
		}
	}

	private boolean validPost(JSONObject post) {
		return post.has("ids") && post.has("new_creation") && post.has("transition") && post.has("datanode_attribute_values") && post.has("dataobject_attribute_values") && post.has("transition_to_datanode");
	}

	/**
	 * 
	 * @param post
	 */
	private void resolveDataNodes(JSONObject post) {
		JSONArray newCreationJson = post.getJSONArray("new_creation");
		for (int i = 0; i < newCreationJson.length(); i++) {
			String id = newCreationJson.getString(i);
			newCreations.add(id);
		}

		JSONObject allDataNodeAttributesJson = post.getJSONObject("datanode_attribute_values");
		for (String id : newCreations) {
			if (!allDataNodeAttributesJson.has(id))
				continue;
			JSONObject dataAttributes = allDataNodeAttributesJson.getJSONObject(id);
			Map<String, Object> dataAttributeValue = new HashMap<>();
			for (Object attributeId : dataAttributes.keySet()) {
				dataAttributeValue.put((String) attributeId, dataAttributes.get((String) attributeId));
			}
			dataNodeAttributeValues.put(id, dataAttributeValue);
		}
	}

	/**
	 * 
	 * @param post
	 */
	private void resolveDataObjects(JSONObject post) {
		List<String> dataObjectIds = new ArrayList<>();
		JSONArray transitionJson = post.getJSONArray("transition");
		for (int i = 0; i < transitionJson.length(); i++) {
			String id = transitionJson.getString(i);
			dataObjectIds.add(id);
		}

		JSONObject transitionToDataNodeJson = post.getJSONObject("transition_to_datanode");
		for (String id : dataObjectIds) {
			String dataNodeId = transitionToDataNodeJson.getString(id);
			transitions.put(id, dataNodeId);
		}

		JSONObject allDataObjectAttributesJson = post.getJSONObject("dataobject_attribute_values");
		for (String id : dataObjectIds) {
			if (!allDataObjectAttributesJson.has(id))
				continue;
			JSONObject dataAttributes = allDataObjectAttributesJson.getJSONObject(id);
			Map<String, Object> dataAttributeValue = new HashMap<>();
			for (Object attributeId : dataAttributes.keySet()) {
				dataAttributeValue.put((String) attributeId, dataAttributes.get((String) attributeId));
			}
			dataObjectAttributeValues.put(id, dataAttributeValue);
		}
	}

	// GETTER & SETTER
	public List<String> getNewCreations() {
		return newCreations;
	}
	public void setNewCreations(List<String> newCreations) {
		this.newCreations = newCreations;
	}

	public Map<String, Map<String, Object>> getDataNodeAttributeValues() {
		return dataNodeAttributeValues;
	}

	public void setDataNodeAttributeValues(Map<String, Map<String, Object>> dataNodeAttributeValues) {
		this.dataNodeAttributeValues = dataNodeAttributeValues;
	}
	public Map<String, String> getTransitions() {
		return transitions;
	}
	public void setTransitions(Map<String, String> transitions) {
		this.transitions = transitions;
	}

	public Map<String, Map<String, Object>> getdDataObjectAttributeValues() {
		return dataObjectAttributeValues;
	}

	public void setDataObjectAttributeValues(Map<String, Map<String, Object>> dataObjectAttributeValues) {
		this.dataObjectAttributeValues = dataObjectAttributeValues;
	}

	public Map<String, Object> getDataNodeAttributeValuesById(String dataNodeId) {
		if (dataNodeAttributeValues.containsKey(dataNodeId))
			return dataNodeAttributeValues.get(dataNodeId);
		return new HashMap<>();
	}

	public Map<String, Object> getDataObjectAttributeValuesById(String dataObjectId) {
		if (dataObjectAttributeValues.containsKey(dataObjectId))
			return dataObjectAttributeValues.get(dataObjectId);
		return new HashMap<>();
	}
}
