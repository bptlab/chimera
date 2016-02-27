package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * This class deals with the split behavior of exclusive gateways.
 */
public class ExclusiveGatewaySplitBehavior extends AbstractParallelOutgoingBehavior {
	private static Logger log = Logger.getLogger(ExclusiveGatewaySplitBehavior.class.getName());
	/**
	 * List of IDs of following control nodes.
	 */
	private LinkedList<LinkedList<Integer>> followingControlNodes = new LinkedList<>();

	private DbState dbState = new DbState();
	private String type = null;

	/**
	 * Initializes and creates an ExclusiveGatewaySplitBehavior.
	 *
	 * @param gatewayId          The id of the gateway.
	 * @param scenarioInstance    An instance from the class ScenarioInstance.
	 * @param fragmentInstanceId The id of the fragment instance.
	 */
	public ExclusiveGatewaySplitBehavior(int gatewayId, ScenarioInstance scenarioInstance,
			int fragmentInstanceId) {
		this.setControlNodeId(gatewayId);
		this.setScenarioInstance(scenarioInstance);
		this.setFragmentInstanceId(fragmentInstanceId);
		initializeFollowingControlNodeIds();
	}

	/**
	 * Adds the ids of the following control nodes to the list.
	 * Creates for every control node a bucket.
	 */
	private void initializeFollowingControlNodeIds() {
		LinkedList<Integer> ids = this.getDbControlFlow()
				.getFollowingControlNodes(getControlNodeId());
		for (int i = 0; i < ids.size(); i++) {
			followingControlNodes.add(new LinkedList<Integer>());
			this.addFollowingControlNode(i, ids.get(i));
		}
	}

	/**
	 * Adds the control node id to the bucket.
	 * Looks for XOR and AND to add the following control nodes.
	 *
	 * @param bucketId The bucket id.
	 * @param id        The id of the control node getting added.
	 */
	private void addFollowingControlNode(int bucketId, int id) {
		LinkedList<Integer> ids = followingControlNodes.get(bucketId);
		ids.add(id);
		followingControlNodes.set(bucketId, ids);
		if (type == null || this.getControlNodeId() != id) {
			type = this.getDbControlNode().getType(id);
		}
		if ("XOR".equals(type) || "AND".equals(type) || "EVENT_BASED".equals(type)) {
			for (int controlNodeId : this.getDbControlFlow()
					.getFollowingControlNodes(id)) {
				this.addFollowingControlNode(bucketId, controlNodeId);
			}
		}
	}

	@Override public void terminate() {
		this.checkAfterTermination();
		this.runAfterTermination();
	}

	/**
	 * Executes the XOR gateway and enable the following control nodes.
	 */
	public void execute() {
		enableFollowing();
	}

