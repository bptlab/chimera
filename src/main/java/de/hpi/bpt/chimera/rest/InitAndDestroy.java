package de.hpi.bpt.chimera.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

@WebListener
public class InitAndDestroy implements ServletContextListener {

	private final static Logger log = Logger.getLogger(InitAndDestroy.class);


	/**
	 * Implements the startup behavior of the webapp.
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("start-up process...");
		DomainModelPersistenceManager.startPermanentCasePersistence();
	}

	/**
	 * Implements the shutdown behavior of the webapp.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("shutdown process...");
		DomainModelPersistenceManager.stopPermanentCasePersistence();
	}
}
