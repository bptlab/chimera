package de.hpi.bpt.chimera.model.fragment;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;

@Entity
public class Fragment {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;

	private String id;
	private String name;
	private int versionNumber;
	@Lob
	private String contentXML;

	@OneToOne(cascade = CascadeType.PERSIST)
	private BpmnFragment bpmnFragment;

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
}
