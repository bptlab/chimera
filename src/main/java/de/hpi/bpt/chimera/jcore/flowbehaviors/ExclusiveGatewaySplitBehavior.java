package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.database.data.DbState;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.XORGrammarCompiler;
import de.hpi.bpt.chimera.jcore.controlnodes.*;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class deals with the termination of exclusive gateways.
 * <p>
 * <p> Exclusive gateways can not be terminated like other control nodes, since
 * the control nodes after the gateway are exclusive to each other. So the gateway
 * has still influence on the execution, although it already activated the following control nodes.
 * <p>
 * <p> Nested gateways are supported by allowing multiple control nodes in each branch, that
 * follows the exclusive gateway.
 * <p>
 * <p> To ensure proper skipping of alternative branches each
 * {@link de.hpi.bpt.chimera.jcore.executionbehaviors.AbstractExecutionBehavior},
 * has to call {@link ScenarioInstance#skipAlternativeControlNodes(int)}, when the
 * state of associated control node instances changes to running.
 * <p>
 * <p> When the exclusive gateway outgoing branches, are annotated with conditions
 * the control flow can be evaluated automatically {@link XORGrammarCompiler}. However
 * the modelling of this is not supported at the moment, so this feature would have to be
 * tested again.
 */
public class ExclusiveGatewaySplitBehavior extends AbstractParallelOutgoingBehavior {
	private static Logger log = Logger.getLogger(ExclusiveGatewaySplitBehavior.class);
	/**
	 * List of IDs of following control nodes.
	 */
	private List<List<Integer>> branches = new ArrayList<>();

	private DbState dbState = new DbState();
	private String type = null;

	/**
	 * Initializes and creates an ExclusiveGatewaySplitBehavior.
	 *
	 * @param gatewayId          The id of the gateway.
	 * @param scenarioInstance   An instance from the class ScenarioInstance.
	 * @param fragmentInstanceId The id of the fragment instance.
	 */
	public ExclusiveGatewaySplitBehavior(int gatewayId, ScenarioInstance scenarioInstance, int fragmentInstanceId) {
		this.setControlNodeId(gatewayId);
		this.setScenarioInstance(scenarioInstance);
		this.setFragmentInstanceId(fragmentInstanceId);
		initializeBranches();
	}

	/**
	 * Adds the ids of the following control nodes to the list.
	 * Creates for every control node a bucket.
	 */
	private void initializeBranches() {
		List<Integer> ids = this.getDbControlFlow().getFollowingControlNodes(getControlNodeId());
		for (int id : ids) {
			branches.add(expandBranch(id));
		}
	}

	/**
	 * Expands branch to cover all control nodes skipping intermediate
	 * gateways.
	 *
	 * @param id The id of the control node getting added.
	 */
	private List<Integer> expandBranch(int id) {
		List<Integer> ids = new ArrayList<>();
		ids.add(id);
		String type = this.getDbControlNode().getType(id);

		if ("XOR".equals(type) || "AND".equals(type) || "EVENT_BASED".equals(type)) {
			for (int controlNodeId : this.getDbControlFlow().getFollowingControlNodes(id)) {
				ids.addAll(expandBranch(controlNodeId));
			}
		}
		return ids;
	}

	@Override
	public void terminate() {
		ScenarioInstance scenarioInstance = this.getScenarioInstance();
		scenarioInstance.updateDataFlow();
		// enableFollowing();
		// do not enable all successors, just the one with fulfilled conditions
		evaluateConditions();
	}

	@Override
	public void skip() {

	}

	/**
	 * Executes the XOR gateway and enable the following control nodes.
	 */
	public void execute() {
		enableFollowing();
	}

	@Override
	protected AbstractControlNodeInstance createFollowingNodeInstance(int controlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance : this.getScenarioInstance().getControlNodeInstances()) {
			if (controlNodeId == controlNodeInstance.getControlNodeId() && !controlNodeInstance.getClass().equals(ActivityInstance.class) && !controlNodeInstance.getState().equals(State.TERMINATED)) {
				return controlNodeInstance;
			}
		}
		if (type == null || this.getControlNodeId() != controlNodeId) {
			type = this.getDbControlNode().getType(controlNodeId);
		}
		ControlNodeFactory controlNodeFactory = new ControlNodeFactory();
		AbstractControlNodeInstance controlNodeInstance = controlNodeFactory.createControlNodeInstance(controlNodeId, getFragmentInstanceId(), getScenarioInstance());
		setAutomaticExecutionToFalse(type, controlNodeInstance);
		getScenarioInstance().getControlNodeInstances().add(controlNodeInstance);
		return controlNodeInstance;
	}

	private void setAutomaticExecutionToFalse(String type, AbstractControlNodeInstance controlNodeInstance) {
		switch (type) {
		case "Activity":
		case "EmailTask":
		case "WebServiceTask":
			((ActivityInstance) controlNodeInstance).setAutomaticTask(false);
			break;
		case "XOR":
		case "EVENT_BASED":
			((GatewayInstance) controlNodeInstance).setAutomaticExecution(false);
			break;
		default:
			break;
		}
	}

	public void skipAlternativeBranches(int controlNodeId) {
		for (List<Integer> branch : branches) {
			if (!branch.contains(controlNodeId)) {
				skipBranch(branch);
			}
		}
	}

	private void skipBranch(List<Integer> branch) {
		for (int toSkip : branch) {
			AbstractControlNodeInstance controlNodeInstance = this.getScenarioInstance().getControlNodeInstanceForControlNodeId(toSkip);
			if (controlNodeInstance != null) controlNodeInstance.skip();
		}
	}

	/**
	 * Evaluates Conditions for the control flow of an XOR gateway.
	 */
	public void evaluateConditions() {
		Map<Integer, String> conditions = this.getDbControlFlow().getConditions(getControlNodeId());
		Integer controlNodeId;
		Integer defaultControlNode = -1;
		String condition;
		Set<Integer> toEnable = new HashSet<>();
		Iterator<Integer> nodes = conditions.keySet().iterator();
		while (nodes.hasNext()) {
			controlNodeId = nodes.next();
			condition = conditions.get(controlNodeId);
			if (condition.equals("DEFAULT")) {
				defaultControlNode = controlNodeId;
			} else if (evaluateCondition(condition)) { // condition true or empty
				toEnable.add(controlNodeId);
			}
		}
		if (toEnable.isEmpty() && defaultControlNode != -1) toEnable.add(defaultControlNode);
		for (Integer nodeId : toEnable) {
			AbstractControlNodeInstance controlNodeInstance = super.createFollowingNodeInstance(nodeId);
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
		if ("".equals(condition)) return true; // empty condition is true
		XORGrammarCompiler compiler = new XORGrammarCompiler();
		CommonTree ast = compiler.compile(condition);
		return ast.getChildCount() > 0 && evaluate(0, ast);
	}

	private boolean evaluate(int i, Tree ast) {
		boolean condition = checkCondition(ast, i);
		if (ast.getChildCount() >= i + 4) {
			if (ast.getChild(i + 3).toStringTree().equals("&") || ast.getChild(i + 3).toStringTree().equals(" & ")) {
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
	 * @param i   index for the ast.
	 * @return the check result.
	 */
	private boolean checkCondition(Tree ast, int i) {
		String left = ast.getChild(i).toStringTree();
		String comparison = ast.getChild(i + 1).toStringTree();
		String right = ast.getChild(i + 2).toStringTree();
		for (DataAttributeInstance dataAttributeInstance : this.getScenarioInstance().getDataAttributeInstances().values()) {
			left = left.replace("#" + (dataAttributeInstance.getDataObject()).getName() + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
			right = right.replace("#" + (dataAttributeInstance.getDataObject()).getName() + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
		}
		for (DataObject dataObject : this.getScenarioInstance().getDataManager().getDataObjects()) {
			left = left.replace("#" + dataObject.getName(), dbState.getStateName(dataObject.getStateId()));
			right = right.replace("#" + dataObject.getName(), dbState.getStateName(dataObject.getStateId()));
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

	public boolean containsControlNodeInFollowing(int controlNodeId) {
		List<Integer> allFollowing = branches.stream().flatMap(Collection::stream).collect(Collectors.toList());
		return allFollowing.contains(controlNodeId);
	}
}
