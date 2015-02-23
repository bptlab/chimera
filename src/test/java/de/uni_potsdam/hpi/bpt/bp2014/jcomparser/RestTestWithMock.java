package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.JComparser;
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 */
//@RunWith(PowerMockRunner.class)
@PrepareForTest(JComparser.class)
public class RestTestWithMock extends JerseyTest {

    private JComparser jcomparser;

    private static final String GET_SCENARIO_NAMES_AND_IDS = "getScenarioNamesAndIDs";

    @Before
    public void setUpMock() {
        JComparser jcomparser = PowerMock.createPartialMock(JComparser.class,
                GET_SCENARIO_NAMES_AND_IDS);
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(REST.class);
    }

    @Before
    public void setUpExecutionService() {
        jcomparser = new JComparser();
    }

    /**
     *
     */
    @Test
    public void testGetScenarios() {

        try {
            PowerMock.mockStatic(JComparser.class);
            PowerMock.replay(JComparser.class);
            PowerMock.expectPrivate(jcomparser, GET_SCENARIO_NAMES_AND_IDS).andAnswer(new IAnswer<HashMap>() {
                @Override
                public HashMap answer() throws Throwable {
                    HashMap<String, Integer> hashMap = new HashMap<String, Integer>()
                    {{
                            put("One", 1);
                            put("Two", 2);
                            put("Three", 3);
                        }};
                    return (hashMap);
                }
            });
            PowerMock.verify(JComparser.class);
            PowerMock.replayAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String getUrl = "jcomparser/scenarios";
        final Response test = target(getUrl).request().get();
        assertEquals("{\"ids\":[1,2,3]}", test.readEntity(String.class));

    }
}
