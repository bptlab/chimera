package de.hpi.bpt.chimera.execution.controlnodes.gateway;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ControlflowBranch {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;

	@ElementCollection
	public List<String> branch = new ArrayList<>();
}
