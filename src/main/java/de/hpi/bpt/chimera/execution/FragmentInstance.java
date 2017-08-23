package de.hpi.bpt.chimera.execution;

import java.util.List;

import de.hpi.bpt.chimera.model.fragment.Fragment;

public class FragmentInstance {
	private Fragment fragment;
	private List<ControlNodeInstance> controlNodeInstances;

	// TODO: re-implement the other functions which are given in
	// ...core.FragmentInstance, like all the initialize functions

	public FragmentInstance(Fragment fragment) {
		this.fragment = fragment;
		// TODO: implement the whole thing with the controlNodeInstances ...
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public List<ControlNodeInstance> getControlNodeInstances() {
		return controlNodeInstances;
	}

	public void setControlNodeInstances(List<ControlNodeInstance> controlNodeInstances) {
		this.controlNodeInstances = controlNodeInstances;
	}
}
