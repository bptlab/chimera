package de.hpi.bpt.chimera.execution.controlnodes.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.xorgrammarcompiler.XORGrammarCompiler;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.EventBasedGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ExclusiveGateway;

/**
 * This class represents an ExclusiveGateway. It provides methods and functions
 * for the incoming and outgoing controlflow logic as well as functions for
 * parsing and executing conditions given as attributes of outgoing controlflows
 * which makes branching possible.
 *
 */
@Entity
public class ExclusiveGatewayInstance extends AbstractGatewayInstance {
	private static Logger log = Logger.getLogger(ExclusiveGatewayInstance.class);

	// ToDo isn't persisted and leads to errors. Probably we have to defined a
	// type which stores the
	// second List, because List<List<>> isn't possible
	private List<List<String>> branches = new ArrayList<>();

	/**
	 * for JPA only
	 */
	public ExclusiveGatewayInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}

	public ExclusiveGatewayInstance(ExclusiveGateway gateway, FragmentInstance fragmentInstance) {
		super(gateway, fragmentInstance);
		initializeBranches();
	}

	/**
	 * Adds the ids of the following control nodes to the list. Creates for
	 * every control node a bucket.
	 */
	private void initializeBranches() {
		for (AbstractControlNode node : this.getControlNode().getOutgoingControlNodes()) {
			branches.add(expandBranch(node));
		}
	}

	/**
	 * Expands branch to cover all following control nodes skipping intermediate
	 * gateways.
	 *
	 * @param id
	 *            The id of the control node getting added.
	 */
	private List<String> expandBranch(AbstractControlNode controlNode) {
		List<String> ids = new ArrayList<>();
		ids.add(controlNode.getId());

		// Expanding each branch along outgoing Gateways until the first non
		// Gateway ControlNode appears
		if (controlNode.getClass().equals(ExclusiveGateway.class) || controlNode.getClass().equals(ParallelGateway.class) || controlNode.getClass().equals(EventBasedGateway.class)) {
			for (AbstractControlNode outgoingNode : controlNode.getOutgoingControlNodes()) {
				ids.addAll(expandBranch(outgoingNode));
			}
		}
		return ids;
	}

	/**
	 * Skips all ControlNodeInstances which aren't on the branch of the given
	 * ControlNode
	 *
	 * @param ongoingNode
	 *            The ControlNode on the ongoing branch. (the begun activity)
	 */
	public void skipAlternativeBranches(AbstractControlNode ongoingNode) {
		for (List<String> branch : branches) {
			if (!branch.contains(ongoingNode.getId())) {
				// log.info("skipping a branch");
				skipBranch(branch);
			}
		}
		this.terminate();
	}

	/**
	 * Skips all the ControlNodes with the given ControlNodeIds
	 *
	 * @param branch
	 *            The list of ControlNodeIds that should be skipped.
	 */
	private void skipBranch(List<String> branch) {
		// Gets the ControlNodesInstances for the given Ids and calls skip for
		// each of the ControlNodeInstances.
		for (String toSkip : branch) {
			ControlNodeInstance node = this.getFragmentInstance().getControlNodeInstanceById(toSkip);
			if (node != null) {
				node.skip();
				// log.info(String.format("skipping the ControlNode with the id:
				// %s", node.getControlNode().getId()));
			}
		}
	}

	/**
	 * Returns true, if the given ControlNode follows this Gateway (directly or
	 * with some other Gateways in between). Returns false otherwise.
	 * 
	 * @param controlNode
	 *            The ControlNode which is tested
	 * @return true when the Controlnode is in following, false otherwise
	 */
	public boolean containsControlNodeInFollowing(AbstractControlNode controlNode) {
		List<String> allFollowing = branches.stream().flatMap(Collection::stream).collect(Collectors.toList());
		return allFollowing.contains(controlNode.getId());
	}

	/**
	 * Automatically begins the Gateway.
	 */
	@Override
	public void enableControlFlow() {
		log.info("XOR-Gateway Controlflow enabled");
		begin();
	}

	/**
	 * Instantiates the following ControlNodes (and starts automatic tasks).
	 */
	@Override
	public void begin() {
		setState(State.EXECUTING);
		// getFragmentInstance().createFollowing(getControlNode());
		this.evaluateConditions();
		// TODO is it right to start automatic Tasks here?
		// this.getFragmentInstance().getCase().getCaseExecutioner().startAutomaticTasks();
		// this.terminate();
	}

	/**
	 * Terminates an executing Gateway (sets its state to TERMINATED).
	 */
	@Override
	public void terminate() {
		if (!getState().equals(State.EXECUTING))
			return;
		setState(State.TERMINATED);
	}

	/**
	 * Skips the gateway (e.g. when this Gateway is part of a branch after an
	 * ExclusiveGateway but the user selects another branch) Sets the state of
	 * the Gateway to SKIPPED.
	 */
	@Override
	public void skip() {
		// TODO Auto-generated method stub
		this.setState(State.SKIPPED);
	}

	/**
	 * returns the ExclusiveGateway to this ExclusiveGatewayInstance.
	 */
	@Override
	public ExclusiveGateway getControlNode() {
		return (ExclusiveGateway) super.getControlNode();
	}


	/**
	 * Evaluates Conditions for the control flow of an XOR gateway.
	 */
	public void evaluateConditions() {
		// Map<AbstractControlNode, String> conditions =
		// this.getControlNode().getOutgoingSequenceFlows().stream().collect(Collectors.toMap(item
		// -> item.getTargetRef(), item -> item.getCondition()));
		List<SequenceFlowAssociation> associations = this.getControlNode().getOutgoingSequenceFlows().stream().collect(Collectors.toList());
		// Map<Integer, String> conditions =
		// this.getDbControlFlow().getConditions(getControlNodeId());
		AbstractControlNode controlNode;
		AbstractControlNode defaultControlNode = null;
		String condition;
		Set<AbstractControlNode> toEnable = new HashSet<>();
		for (SequenceFlowAssociation association : associations) {
			controlNode = association.getTargetRef();
			condition = association.getCondition();
			if (condition.equals("DEFAULT")) {
				defaultControlNode = controlNode;
			} else if (evaluateCondition(condition)) { // condition true or
														// empty
				toEnable.add(controlNode);
			}
		}
		if (toEnable.isEmpty() && defaultControlNode != null)
			toEnable.add(defaultControlNode);
		for (AbstractControlNode node : toEnable) {
			ControlNodeInstance controlNodeInstance = this.getFragmentInstance().createControlNodeInstance(node);
			if (controlNodeInstance.getClass() == AbstractActivityInstance.class) {
				((AbstractActivityInstance) controlNodeInstance).forbidAutomaticStart();
			}
			controlNodeInstance.enableControlFlow();
		}
	}


	/**
	 * Evaluates one specific condition.
	 *
	 * @param condition
	 *            The condition as String.
	 * @return true if the condition ist true.
	 */
	public boolean evaluateCondition(String condition) {
		if ("".equals(condition))
			return true; // empty condition is true
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
	 * @param ast
	 *            a tree (ast).
	 * @param i
	 *            index for the ast.
	 * @return the check result.
	 */
	private boolean checkCondition(Tree ast, int i) {
		String left = ast.getChild(i).toStringTree();
		String comparison = ast.getChild(i + 1).toStringTree();
		String right = ast.getChild(i + 2).toStringTree();

		log.info("Beginning to replace dataObject references.");
		for (DataObject dataObject : this.getCaseExecutioner().getDataManager().getDataObjects()) {
			String dataObjectName = dataObject.getDataClass().getName();
			log.info("Beginning to replace dataAttribute references. There are " + dataObject.getDataAttributeInstanceIdToInstance().size());
			for (DataAttributeInstance dataAttributeInstance : dataObject.getDataAttributeInstanceIdToInstance().values()) {
				log.info("a dataAttribute Instance");
				try {
				log.info(String.format("iterating through %s.%s with values:%s", dataObjectName, dataAttributeInstance.getDataAttribute().getName(), dataAttributeInstance.getValue().toString()));
				left = left.replace("#" + dataObjectName + "." + dataAttributeInstance.getDataAttribute().getName(), dataAttributeInstance.getValue().toString());
				right = right.replace("#" + dataObjectName + "." + dataAttributeInstance.getDataAttribute().getName(), dataAttributeInstance.getValue().toString());
				} catch (Exception e) {
					log.error("Error", e);
				}
			}
		}
		// TODO
		// for (DataObject dataObject :
		// this.getCaseExecutioner().getDataObjectInstances()) {
		// left = left.replace("#" + dataObject.getDataClass().getName(),
		// dbState.getStateName(dataObject.getStateId()));
		// right = right.replace("#" + dataObject.getDataClass().getName(),
		// dbState.getStateName(dataObject.getStateId()));
		// }

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
