package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

public class CaseStarterTest extends Unicorn {
	// TODO create CaseModel for CaseStarterTests
	private final String filepath = "src/test/resources/execution/FragmentConcurrencyCreationTest.json";
	private CaseModel cm;

	@Before
	public void setUpTest() {
		super.setUpTest();
		String json = CaseModelTestHelper.getJsonString(filepath);
		cm = CaseModelManager.parseCaseModel(json);
	}

	@Test
	public void testTrigger() {
		// assertEquals(1, cm.getStartCaseTrigger().size());
	}
}
