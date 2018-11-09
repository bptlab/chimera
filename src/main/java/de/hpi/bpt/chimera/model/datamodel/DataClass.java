package de.hpi.bpt.chimera.model.datamodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.Listable;
import de.hpi.bpt.chimera.model.Nameable;

@Entity
public class DataClass implements Nameable, Listable {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int dbId;
	
	private String name;

	// Only when the DataClass is EventType
	private boolean isEventType;
	private String timestampName;

	@OneToMany(cascade = CascadeType.ALL)
	private List<DataAttribute> dataAttributes;


	public Map<String, DataAttribute> getNameToDataAttribute() {
		Map<String, DataAttribute> nameToDataAttribute = new HashMap<>();

		for (DataAttribute dataAttribute : this.dataAttributes) {
			nameToDataAttribute.put(dataAttribute.getName(), dataAttribute);
		}
		return nameToDataAttribute;
	}

	@OneToOne(cascade = CascadeType.ALL)
	private ObjectLifecycle objectLifecycle;

	public void setObjectLifecycle(ObjectLifecycle objectLifecycle) {
		this.objectLifecycle = objectLifecycle;
	}

	public ObjectLifecycle getObjectLifecycle() {
		return this.objectLifecycle;
	}

	/**
	 * Make a Map from name of ObjectLifecycle-State to the referring
	 * ObjectLifecycle-State.
	 * 
	 * @return HashMap
	 */
	public Map<String, ObjectLifecycleState> getNameToObjectLifecycleState() {
		Map<String, ObjectLifecycleState> nameToObjectLifecycleState = new HashMap<>();

		for (ObjectLifecycleState objectLifecycleState : this.objectLifecycle.getObjectLifecycleStates()) {
			nameToObjectLifecycleState.put(objectLifecycleState.getName(), objectLifecycleState);
		}
		return nameToObjectLifecycleState;
	}

	public Optional<ObjectLifecycleState> getObjectLifecycleStateByName(String stateName) {
		return getObjectLifecycle().getObjectLifecycleStates().stream()
				.filter(s -> s.getName().equals(stateName))
				.findFirst();
	}

	public Optional<DataAttribute> getDataAttributeByName(String dataAttributeName) {
		return getDataAttributes().stream()
				.filter(d -> d.getName().equals(dataAttributeName))
				.findFirst();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public List<DataAttribute> getDataAttributes() {
		return dataAttributes;
	}

	public void setDataAttributes(List<DataAttribute> dataAttributes) {
		this.dataAttributes = dataAttributes;
	}

	public boolean isEvent() {
		return isEventType;
	}

	public void setEvent(boolean isEvent) {
		this.isEventType = isEvent;
	}

	public String getTimestampName() {
		return timestampName;
	}

	public void setTimestampName(String timestampName) {
		this.timestampName = timestampName;
	}
}
