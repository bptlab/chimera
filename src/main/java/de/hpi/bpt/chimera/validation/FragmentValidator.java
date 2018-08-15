package de.hpi.bpt.chimera.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateCatchEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateThrowEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.SpecialBehavior;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

// TODO: think about how to aggregate the errors
public class FragmentValidator {

	private Fragment fragment;

	private final List<SpecialBehavior> allowedStartEventBehavior = new ArrayList<>(Arrays.asList(SpecialBehavior.NONE, SpecialBehavior.MESSAGE_RECEIVE));
	private final List<SpecialBehavior> allowedEndEventBehavior = new ArrayList<>(Arrays.asList(SpecialBehavior.NONE, SpecialBehavior.MESSAGE_SEND));
	private final List<SpecialBehavior> allowedThrowEventBehavior = new ArrayList<>(Arrays.asList(SpecialBehavior.MESSAGE_SEND));
	private final List<SpecialBehavior> allowedCatchEventBehavior = new ArrayList<>(Arrays.asList(SpecialBehavior.MESSAGE_RECEIVE, SpecialBehavior.TIMER));
	private final List<SpecialBehavior> allowedBoundaryEventBehavior = new ArrayList<>(Arrays.asList(SpecialBehavior.MESSAGE_RECEIVE, SpecialBehavior.TIMER));

	public FragmentValidator(Fragment fragment) {
		this.fragment = fragment;
	}

	/**
	 * Validate that the amount of Fragments in a CaseModel is not zero.
	 * 
	 * @param fragmentAmount
	 */
	public static void validateFragmentAmount(int fragmentAmount) {
		if (fragmentAmount == 0) {
			throw new IllegalArgumentException("no fragments specified");
		}
	}

	public void validate() {
		try {
			validateHasStartEvent();
			validateHasEndEvent();
			validateTimerEventPosition();
			validateEventType();
			validateDataControlNodeAmount();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * The Fragment needs to be rejected when one of the first Control Nodes in
	 * any Fragment is an TimerEvent.
	 */
	private void validateTimerEventPosition() {
		StartEvent startEvent = fragment.getBpmnFragment().getStartEvent();
		if (hasPrecedingTimerEvent(startEvent)) {
			throw new IllegalArgumentException("StartEvent cannot be followed by Timer Event");
		}
	}

	/**
	 * 
	 * @param controlNode
	 * @return true if the control node has a preceding timer event in the
	 *         control flow.
	 */
	private boolean hasPrecedingTimerEvent(AbstractControlNode controlNode) {
		if (controlNode instanceof AbstractEvent && !controlNode.equals(fragment.getBpmnFragment().getStartEvent())) {
			AbstractEvent event = (AbstractEvent) controlNode;
			if (event.getSpecialBehavior().equals(SpecialBehavior.TIMER)) {
				return true;
			}
		} else if (controlNode instanceof AbstractGateway || controlNode.equals(fragment.getBpmnFragment().getStartEvent())) {
			for (AbstractControlNode precedingControlNode : controlNode.getOutgoingControlNodes()) {
				if (hasPrecedingTimerEvent(precedingControlNode)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Every fragment needs to have a start event.
	 */
	private void validateHasStartEvent() {
		if (fragment.getBpmnFragment().getStartEvent() == null) {
			throw new IllegalArgumentException("No StartEvent");
		}
	}

	/**
	 * Every fragment needs to have an end event.
	 */
	private void validateHasEndEvent() {
		if (fragment.getBpmnFragment().getEndEvent() == null) {
			throw new IllegalArgumentException("No EndEvent");
		}
	}

	/**
	 * Events need to have the allowed behavior.
	 */
	private void validateEventType() {
		List<AbstractEvent> events = fragment.getBpmnFragment().getEvents();

		for (AbstractEvent event : events) {
			SpecialBehavior behavior = event.getSpecialBehavior();
			if (event instanceof StartEvent && !allowedStartEventBehavior.contains(behavior)) {
				throw new IllegalArgumentException(String.format("A StartEvent cannot have the type %s", behavior.toString()));
			}
			if (event instanceof EndEvent && !allowedEndEventBehavior.contains(behavior)) {
				throw new IllegalArgumentException(String.format("An EndEvent cannot have the type %s", behavior.toString()));
			}
			if (event instanceof IntermediateCatchEvent && !allowedCatchEventBehavior.contains(behavior)) {
				throw new IllegalArgumentException(String.format("An IntermediateCatchEvent cannot have the type %s", behavior.toString()));
			}
			if (event instanceof BoundaryEvent && !allowedBoundaryEventBehavior.contains(behavior)) {
				throw new IllegalArgumentException(String.format("A BoundaryEvent cannot have the type %s", behavior.toString()));
			}
			if (event instanceof IntermediateThrowEvent && !allowedThrowEventBehavior.contains(behavior)) {
				throw new IllegalArgumentException(String.format("An IntermediateCatchEvent cannot have the type %s", behavior.toString()));
			}
		}
	}

	/**
	 * A fragment should always have at least 3 data control nodes to prevent a
	 * concurrency overflow. The exception is when the start event is of type
	 * message receive event.
	 */
	private void validateDataControlNodeAmount() {
		int dataControlNodeAmount = fragment.getBpmnFragment().getDataControlNodes().size();
		if (!fragment.getBpmnFragment().getStartEvent().getSpecialBehavior().equals(SpecialBehavior.MESSAGE_RECEIVE) && dataControlNodeAmount <= 2) {
			throw new IllegalArgumentException("The fragment should contain at least 3 data control nodes");
		}
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}
}
