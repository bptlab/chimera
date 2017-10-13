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
public class DataStateCondition implements ConditionStatable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToOne(cascade = CascadeType.ALL)
	private DataClass dataClass;
	@OneToOne(cascade = CascadeType.ALL)
	private ObjectLifecycleState state;

	// TODO: add Feature: JsonPath

	public DataStateCondition() {
		this.dataClass = null;
		this.state = null;
	}

	public DataStateCondition(DataClass dataClass, ObjectLifecycleState state) {
		this.dataClass = dataClass;
		this.state = state;
	}

	/**
	 * Copies the condition by referring to the same DataClass and
	 * ObjectLifecycleState.
	 * 
	 * @param condition
	 */
	public DataStateCondition(DataStateCondition condition) {
		this.dataClass = condition.getDataClass();
		this.state = condition.getState();
	}

	@Override
	public DataStateCondition getCondition() {
		return this;
	}

	public DataClass getDataClass() {
		return dataClass;
	}

	public void setDataClass(DataClass dataClass) {
		this.dataClass = dataClass;
	}

	public ObjectLifecycleState getState() {
		return state;
	}

	public void setState(ObjectLifecycleState state) {
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
		if (o instanceof ConditionStatable) {
			DataStateCondition condition = ((ConditionStatable) o).getCondition();

			// return
			// this.getDataClassName().equals(condition.getDataClassName()) &&
			// this.getStateName().equals(condition.getStateName());
			return this.dataClass.equals(condition.getDataClass()) && this.state.equals(condition.getState());
		}
		return false;
	}

	// TODO: think about hashing

	@Override
	public int hashCode() {
		return (int) (((long) this.dataClass.hashCode() + (long) this.state.hashCode()) / 2);
	}

}
