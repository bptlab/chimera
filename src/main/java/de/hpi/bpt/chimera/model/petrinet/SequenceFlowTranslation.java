package de.hpi.bpt.chimera.model.petrinet;

import java.util.Map;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public class SequenceFlowTranslation extends AbstractTranslation {

	final Map<String, AbstractControlNodeTranslation> controlNodeTranslationsById;

	public SequenceFlowTranslation(TranslationContext translationContext, SequenceFlowAssociation flowAssociation,
			final Map<String, AbstractControlNodeTranslation> controlNodeTranslationsById) {
		super(translationContext, flowAssociation.getId());

		this.controlNodeTranslationsById = controlNodeTranslationsById;

		final AbstractControlNode sourceNode = flowAssociation.getSourceRef();
		final AbstractControlNode targetNode = flowAssociation.getTargetRef();

		final String sourceId = sourceNode.getId();
		final String targetId = targetNode.getId();

		AbstractControlNodeTranslation sourceTranslation = controlNodeTranslationsById.get(sourceId);
		AbstractControlNodeTranslation targetTranslation = controlNodeTranslationsById.get(targetId);

		if (sourceTranslation == null) {
			controlNodeTranslationsById.keySet().stream().forEach(System.out::println);
			throw new RuntimeException("cannot find source: " + sourceId);
		}
		if (targetTranslation == null) {
			controlNodeTranslationsById.keySet().stream().forEach(System.out::println);
			throw new RuntimeException("cannot find target: " + targetId);
		}

		Place sequenceFlowInputPlace = getFinalPlaceOfNode(flowAssociation, sourceNode, sourceTranslation);
		Place sequenceFlowOutputPlace = getInitialPlaceOfNode(flowAssociation, targetNode, targetTranslation);

		// Try to fuse the places
		if (!sequenceFlowInputPlace.isSignificant() && !sequenceFlowOutputPlace.isSignificant()
				&& TranslationContext.isOptimizeTranslation()) {

			final String fusedPlaceName = sourceTranslation.getName() + "_to_" + targetTranslation.getName();

			// Create new fused place
			fusePlaces(sequenceFlowInputPlace, sequenceFlowOutputPlace, fusedPlaceName);

		} else {
			// If fusing is not possible, create new transition
			final String transitionName = sourceTranslation.getName() + "_to_" + targetTranslation.getName();
			addTransition(transitionName, sequenceFlowInputPlace, sequenceFlowOutputPlace);
		}
	}

	private Place getInitialPlaceOfNode(SequenceFlowAssociation flowAssociation, final AbstractControlNode targetNode,
			AbstractControlNodeTranslation targetTranslation) {

		if (targetNode instanceof AbstractGateway) {
			int gatewayIncomingFlowIndex = targetNode.getIncomingSequenceFlows().indexOf(flowAssociation);
			return ((AbstractGatewayTranslation) targetTranslation).getInputPlaces().get(gatewayIncomingFlowIndex);
		} else {
			return targetTranslation.getInitialPlace();
		}

	}

	private Place getFinalPlaceOfNode(SequenceFlowAssociation flowAssociation, final AbstractControlNode sourceNode,
			AbstractControlNodeTranslation sourceTranslation) {

		if (sourceNode instanceof AbstractGateway) {
			int gatewayOutgoingFlowIndex = sourceNode.getOutgoingSequenceFlows().indexOf(flowAssociation);
			return ((AbstractGatewayTranslation) sourceTranslation).getOutputPlaces().get(gatewayOutgoingFlowIndex);
		} else {
			return sourceTranslation.getFinalPlace();
		}

	}

}
