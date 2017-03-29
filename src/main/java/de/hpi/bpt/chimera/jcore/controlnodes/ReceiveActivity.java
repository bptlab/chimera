package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.database.data.DbDataNode;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Receive activities are used to represent events that also change the states of data
 * objects.
 */
public class ReceiveActivity extends AbstractEvent {

	public ReceiveActivity(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance) {
		super(controlNodeId, fragmentInstanceId, scenarioInstance);
		this.setFragmentInstanceId(fragmentInstanceId);
	}

	public ReceiveActivity(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance, int controlNodeInstanceId) {
		super(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
	}

	@Override
	public String getType() {
		return "ReceiveActivity";
	}

	/**
	 * Terminates a ReceiveActivity.
	 * When terminating a , the terminating behavior of the
	 * {@link AbstractEvent} is executed setting the data attribute values from the
	 * json.
	 * <p>
	 * Additional to this, the
	 *
	 * @param eventJson the json string containing the values.
	 */
	@Override
	public void terminate(String eventJson) {
		super.terminate(eventJson);
		List<Integer> outputSets = new DbDataFlow().getOutputSetsForControlNode(this.getControlNodeId());
		assert outputSets.size() == 1 : "Receive tasks should have exactly one output set.";
		int outputSetId = outputSets.get(0);
		Map<Integer, Integer> idToState = new DbDataNode().getDataSetClassToStateMap(outputSetId);

		DataManager dataManager = this.getScenarioInstance().getDataManager();
		List<DataObject> availableInput = dataManager.getAvailableInput(this.getControlNodeId());
		Map<Integer, DataObject> inputMap = availableInput.stream().collect(Collectors.toMap(x -> x.getDataClassId(), Function.identity()));

		for (Map.Entry<Integer, Integer> entry : idToState.entrySet()) {
			DataObject inputObject = inputMap.get(entry.getKey());
			dataManager.changeDataObjectState(inputObject.getId(), entry.getValue(), this.getControlNodeInstanceId());
		}
	}
}
