package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.condition.DataObjectStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;

public class DataFlowResolver {
	/**
	 * Map from Id of DataNodeObjectReference to DataNode.
	 */
	private Map<String, DataNode> associationMap = new HashMap<>();

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
			DataNode dataNode = new DataNode();

			dataNode.setId(dataNodeReference.getDataNodeId());
			dataNode.setName(dataNodeReference.getName());
			dataNode.setJsonPath(dataNodeReference.getJsonPath());

			DataClass dataClass = parserHelper.getNameToDataClass(dataNodeReference.getDataClassName());
			ObjectLifecycleState olcState = parserHelper.getNameToObjectLifecycleState(dataClass, dataNodeReference.getStateName());

			DataObjectStateCondition condition = new DataObjectStateCondition(dataClass, olcState);
			dataNode.setDataObjectState(condition);

			associationMap.put(dataNodeReference.getDataNodeObjectReferenceId(), dataNode);
		}
	}

	/**
	 * Resolves the incoming DataFlow for a certain ControlNode and sets them.
	 * 
	 * @param incomingDataNodeObjectReferences
	 */
	public void resolveIncomingDataFlow(List<String> incomingDataNodeObjectReferences, AbstractDataControlNode controlNode) {
		List<DataNode> incomingDataNodes = new ArrayList<>();
		for (String id : incomingDataNodeObjectReferences) {
			if (associationMap.containsKey(id)) {
				DataNode dataNode = associationMap.get(id);
				incomingDataNodes.add(dataNode);
			}
		}
		controlNode.setIncomingDataNodes(incomingDataNodes);
	}

	/**
	 * Resolves the outgoing DataFlow for a certain ControlNode and sets them.
	 * 
	 * @param outgoingDataNodeObjectReferences
	 */
	public void resolveOutgoingDataFlow(List<String> outgoingDataNodeObjectReferences, AbstractDataControlNode controlNode) {
		List<DataNode> outgoingDataNodes = new ArrayList<>();
		for (String id : outgoingDataNodeObjectReferences) {
			if (associationMap.containsKey(id)) {
				DataNode dataNode = associationMap.get(id);
				outgoingDataNodes.add(dataNode);
			}
		}
		controlNode.setOutgoingDataNodes(outgoingDataNodes);
	}

	/**
	 * Get all DataNodes that were resolved.
	 * 
	 * @return List of DataNodes
	 */
	public List<DataNode> getResolvedDataNodes() {
		Collection<DataNode> dataNodes = associationMap.values();
		return new ArrayList<>(dataNodes);
	}
}
