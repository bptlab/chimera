package de.hpi.bpt.chimera.model.petrinet;

import java.util.HashMap;
import java.util.Map;

import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public class FragmentTranslation extends AbstractTranslation {
	private final Fragment fragment;

	private final Map<String, AbstractTranslation> childTranslationsById = new HashMap<>();

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
			translateGateway(gateway);
		}
		for (AbstractGateway gateway : bpmnFragment.getExclusiveGateways()) {
			translateGateway(gateway);
		}
		for (AbstractGateway gateway : bpmnFragment.getParallelGateways()) {
			translateGateway(gateway);
		}

		// Translate sequence flow
		for (SequenceFlowAssociation flowAssociation : bpmnFragment.getSequenceFlowAssociations()) {
			translateSequenceFlow(flowAssociation);
		}

		// Connect start event
		AbstractTranslation startEventTranslation = childTranslationsById.get(bpmnFragment.getStartEvent().getId());
		initialPlace = startEventTranslation.getInitialPlace();

		// Connect end event
		AbstractTranslation endEventTranslation = childTranslationsById.get(bpmnFragment.getEndEvent().getId());
		finalPlace = endEventTranslation.getFinalPlace();
	}

	private void translateActivity(AbstractActivity activity) {
		System.out.println(activity + " " + activity.getId());
		ActivityTranslation activityTranslation = new ActivityTranslation(translationContext, activity);
		childTranslationsById.put(activity.getId(), activityTranslation);
	}

	private void translateEvent(AbstractEvent event) {
		System.out.println(event + " " + event.getId());
		EventTranslation eventTranslation = new EventTranslation(translationContext, event);
		childTranslationsById.put(event.getId(), eventTranslation);
	}

	private void translateGateway(AbstractGateway gateway) {
		System.out.println(gateway + " " + gateway.getId());
		GatewayTranslation gatewayTranslation = new GatewayTranslation(translationContext, gateway);
		childTranslationsById.put(gateway.getId(), gatewayTranslation);
	}

	private void translateSequenceFlow(SequenceFlowAssociation flowAssociation) {
		final String sourceId = flowAssociation.getSourceRef().getId();
		final String targetId = flowAssociation.getTargetRef().getId();

		System.out.println(flowAssociation.getId() + " " + flowAssociation);
		System.out.println("- " + sourceId + " " + flowAssociation.getSourceRef());
		System.out.println("- " + targetId + " " + flowAssociation.getTargetRef());

		AbstractTranslation source = childTranslationsById.get(sourceId);
		AbstractTranslation target = childTranslationsById.get(targetId);

		if (source == null) {
			System.out.println("cannot find source: " + sourceId);
			childTranslationsById.keySet().stream().forEach(id -> System.out.println(id));
		}
		if (target == null) {
			System.out.println("cannot find target: " + targetId);
			childTranslationsById.keySet().stream().forEach(id -> System.out.println(id));
		}

		final String transitionName = source.getPrefixString() + "to_" + target.getPrefixString();

		Transition transition = new Transition(transitionName);
		transition.addInputPlace(source.getFinalPlace());
		transition.addOutputPlace(target.getInitialPlace());
		getPetriNet().addTransition(transition);
	}
}
