package de.hpi.bpt.chimera.rest.beans.casemodel;

import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.petrinet.CaseModelTranslation;
import de.hpi.bpt.chimera.model.petrinet.PetriNet;

@XmlRootElement
public class CaseModelPetriNetRepresentationJaxBean {
	private String id;
	private String name;
	private PetriNet petriNet;

	public CaseModelPetriNetRepresentationJaxBean(CaseModel cm) {
		setId(cm.getId());
		setName(cm.getName());

		final CaseModelTranslation caseModelTranslation = new CaseModelTranslation(cm);

		setPetriNet(caseModelTranslation.getPetriNet());
	}

	public String getLolaOutput() {
		StringBuilder builder = new StringBuilder();
		builder.append("PLACE\n");
		builder.append(petriNet.getPlaces().stream().map(place -> place.getName()).collect(Collectors.joining(", ")));
		builder.append(";\n\n");
		builder.append("MARKING\n");
		builder.append(petriNet.getPlaces().stream().filter(place -> place.getNumTokens() > 0)
				.map(place -> place.getName() + ":" + place.getNumTokens()).collect(Collectors.joining(", ")));
		builder.append(";\n\n");
		builder.append(petriNet.getTransitions().stream().map(transition -> {
			return "TRANSITION " + transition.getName() + "\n" + "  CONSUME "
					+ transition.getInputPlaces().stream().map(place -> place.getName())
							.collect(Collectors.joining(", "))
					+ ";\n" + "  PRODUCE " + transition.getOutputPlaces().stream().map(place -> place.getName())
							.collect(Collectors.joining(", "))
					+ ";\n";
		}).collect(Collectors.joining("\n")));
		return builder.toString();
	}

	public String getDotOutput() {
		StringBuilder builder = new StringBuilder();
		builder.append("digraph G {\n");

		builder.append("  subgraph places {\n");
		builder.append("    graph [shape=circle];\n");
		builder.append("    node [shape=circle,fixedsize=true,width=2];\n");
		builder.append(petriNet.getPlaces().stream().map(place -> "    \"" + place.getName() + "\";\n")
				.collect(Collectors.joining("")));
		builder.append("  }\n");

		builder.append("  subgraph transitions {\n");
		builder.append("    node [shape=rect,height=0.2,width=2];\n");
		builder.append(petriNet.getTransitions().stream().map(transition -> "    \"" + transition.getName() + "\";\n")
				.collect(Collectors.joining("")));
		builder.append("  }\n");

		builder.append(petriNet.getTransitions().stream().map(transition -> {
			return ""
					+ transition.getInputPlaces().stream()
							.map(place -> "  \"" + place.getName() + "\" -> \"" + transition.getName() + "\";\n")
							.collect(Collectors.joining(""))
					+ transition.getOutputPlaces().stream()
							.map(place -> "  \"" + transition.getName() + "\" -> \"" + place.getName() + "\";\n")
							.collect(Collectors.joining(""));
		}).collect(Collectors.joining("")));

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
