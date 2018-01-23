package de.hpi.bpt.chimera.persistencemanager;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;

public class CasePersistenceTask extends TimerTask {
	final static Logger log = Logger.getLogger(CasePersistenceTask.class);

	@Override
	public void run() {
		log.debug("Starting to persist all case-models.");
		for (CaseExecutioner caseExecutioner : ExecutionService.getAllExecutingCaseExecutioner()) {
			synchronized (this) {
				DomainModelPersistenceManager.saveCase(caseExecutioner.getCase());
			}
		}
		log.debug("Finished persisting all case-modals.");
	}

}
