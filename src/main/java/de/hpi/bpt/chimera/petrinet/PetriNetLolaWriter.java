package de.hpi.bpt.chimera.petrinet;

import java.util.stream.Collectors;

import de.hpi.bpt.chimera.model.petrinet.PetriNet;
import de.hpi.bpt.chimera.model.petrinet.Transition;

public class PetriNetLolaWriter extends AbstractPetriNetWriter {

	@Override
	public String write(PetriNet petriNet) {

		StringBuilder builder = new StringBuilder();

		builder.append("PLACE\n");
		builder.append(petriNet.getPlaces().stream().distinct().map(place -> place.getPrefixedIdString())
				.collect(Collectors.joining(", ")));
		builder.append(";\n\n");

		builder.append("MARKING\n");
		builder.append(petriNet.getPlaces().stream().distinct().filter(place -> place.getNumTokens() > 0)
				.map(place -> place.getPrefixedIdString() + ":" + place.getNumTokens())
				.collect(Collectors.joining(", ")));
		builder.append(";\n\n");

		for (Transition transition : petriNet.getTransitions()) {
			builder.append(formatTransition(transition));
		}

		return builder.toString();

	}

	private String formatTransition(Transition transition) {
		StringBuilder builder = new StringBuilder();
		String fairnessAssumption = getFairnessString(transition);

		builder.append("TRANSITION ").append(transition.getPrefixedIdString()).append(" ").append(fairnessAssumption)
				.append("\n").append("  CONSUME ")
				.append(transition.getInputPlaces().stream().distinct().map(place -> place.getPrefixedIdString())
						.collect(Collectors.joining(", ")))
				.append(";\n").append("  PRODUCE ").append(transition.getOutputPlaces().stream().distinct()
						.map(place -> place.getPrefixedIdString()).collect(Collectors.joining(", ")))
				.append(";\n").append("\n");
		return builder.toString();
	}

	private String getFairnessString(Transition transition) {
		switch (transition.getFairness()) {
		case STRONG:
			return "STRONG FAIR";
		case WEAK:
			return "WEAK FAIR";
		case NONE:
		default:
			return "";
		}
	}

}
