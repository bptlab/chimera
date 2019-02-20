package de.hpi.bpt.chimera.petrinet;

import java.util.Collections;

import de.hpi.bpt.chimera.model.petrinet.PetriNet;

public abstract class AbstractPetriNetWriter {

	public abstract String write(PetriNet petriNet);

	protected String indent(int indentationLevel) {
		return String.join("", Collections.nCopies(indentationLevel, "  "));
	}
}
