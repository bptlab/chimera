package de.hpi.bpt.chimera.model.condition;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

@Entity
public class AtomicDataStateCondition {
	@Id
	@GeneratedValue
	private int dbId;
	@OneToOne(cascade = CascadeType.ALL)
	protected DataClass dataClass;
	@OneToOne(cascade = CascadeType.ALL)
	protected ObjectLifecycleState state;

	public AtomicDataStateCondition() {
		this.dataClass = null;
		this.state = null;
	}

	public AtomicDataStateCondition(DataClass dataClass, ObjectLifecycleState state) {
		this.dataClass = dataClass;
		this.state = state;
	}

	/**
	 * Copies the condition by referring to the same DataClass and
	 * ObjectLifecycleState.
	 * 
	 * @param condition
	 */
	public AtomicDataStateCondition(AtomicDataStateCondition condition) {
		this.dataClass = condition.getDataClass();
		this.state = condition.getObjectLifecycleState();
	}

	public DataClass getDataClass() {
		return dataClass;
	}

	public void setDataClass(DataClass dataClass) {
		this.dataClass = dataClass;
	}

	public ObjectLifecycleState getObjectLifecycleState() {
		return state;
	}

	public void setObjectLifecycleState(ObjectLifecycleState state) {
		this.state = state;
	}

	public String getDataClassName() {
		return dataClass.getName();
	}

	public String getStateName() {
		return state.getName();
	}

	/**
	 * Compares the DataStateConditions by (the names of) Dataclass and
	 * ObjectLifecycleState.
	 * 
	 * @param condition
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof AtomicDataStateCondition) {
			AtomicDataStateCondition condition = (AtomicDataStateCondition) o;

			// return
			// this.getDataClassName().equals(condition.getDataClassName()) &&
			// this.getStateName().equals(condition.getStateName());
			return dataClass.equals(condition.getDataClass()) && state.equals(condition.getObjectLifecycleState());
		}
		return false;
	}

	// TODO: think about hashing

	@Override
	public int hashCode() {
		return Objects.hash(dataClass, state);
	}

}
