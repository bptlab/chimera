package de.hpi.bpt.chimera.rest.beans.casemodel;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.petrinet.CaseModelTranslation;
import de.hpi.bpt.chimera.model.petrinet.PetriNet;

@XmlRootElement
public class CaseModelPetriNetRepresentationJaxBean {
	private String id;
	private String name;
	private PetriNet petriNet;

	public CaseModelPetriNetRepresentationJaxBean(CaseModel cm) {
		setId(cm.getId());
		setName(cm.getName());

		final CaseModelTranslation caseModelTranslation = new CaseModelTranslation(cm);

		setPetriNet(caseModelTranslation.getPetriNet());
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

	public PetriNet getPetriNet() {
		return petriNet;
	}

	public void setPetriNet(PetriNet petriNet) {
		this.petriNet = petriNet;
	}
}
