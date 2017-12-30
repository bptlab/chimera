package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;

public class BpmnXmlFragmentParserTest {
	final String path = "src/test/resources/parser/JsonStringVerySimpleCaseModel";

	@Test
	public void parseCaseModel() {
		CaseModel cm1 = CaseModelTestHelper.parseCaseModel(path);
		assertEquals(cm1.getFragments().get(0).getBpmnFragment().getStartEvent().getOutgoingControlNodes().get(0), cm1.getFragments().get(0).getBpmnFragment().getEndEvent().getIncomingControlNodes().get(0));
	}

}
