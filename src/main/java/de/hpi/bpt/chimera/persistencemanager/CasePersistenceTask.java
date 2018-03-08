package de.hpi.bpt.chimera.persistencemanager;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;

public class CasePersistenceTask extends TimerTask {
	final static Logger log = Logger.getLogger(CasePersistenceTask.class);

	@Override
	public void run() {
		log.debug("Started persisting all case-models. (Timer hash: " + this.hashCode());
		for (CaseExecutioner caseExecutioner : ExecutionService.getAllExecutingCaseExecutioner()) {
			synchronized (this) {
				try {
				DomainModelPersistenceManager.saveCase(caseExecutioner.getCase());
				} catch (Exception e) {
					log.error("Error during persisting in regular persisting task", e);
				}
			}
		}
		log.debug("Finished persisting all case-modals.");
	}

}
