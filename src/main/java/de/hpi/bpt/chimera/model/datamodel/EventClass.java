package de.hpi.bpt.chimera.model.datamodel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EventClass extends DataModelClass {
	@Id
	@GeneratedValue
	private int dbId;

}
