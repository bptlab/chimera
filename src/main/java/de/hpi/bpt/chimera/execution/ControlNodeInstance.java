package de.hpi.bpt.chimera.execution;

import java.util.UUID;

import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;

public abstract class ControlNodeInstance implements Behaving {
	// TODO: think about how to implement the Id
	private String id;
	private AbstractControlNode controlNode;
	private FragmentInstance fragmentInstance;
	private State state;

	public ControlNodeInstance(AbstractControlNode controlNode, FragmentInstance fragmentInstance) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.controlNode = controlNode;
		this.fragmentInstance = fragmentInstance;
	}

	public String getId() {
		return id;
	}

	public AbstractControlNode getControlNode() {
		return controlNode;
	}

	public FragmentInstance getFragmentInstance() {
		return fragmentInstance;
	}

	public CaseExecutioner getCaseExecutioner() {
		return fragmentInstance.getCase().getCaseExecutioner();
	}

	public DataManager getDataManager() {
		return getCaseExecutioner().getDataManager();
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}
}
