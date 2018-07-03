package de.hpi.bpt.chimera.persistencemanager;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.model.CaseModel;

public class CasePersistenceTask extends Thread {
	static final Logger log = Logger.getLogger(CasePersistenceTask.class);

	private volatile boolean shouldEnd = false;

	@Override
	public void run() {
		// log.debug("Started persisting all case-models. (Timer hash: " +
		// this.hashCode());
		//
		// for (Map.Entry<String, CaseExecutioner> entry :
		// ExecutionService.getCasesMap().entrySet()) {
		// // TODO delete the following part when we don't need to comment it
		// // in for testing any more.
		//// log.debug("CaseExecutionier with Case Id:" +
		// caseExecutioner.getCase().getId());
		//// log.debug("has this MapForRequestKeys:");
		//// for (Entry<String, AbstractEventInstance> entry :
		// caseExecutioner.keyToRegisteredEvent.entrySet()) {
		//// log.debug("key:" + entry.getKey() + " value:" + entry.getValue());
		//// }
		//// log.debug("has this MapForIds:");
		//// for (Entry<String, AbstractEventInstance> entry :
		// caseExecutioner.idToRegisteredEvent.entrySet()) {
		//// log.debug("key:" + entry.getKey() + " value:" + entry.getValue());
		//// }
		// synchronized (this) {
		// try {
		// entry.setValue(DomainModelPersistenceManager.saveCase(entry.getValue().getCase()).getCaseExecutioner());
		// } catch (Exception e) {
		// log.error("Error during persisting in regular persisting task", e);
		// }
//			}
		// }
		/*
		 * for (Map.Entry<String, CaseModel> entry :
		 * CaseModelManager.getCaseModelsMap().entrySet()) { synchronized (this)
		 * { try {
		 * entry.setValue(DomainModelPersistenceManager.saveCaseModel(entry.
		 * getValue())); } catch (Exception e) {
		 * log.error("Error during persisting in regular persisting task", e); }
		 * } }
		 */
		while (!shouldEnd) {
			log.info("CasePersistenceTask is running.");
			try {
				DomainModelPersistenceManager.saveAllCaseModelsWithCases();
			} catch (Exception e) {
				log.error("Error while persisting all CaseModels and Cases", e);
			}
			try {
				sleep(20000);
			} catch (InterruptedException e) {
				log.error("Error while waiting for the next saveAllCaseModelsWithCases iteration.");
			}
		}
		log.info("End permanent Case persistence.");
	}


	public void endNextTimePossible() {
		shouldEnd = true;
	}

}
