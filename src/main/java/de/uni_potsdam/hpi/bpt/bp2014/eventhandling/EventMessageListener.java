package de.uni_potsdam.hpi.bpt.bp2014.eventhandling;

import javax.jms.*;

/**
 * Class responsible for receiving events.
 */
public class EventMessageListener implements MessageListener {
	private EventQueryQueue queue;
	private Connection connection;

	public EventMessageListener(EventQueryQueue queue, Connection connection) {
		this.queue = queue;
		this.connection = connection;
	}

	@Override public void onMessage(Message message) {
		try {
			this.connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		//TODO implement reaction on receiving an event
		queue.hasReceived = true;
	}
}
