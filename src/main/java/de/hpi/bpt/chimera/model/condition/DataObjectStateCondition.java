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

	public DataObjectStateCondition() {
		this.dataClass = null;
		this.state = null;
	}

	public DataObjectStateCondition(DataClass dataClass, ObjectLifecycleState state) {
		this.dataClass = dataClass;
		this.state = state;
	}

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

	@Override
	public boolean equals(Object o) {
		if (o instanceof DataObjectStateCondition) {
			DataObjectStateCondition toCompare = (DataObjectStateCondition) o;
			return this.dataClass.equals(toCompare.getDataClass()) && this.state.equals(toCompare.getState());
		}
		return false;
	}

	// TODO: improve the hashing
	@Override
	public int hashCode() {
		return (int) (((long) this.dataClass.hashCode() + (long) this.state.hashCode()) / 2);
	}
}
