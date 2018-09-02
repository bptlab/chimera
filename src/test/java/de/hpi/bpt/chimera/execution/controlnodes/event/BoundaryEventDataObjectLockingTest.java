package de.hpi.bpt.chimera.execution.controlnodes.event;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.Unicorn;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

/**
 * This test should ensure that there do not occur any problems if a
 * BoundaryEvent wants to use the same DataObject as the bounded Activity.
 */
public class BoundaryEventDataObjectLockingTest extends Unicorn {
	private final String filepath = "src/test/resources/execution/event/BoundaryEventDataObjectLockingTest.json";

	private CaseExecutioner caseExecutioner;
	private final String eventJson = "{\"attribute\": \"1\"}";
	
	@Before
	public void setUpTest() {
		super.setUpTest();
		String json = CaseModelTestHelper.getJsonString(filepath);
		CaseModel cm = CaseModelManager.parseCaseModel(json);
		caseExecutioner = ExecutionService.createCaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	@Test
	public void boundaryEventLocksSameDataObjectAsBoundedActivity() {
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");
		AbstractActivityInstance createDataObject = CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, fragmentInstance, "Create DataObject");

		DataObject dataObject = createDataObject.getOutputDataObjects().get(0);

		AbstractActivityInstance boundedTask = CaseExecutionerTestHelper.getActivityInstanceByName(fragmentInstance, "Bounded Task");

		caseExecutioner.beginDataControlNodeInstance(boundedTask, new ArrayList<>(Arrays.asList(dataObject)));

		BoundaryEventInstance boundaryEvent = (BoundaryEventInstance) CaseExecutionerTestHelper.getEventInstanceByName(fragmentInstance, "Boundary Event");

		assertEquals(State.REGISTERED, boundaryEvent.getState());
		CaseExecutionerTestHelper.triggerEvent(caseExecutioner, boundaryEvent, getBase(), eventJson);

		assertEquals(State.CANCEL, boundedTask.getState());
		assertEquals(State.TERMINATED, boundaryEvent.getState());

		DataAttributeInstance attribute = dataObject.getDataAttributeInstances().get(0);
		// see event json
		assertEquals("1", attribute.getValue());
	}
}
