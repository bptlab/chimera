package de.hpi.bpt.chimera.model.condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

@Entity
public class CaseStartTriggerConsequence {
	@Id
	@GeneratedValue
	private int dbId;
	@OneToOne(cascade = CascadeType.ALL)
	private AtomicDataStateCondition dataObjectState;
	@OneToMany(cascade = CascadeType.ALL)
	private List<DataAttributeJsonPath> mapping;

	@Transient
	private Map<DataAttribute, String> dataAttributeToJsonPath;
	// no-argument constructor is needed by EclipseLink to persist and restore
	// objects.
	CaseStartTriggerConsequence() {
	}

	public CaseStartTriggerConsequence(AtomicDataStateCondition dataObjectStateCondition, List<DataAttributeJsonPath> mapping) {
		this.dataObjectState = dataObjectStateCondition;
		this.mapping = mapping;
	}

	public AtomicDataStateCondition getDataObjectState() {
		return dataObjectState;
	}

	public void setDataObjectState(AtomicDataStateCondition dataObjectState) {
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

	public Map<DataAttribute, String> getDataAttributeToJsonPath() {
		if (dataAttributeToJsonPath == null) {
			dataAttributeToJsonPath = new HashMap<>();
			for (DataAttributeJsonPath dataAttributeJsonPath : mapping) {
				dataAttributeToJsonPath.put(dataAttributeJsonPath.getDataAttribute(), dataAttributeJsonPath.getJsonPath());
			}
		}
		return dataAttributeToJsonPath;
	}
}
