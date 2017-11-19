package de.hpi.bpt.chimera.persistencemanager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.CaseModel;

public class DomainModelPersistenceManager {
	// TODO in persistence.xml change <property
	// name="eclipselink.ddl-generation" value="drop-and-create-tables" />
	// to <property name="eclipselink.ddl-generation" value="create-tables" />
	// Otherwise the dababase will be reset every time.
	private static final Logger log = Logger.getLogger(DomainModelPersistenceManager.class);

	private static final String PERSISTENCE_UNIT_NAME = "CaseModel";
	private static EntityManagerFactory entityManagerFactory;
	private static boolean isEntityManagerFactoryInitialized = false;

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
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();

		entityManager.getTransaction().begin();
		entityManager.merge(caseModel);
		entityManager.getTransaction().commit();
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
		EntityManager em = getEntityManagerFactory().createEntityManager();
		return em.find(CaseModel.class, cmId);
	}

	public static List<CaseModel> loadAllCaseModels() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		List<CaseModel> caseModelList = em.createNamedQuery("CaseModel.getAll", CaseModel.class).getResultList();
		em.getTransaction().commit();

		if (caseModelList == null)
			return new ArrayList<>();
		else
			return caseModelList;
	}

	public static void deleteCaseModel(String cmId) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		CaseModel cmToRemove = em.find(CaseModel.class, cmId);
		if (cmToRemove == null)
			throw new IllegalArgumentException(String.format("CaseModel id : %s is not assigned.", cmId));
		em.getTransaction().begin();
		em.remove(cmToRemove);
		em.getTransaction().commit();
	}

	public static EventMapper loadEventMapper() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
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
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();

		entityManager.getTransaction().begin();
		entityManager.merge(eventMapper);
		entityManager.getTransaction().commit();
	}
}
