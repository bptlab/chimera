package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import org.easymock.IAnswer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ExecutionService.class)
public class RestConnectionTestWithMock extends JerseyTest {

    private ExecutionService executionService;

    private static final String GET_ALL_SCENARIOS = "getAllScenarioIDs";
    private static final String LIST_ALL_SCENARIO_INSTANCES = "listAllScenarioInstancesForScenario";

    @Before
    public void setUpMock() {
        ExecutionService executionService = PowerMock.createPartialMock(ExecutionService.class,
                GET_ALL_SCENARIOS,
                LIST_ALL_SCENARIO_INSTANCES);
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(RestConnection.class);
    }

    @Before
    public void setUpExecutionService() {
        executionService = new ExecutionService();
    }

    /**
     *
     */
    //@Test
    public void testGetScenarios() {

         try {

            PowerMock.mockStatic(ExecutionService.class);
            PowerMock.replay(ExecutionService.class);
            PowerMock.expectPrivate(executionService, "getAllScenarioIDs").andAnswer(new IAnswer<LinkedList>() {
                @Override
                public LinkedList answer() throws Throwable {
                    LinkedList ll = new LinkedList();
                    ll.add(1);
                    ll.add(2);
                    ll.add(3);
                    return (ll);
                }
            });
            PowerMock.verify(ExecutionService.class);
            PowerMock.replayAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String getUrl = "interface/v1/en/scenarios/0/";
        final Response test = target(getUrl).request().get();
        assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));

    }
}
