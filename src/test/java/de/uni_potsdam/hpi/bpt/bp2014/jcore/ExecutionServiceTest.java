package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;

public class ExecutionServiceTest extends AbstractDatabaseDependentTest {

	@Test
	public void testOneExecutionServicePerScenarioID() {
		ExecutionService.dropCachedInstances();
		assertEquals(0, ExecutionService.getInstancesSize());
		//launch first scenario -> assert HashMap.size == 1
		ExecutionService ex1 = ExecutionService.getInstance(1);
		ex1.startNewScenarioInstance();
		assertEquals(1, ExecutionService.getInstancesSize());
		ex1.startNewScenarioInstance();
		assertEquals(1, ExecutionService.getInstancesSize());
		ExecutionService ex2 = ExecutionService.getInstance(2);
		ex2.startNewScenarioInstance();
		assertEquals(2, ExecutionService.getInstancesSize());
		//launch again scenario 1
		ex1.startNewScenarioInstance();
		assertEquals(2, ExecutionService.getInstancesSize());
		//launch scenario 2 in a static way
		ExecutionService.startNewScenarioInstanceStatic(2);
		assertEquals(2, ExecutionService.getInstancesSize());
	}

}
