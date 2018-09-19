package de.hpi.bpt.chimera.model.fragment;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.condition.FragmentPreCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;

@Entity
public class Fragment {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int dbId;

	private String id;
	private String name;
	private int versionNumber;
	@Lob
	private String contentXML;

	@OneToOne(cascade = CascadeType.ALL)
	private BpmnFragment bpmnFragment;
	@OneToOne(cascade = CascadeType.ALL)
	private FragmentPreCondition fragmentPreCondition;

	@Enumerated(EnumType.STRING)
	private FragmentInstantiationPolicy policy;
	private boolean hasBound;
	private int instantiationLimit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getContentXML() {
		return contentXML;
	}
	public void setContentXML(String contentXML) {
		this.contentXML = contentXML;
	}

	public BpmnFragment getBpmnFragment() {
		return bpmnFragment;
	}

	public void setBpmnFragment(BpmnFragment bpmnFragment) {
		this.bpmnFragment = bpmnFragment;
	}

	public FragmentPreCondition getFragmentPreCondition() {
		return fragmentPreCondition;
	}

	public void setFragmentPreCondition(FragmentPreCondition fragmentPreCondition) {
		this.fragmentPreCondition = fragmentPreCondition;
	}

	public FragmentInstantiationPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(FragmentInstantiationPolicy policy) {
		this.policy = policy;
	}

	public boolean getHasBound() {
		return hasBound;
	}

	public void setHasBound(boolean hasBound) {
		this.hasBound = hasBound;
	}

	public int getInstantiationLimit() {
		return instantiationLimit;
	}

	public void setInstantiationLimit(int instantiationLimit) {
		this.instantiationLimit = instantiationLimit;
	}
}
