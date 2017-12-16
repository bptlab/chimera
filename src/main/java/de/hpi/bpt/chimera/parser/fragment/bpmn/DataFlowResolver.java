package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataAttributeJsonPath;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;

public class DataFlowResolver {
	/**
	 * Map from Id of DataNodeObjectReference to DataNode.
	 */
	private Map<String, AtomicDataStateCondition> associationMap = new HashMap<>();

	/**
	 * Creates a Resolver for the SequenceFlows of a certain Fragment. Therefore
	 * the CaseModelParserHelper is needed that contains Map from Name to
	 * DataClass and Name to ObjectLifecycleState.
	 * 
	 * @param fragXmlWrap
	 * @param parserHelper
	 */
	public DataFlowResolver(FragmentXmlWrapper fragXmlWrap, CaseModelParserHelper parserHelper) {
		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.BpmnDataNode dataNodeReference : fragXmlWrap.getDataNodes()) {
			DataNode dataNode = new DataNode();

			DataClass dataClass = parserHelper.getNameToDataClass(dataNodeReference.getDataClassName());
			ObjectLifecycleState olcState = parserHelper.getNameToObjectLifecycleState(dataClass, dataNodeReference.getStateName());

			String unparsedJsonPathMapping = dataNodeReference.getJsonPath();
			List<DataAttributeJsonPath> dataAttributeJsonPaths = parseDataAttributeJsonPaths(unparsedJsonPathMapping, dataClass, parserHelper);

			dataNode.setDataClass(dataClass);
			dataNode.setObjectLifecycleState(olcState);
			dataNode.setDataAttributeJsonPaths(dataAttributeJsonPaths);

			associationMap.put(dataNodeReference.getDataNodeObjectReferenceId(), dataNode);
		}
	}

	/**
	 * Parse a List of DataAttributeJsonPaths by a given JSON-String that
	 * contains the mapping from the name of the DataAttribute to the
	 * corresponding Json Path that will be used for writing the values of an
	 * DataAttributeInstance.
	 * 
	 * @param dataAttributeJsonPathMapping
	 * @param dataClass
	 * @param parserHelper
	 * @return List of DataAttributeJsonPath
	 */
	private List<DataAttributeJsonPath> parseDataAttributeJsonPaths(String dataAttributeJsonPathMapping, DataClass dataClass, CaseModelParserHelper parserHelper) {
		List<DataAttributeJsonPath> dataAttributeJsonPaths = new ArrayList<>();
		if (dataAttributeJsonPathMapping.isEmpty()) {
			return dataAttributeJsonPaths;
		}

		String xmlEscapedPathObject = StringEscapeUtils.unescapeHtml4(dataAttributeJsonPathMapping);
		JSONObject jsonMapping = new JSONObject(xmlEscapedPathObject);

		for (Object key : jsonMapping.keySet()) {
			if (!(key instanceof String)) {
				continue;
			}
			String dataAttributeName = key.toString();
			DataAttribute dataAttribute = parserHelper.getNameToDataAttribute(dataClass, dataAttributeName);
			
			String unescapedJsonPath = jsonMapping.getString(dataAttributeName);
			String jsonPath = unescapedJsonPath.replace("'", "''");

			DataAttributeJsonPath dataAttributeJsonPath = new DataAttributeJsonPath(dataAttribute, jsonPath);
			dataAttributeJsonPaths.add(dataAttributeJsonPath);
		}

		return dataAttributeJsonPaths;
	}

	/**
	 * Get all AtomicDataStateConditions by their ids.
	 * 
	 * @param dataNodeReferences
	 * @return List of DataStateCondition
	 */
	public List<AtomicDataStateCondition> resolveDataNodeReferences(List<String> dataNodeReferences) {
		List<AtomicDataStateCondition> conditions = new ArrayList<>();
		for (String id : dataNodeReferences) {
			if (associationMap.containsKey(id)) {
				AtomicDataStateCondition condition = associationMap.get(id);
				conditions.add(condition);
			}
		}
		return conditions;
	}

	/**
	 * Resolves the DataFlow for a ControlNode by the ids of DataNodes, parse
	 * them into ConditionSets.
	 * 
	 * @param availableConditions
	 */
	public DataStateCondition parseDataStateCondition(List<AtomicDataStateCondition> availableConditions) {
		// List<AtomicDataStateCondition> resolvedConditions = resolveDataNodeReferences(availableConditions);
		if (availableConditions.isEmpty()) {
			return new DataStateCondition();
		}
		List<AtomicDataStateCondition> definiteConditions = getDefiniteConditions(availableConditions);
		availableConditions.removeAll(definiteConditions);

		List<List<AtomicDataStateCondition>> remainingConditions = sortRemainingConditions(availableConditions);

		List<List<AtomicDataStateCondition>> cartesianProductOfRemainingConditions = cartesianProduct(remainingConditions);

		List<ConditionSet> conditionSets = new ArrayList<>();
		for (List<AtomicDataStateCondition> conditions : cartesianProductOfRemainingConditions) {
			conditions.addAll(definiteConditions);
			conditionSets.add(new ConditionSet(conditions));
		}

		return new DataStateCondition(conditionSets);
	}

	/**
	 * Get those AtomicDataStateConditions which DataClass (TODO: identity) does
	 * not occur twice. These are the AtomicDataStateConditions that occur in
	 * all ConditionSets that will be parsed. If afterwards the
	 * resolvedConditions are empty the ControlNode will only have one
	 * ConditionSet because all conditions are definite.
	 * 
	 * @param resolvedConditions
	 * @return List of AtomicDataStateConditions
	 */
	private List<AtomicDataStateCondition> getDefiniteConditions(List<AtomicDataStateCondition> resolvedConditions) {
		Map<DataClass, AtomicDataStateCondition> dataClassToDefineCondition = new HashMap<>();

		for (AtomicDataStateCondition condition : resolvedConditions) {
			DataClass dataClass = condition.getDataClass();
			if (!dataClassToDefineCondition.containsKey(dataClass)) {
				dataClassToDefineCondition.put(dataClass, null);
			}
		}

		for (AtomicDataStateCondition condition : resolvedConditions) {
			DataClass dataClass = condition.getDataClass();
			if (dataClassToDefineCondition.containsKey(dataClass)) {
				AtomicDataStateCondition conditionInMap = dataClassToDefineCondition.get(dataClass);
				if (conditionInMap == null) {
					dataClassToDefineCondition.put(dataClass, condition);
				} else {
					dataClassToDefineCondition.remove(dataClass);
				}
			}
		}

		Collection<AtomicDataStateCondition> definiteConditions = dataClassToDefineCondition.values();
		return new ArrayList<>(definiteConditions);
	}

	/**
	 * Sort the remaining DataStateConditions by their DataClass (TODO: and
	 * their identity) and put those which have the same DataClass (and
	 * identity) in the same List.
	 * 
	 * @param resolvedConditions
	 * @return List of List of DataStateCondition
	 */
	private List<List<AtomicDataStateCondition>> sortRemainingConditions(List<AtomicDataStateCondition> resolvedConditions) {
		Map<DataClass, List<AtomicDataStateCondition>> conditionMap = new HashMap<>();
		for (AtomicDataStateCondition remainingCondition : resolvedConditions) {
			DataClass dataClass = remainingCondition.getDataClass();
			if (conditionMap.containsKey(dataClass)) {
				List<AtomicDataStateCondition> similiarConditions = conditionMap.get(dataClass);
				// TODO: check conditionSet for same identity
				similiarConditions.add(remainingCondition);
			} else {
				List<AtomicDataStateCondition> conditions = new ArrayList<>();
				conditions.add(remainingCondition);
				conditionMap.put(dataClass, conditions);
			}
		}
		List<List<AtomicDataStateCondition>> values = new ArrayList<>();
		for (List<AtomicDataStateCondition> list : conditionMap.values()) {
			values.add(list);
		}
		return values;
	}

	/**
	 * Create the Cartesian Product by a List of List of DataStateConditions.
	 * 
	 * @param lists
	 * @return List of List of DataStateConditions
	 */
	private List<List<AtomicDataStateCondition>> cartesianProduct(List<List<AtomicDataStateCondition>> lists) {
		List<List<AtomicDataStateCondition>> resultLists = new ArrayList<>();
		if (lists.isEmpty()) {
			resultLists.add(new ArrayList<AtomicDataStateCondition>());
			return resultLists;
		} else {
			List<AtomicDataStateCondition> firstList = lists.get(0);
			List<List<AtomicDataStateCondition>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
			for (AtomicDataStateCondition condition : firstList) {
				for (List<AtomicDataStateCondition> remainingList : remainingLists) {
					ArrayList<AtomicDataStateCondition> resultList = new ArrayList<>();
					resultList.add(condition);
					resultList.addAll(remainingList);
					resultLists.add(resultList);
				}
			}
		}
		return resultLists;
	}

	/**
	 * Get all DataNodes that were resolved.
	 * 
	 * @return List of DataNodes
	 */
	public List<AtomicDataStateCondition> getResolvedDataNodes() {
		Collection<AtomicDataStateCondition> dataNodes = associationMap.values();
		return new ArrayList<>(dataNodes);
	}
}
