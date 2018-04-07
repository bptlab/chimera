package de.hpi.bpt.chimera.persistencemanager;

import java.util.Map.Entry;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;

public class CasePersistenceTask extends TimerTask {
	final static Logger log = Logger.getLogger(CasePersistenceTask.class);

	@Override
	public void run() {
		log.debug("Started persisting all case-models. (Timer hash: " + this.hashCode());
		for (CaseExecutioner caseExecutioner : ExecutionService.getAllExecutingCaseExecutioner()) {
			// TODO delete the following part when we don't need to comment it
			// in for testing any more.
//			log.debug("CaseExecutionier with Case Id:" + caseExecutioner.getCase().getId());
//			log.debug("has this MapForRequestKeys:");
//			for (Entry<String, AbstractEventInstance> entry : caseExecutioner.keyToRegisteredEvent.entrySet()) {
//				log.debug("key:" + entry.getKey() + " value:" + entry.getValue());
//			}
//			log.debug("has this MapForIds:");
//			for (Entry<String, AbstractEventInstance> entry : caseExecutioner.idToRegisteredEvent.entrySet()) {
//				log.debug("key:" + entry.getKey() + " value:" + entry.getValue());
//			}
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
