package de.hpi.bpt.chimera.model.petrinet;

import java.util.HashMap;
import java.util.Map;

import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public class FragmentTranslation extends AbstractTranslation {
	private final Fragment fragment;
	protected final Place initialPlace;
	protected final Place finalPlace;

	private final Map<String, AbstractControlNodeTranslation> controlNodeTranslationsById = new HashMap<>();

	public FragmentTranslation(TranslationContext translationContext, Fragment fragment) {
		super(translationContext, fragment.getName());
		this.fragment = fragment;
		final BpmnFragment bpmnFragment = fragment.getBpmnFragment();

		// Translate activites
		for (AbstractActivity activity : bpmnFragment.getActivities()) {
			translateActivity(activity);
		}

		// Translate events
		for (AbstractEvent event : bpmnFragment.getEvents()) {
			translateEvent(event);
		}

		// Translate gateways
		for (AbstractGateway gateway : bpmnFragment.getEventBasedGateways()) {
			translateEventGateway(gateway);
		}
		for (AbstractGateway gateway : bpmnFragment.getExclusiveGateways()) {
			translateExclusiveGateway(gateway);
		}
		for (AbstractGateway gateway : bpmnFragment.getParallelGateways()) {
			translateParallelGateway(gateway);
		}

		// Translate sequence flow
		for (SequenceFlowAssociation flowAssociation : bpmnFragment.getSequenceFlowAssociations()) {
			translateSequenceFlow(flowAssociation);
		}

		// Connect start event
		AbstractControlNodeTranslation startEventTranslation = controlNodeTranslationsById
				.get(bpmnFragment.getStartEvent().getId());
		initialPlace = startEventTranslation.getInitialPlace();

		// Connect end event
		AbstractControlNodeTranslation endEventTranslation = controlNodeTranslationsById
				.get(bpmnFragment.getEndEvent().getId());
		finalPlace = endEventTranslation.getFinalPlace();
	}

	private void translateActivity(AbstractActivity activity) {
		ActivityTranslation activityTranslation = new ActivityTranslation(context, activity);
		controlNodeTranslationsById.put(activity.getId(), activityTranslation);
	}

	private void translateEvent(AbstractEvent event) {
		EventTranslation eventTranslation = new EventTranslation(context, event);
		controlNodeTranslationsById.put(event.getId(), eventTranslation);
	}

	private void translateExclusiveGateway(AbstractGateway gateway) {
		AbstractGatewayTranslation gatewayTranslation = new ExclusiveGatewayTranslation(context, gateway);
		controlNodeTranslationsById.put(gateway.getId(), gatewayTranslation);
	}

	private void translateParallelGateway(AbstractGateway gateway) {
		AbstractGatewayTranslation gatewayTranslation = new ParallelGatewayTranslation(context, gateway);
		controlNodeTranslationsById.put(gateway.getId(), gatewayTranslation);
	}

	private void translateEventGateway(AbstractGateway gateway) {
		translateExclusiveGateway(gateway);
	}

	private void translateSequenceFlow(SequenceFlowAssociation flowAssociation) {
		final AbstractControlNode sourceNode = flowAssociation.getSourceRef();
		final AbstractControlNode targetNode = flowAssociation.getTargetRef();

		final String sourceId = flowAssociation.getSourceRef().getId();
		final String targetId = flowAssociation.getTargetRef().getId();

		AbstractControlNodeTranslation sourceTranslation = controlNodeTranslationsById.get(sourceId);
		AbstractControlNodeTranslation targetTranslation = controlNodeTranslationsById.get(targetId);

		if (sourceTranslation == null) {
			controlNodeTranslationsById.keySet().stream().forEach(id -> System.out.println(id));
			throw new RuntimeException("cannot find source: " + sourceId);
		}
		if (targetTranslation == null) {
			controlNodeTranslationsById.keySet().stream().forEach(id -> System.out.println(id));
			throw new RuntimeException("cannot find target: " + targetId);
		}

		Place inputPlace;
		Place outputPlace;
		if (sourceNode instanceof AbstractGateway) {
			int gatewayOutgoingFlowIndex = sourceNode.getOutgoingSequenceFlows().indexOf(flowAssociation);
			inputPlace = ((AbstractGatewayTranslation) sourceTranslation).getOutputPlaces()
					.get(gatewayOutgoingFlowIndex);
		} else {
			inputPlace = sourceTranslation.getFinalPlace();
		}

		if (targetNode instanceof AbstractGateway) {
			int gatewayIncomingFlowIndex = targetNode.getIncomingSequenceFlows().indexOf(flowAssociation);
			outputPlace = ((AbstractGatewayTranslation) targetTranslation).getInputPlaces()
					.get(gatewayIncomingFlowIndex);
		} else {
			outputPlace = targetTranslation.getInitialPlace();
		}

		final String transitionName = sourceTranslation.getPrefixString() + "to_" + targetTranslation.getPrefixString();

		addTransition(transitionName, inputPlace, outputPlace);
	}

	public Place getInitialPlace() {
		return initialPlace;
	}

	public Place getFinalPlace() {
		return finalPlace;
	}
}
