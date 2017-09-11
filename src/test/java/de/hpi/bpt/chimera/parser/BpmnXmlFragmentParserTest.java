package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

public class BpmnXmlFragmentParserTest {
	String jsonString = "";
	final String fileName = "JsonStringVerySimpleCaseModel";

	@Before
	public void getJsonString() {
		try {
			String file = getClass().getResource(fileName).getFile();
			FileInputStream inputStream = new FileInputStream(file);
			jsonString = IOUtils.toString(inputStream);
			inputStream.close();
		} catch (Exception e) {
			assertEquals("Error", 1, e);
		}
	}


	@Test
	public void parseCaseModel() {

		// Parse a CaseModel from jsonString and give it to the CaseModel
		// manager
		CaseModel cm1 = CaseModelManager.parseCaseModel(jsonString);

		assertEquals(cm1.getFragments().get(0).getBpmnFragment().getStartEvent().getOutgoingControlNodes().get(0).getTargetRef(), cm1.getFragments().get(0).getBpmnFragment().getEndEvent().getIncomingControlNodes().get(0).getSourceRef());
	}

}
