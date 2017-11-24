package de.hpi.bpt.chimera.persistencemanager;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;

@Entity
@NamedQuery(name = "EventMapper.get", query = "SELECT e FROM EventMapper e")
public class EventMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToMany(cascade = CascadeType.ALL)
	private Map<String, CaseStartTrigger> caseStartEvents;

	private static final Logger log = Logger.getLogger(EventMapper.class);

	public EventMapper() {
		caseStartEvents = new HashMap<>();
	}

	public void addCaseStartEvent(String eventKey, CaseStartTrigger caseStartTrigger) {
		if (!caseStartEvents.containsKey(eventKey)) {
			caseStartEvents.put(eventKey, caseStartTrigger);
		}
	}

	public CaseStartTrigger getCaseStartTriggerToEventKey(String eventKey) {
		if (caseStartEvents.containsKey(eventKey)) {
			return caseStartEvents.get(eventKey);
		} else {
			log.info("no CaseStartTrigger with eventKey" + eventKey + "found");
			return null;
		}
	}
}
