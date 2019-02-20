package de.hpi.bpt.chimera.petrinet;

import de.hpi.bpt.chimera.model.petrinet.PetriNet;
import de.hpi.bpt.chimera.model.petrinet.Place;
import de.hpi.bpt.chimera.model.petrinet.Transition;

public class PetriNetDotWriter extends AbstractPetriNetWriter {

	@Override
	public String write(PetriNet petriNet) {

		PetriNetCluster rootPetriNetCluster = new PetriNetCluster("");

		// PetriNetCluster places
		for (Place place : petriNet.getPlaces()) {
			PetriNetCluster cluster = rootPetriNetCluster;
			// Navigate clusters and create on-demand
			for (String prefix : place.getContext().getPrefixes()) {
				cluster = cluster.getChildByName(prefix);
			}
			cluster.addPlace(place);
		}

		// PetriNetCluster transitions
		for (Transition transition : petriNet.getTransitions()) {
			PetriNetCluster cluster = rootPetriNetCluster;
			// Navigate clusters and create on-demand
			for (String prefix : transition.getContext().getPrefixes()) {
				cluster = cluster.getChildByName(prefix);
			}
			cluster.addTransition(transition);
		}

		// Outer skeleton
		StringBuilder builder = new StringBuilder();
		builder.append("digraph G {\n");
		builder.append(indent(1)).append("rankdir=LR;\n");
		builder.append(indent(1)).append("splines=ortho;\n");

		// PetriNetClusters
		getPetriNetClusterDotOutput(builder, rootPetriNetCluster, 0);

		// Edges
		for (Transition t : petriNet.getTransitions()) {
			builder.append(formatTransitionEdges(t, 1));
		}

		builder.append("}");
		return builder.toString();
	}

	private String getPlaceFormat(int indentationLevel) {
		return indent(indentationLevel) + "node [shape=circle,fixedsize=true,width=2];\n";
	}

	private String formatPlace(Place p, int indentationLevel) {
		return indent(indentationLevel) + "\"" + p.getPrefixedIdString() + "\" [label=\"" + p.getName() + "\n"
				+ p.getPrefixedIdString() + "\"];\n";
	}

	private String getTransitionFormat(int indentationLevel) {
		return indent(indentationLevel) + "node [shape=rect,height=2,width=0.2];\n";
	}

	private String formatTransitionEdges(Transition t, int indentationLevel) {
		String s = "";
		for (Place ip : t.getInputPlaces()) {
			s += indent(indentationLevel);
			s += "\"" + ip.getPrefixedIdString() + "\" -> \"" + t.getPrefixedIdString() + "\";\n";
		}
		for (Place op : t.getOutputPlaces()) {
			s += indent(indentationLevel);
			s += "\"" + t.getPrefixedIdString() + "\" -> \"" + op.getPrefixedIdString() + "\";\n";
		}
		return s;
	}

	private String formatTransition(Transition t, int indentationLevel) {
		return indent(indentationLevel) + "\"" + t.getPrefixedIdString() + "\" [label=\"" + t.getName() + "\n"
				+ t.getPrefixedIdString() + "\"];\n";
	}

	private void getPetriNetClusterDotOutput(StringBuilder builder, PetriNetCluster cluster, int indentationLevel) {

		int innerIndentationLevel = indentationLevel + 1;

		// root cluster has no subgraph
		if (indentationLevel > 0) {
			builder.append(indent(indentationLevel));
			builder.append("subgraph cluster_" + cluster.getName() + " {\n");
			builder.append(indent(innerIndentationLevel));
			builder.append("label=\"" + cluster.getName() + "\";\n");
		}

		// nested clusters
		for (PetriNetCluster childPetriNetCluster : cluster.getChildren()) {
			getPetriNetClusterDotOutput(builder, childPetriNetCluster, innerIndentationLevel);
		}

		// places
		if (!cluster.getPlaces().isEmpty()) {
			builder.append(getPlaceFormat(innerIndentationLevel));
		}
		for (Place p : cluster.getPlaces()) {
			builder.append(formatPlace(p, innerIndentationLevel));
		}

		// transitions
		if (!cluster.getTransitions().isEmpty()) {
			builder.append(getTransitionFormat(innerIndentationLevel));
		}
		for (Transition t : cluster.getTransitions()) {
			builder.append(formatTransition(t, innerIndentationLevel));
		}

		if (indentationLevel > 0) {
			builder.append(indent(indentationLevel));
			builder.append("}\n");
		}
	}

}
