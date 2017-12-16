package de.hpi.bpt.chimera.model.fragment.bpmn;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.DataAttributeJsonPath;

/**
 * A class that extends the AtomicDataStateCondition with a mapping from
 * DataAttribute to JsonPath. This class refers to the steady DataNodes in the
 * Fragment which represent and fixed AtomicDataStateCondition. It inherits from
 * AtomicDataStateCondition so it can be easily compared with the
 * AtomicDataStateConditions that are part of running DataObjects.
 *
 */
@Entity
public class DataNode extends AtomicDataStateCondition {

	@OneToMany(cascade = CascadeType.ALL)
	private List<DataAttributeJsonPath> dataAttributeJsonPaths;

	public DataNode() {
		super();
	}

	public List<DataAttributeJsonPath> getDataAttributeJsonPaths() {
		return dataAttributeJsonPaths;
	}

	public void setDataAttributeJsonPaths(List<DataAttributeJsonPath> dataAttributeJsonPaths) {
		this.dataAttributeJsonPaths = dataAttributeJsonPaths;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof AtomicDataStateCondition) {
			AtomicDataStateCondition condition = (AtomicDataStateCondition) o;

			return dataClass.equals(condition.getDataClass()) && state.equals(condition.getObjectLifecycleState());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataClass, state);
	}
}
