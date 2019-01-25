package de.hpi.bpt.chimera.rest.beans.casemodel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.petrinet.CaseModelTranslation;
import de.hpi.bpt.chimera.model.petrinet.PetriNet;
import de.hpi.bpt.chimera.model.petrinet.Place;
import de.hpi.bpt.chimera.model.petrinet.Transition;

@XmlRootElement
public class CaseModelPetriNetRepresentationJaxBean {
	private String id;
	private String name;
	private PetriNet petriNet;

	private static class Cluster {
		private final String name;
		private final Map<String, Cluster> childrenByName = new HashMap<>();
		private final Collection<Place> places = new HashSet<>();
		private final Collection<Transition> transitions = new HashSet<>();

		public Cluster(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Collection<Place> getPlaces() {
			return places;
		}

		public void addPlace(Place p) {
			places.add(p);
		}

		public Collection<Transition> getTransitions() {
			return transitions;
		}

		public void addTransition(Transition t) {
			transitions.add(t);
		}

		public Cluster getChildByName(String clusterName) {
			if (!childrenByName.containsKey(clusterName)) {
				childrenByName.put(clusterName, new Cluster(clusterName));
			}
			return childrenByName.get(clusterName);
		}

		public Collection<Cluster> getChildren() {
			return childrenByName.values();
		}
	}

	public CaseModelPetriNetRepresentationJaxBean(CaseModel cm) {
		setId(cm.getId());
		setName(cm.getName());

		final CaseModelTranslation caseModelTranslation = new CaseModelTranslation(cm);

		setPetriNet(caseModelTranslation.getPetriNet());
	}

	public String getLolaOutput() {
		StringBuilder builder = new StringBuilder();
		builder.append("PLACE\n");
		builder.append(petriNet.getPlaces().stream().distinct().map(place -> place.getName())
				.collect(Collectors.joining(", ")));
		builder.append(";\n\n");
		builder.append("MARKING\n");
		builder.append(petriNet.getPlaces().stream().distinct().filter(place -> place.getNumTokens() > 0)
				.map(place -> place.getName() + ":" + place.getNumTokens()).collect(Collectors.joining(", ")));
		builder.append(";\n\n");
		builder.append(petriNet.getTransitions().stream().distinct().map(transition -> {
			return "TRANSITION " + transition.getName() + "\n" + "  CONSUME "
					+ transition.getInputPlaces().stream().distinct().map(place -> place.getName())
							.collect(Collectors.joining(", "))
					+ ";\n" + "  PRODUCE " + transition.getOutputPlaces().stream().distinct()
							.map(place -> place.getName()).collect(Collectors.joining(", "))
					+ ";\n";
		}).collect(Collectors.joining("\n")));
		return builder.toString();
	}

	private String indent(int indentationLevel) {
		return String.join("", Collections.nCopies(indentationLevel, "  "));
	}

	private String getPlaceFormat(int indentationLevel) {
		return indent(indentationLevel) + "node [shape=circle,fixedsize=true,width=2];\n";
	}

	private String formatPlace(Place p, int indentationLevel) {
		return indent(indentationLevel) + "\"" + p.getPrefixedIdString() + "\" [label=\"" + p.getName() + "\"];\n";
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
		return indent(indentationLevel) + "\"" + t.getPrefixedIdString() + "\" [label=\"" + t.getName() + "\"];\n";
	}

	private void getClusterDotOutput(StringBuilder builder, Cluster cluster, int indentationLevel) {

		int innerIndentationLevel = indentationLevel + 1;

		// root cluster has no subgraph
		if (indentationLevel > 0) {
			builder.append(indent(indentationLevel));
			builder.append("subgraph cluster_" + cluster.getName() + " {\n");
			builder.append(indent(innerIndentationLevel));
			builder.append("label=\"" + cluster.getName() + "\";\n");
		}

		// nested clusters
		for (Cluster childCluster : cluster.getChildren()) {
			getClusterDotOutput(builder, childCluster, innerIndentationLevel);
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

	public String getDotOutput() {

		Cluster rootCluster = new Cluster("");

		// Cluster places
		for (Place place : petriNet.getPlaces()) {
			Cluster cluster = rootCluster;
			// Navigate clusters and create on-demand
			for (String prefix : place.getContext().getPrefixes()) {
				cluster = cluster.getChildByName(prefix);
			}
			cluster.addPlace(place);
		}

		// Cluster transitions
		for (Transition transition : petriNet.getTransitions()) {
			Cluster cluster = rootCluster;
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

		// Clusters
		getClusterDotOutput(builder, rootCluster, 0);

		// Edges
		for (Transition t : petriNet.getTransitions()) {
			builder.append(formatTransitionEdges(t, 1));
		}

		builder.append("}");
		return builder.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PetriNet getPetriNet() {
		return petriNet;
	}

	public void setPetriNet(PetriNet petriNet) {
		this.petriNet = petriNet;
	}
}
