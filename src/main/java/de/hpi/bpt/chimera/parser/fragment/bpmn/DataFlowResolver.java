package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;

public class DataFlowResolver {
	/**
	 * Map from Id of DataNodeObjectReference to DataNode.
	 */
	private Map<String, DataStateCondition> associationMap = new HashMap<>();

	/**
	 * Creates a Resolver for the SequenceFlows of a certain Fragment. Therefore
	 * the CaseModelParserHelper is needed that contains Map from Name to
	 * DataClass and Name to ObjectLifecycleState.
	 * 
	 * @param fragXmlWrap
	 * @param parserHelper
	 */
	public DataFlowResolver(FragmentXmlWrapper fragXmlWrap, CaseModelParserHelper parserHelper) {
		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.DataNode dataNodeReference : fragXmlWrap.getDataNodes()) {
			DataStateCondition condition = new DataStateCondition();
			// TODO:
			// condition.setJsonPath(dataNodeReference.getJsonPath());

			DataClass dataClass = parserHelper.getNameToDataClass(dataNodeReference.getDataClassName());
			ObjectLifecycleState olcState = parserHelper.getNameToObjectLifecycleState(dataClass, dataNodeReference.getStateName());

			condition.setDataClass(dataClass);
			condition.setState(olcState);

			associationMap.put(dataNodeReference.getDataNodeObjectReferenceId(), condition);
		}
	}

	/**
	 * Resolves the DataFlow for a ControlNode by the ids of DataNodes, parse
	 * them into ConditionSets.
	 * 
	 * @param dataNodeReferences
	 */
	public List<ConditionSet> resolveDataFlow(List<String> dataNodeReferences) {
		List<DataStateCondition> resolvedConditions = resolveDataNodeReferences(dataNodeReferences);

		List<DataStateCondition> definiteConditions = getDefiniteConditions(resolvedConditions);
		resolvedConditions.removeAll(definiteConditions);

		List<List<DataStateCondition>> remainingConditions = sortRemainingConditions(resolvedConditions);

		List<List<DataStateCondition>> cartesianProductOfRemainingConditions = cartesianProduct(remainingConditions);

		List<ConditionSet> conditionSets = new ArrayList<>();
		for (List<DataStateCondition> conditions : cartesianProductOfRemainingConditions) {
			conditions.addAll(definiteConditions);
			conditionSets.add(new ConditionSet(conditions));
		}

		return conditionSets;
	}

	/**
	 * Get all DataStateConditions by their ids.
	 * 
	 * @param dataNodeReferences
	 * @return List of DataStateCondition
	 */
	private List<DataStateCondition> resolveDataNodeReferences(List<String> dataNodeReferences) {
		List<DataStateCondition> conditions = new ArrayList<>();
		for (String id : dataNodeReferences) {
			if (associationMap.containsKey(id)) {
				DataStateCondition condition = associationMap.get(id);
				conditions.add(condition);
			}
		}
		return conditions;
	}

	/**
	 * Get those DataStateConditions which DataClass (TODO: identity) does not
	 * occur twice. These are the DataStateConditions that occur in all
	 * ConditionSets that will be parsed. If afterwards the resolvedConditions
	 * are empty the ControlNode will only have one ConditionSet because all
	 * conditions are definite.
	 * 
	 * @param resolvedConditions
	 * @return List of DataStateCondition
	 */
	private List<DataStateCondition> getDefiniteConditions(List<DataStateCondition> resolvedConditions) {
		Map<DataClass, DataStateCondition> dataClassToDefineCondition = new HashMap<>();

		for (DataStateCondition condition : resolvedConditions) {
			DataClass dataClass = condition.getDataClass();
			if (!dataClassToDefineCondition.containsKey(dataClass)) {
				dataClassToDefineCondition.put(dataClass, null);
			}
		}

		for (DataStateCondition condition : resolvedConditions) {
			DataClass dataClass = condition.getDataClass();
			if (dataClassToDefineCondition.containsKey(dataClass)) {
				DataStateCondition conditionInMap = dataClassToDefineCondition.get(dataClass);
				if (conditionInMap == null) {
					dataClassToDefineCondition.put(dataClass, condition);
				} else {
					dataClassToDefineCondition.remove(dataClass);
				}
			}
		}

		Collection<DataStateCondition> definiteConditions = dataClassToDefineCondition.values();
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
	private List<List<DataStateCondition>> sortRemainingConditions(List<DataStateCondition> resolvedConditions) {
		Map<DataClass, List<DataStateCondition>> conditionMap = new HashMap<>();
		for (DataStateCondition remainingCondition : resolvedConditions) {
			DataClass dataClass = remainingCondition.getDataClass();
			if (conditionMap.containsKey(dataClass)) {
				List<DataStateCondition> similiarConditions = conditionMap.get(dataClass);
				// TODO: check conditionSet for same identity
				similiarConditions.add(remainingCondition);
			} else {
				List<DataStateCondition> conditions = new ArrayList<>();
				conditions.add(remainingCondition);
				conditionMap.put(dataClass, conditions);
			}
		}
		List<List<DataStateCondition>> values = new ArrayList<>();
		for (List<DataStateCondition> list : conditionMap.values()) {
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
	private List<List<DataStateCondition>> cartesianProduct(List<List<DataStateCondition>> lists) {
		List<List<DataStateCondition>> resultLists = new ArrayList<>();
		if (lists.isEmpty()) {
			resultLists.add(new ArrayList<DataStateCondition>());
			return resultLists;
		} else {
			List<DataStateCondition> firstList = lists.get(0);
			List<List<DataStateCondition>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
			for (DataStateCondition condition : firstList) {
				for (List<DataStateCondition> remainingList : remainingLists) {
					ArrayList<DataStateCondition> resultList = new ArrayList<>();
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
	public List<DataStateCondition> getResolvedDataNodes() {
		Collection<DataStateCondition> dataNodes = associationMap.values();
		return new ArrayList<>(dataNodes);
	}
}
