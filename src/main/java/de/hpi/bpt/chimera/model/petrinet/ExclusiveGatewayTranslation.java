package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public class ExclusiveGatewayTranslation extends AbstractGatewayTranslation {

	public ExclusiveGatewayTranslation(TranslationContext translationContext, AbstractGateway gateway) {
		super(translationContext, gateway);

		final String prefixString = this.context.getPrefixString();

		for (SequenceFlowAssociation incomingSequenceFlow : gateway.getIncomingSequenceFlows()) {
			inputPlaces.add(addPlace(prefixString + "from_" + incomingSequenceFlow.getSourceRef().getId()));
		}
		for (SequenceFlowAssociation outgoingSequenceFlow : gateway.getOutgoingSequenceFlows()) {
			outputPlaces.add(addPlace(prefixString + "to_" + outgoingSequenceFlow.getTargetRef().getId()));
		}

		assert (getInputPlaces().size() == 1 || getOutputPlaces().size() == 1);

		if (isSplit()) {
			for (Place outputPlace : getOutputPlaces()) {
				addTransition(prefixString + outputPlace.getName(), getInitialPlace(), outputPlace);
			}
		} else {
			for (Place inputPlace : getInputPlaces()) {
				addTransition(prefixString + inputPlace.getName(), inputPlace, getFinalPlace());
			}
		}
	}
}
