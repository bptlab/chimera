package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

/**
 * Test that a new fragment instance will be created after the End Event was
 * reached.
 */
public class FragmentSequentialTest {
	private final String filepath = "src/test/resources/execution/SequentialFragmentInstantiationTest.json";

	private CaseExecutioner caseExecutioner;

	@Before
	public void setUp() {
		String json = CaseModelTestHelper.getJsonString(filepath);
		CaseModel cm = CaseModelManager.parseCaseModel(json);
		caseExecutioner = ExecutionService.createCaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	@Test
	public void testSequentialFragmentInstantiation() {
		String fragmentName = "Default Fragment";
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, fragmentName);
		String activityName = "First Task";
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, fragmentInstance, activityName);
		int fragmentInstanceAmount = caseExecutioner.getCase().getFragmentInstances().size();
		assertEquals(1, fragmentInstanceAmount);

		activityName = "Second Task";
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, fragmentInstance, activityName);
		fragmentInstanceAmount = caseExecutioner.getCase().getFragmentInstances().size();
		assertEquals(2, fragmentInstanceAmount);
	}
}
