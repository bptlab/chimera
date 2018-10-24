package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.Nameable;

public class Place implements Nameable {
	private String name;
	private int numTokens = 0;

	public Place() {

	}

	public Place(String name) {
		setName(name);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getNumTokens() {
		return numTokens;
	}

	public void setNumTokens(int numTokens) {
		this.numTokens = numTokens;
	}

}
