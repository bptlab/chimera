package de.uni_potsdam.hpi.bpt.bp2014.events;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling.SseNotifier;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventNotifierTest extends JerseyTest {

    WebTarget base;

    @Before
    public void setUpBase() {
        base = target("sse");
    }


    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(SseNotifier.class);
    }

    // TODO figure out how to test HttpServlet SSE connection
    @Test @Ignore
    public void testActivateEvent() throws InterruptedException, IOException {

        Client client = ClientBuilder.newBuilder().register(SseNotifier.class).build();
        EventSource eventSource = EventSource.target(base).build();
        MyEventListener listener = new MyEventListener();
        eventSource.register(listener, "refresh");
        eventSource.open();

        assertFalse(listener.isNotified);

        SseNotifier.notifyRefresh();

        // wait for the event to be received
        Thread.sleep(1000);

        assertTrue(listener.isNotified);

        eventSource.close();

    }

    private class MyEventListener implements EventListener{
        private boolean isNotified;

        MyEventListener() {
            super();
            isNotified = false;
        }

        @Override
        public void onEvent(InboundEvent inboundEvent) {
            isNotified = true;
        }
    }
}
