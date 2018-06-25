package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.TimerEventBehavior;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

public class TerminationTimerTest {
	private final String filepath = "src/test/resources/execution/TimerEventTest2";
	private CaseModel cm;
	// private CaseExecutioner caseExecutioner;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
	}

	/*
	 * Checks whether registered TimerEvents are canceled when the fragment is
	 * terminated."
	 */
	@Test
	public void testTermination() {
		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
		FragmentInstance fragInst = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");

		// Some controlflow to get the TerminationCondition fulfilled.
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, fragmentInstance, "activity1");

		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(fragInst, "timerevent1");
		assertTrue("Activity3 should be enabled before terminating the case.", eventInstance.getState() == State.REGISTERED);

		caseExecutioner.terminate();

		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail;
			jobDetail = scheduler.getJobDetail(((TimerEventBehavior) eventInstance.getBehavior()).getJobKey());
			assertEquals("There should no Jobs and therfore no JobDetails", jobDetail, null);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
