package de.hpi.bpt.chimera.execution;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.Fragment;

@Entity(name = "ChimeraCase")
public class Case {
	@Id
	private String id;
	private Date instantiation;
	private String name;
	@OneToOne(cascade = CascadeType.ALL)
	private CaseExecutioner caseExecutioner;
	@OneToMany(cascade = CascadeType.ALL)
	private Map<String, FragmentInstance> fragmentInstances;

	private static Logger log = Logger.getLogger(Case.class);
	private DataClass caseClass;

	/**
	 * for JPA only
	 */
	public Case() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}

	public Case(String caseName, CaseModel caseModel, CaseExecutioner caseExecutioner) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		Date date = new Date();
		this.instantiation = new java.sql.Timestamp(date.getTime());
		this.name = caseName;
		this.caseExecutioner = caseExecutioner;
		this.fragmentInstances = new HashMap<>();
		instantiate(caseModel);
	}

	/**
	 * Create Instances of all Fragments of a specific CaseModel.
	 * 
	 * @param caseModel
	 */
	private void instantiate(CaseModel caseModel) {
		for (Fragment fragment : caseModel.getFragments()) {
			instantiateFragment(fragment);
		}

		// Instantiate case class
		caseClass = caseModel.getDataModel().getCaseClass();
		if (caseClass == null) {
			throw new RuntimeException("There is not case class to instantiate.");
		}
		ObjectLifecycleState initialOlcState = caseClass.getObjectLifecycle().getInitialState();
		if (initialOlcState == null) {
			throw new RuntimeException("There is no initial state for the case class.");
		}
		caseExecutioner.getDataManager().createDataObject(new AtomicDataStateCondition(caseClass, initialOlcState));
	}

	/**
	 * Create an instance for a specific fragment.
	 * 
	 * @param fragment - that will be instantiated.
	 * @return the newly created fragment instance if there the instantiation was
	 *         allowed otherwise {@code null}.
	 */
	public synchronized FragmentInstance instantiateFragment(Fragment fragment) {
		if (isInstantiable(fragment)) {
			FragmentInstance fragmentInstance = new FragmentInstance(fragment, this);
			fragmentInstances.put(fragmentInstance.getId(), fragmentInstance);
			return fragmentInstance;
		} else {
			log.warn(
					"No instances of fragment %s because the maximum amount of concurrent running instances of this fragment has been reached.");
		}
		return null;
	}

	/**
	 * Instantiate a specific fragment and enable it if the instantiation was
	 * successful.
	 * 
	 * @param fragment - that will be instantiated.
	 * @see #instantiateFragment(Fragment) instantiateFragment
	 */
	public synchronized void instantiateFragmentAndEnableInstance(Fragment fragment) {
		FragmentInstance fragmentInstance = instantiateFragment(fragment);
		if (fragmentInstance != null) {
			fragmentInstance.enable();
		}
	}

	/**
	 * Check whether a new instantiation of a fragment can be created. Therefore it
	 * must be defined that the fragment has no bound or there are exists less
	 * instantiation than the defined limit.
	 * 
	 * @param fragment - to check
	 * @return true if another instantiation is possible.
	 * @see {@link Fragment}
	 */
	private boolean isInstantiable(Fragment fragment) {
		if (!fragment.getHasBound()) {
			return true;
		}
		long existingInstancesAmount = getFragmentInstances().values().stream()
				.filter(f -> f.getFragment().equals(fragment)).count();
		return existingInstancesAmount < fragment.getInstantiationLimit();
	}

	// TODO: also delete the instances but keep track of the instantiations of
	// one fragment. Then the Fragment State 'Terminated' is not needed anymore.
	/**
	 * Terminate a specific fragment instance.
	 * 
	 * @param fragmentInstance
	 */
	public void terminateFragmentInstance(FragmentInstance fragmentInstance) {
		String fragmentInstanceId = fragmentInstance.getId();
		if (fragmentInstances.containsKey(fragmentInstanceId)) {
			fragmentInstance.getControlNodeInstances().stream().filter(c -> !c.getState().equals(State.TERMINATED))
					.forEach(ControlNodeInstance::skip);
			fragmentInstance.getControlNodeIdToInstance().clear();
			fragmentInstance.getControlNodeInstanceIdToInstance().clear();
			fragmentInstance.setState(FragmentState.TERMINATED);
		}
	}

	// GETTER & SETTER

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

	public Map<String, FragmentInstance> getFragmentInstances() {
		return fragmentInstances;
	}

	public void setFragmentInstances(Map<String, FragmentInstance> fragmentInstances) {
		this.fragmentInstances = fragmentInstances;
	}

	public CaseExecutioner getCaseExecutioner() {
		return caseExecutioner;
	}

	public void setCaseExecutioner(CaseExecutioner caseExecutioner) {
		this.caseExecutioner = caseExecutioner;
	}

	public Date getInstantiation() {
		return instantiation;
	}

	public void setInstantiation(Date instantiation) {
		this.instantiation = instantiation;
	}
}
