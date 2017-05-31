package de.hpi.bpt.chimera.model.condition;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public class CaseStartTriggerConsequence {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToOne(cascade = CascadeType.PERSIST)
	private DataObjectStateCondition dataObjectState;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<DataAttributeJsonPath> mapping;

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
}
