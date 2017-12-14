package de.hpi.bpt.chimera.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.configuration.EmailConfiguration;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

@Entity
@NamedQuery(name = "CaseModel.getAll", query = "SELECT c FROM CaseModel c")
public class CaseModel {
	public static final Logger log = Logger.getLogger(CaseModel.class);
	@Id
	private String cmId;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dbId;
	private String name;
	private int versionNumber;
	@OneToOne(cascade = CascadeType.ALL)
	private DataModel dataModel;
	@OneToMany(cascade = CascadeType.ALL)
	private List<CaseStartTrigger> startCaseTrigger;
	@OneToOne(cascade = CascadeType.ALL)
	private TerminationCondition terminationCondition;
	@OneToMany(cascade = CascadeType.ALL)
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

	/**
	 * Return the activity with the given Id. Throws an exception when there is
	 * no or there are more than one activities with that Id.
	 * 
	 * @param controlNodeId
	 *            the Id to identify the Activity
	 * @return the found activity
	 */
	public AbstractActivity getActivityById(String controlNodeId){
		// creates a list of all activities of all fragments and filters these
		// list for the given Id.
		// all activities that have the given Id are stored in result
		List<AbstractActivity> result = this.getFragments().stream().map(fragment -> fragment.getBpmnFragment().getTasks()).flatMap(List::stream).filter(activity -> activity.getId().equals(controlNodeId)).collect(Collectors.toList());
		// there should exact 1 activity with the given Id
		if (result.size() != 1) {
			throw new IllegalStateException();
		}
		// return the found activity
		return result.get(0);
	}

	// GETTER & SETTER
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

	public List<String> getContentXmlStrings() {
		List<String> contentXmlStrings = new ArrayList<>();
		for (Fragment fragment : fragments) {
			contentXmlStrings.add(fragment.getContentXML());
		}
		return contentXmlStrings;
	}
}
