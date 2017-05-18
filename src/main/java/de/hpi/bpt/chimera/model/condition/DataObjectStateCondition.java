package de.hpi.bpt.chimera.model.condition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

@Entity
public class DataObjectStateCondition {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dbId;
	@OneToOne(cascade = CascadeType.PERSIST)
	private DataClass dataClass;
	@OneToOne(cascade = CascadeType.PERSIST)
	private ObjectLifecycleState state;

	public DataClass getDataClass() {
		return dataClass;
	}

	public void setDataClass(DataClass dataModelClass) {
		this.dataClass = dataModelClass;
	}

	public ObjectLifecycleState getState() {
		return state;
	}

	public void setState(ObjectLifecycleState state) {
		this.state = state;
	}


}
