package de.hpi.bpt.chimera.execution.controlnodes;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;

@Entity
public abstract class ControlNodeInstance implements Behaving {
	// TODO: think about how to implement the Id
	@Id
	private String id = "";
	@OneToOne // (cascade = CascadeType.ALL)
	private AbstractControlNode controlNode;
	@OneToOne(cascade = CascadeType.ALL)
	private FragmentInstance fragmentInstance;
	private State state;


	/**
	 * for JPA only
	 */
	public ControlNodeInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public ControlNodeInstance(AbstractControlNode controlNode, FragmentInstance fragmentInstance) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.controlNode = controlNode;
		this.fragmentInstance = fragmentInstance;
		setState(State.INIT);
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

	public void skip() {
		this.setState(State.SKIPPED);
	}
}
