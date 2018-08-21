package de.hpi.bpt.chimera.rest.history;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.model.CaseModel;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class HistoryService {
	public Document getTracesForScenarioId(CaseModel cm) throws ParserConfigurationException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element rootElement = doc.createElementNS("http://www.xes-standard.org", "log");
		doc.appendChild(rootElement);

		List<CaseExecutioner> caseExecutioners = ExecutionService.getAllCasesOfCaseModel(cm.getId());

		for (CaseExecutioner caseExecutioner : caseExecutioners) {
			Trace trace = new Trace(caseExecutioner);
			trace.appendToLog(rootElement);
		}

		return doc;
	}
}