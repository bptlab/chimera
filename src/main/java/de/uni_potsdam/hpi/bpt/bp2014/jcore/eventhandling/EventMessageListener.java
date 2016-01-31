package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractEvent;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Class responsible for receiving events. After an event is received calls terminate on event.
 */
public class EventMessageListener implements MessageListener {
    private final AbstractEvent event;
	private Connection connection;

    /**
     *
     * @param connection ActiveMq connection to the JMS
     * @param event event control node which should be terminated on
     */
	public EventMessageListener(Connection connection, AbstractEvent event) {
		this.connection = connection;
	    this.event = event;
    }

	@Override public void onMessage(Message message) {
		this.event.terminate();
		try {
			this.connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
}
