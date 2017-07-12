package de.hpi.bpt.chimera.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;

public class Case {
	private String id;
	private String name;
	private List<FragmentInstance> fragmentInstances;
	// List<TerminationCondition>

	public Case(String caseName, CaseModel caseModel) {
		this.id = UUID.randomUUID().toString();
		this.name = caseName;
		instantiate(caseModel);

		// something to start automatic running control node instances e.g.
		// e-mail task, but this function is not implemented in old version
	}

	private void instantiate(CaseModel caseModel) {
		fragmentInstances = new ArrayList<>();
		for (Fragment fragment : caseModel.getFragments()) {
			FragmentInstance fragmentInstance = new FragmentInstance(fragment);
			fragmentInstances.add(fragmentInstance);
		}
	}

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

	public List<FragmentInstance> getFragmentInstances() {
		return fragmentInstances;
	}

	public void setFragmentInstances(List<FragmentInstance> fragmentInstances) {
		this.fragmentInstances = fragmentInstances;
	}

}
