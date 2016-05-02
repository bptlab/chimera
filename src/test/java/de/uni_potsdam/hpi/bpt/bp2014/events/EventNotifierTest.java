package de.uni_potsdam.hpi.bpt.bp2014.events;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.EventDispatcher;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.Notifier;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventNotifierTest extends JerseyTest {

    WebTarget base;

    Notifier notifier;

    @Before
    public void setUpBaseAndNotifier() {
        base = target("sse");
        notifier = new Notifier();
    }


    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Notifier.class);
    }

    @Test
    public void testActivateEvent() throws InterruptedException {

        Client client = ClientBuilder.newBuilder().register(Notifier.class).build();
        MyEventSource eventSource = new MyEventSource(base);

        assertFalse(eventSource.isNotified);

        boolean notificationSent = notifier.notifyEventOccurrance();
        assertTrue(notificationSent);

        // wait for the event to be received
        Thread.sleep(1000);

        assertTrue(eventSource.isNotified);

        eventSource.close();

    }

    private class MyEventSource extends EventSource {
        private boolean isNotified;

        MyEventSource(WebTarget target) {
            super(target);
            isNotified = false;
        }

        @Override
        public void onEvent(InboundEvent inboundEvent) {
            isNotified = true;
        }
    }
}
