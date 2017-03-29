package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.database.history.DbLogEntry;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;

/**
 * I create instances of control nodes or load them from the database. Other control node instances
 * call my methods when they terminate and want to continue the process flow.
 */
public class ControlNodeFactory {

	/**
	 * Given the Id of a control node instance I look up its type in the database and create the
	 * appropriate AbstractControlNodeInstance, e.g. a XorGatewayInstance.
	 * TODO I have to find out, why I ignore Activities, EmailTasks, WebServiceTasks, and SendTasks,
	 * 		who handles them?
	 *
	 * @param controlNodeInstanceId
	 * @param scenarioInstance
	 * @return a control node instance of the correct type
	 */
	public static AbstractControlNodeInstance loadControlNodeInstance(int controlNodeInstanceId, ScenarioInstance scenarioInstance) {
		AbstractControlNodeInstance controlNodeInstance = null;
		DbControlNodeInstance controlNodeDao = new DbControlNodeInstance();
		int controlNodeId = controlNodeDao.getControlNodeId(controlNodeInstanceId);
		int fragmentInstanceId = controlNodeDao.getFragmentInstanceId(controlNodeInstanceId);
		String type = new DbControlNode().getType(controlNodeId);
		switch (type) {
			case "Activity":
			case "EmailTask":
			case "WebServiceTask":
			case "SendTask":
			case "IntermediateThrowEvent":
				controlNodeInstance = new ActivityInstance(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
				break;
			case "XOR":
				controlNodeInstance = new XorGatewayInstance(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
				break;
			case "AND":
				controlNodeInstance = new AndGatewayInstance(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
				break;
			case "StartEvent":
				controlNodeInstance = new StartEvent(controlNodeId, fragmentInstanceId, scenarioInstance);
				break;
			case "IntermediateCatchEvent":
				controlNodeInstance = new IntermediateEvent(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
				break;
			case "ReceiveActivity":
				controlNodeInstance = new ReceiveActivity(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
				break;
			case "TimerEvent":
				controlNodeInstance = new TimerEventInstance(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
				break;
			case "BoundaryEvent":
				controlNodeInstance = new BoundaryEvent(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
				break;
			case "EventBasedGateway":
				controlNodeInstance = new EventBasedGatewayInstance(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
				break;
			default:
				break;
		}

		return controlNodeInstance;
	}

	/**
	 * @param controlNodeId the id of the control node to create.
	 * @return a control node instance of the specified type.
	 */
	public AbstractControlNodeInstance createControlNodeInstance(int controlNodeId, int fragmentInstanceId, ScenarioInstance scenarioInstance) {
		AbstractControlNodeInstance controlNodeInstance = null;
		String type = new DbControlNode().getType(controlNodeId);
		switch (type) {
			case "Activity":
			case "EmailTask":
			case "WebServiceTask":
			case "SendTask":
			case "IntermediateThrowEvent":
				controlNodeInstance = new ActivityInstance(controlNodeId, fragmentInstanceId, scenarioInstance);
				new DbLogEntry().logActivity(controlNodeInstance.getControlNodeInstanceId(), "init", scenarioInstance.getId());
				break;
			case "EndEvent":
				controlNodeInstance = new EndEvent(controlNodeId, fragmentInstanceId, scenarioInstance);
				break;
			case "XOR":
				controlNodeInstance = new XorGatewayInstance(controlNodeId, fragmentInstanceId, scenarioInstance);
				break;
			case "AND":
				controlNodeInstance = new AndGatewayInstance(controlNodeId, fragmentInstanceId, scenarioInstance);
				break;
			case "IntermediateCatchEvent":
				controlNodeInstance = new IntermediateEvent(controlNodeId, fragmentInstanceId, scenarioInstance);
				break;
			case "ReceiveActivity":
				controlNodeInstance = new ReceiveActivity(controlNodeId, fragmentInstanceId, scenarioInstance);
				break;
			case "TimerEvent":
				controlNodeInstance = new TimerEventInstance(controlNodeId, fragmentInstanceId, scenarioInstance);
				break;
			case "BoundaryEvent":
				controlNodeInstance = new BoundaryEvent(controlNodeId, fragmentInstanceId, scenarioInstance);
				break;
			case "EventBasedGateway":
				controlNodeInstance = new EventBasedGatewayInstance(controlNodeId, fragmentInstanceId, scenarioInstance);
				break;
			default:
				String errorMsg = "Invalid control node type %s";
				throw new IllegalArgumentException(String.format(errorMsg, type));
		}
		return controlNodeInstance;
	}

}
