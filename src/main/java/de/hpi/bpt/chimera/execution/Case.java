package de.hpi.bpt.chimera.execution;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;

@Entity(name = "ChimeraCase")
public class Case {
	@Id
	private String id;
	private Date instantiation;
	private String name;
	@OneToOne(cascade = CascadeType.ALL)
	private CaseExecutioner caseExecutioner;
	@OneToMany(cascade = CascadeType.ALL)
	private Map<String, FragmentInstance> fragmentInstances;


	/**
	 * for JPA only
	 */
	public Case() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public Case(String caseName, CaseModel caseModel, CaseExecutioner caseExecutioner) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		Date date = new Date();
		this.instantiation = new java.sql.Timestamp(date.getTime());
		this.name = caseName;
		this.caseExecutioner = caseExecutioner;
		this.fragmentInstances = new HashMap<>();
		instantiate(caseModel);

		// something to start automatic running control node instances e.g.
		// e-mail task, but this function is not implemented in old version
	}

	/**
	 * Create Instances of all Fragments of a specific CaseModel.
	 * 
	 * @param caseModel
	 */
	private void instantiate(CaseModel caseModel) {
		for (Fragment fragment : caseModel.getFragments()) {
			FragmentInstance fragmentInstance = new FragmentInstance(fragment, this);
			fragmentInstances.put(fragmentInstance.getId(), fragmentInstance);
		}
	}

	// GETTER & SETTER

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, FragmentInstance> getFragmentInstances() {
		return fragmentInstances;
	}

	public void setFragmentInstances(Map<String, FragmentInstance> fragmentInstances) {
		this.fragmentInstances = fragmentInstances;
	}

	public CaseExecutioner getCaseExecutioner() {
		return caseExecutioner;
	}

	public void setCaseExecutioner(CaseExecutioner caseExecutioner) {
		this.caseExecutioner = caseExecutioner;
	}


	public Date getInstantiation() {
		return instantiation;
	}


	public void setInstantiation(Date instantiation) {
		this.instantiation = instantiation;
	}
}
