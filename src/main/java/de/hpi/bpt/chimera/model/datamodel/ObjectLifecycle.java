package de.hpi.bpt.chimera.model.datamodel;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ObjectLifecycle {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;

	@OneToMany(cascade = CascadeType.ALL)
	List<ObjectLifecycleState> objectLifecycleStates;

	public void setObjectLifecycleStates(List<ObjectLifecycleState> objectLifecycleStates) {
		this.objectLifecycleStates = objectLifecycleStates;
	}

	public List<ObjectLifecycleState> getObjectLifecycleStates() {
		return this.objectLifecycleStates;
	}

	public ObjectLifecycleState getInitialState() {
		List<ObjectLifecycleState> initialStates = objectLifecycleStates.stream()
				.filter(olcState -> olcState.getPredecessors().isEmpty()).collect(Collectors.toList());
		if (initialStates.size() != 1) {
			throw new IllegalStateException("There has to be exactly one initial state");
		}
		return initialStates.iterator().next();
	}
}
