package de.hpi.bpt.chimera.model.condition;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class CaseStartTriggerConsequence {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToOne(cascade = CascadeType.PERSIST)
	private DataObjectStateCondition dataObjectState;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<DataAttributeJsonPath> mapping;

	// no-argument constructor is needed by EclipseLink to persist and restore
	// objects.
	CaseStartTriggerConsequence() {
	}

	public CaseStartTriggerConsequence(DataObjectStateCondition dataObjectStateCondition, List<DataAttributeJsonPath> dataAttributeJsonPath) {
		this.dataObjectState = dataObjectStateCondition;
		this.mapping = dataAttributeJsonPath;
	}

	public DataObjectStateCondition getDataObjectState() {
		return dataObjectState;
	}

	public void setDataObjectState(DataObjectStateCondition dataObjectState) {
		this.dataObjectState = dataObjectState;
	}

	public List<DataAttributeJsonPath> getMapping() {
		return mapping;
	}

	public void setMapping(List<DataAttributeJsonPath> mapping) {
		this.mapping = mapping;
	}

	public void addMapping(List<DataAttributeJsonPath> mapping) {
		this.mapping.addAll(mapping);
	}
}
