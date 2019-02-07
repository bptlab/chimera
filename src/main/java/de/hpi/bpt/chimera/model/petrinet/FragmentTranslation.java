package de.hpi.bpt.chimera.model.petrinet;

import java.util.HashMap;
import java.util.Map;

import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public class FragmentTranslation extends AbstractTranslation {
	private final Fragment fragment;

	protected Place initialPlace;

	private final Map<String, AbstractControlNodeTranslation> controlNodeTranslationsById = new HashMap<>();
	private final Map<String, SequenceFlowTranslation> sequenceFlowTranslationsById = new HashMap<>();

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

		// Connect start event, translate pre-condition and fragment re-initialization
		translateStartEvent(bpmnFragment.getStartEvent());

		// Connect end event
		translateEndEvent(bpmnFragment.getEndEvent());
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
		// Event gateway is treated as exclusive gateway
		translateExclusiveGateway(gateway);
	}

	private void translateSequenceFlow(SequenceFlowAssociation flowAssociation) {
		SequenceFlowTranslation sequenceFlowTranslation = new SequenceFlowTranslation(context, flowAssociation,
				controlNodeTranslationsById);
		sequenceFlowTranslationsById.put(flowAssociation.getId(), sequenceFlowTranslation);
	}

	private void translateStartEvent(StartEvent startEvent) {
		// Connect start event
		EventTranslation startEventTranslation = (EventTranslation) controlNodeTranslationsById.get(startEvent.getId());
		Place startEventInitialPlace = startEventTranslation.getInitialPlace();

		initialPlace = startEventInitialPlace;
		initialPlace.setSignificant(true);

		// If there is a fragment pre-condition, add it before the start event
		if (fragment.getFragmentPreCondition() != null
				&& !fragment.getFragmentPreCondition().getConditionSets().isEmpty()) {
			Place fragmentInitialPlace = addPlace(fragment.getName() + "_init");
			DataStatePreConditionTranslation preConditionTranslation = new DataStatePreConditionTranslation(context,
					fragment.getFragmentPreCondition(), fragment.getName() + "_pre", fragmentInitialPlace,
					startEventInitialPlace);
			initialPlace = fragmentInitialPlace;
		}

		// Re-initialization after fragment started
		translateReInitialization(startEventTranslation);
	}

	private void translateReInitialization(EventTranslation startEventTranslation) {
		Transition startEventTransition = startEventTranslation.getEventTransition();
		startEventTransition.addOutputPlace(initialPlace);
	}

	private void translateEndEvent(EndEvent endEvent) {
		EventTranslation endEventTranslation = (EventTranslation) controlNodeTranslationsById.get(endEvent.getId());
		Transition endEventTransition = endEventTranslation.getEventTransition();
		final Place finalPlace = endEventTranslation.getFinalPlace();

		// Disconnect final place (transition just consumes the token)
		endEventTransition.getOutputPlaces().remove(finalPlace);
		assert (getPetriNet().getTransitions().stream().noneMatch(t -> t.getInputPlaces().contains(finalPlace)));
		assert (getPetriNet().getTransitions().stream().noneMatch(t -> t.getOutputPlaces().contains(finalPlace)));
		getPetriNet().getPlaces().remove(finalPlace);
		// TODO the end event translation still has a reference to the final place...
	}

	public Place getInitialPlace() {
		return initialPlace;
	}
}
