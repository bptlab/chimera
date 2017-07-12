package de.hpi.bpt.chimera.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

@Entity
public class CaseModel {
	@Id
	private String cmId;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dbId;
	private String name;
	private int versionNumber;
	@OneToOne(cascade = CascadeType.PERSIST)
	private DataModel dataModel;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<CaseStartTrigger> startCaseTrigger;
	@OneToOne(cascade = CascadeType.PERSIST)
	private TerminationCondition terminationCondition;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Fragment> fragments;


	/**
	 * Persists a the CaseModel to the database using the Java Persistence API
	 * "EclipseLink".
	 */
	public void saveCaseModelToDB() {
		EntityManager entityManager = DomainModelPersistenceManager.getEntityManagerFactory().createEntityManager();

		entityManager.getTransaction().begin();
		entityManager.merge(this);
		entityManager.getTransaction().commit();
	}

	public String getId() {
		return cmId;
	}

	public void setId(String cmId) {
		this.cmId = cmId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public List<CaseStartTrigger> getStartCaseTrigger() {
		return startCaseTrigger;
	}

	public void setStartCaseTrigger(List<CaseStartTrigger> startCaseTrigger) {
		this.startCaseTrigger = startCaseTrigger;
	}

	public TerminationCondition getTerminationCondition() {
		return terminationCondition;
	}

	public void setTerminationCondition(TerminationCondition terminationCondition) {
		this.terminationCondition = terminationCondition;
	}

	public List<Fragment> getFragments() {
		return fragments;
	}

	public void setFragments(List<Fragment> fragments) {
		this.fragments = fragments;
	}
}