	@Override protected AbstractControlNodeInstance createFollowingNodeInstance(
			int controlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance
				: this.getScenarioInstance().getControlNodeInstances()) {
			if (controlNodeId == controlNodeInstance.getControlNodeId()
					&& !controlNodeInstance.getClass().equals(
					ActivityInstance.class) && !controlNodeInstance
					.getStateMachine().getState().equals(AbstractStateMachine.STATE.TERMINATED)) {
				return controlNodeInstance;
			}
		}
		if (type == null || this.getControlNodeId() != controlNodeId) {
			type = this.getDbControlNode().getType(controlNodeId);
		}
		AbstractControlNodeInstance controlNodeInstance = createControlNode(
				type, controlNodeId);
		setAutomaticExecutionToFalse(type, controlNodeInstance);
		return controlNodeInstance;
	}

	private void setAutomaticExecutionToFalse(String type,
			AbstractControlNodeInstance controlNodeInstance) {
		switch (type) {
		case "Activity":
		case "EmailTask":
		case "WebServiceTask":
			((ActivityInstance) controlNodeInstance).setAutomaticExecution(false);
			break;
		case "XOR":
		case "EVENT_BASED":
			((GatewayInstance) controlNodeInstance).setAutomaticExecution(false);
			break;
		default:
			break;
		}
	}

	/**
	 * Checks if the gateway can terminate
	 * (because the given control node has changed his state).
	 *
	 * @param controlNodeId The id of the control node.
	 * @return True if the gateway can terminate. false if not.
	 */
	public boolean checkTermination(int controlNodeId) {
		if (type == null || this.getControlNodeId() != controlNodeId) {
			type = this.getDbControlNode().getType(controlNodeId);
		}
		if (("AND".equals(type)) || ("XOR".equals(type))) {
			return false;
		}
		for (int i = 0; i < followingControlNodes.size(); i++) {
			if (followingControlNodes.get(i).contains(new Integer(controlNodeId))) {
				followingControlNodes.remove(i);
				for (LinkedList<Integer> followingControlNodeIds
						: followingControlNodes) {
					for (int id : followingControlNodeIds) {
						AbstractControlNodeInstance controlNodeInstance
								= this.getScenarioInstance()
						.getControlNodeInstanceForControlNodeId(id);
						controlNodeInstance.skip();
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Evaluates Conditions for the control flow of an XOR gateway.
	 */
	public void evaluateConditions() {
		Map<Integer, String> conditions = this.getDbControlFlow()
				.getConditions( getControlNodeId());
		Set<Integer> keys = conditions.keySet();
		Iterator<Integer> key = keys.iterator();
		Integer controlNodeId = 0;
		boolean defaultExecution = true;
		int defaultControlNode = -1;
		while (key.hasNext()) {
			controlNodeId = key.next();
			if ((conditions.get(controlNodeId)).equals("DEFAULT")) {
				defaultControlNode = controlNodeId;
			} else if (evaluateCondition(conditions.get(controlNodeId))) {
				defaultExecution = false;
				break;
			}
		}
		if (defaultExecution) {
			if (defaultControlNode != -1) {
				AbstractControlNodeInstance controlNodeInstance = super
						.createFollowingNodeInstance(defaultControlNode);
				controlNodeInstance.enableControlFlow();
			}
		} else {
			AbstractControlNodeInstance controlNodeInstance = super
					.createFollowingNodeInstance(controlNodeId);
			controlNodeInstance.enableControlFlow();
		}

	}

	/**
	 * Evaluates one specific condition.
	 *
	 * @param condition The condition as String.
	 * @return true if the condition ist true.
	 */
	public boolean evaluateCondition(String condition) {
		XORGrammarCompiler compiler = new XORGrammarCompiler();
		CommonTree ast = compiler.compile(condition);
		return ast.getChildCount() > 0 && evaluate(0, ast);
	}

	private boolean evaluate(int i, Tree ast) {
		boolean condition = checkCondition(ast, i);
		if (ast.getChildCount() >= i + 4) {
			if (ast.getChild(i + 3).toStringTree().equals("&")
					|| ast.getChild(i + 3).toStringTree()
					.equals(" & ")) {
				return (condition & evaluate(i + 4, ast));
			} else {
				return (condition | evaluate(i + 4, ast));
			}
		} else {
			return condition;
		}
	}

	/**
	 * @param ast a tree (ast).
	 * @param i index for the ast.
	 * @return the check result.
	 */
	private boolean checkCondition(Tree ast, int i) {
		String left = ast.getChild(i).toStringTree();
		String comparison = ast.getChild(i + 1).toStringTree();
		String right = ast.getChild(i + 2).toStringTree();
		for (DataAttributeInstance dataAttributeInstance : this.getScenarioInstance()
				.getDataAttributeInstances().values()) {
			left = left.replace(
					"#" + (dataAttributeInstance.getDataObjectInstance())
							.getName() + "."
							+ dataAttributeInstance.getName(),
					dataAttributeInstance.getValue().toString());
			right = right.replace(
					"#" + (dataAttributeInstance.getDataObjectInstance())
							.getName() + "."
							+ dataAttributeInstance.getName(),
					dataAttributeInstance.getValue().toString());
		}
		for (DataObjectInstance dataObjectInstance
				: this.getScenarioInstance().getDataObjectInstances()) {
			left = left.replace("#" + dataObjectInstance.getName(),
					dbState.getStateName(dataObjectInstance.getStateId()));
			right = right.replace("#" + dataObjectInstance.getName(),
					dbState.getStateName(dataObjectInstance.getStateId()));
		}
		try {
			switch (comparison) {
			case "=":
				return left.equals(right);
			case "<":
				return Float.parseFloat(left) < Float.parseFloat(right);
			case "<=":
				return Float.parseFloat(left) <= Float.parseFloat(right);
			case ">":
				return Float.parseFloat(left) > Float.parseFloat(right);
			case ">=":
				return Float.parseFloat(left) >= Float.parseFloat(right);
			case "!=":
				return !left.equals(right);
			case "!<":
				return !(Float.parseFloat(left) < Float.parseFloat(right));
			case "!<=":
				return !(Float.parseFloat(left) <= Float.parseFloat(right));
			case "!>":
				return !(Float.parseFloat(left) > Float.parseFloat(right));
			case "!>=":
				return !(Float.parseFloat(left) >= Float.parseFloat(right));
			default:
				break;
			}
		} catch (NumberFormatException e) {
			log.error("Error can't convert String to Float:", e);
		} catch (NullPointerException e) {
			log.error("Error can't convert String to Float, String is null:", e);
		}
		return false;
	}
}
