package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("sse")
public class Notifier {

    private static Logger LOGGER = Logger.getLogger(Notifier.class);

    private static EventOutput eventOutput = new EventOutput();

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getConnection() {
        return eventOutput;
    }

    public boolean notifyEventOccurrance() {
        try {
            eventOutput.write(new OutboundEvent.Builder()
                    .name("Event")
                    .mediaType(MediaType.TEXT_PLAIN_TYPE)
                    .data(String.class, "Event received.")
                    .build());
            return true;
        } catch (IOException e) {
            LOGGER.error("Frontend could not be notified about event occurrance. Reason: ", e);
            return false;
        }
    }

    @DELETE
    public void close() throws IOException {
        eventOutput.close();
    }
}
