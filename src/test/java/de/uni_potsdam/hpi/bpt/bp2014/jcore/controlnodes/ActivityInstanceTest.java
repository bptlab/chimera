package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbSelectedDataObjects;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.AbstractStateMachine;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ActivityInstanceTest {

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    private DataManager createExampleDM() {
        DataManager instance = EasyMock.createNiceMock(DataManager.class);
        expect(instance.getDataObjects()).andReturn(new ArrayList<>()).anyTimes();
        replay(instance);
        return instance;
    }

    private ScenarioInstance createExample(int controlNodeId, int scenarioInstanceId) {
        ScenarioInstance instance = EasyMock.createNiceMock(ScenarioInstance.class);
        instance.updateDataFlow();
        expectLastCall();
        instance.checkXorGatewaysForTermination(controlNodeId);
        expectLastCall();
        expect(instance.getScenarioInstanceId()).andReturn(scenarioInstanceId);
        expect(instance.getControlNodeInstances()).andReturn(new ArrayList<>()).anyTimes();
        expect(instance.getControlFlowEnabledControlNodeInstances()).andReturn(
                new ArrayList<>()).anyTimes();
        expect(instance.getDataEnabledControlNodeInstances()).andReturn(
                new ArrayList<>()).anyTimes();
        expect(instance.getEnabledControlNodeInstances()).andReturn(new ArrayList<>()).anyTimes();
        expect(instance.getRunningControlNodeInstances()).andReturn(new ArrayList<>()).anyTimes();
        expect(instance.getTerminatedControlNodeInstances()).andReturn(new ArrayList<>()).anyTimes();
        expect(instance.getDataManager()).andReturn(createExampleDM()).anyTimes();
        replay(instance);
        return instance;
    }

    @Test
    public void testBegin() {
        int controlNodeId = 1;
        int fragmentInstanceId = 1;
        int scenarioInstanceId = 1;
        ScenarioInstance instance = createExample(controlNodeId, scenarioInstanceId);
        ActivityInstance activity = new ActivityInstance(
                controlNodeId, fragmentInstanceId, instance);
        activity.begin();
        verify(instance);
    }


    @Test
    public void testBeginWorkingItems() {
        int controlNodeId = 1;
        int fragmentInstanceId = 1;
        int scenarioInstanceId = 1;
        ScenarioInstance instance = createExample(controlNodeId, scenarioInstanceId);
        ActivityInstance activity = new ActivityInstance(
                controlNodeId, fragmentInstanceId, instance);
        activity.begin(Arrays.asList(1, 2, 3));
        DbSelectedDataObjects dataSelection = new DbSelectedDataObjects();
        List<Integer> dataObjects = dataSelection.getDataObjectSelection(
                scenarioInstanceId, activity.getControlNodeInstanceId());
        verify(instance);
        assertEquals(Arrays.asList(1, 2, 3), dataObjects);
    }

    @Test
    public void testTerminateNoAlternative() {
        int controlNodeId = 1;
        int fragmentInstanceId = 1;
        int scenarioInstanceId = 1;
        ScenarioInstance instance = createExample(controlNodeId, scenarioInstanceId);
        ActivityInstance activity = new ActivityInstance(
                controlNodeId, fragmentInstanceId, instance);
        activity.setCanTerminate(true);
        activity.getStateMachine().setState(AbstractStateMachine.STATE.RUNNING);
        activity.terminate();
        assertEquals("Couldn't correctly terminate activity", AbstractStateMachine.STATE.TERMINATED, activity.getState());
    }

    @Test
    public void testTerminate() {
        int controlNodeId = 1;
        int fragmentInstanceId = 1;
        int scenarioInstanceId = 1;
        ScenarioInstance instance = createExample(controlNodeId, scenarioInstanceId);
        ActivityInstance activity = new ActivityInstance(
                controlNodeId, fragmentInstanceId, instance);
        activity.setCanTerminate(true);
        activity.getStateMachine().setState(AbstractStateMachine.STATE.RUNNING);
        activity.terminate(new HashMap<>());
        assertEquals("Couldn't correctly terminate activity", AbstractStateMachine.STATE.TERMINATED, activity.getState());
    }

    @Test
    public void testCheckDataFlowEnabled() {
        int controlNodeId = 1;
        int fragmentInstanceId = 1;
        int scenarioInstanceId = 1;
        ScenarioInstance instance = createExample(controlNodeId, scenarioInstanceId);
        ActivityInstance activity = new ActivityInstance(
                controlNodeId, fragmentInstanceId, instance);

    }

    @Test
    public void testCancel() throws Exception {
        int controlNodeId = 1;
        int fragmentInstanceId = 1;
        int scenarioInstanceId = 1;
        ScenarioInstance instance = createExample(controlNodeId, scenarioInstanceId);
        ActivityInstance activity = new ActivityInstance(
                controlNodeId, fragmentInstanceId, instance);
        activity.cancel();
        assertEquals(AbstractStateMachine.STATE.CANCEL, activity.getState());
    }
}