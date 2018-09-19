package de.hpi.bpt.chimera.model.fragment;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.exception.IllegalFragmentPolicyNameException;


public enum FragmentInstantiationPolicy {
	SEQUENTIAL("sequential"), CONCURRENT("concurrent");
	private static Logger log = Logger.getLogger(FragmentInstantiationPolicy.class);

	private String text;

	FragmentInstantiationPolicy() {
		this.text = "";
	}

	FragmentInstantiationPolicy(String text) {
		this.text = text;
	}


	public String getText() {
		return text;
	}

	/**
	 * Receive a FragmentInstantiationPolicy by a given text.
	 * 
	 * @param text
	 *            - of the policy
	 * @return the corresponding FragmentInstantiationPolicy
	 * @throws IllegalPolicyNameException
	 *             if the text does not match a FragmentInstantiationPolicy.
	 */
	public static FragmentInstantiationPolicy fromText(String text) {
		if (text.isEmpty())
			return null;
		for (FragmentInstantiationPolicy policy : FragmentInstantiationPolicy.values()) {
			if (policy.text.equalsIgnoreCase(text)) {
				return policy;
			}
		}
		IllegalFragmentPolicyNameException e = new IllegalFragmentPolicyNameException(text);
		log.error(e.getMessage());
		throw e;
	}
}
