package de.hpi.bpt.chimera.parser.fragment;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;

public class BpmnXmlFragmentParserTest {
	final String path = "src/test/resources/parser/JsonStringVerySimpleCaseModel";

	@Test
	public void parseCaseModel() {
		CaseModel cm1 = CaseModelTestHelper.parseCaseModel(path);
		StartEvent startEvent = cm1.getFragments().get(0).getBpmnFragment().getStartEvent();
		assertNotNull(startEvent);
		assertEquals(startEvent.getOutgoingControlNodes().get(0), cm1.getFragments().get(0).getBpmnFragment().getEndEvent().getIncomingControlNodes().get(0));
	}

}
