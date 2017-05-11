package de.hpi.bpt.chimera.persistencemanager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import de.hpi.bpt.chimera.model.CaseModel;

public class DomainModelPersistenceManager {
	// TODO in persistence.xml change <property
	// name="eclipselink.ddl-generation" value="drop-and-create-tables" />
	// to <property name="eclipselink.ddl-generation" value="create-tables" />
	// Otherwise the dababase will be reset every time.
	private static final String PERSISTENCE_UNI_NAME = "domainModel";
	private static EntityManagerFactory entityManagerFactory;

	private DomainModelPersistenceManager() {
	}

	/**
	 * Persists a given CaseModel to the database using the Java Persistence API
	 * "EclipseLink".
	 * 
	 * @param caseModel
	 *            the CaseModel that should be persisted.
	 */
	public static void saveCaseModel(CaseModel caseModel) {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNI_NAME);
		EntityManager entitiyManager = entityManagerFactory.createEntityManager();

		entitiyManager.getTransaction().begin();
		entitiyManager.persist(caseModel);
		entitiyManager.getTransaction().commit();
	}

	/**
	 * Loads a previously persisted CaseModel from the database.
	 * 
	 * @param id
	 *            The id from the CaseModel which should be loaded from the
	 *            database.
	 * @return the loaded CaseModel Object
	 */
	public static CaseModel loadCaseModel(String id) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		Query q = em.createQuery("SELECT c FROM CaseModel c WHERE c.id=:id");
		q.setParameter("id", id);

		return (CaseModel) q.getSingleResult();
	}

}
