package de.hpi.bpt.chimera.validation;

import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.SpecialBehavior;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

public class FragmentValidator {

	private Fragment fragment;

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
			validateStartEvent();
			validateTimerEventPosition();
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

	private void validateStartEvent() {
		if (fragment.getBpmnFragment().getStartEvent() == null) {
			throw new IllegalArgumentException("No StartEvent");
		}
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}
}
