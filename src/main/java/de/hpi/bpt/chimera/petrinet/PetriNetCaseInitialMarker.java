package de.hpi.bpt.chimera.petrinet;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.petrinet.CaseModelTranslation;

public class PetriNetCaseInitialMarker extends AbstractPetriNetMarker {

	private final CaseModelTranslation caseModelTranslation;

	public PetriNetCaseInitialMarker(CaseModelTranslation caseModelTranslation) {
		this.caseModelTranslation = caseModelTranslation;
	}

	public void addInitialMarking() {
		// Initial place
		caseModelTranslation.getInitialPlace().setNumTokens(1);

		// Initialize case class with one token
		DataClass caseClass = caseModelTranslation.getCaseModel().getDataModel().getCaseClass();
		String initialStateName = caseClass.getObjectLifecycle().getInitialState().getName();
		caseModelTranslation.getDataClassTranslationsByName().get(caseClass.getName()).getOlcStatePlacesByName()
				.get(initialStateName).setNumTokens(1);

		// TODO fragment re-initialization pools?
	}

}
