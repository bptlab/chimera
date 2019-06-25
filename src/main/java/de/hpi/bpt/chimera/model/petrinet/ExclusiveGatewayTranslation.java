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
			for (Place outputPlace : getOutputPlaces()) {
				Transition t = addTransition("exclusiveSplit_" + outputPlace.getName(), getInitialPlace(), outputPlace);
				// TODO FIXME this does not work in LoLA.
				//t.setFairness(Transition.Fairness.STRONG);
			}
		} else {
			for (Place inputPlace : getInputPlaces()) {
				Transition t = addTransition("exclusiveJoin_" + inputPlace.getName(), inputPlace, getFinalPlace());
			}
		}
	}
}
