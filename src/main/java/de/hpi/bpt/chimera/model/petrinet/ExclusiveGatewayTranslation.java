package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public class ExclusiveGatewayTranslation extends AbstractGatewayTranslation {

	public ExclusiveGatewayTranslation(TranslationContext translationContext, AbstractGateway gateway) {
		super(translationContext, gateway);

		for (SequenceFlowAssociation incomingSequenceFlow : gateway.getIncomingSequenceFlows()) {
			inputPlaces.add(addPlace("from_" + incomingSequenceFlow.getSourceRef().getId()));
		}
		for (SequenceFlowAssociation outgoingSequenceFlow : gateway.getOutgoingSequenceFlows()) {
			outputPlaces.add(addPlace("to_" + outgoingSequenceFlow.getTargetRef().getId()));
		}

		assert (getInputPlaces().size() == 1 || getOutputPlaces().size() == 1);

		if (isSplit()) {
			if (getOutputPlaces().stream().noneMatch(p -> p.isSignificant())) {
				// connect following nodes (transitions) directly to this gateway's input place
				Place inputPlace = getInputPlaces().iterator().next();
				for (Place outputPlace : getOutputPlaces()) {
					assert (this.context.getCaseModelTranslation().getPetriNet().getPlaces().contains(inputPlace));
					System.out.println("merging " + outputPlace.getPrefixedIdString() + " into "
							+ inputPlace.getPrefixedIdString());
					((PlaceReference) outputPlace).setImpl(((PlaceReference) inputPlace).getImpl());
					inputPlace.setSignificant(true);
				}
			} else {
				// Fallback "traditional" translation
				// TODO this has the drawback of enabling deadlocks
				// (picking an outgoing branch that will never be data-enabled)!
				for (Place outputPlace : getOutputPlaces()) {
					addTransition("exclusiveSplit_" + outputPlace.getName(), getInitialPlace(), outputPlace);
				}
			}
		} else {
			for (Place inputPlace : getInputPlaces()) {
				addTransition("exclusiveJoin_" + inputPlace.getName(), inputPlace, getFinalPlace());
			}
		}
	}
}
