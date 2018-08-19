package de.hpi.bpt.chimera.rest.history;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.rest.beans.history.ActivityLog;

import java.util.List;

public class Trace {
	CaseExecutioner caseExecutioner;

	public Trace(CaseExecutioner caseExecutioner) {
		this.caseExecutioner = caseExecutioner;
	}

	public void appendToLog(Element rootElement) {
		Document doc = rootElement.getOwnerDocument();
		Element traceXml = doc.createElement("trace");
		List<ActivityLog> logEntries = caseExecutioner.getActivityLogs();
		for (ActivityLog logEntry : logEntries) {
			// ProM only cares about completed activities
			if ("terminated".equalsIgnoreCase(logEntry.getNewValue())) {
				logEntry.appendToTrace(traceXml);
			}
		}
		rootElement.appendChild(traceXml);
	}
}
