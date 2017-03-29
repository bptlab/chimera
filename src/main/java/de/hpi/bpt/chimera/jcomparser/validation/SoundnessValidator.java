package de.hpi.bpt.chimera.jcomparser.validation;

import de.hpi.bpt.chimera.jcomparser.saving.AbstractControlNode;
import de.hpi.bpt.chimera.jcomparser.saving.Fragment;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class SoundnessValidator {
	public static void validateStructuralSoundness(Fragment fragment) {
		//problem: while parallel gateways are not considered as control nodes,
		//incoming/outgoing includes all sequence flows, resulting in inconsistencies.
		Map<String, Set<String>> graph = buildGraphFromFragment(fragment);
		Map<String, Set<String>> reverseGraph = buildReverseGraph(graph);
		Set<String> nodes = fragment.getControlNodes().stream().map(AbstractControlNode::getId).collect(Collectors.toSet());
		// End of reverse graph == start of original graph
		Boolean hasOnlyOneStart = checkOnlyOneEnd(nodes, reverseGraph);
		Boolean hasOnlyOneEnd = checkOnlyOneEnd(nodes, graph);
		if (!hasOnlyOneStart || !hasOnlyOneEnd) {
			throw new IllegalArgumentException("There is not exactly one start or end place!");
		}
		String start = getLastNode(nodes, reverseGraph);
		String end = getLastNode(nodes, graph);
		Set<String> reachableFromStart = getReachableNodes(start, graph);
		Set<String> reachableFromEnd = getReachableNodes(end, reverseGraph);
		if (reachableFromStart.size() != reachableFromEnd.size()) {
			throw new IllegalArgumentException("The fragment is not structural sound");
		}
	}

	public static Map<String, Set<String>> buildGraphFromFragment(Fragment fragment) {
		Map<String, Set<String>> graph = new HashMap<>();
		for (AbstractControlNode node : fragment.getControlNodes()) {
			for (String currentIncoming : node.getIncoming()) {
				if (!graph.containsKey(currentIncoming)) {
					graph.put(currentIncoming, new HashSet<>());
				}
				graph.get(currentIncoming).add(node.getId());
			}
			for (String currentOutgoing : node.getOutgoing()) {
				if (!graph.containsKey(node.getId())) {
					graph.put(node.getId(), new HashSet<>());
				}
				graph.get(node.getId()).add(currentOutgoing);
			}
		}
		return graph;
	}

	public static Map<String, Set<String>> buildReverseGraph(Map<String, Set<String>> graph) {
		Map<String, Set<String>> reverseGraph = new HashMap<>();
		for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
			for (String currentValue : entry.getValue()) {
				if (!reverseGraph.containsKey(currentValue)) {
					reverseGraph.put(currentValue, new HashSet<>());
				}
				reverseGraph.get(currentValue).add(entry.getKey());
			}
		}
		return reverseGraph;
	}

	/**
	 * A place with no outgoing edges must be an output place,
	 * so we can check by subtracting nodes with outgoing edges from the list of all nodes.
	 *
	 * @param originalNodes A list of all nodes in the fragment.
	 * @param graph         The graph of the fragment.
	 * @return a Boolean stating whether there is exactly one output place.
	 */
	public static Boolean checkOnlyOneEnd(Set<String> originalNodes, Map<String, Set<String>> graph) {
		Set<String> nodes = new HashSet<>(originalNodes);
		nodes.removeAll(graph.keySet());
		return nodes.size() == 1;
	}

	public static String getLastNode(Set<String> originalNodes, Map<String, Set<String>> graph) {
		Set<String> nodes = new HashSet<>(originalNodes);
		nodes.removeAll(graph.keySet());
		assert nodes.size() == 1 : "There is more than one end.";
		return nodes.iterator().next();
	}

	public static Set<String> getReachableNodes(String start, Map<String, Set<String>> graph) {
		Queue<String> queue = new LinkedList<>();
		Set<String> reachableNodes = new HashSet<>();
		queue.add(start);
		reachableNodes.add(start);
		while (!queue.isEmpty()) {
			String node = queue.remove();
			if (graph.containsKey(node)) {
				for (String childNode : graph.get(node)) {
					//avoid cycles
					if (!reachableNodes.contains(childNode)) {
						queue.add(childNode);
						reachableNodes.add(childNode);
					}
				}
			}
		}
		return reachableNodes;
	}
}
