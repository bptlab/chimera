package de.hpi.bpt.chimera.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;
import de.hpi.bpt.chimera.usermanagment.OrganizationManager;

@WebListener
public class InitAndDestroy implements ServletContextListener {

	private static final Logger log = Logger.getLogger(InitAndDestroy.class);


	/**
	 * Implements the startup behavior of the webapp.
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("start-up process...");
		DomainModelPersistenceManager.startPermanentCasePersistence();
		DomainModelPersistenceManager.loadAll();
		log.info("Loaded all resources.");
		OrganizationManager.createDefaultOrganization();
	}

	/**
	 * Implements the shutdown behavior of the webapp.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("shutdown process...");
		DomainModelPersistenceManager.stopPermanentCasePersistence();
		DomainModelPersistenceManager.saveAll();
		DomainModelPersistenceManager.closeEntityManager();
	}
}
