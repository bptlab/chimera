package de.hpi.bpt.chimera.persistencemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.Case;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.model.CaseModel;

public class DomainModelPersistenceManager {
	// TODO in persistence.xml change <property
	// name="eclipselink.ddl-generation" value="drop-and-create-tables" />
	// to <property name="eclipselink.ddl-generation" value="create-tables" />
	// Otherwise the dababase will be reset every time.
	private static final Logger log = Logger.getLogger(DomainModelPersistenceManager.class);

	private static final int PERSISTENCE_INTERVAL = 10000;

	private static final String PERSISTENCE_UNIT_NAME = "CaseModel";
	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager = null;
	private static boolean isEntityManagerFactoryInitialized = false;

	private static Timer timer;

	private DomainModelPersistenceManager() {
	}

	/**
	 * Returns the EntityManagerFactory. If the EntityManagerFactory isn't
	 * initilized, then first initialize it.
	 * 
	 * @return EntityManagerFactory
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		if (!isEntityManagerFactoryInitialized) {
			entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			isEntityManagerFactoryInitialized = true;
		}
		return entityManagerFactory;
	}

	/**
	 * Returns the EntityManager. If the EntityManager isn't initilized, then
	 * first initialize it.
	 * 
	 * @return EntityManagerFactory
	 */
	public static EntityManager getEntityManager() {
		if (entityManager == null || !entityManager.isOpen()) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			entityManager = emf.createEntityManager();
		}
		return entityManager;
	}


	/**
	 * Closes the EntityManager used for persistence. Usually invoked when
	 * application context is destroyed (e.g. server shut down).
	 * 
	 */
	public static void closeEntityManager() {
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
	}

	/**
	 * Persists a given CaseModel to the database using the Java Persistence API
	 * "EclipseLink".
	 * 
	 * @deprecated use {@link de.hpi.bpt.chimera.model.CaseModel#saveCaseModelToDB()
	 *             CaseModel.saveCaseModel()} instead.
	 * 
	 * @param caseModel
	 *            the CaseModel that should be persisted.
	 */
	@Deprecated
	public static void saveCaseModel(CaseModel caseModel) {
		EntityManager em = getEntityManager();

		em.getTransaction().begin();
		CaseModel alreadyExistingCaseModel = em.find(CaseModel.class, caseModel.getId());
		if (alreadyExistingCaseModel != null) {
			em.remove(alreadyExistingCaseModel);
		}
		em.flush();
		em.persist(caseModel);
		em.getTransaction().commit();
	}

	// ToDo make a sober implementation of saving cases. Maybe with an own class
	// and PersistenceUnit
	/**
	 * 
	 * Only for quick and dirty testing.
	 */
	@Deprecated
	public static void saveCase(Case caze) {
		EntityManager em = getEntityManager();

		try {
			em.getTransaction().begin();
			em.persist(caze);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Case persistence Exception", e);
		}


	}

	public static Case loadCase(String caseId) {
		Case caze = null;
		try{
			EntityManager em = getEntityManager();
			caze = em.find(Case.class, caseId);
		}
		catch(Exception e){
			log.error("Case load Exception", e);
		}
		return caze;
	}

	public static void deleteCase(Case caze) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			Case caseToRemove = em.find(Case.class, caze.getId());
			em.remove(caseToRemove);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Can't delete Case. Maybe it's not stored in the database or an other error occured (see error message).", e);
		}
	}

	/**
	 * Returns a list of all running Cases of a CaseModel (specified by Id).
	 * 
	 * @param cmId
	 *            The Id of the CaseModel to which all executing Cases should be
	 *            searched.
	 * @return a List of CaseExecutioners, which executes cases of CaseModels
	 *         with the given Id.
	 */
	public static List<CaseExecutioner> loadAllCaseExecutionersWithCaseModelId(String cmId) {
		// TODO don't use this native query which depends on the column name
		// CASEMODEL_CMID
		String queryString = "SELECT * FROM caseexecutioner ce WHERE ce.CASEMODEL_CMID = '" + cmId + "';";
		List<CaseExecutioner> caseExecutioners = null;

		try {
			EntityManager em = getEntityManager();
			// TODO maybe remove this native statement and make code
			// independent of the column name (CASEMODEL_CMID).
			Query q = em.createNativeQuery(queryString, CaseExecutioner.class);
			caseExecutioners = q.getResultList();
		} catch (Exception e) {
			log.error("Error while loading all Cases of a CaseModel Id from database", e);
		}

		return caseExecutioners;
	}


	/**
	 * Starts a Timer that every {@link PERSISTENCE_INTERVAL} seconds saves all
	 * existing cases to DB.
	 */
	public static void startPermanentCasePersistence() {
		if (timer == null) {
			log.info("Starting a new permanent repeating CasePersistenceTask");
			timer = new Timer();
			timer.scheduleAtFixedRate(new CasePersistenceTask(), 0, PERSISTENCE_INTERVAL);
		}
	}

	/**
	 * Stops the permanent persisting of all cases.
	 */
	public static void stopPermanentCasePersistence() {
		if (timer != null) {
			timer.cancel();
		}
	}


	/**
	 * Loads a previously persisted CaseModel from the database.
	 * 
	 * @param id
	 *            The id from the CaseModel which should be loaded from the
	 *            database.
	 * @return the loaded CaseModel Object
	 */
	public static CaseModel loadCaseModel(String cmId) {
		CaseModel caseModel = null;
		EntityManager em = getEntityManager();
		caseModel = em.find(CaseModel.class, cmId);

		return caseModel;
	}

	public static List<CaseModel> loadAllCaseModels() {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		List<CaseModel> caseModelList = em.createNamedQuery("CaseModel.getAll", CaseModel.class).getResultList();
		em.getTransaction().commit();

		if (caseModelList == null)
			return new ArrayList<>();
		else
			return caseModelList;
	}

	public static void deleteCaseModel(String cmId) {
		EntityManager em = getEntityManager();
		CaseModel cmToRemove = em.find(CaseModel.class, cmId);
		if (cmToRemove == null)
			throw new IllegalArgumentException(String.format("CaseModel id : %s is not assigned.", cmId));
		em.getTransaction().begin();
		em.remove(cmToRemove);
		em.getTransaction().commit();
	}

	public static EventMapper loadEventMapper() {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		List<EventMapper> eventMapperList = em.createNamedQuery("EventMapper.get", EventMapper.class).getResultList();
		em.getTransaction().commit();

		if (eventMapperList.isEmpty()) {
			return new EventMapper();
		} else {
			return eventMapperList.get(0);
		}
	}

	public static void saveEventMapper(EventMapper eventMapper) {
		EntityManager entityManager = getEntityManager();

		entityManager.getTransaction().begin();
		entityManager.merge(eventMapper);
		entityManager.getTransaction().commit();
	}

}
