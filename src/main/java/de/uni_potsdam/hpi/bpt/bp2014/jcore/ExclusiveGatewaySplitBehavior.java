package de.uni_potsdam.hpi.bpt.bp2014.jcore;


import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


public class ExclusiveGatewaySplitBehavior extends ParallelOutgoingBehavior {
    static Logger log = Logger.getLogger(ExclusiveGatewaySplitBehavior.class.getName());
    /**
     * List of IDs of following control nodes.
     */
    private LinkedList<LinkedList<Integer>> followingControlNodes = new LinkedList<>();

    private DbState dbState = new DbState();

    /**
     * Initializes and creates an ExclusiveGatewaySplitBehavior
     *
     * @param gateway_id          The id of the gateway.
     * @param scenarioInstance    An instance from the class ScenarioInstance.
     * @param fragmentInstance_id The id of the fragment instance.
     */
    public ExclusiveGatewaySplitBehavior(int gateway_id, ScenarioInstance scenarioInstance, int fragmentInstance_id) {
        this.controlNode_id = gateway_id;
        this.scenarioInstance = scenarioInstance;
        this.fragmentInstance_id = fragmentInstance_id;
        initializeFollowingControlNodeIds();
    }


    /**
     * Adds the ids of the following control nodes to the list.
     * Creates for every control node a bucket.
     */
    private void initializeFollowingControlNodeIds() {
        LinkedList<Integer> ids = this.dbControlFlow.getFollowingControlNodes(controlNode_id);
        for (int i = 0; i < ids.size(); i++) {
            followingControlNodes.add(new LinkedList<Integer>());
            this.addFollowingControlNode(i, ids.get(i));
        }
    }

    /**
     * Adds the control node id to the bucket. Looks for XOR and AND to add the following control nodes.
     *
     * @param bucket_id The bucket id.
     * @param id        The id of the control node getting added.
     */
    private void addFollowingControlNode(int bucket_id, int id) {
        LinkedList<Integer> ids = followingControlNodes.get(bucket_id);
        ids.add(id);
        followingControlNodes.set(bucket_id, ids);
        if (dbControlNode.getType(id).equals("XOR") || dbControlNode.getType(id).equals("AND")) {
            for (int controlNode_id : dbControlFlow.getFollowingControlNodes(id)) {
                this.addFollowingControlNode(bucket_id, controlNode_id);
            }
        }
    }

    @Override
    public void terminate() {
        this.checkAfterTermination();
        this.runAfterTermination();
    }

    /**
     * Executes the XOR gateway and enable the following control nodes.
     */
    public void execute() {
        enableFollowing();
    }

    @Override
    protected ControlNodeInstance createFollowingNodeInstance(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : scenarioInstance.getControlNodeInstances()) {
            if (controlNode_id == controlNodeInstance.controlNode_id && !controlNodeInstance.getClass().equals(ActivityInstance.class) && !controlNodeInstance.getStateMachine().state.equals("terminated")) {
                return controlNodeInstance;
            }
        }
        String type = dbControlNode.getType(controlNode_id);
        ControlNodeInstance controlNodeInstance = createControlNode(type, controlNode_id);
        setAutomaticExecutionToFalse(type, controlNodeInstance);
        return controlNodeInstance;
    }

    private void setAutomaticExecutionToFalse(String type, ControlNodeInstance controlNodeInstance) {
        switch (type) {
            case "Activity":
            case "EmailTask":
            case "WebServiceTask":
                ((ActivityInstance) controlNodeInstance).setAutomaticExecution(false);
                break;
            case "AND":
                ((GatewayInstance) controlNodeInstance).setAutomaticExecution(false);
                break;
        }
    }

    /**
     * Checks if the gateway can terminate, because the given control node has changed his state.
     *
     * @param controlNode_id The id of the control node.
     * @return True if the gateway can terminate. false if not.
     */
    public boolean checkTermination(int controlNode_id) {
        if ((dbControlNode.getType(controlNode_id).equals("AND")) || (dbControlNode.getType(controlNode_id).equals("XOR"))) {
            return false;
        }
        for (int i = 0; i < followingControlNodes.size(); i++) {
            if (followingControlNodes.get(i).contains(new Integer(controlNode_id))) {
                followingControlNodes.remove(i);
                for (LinkedList<Integer> followingControlNode_ids : followingControlNodes) {
                    for (int id : followingControlNode_ids) {
                        ControlNodeInstance controlNodeInstance = scenarioInstance.getControlNodeInstanceForControlNodeId(id);
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
        Map<Integer, String> conditions = dbControlFlow.getConditions(controlNode_id);
        Set keys = conditions.keySet();
        Iterator key = keys.iterator();
        Integer controlNode_id = 0;
        boolean defaultExecution = true;
        int defaultControlNode = -1;
        while (key.hasNext()) {
            controlNode_id = (Integer) key.next();
            if ((conditions.get(controlNode_id)).equals("DEFAULT")) {
                defaultControlNode = controlNode_id;
            } else if (evaluateCondition(conditions.get(controlNode_id))) {
                defaultExecution = false;
                break;
            }
        }
        if (defaultExecution) {
            if (defaultControlNode != -1) {
                ControlNodeInstance controlNodeInstance = super.createFollowingNodeInstance(defaultControlNode);
                controlNodeInstance.enableControlFlow();
            }
        } else {
            ControlNodeInstance controlNodeInstance = super.createFollowingNodeInstance(controlNode_id);
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
     * @param i
     * @return
     */
    private boolean checkCondition(Tree ast, int i) {
        String left = ast.getChild(i).toStringTree();
        String comparison = ast.getChild(i + 1).toStringTree();
        String right = ast.getChild(i + 2).toStringTree();
        for (DataAttributeInstance dataAttributeInstance : scenarioInstance.getDataAttributeInstances().values()) {
            left = left.replace(
                    "#" + (dataAttributeInstance.getDataObjectInstance()).getName()
                            + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
            right = right.replace(
                    "#" + (dataAttributeInstance.getDataObjectInstance()).getName()
                            + "." + dataAttributeInstance.getName(), dataAttributeInstance.getValue().toString());
        }
        for (DataObjectInstance dataObjectInstance : scenarioInstance.getDataObjectInstances()) {
            left = left.replace(
                    "#" + dataObjectInstance.getName(), dbState.getStateName(dataObjectInstance.getState_id()));
            right = right.replace(
                    "#" + dataObjectInstance.getName(), dbState.getStateName(dataObjectInstance.getState_id()));
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

            }
        } catch (NumberFormatException e) {
            log.error("Error can't convert String to Float:", e);
        } catch (NullPointerException e) {
            log.error("Error can't convert String to Float, String is null:", e);
        }
        return false;
    }
}
