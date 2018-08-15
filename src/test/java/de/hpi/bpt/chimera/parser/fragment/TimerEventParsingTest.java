package de.hpi.bpt.chimera.parser.fragment;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.parser.IllegalCaseModelException;

public class TimerEventParsingTest {
	private final String filepath = "src/test/resources/parser/fragment/DeclineTimerEventPositionTest.json";

	/**
	 * The CaseModel needs to be rejected when the first Control Node in any
	 * Fragment is an TimerEvent.
	 */
	@Test
	public void testDeclinedTimerEventPosition() {
		IllegalCaseModelException exception = null;
		try {
			CaseModelTestHelper.parseCaseModel(filepath);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalCaseModelException);
			exception = (IllegalCaseModelException) e;
		}

		assertNotNull(exception);
		assertEquals(exception.getMessage(), "Invalid Fragment: StartEvent cannot be followed by Timer Event");
	}
}
