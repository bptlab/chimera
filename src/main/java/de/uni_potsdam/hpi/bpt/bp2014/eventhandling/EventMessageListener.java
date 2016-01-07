package de.uni_potsdam.hpi.bpt.bp2014.eventhandling;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.FragmentInstance;

import javax.jms.*;

/**
 * Class which is responsible for receiving events.
 */
public class EventMessageListener implements MessageListener {
	private EventQueryQueue q;
	private Connection connection;
//	private FragmentInstance fragmentInstance;

	public EventMessageListener(EventQueryQueue q, Connection connection) {
		this.q = q;
		this.connection = connection;
//		this.fragmentInstance = fragmentInstance;
	}

	@Override public void onMessage(Message message) {
		try {
			this.connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		q.hasReceived = true;
		//fragmentInstance.createDatabaseFragmentInstance();
	}
}
