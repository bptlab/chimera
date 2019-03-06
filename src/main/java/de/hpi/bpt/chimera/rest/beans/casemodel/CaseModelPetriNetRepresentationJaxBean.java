package de.hpi.bpt.chimera.rest.beans.casemodel;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.execution.Case;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.petrinet.CaseModelTranslation;
import de.hpi.bpt.chimera.model.petrinet.PetriNet;
import de.hpi.bpt.chimera.petrinet.AbstractPetriNetWriter;
import de.hpi.bpt.chimera.petrinet.PetriNetCaseInitialMarker;
import de.hpi.bpt.chimera.petrinet.PetriNetCaseInstanceMarker;
import de.hpi.bpt.chimera.petrinet.PetriNetDotWriter;
import de.hpi.bpt.chimera.petrinet.PetriNetLolaWriter;

@XmlRootElement
public class CaseModelPetriNetRepresentationJaxBean {
	private final CaseModel caseModel;
	private final CaseModelTranslation caseModelTranslation;
	private final String id;
	private final String name;
	private final PetriNet petriNet;

	public CaseModelPetriNetRepresentationJaxBean(CaseModel cm) {
		this.caseModel = cm;
		this.id = cm.getId();
		this.name = cm.getName();

		this.caseModelTranslation = new CaseModelTranslation(cm);
		this.petriNet = caseModelTranslation.getPetriNet();
	}

	public void addMarkingForInitialState() {
		PetriNetCaseInitialMarker initialMarker = new PetriNetCaseInitialMarker(caseModelTranslation);
		initialMarker.addInitialMarking();
	}

	public void addMarkingForInstance(Case caseInstance) {
		PetriNetCaseInstanceMarker instanceMarker = new PetriNetCaseInstanceMarker(caseModelTranslation, caseInstance);
		instanceMarker.addMarkingForInstance();
	}

	public void addFragmentToPetriNet(Fragment newFragment) {
		if (caseModelTranslation.getFragmentTranslationsByName().containsKey(newFragment.getName())) {
			throw new IllegalArgumentException("Case model already contains a fragment named " + newFragment.getName());
		}
		// Translate and add fragment
		caseModelTranslation.translateFragment(newFragment);

		// // Add initial marking to allow fragment instantiation
		caseModelTranslation.getFragmentTranslationsByName().get(newFragment.getName()).getInitialPlace().addTokens(1);
	}

	public String getDotOutput() {
		AbstractPetriNetWriter dotWriter = new PetriNetDotWriter();
		return dotWriter.write(petriNet);
	}

	public String getLolaOutput() {
		AbstractPetriNetWriter lolaWriter = new PetriNetLolaWriter();
		return lolaWriter.write(petriNet);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public PetriNet getPetriNet() {
		return petriNet;
	}

	public CaseModel getCaseModel() {
		return caseModel;
	}
}
