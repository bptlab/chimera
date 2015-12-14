package de.uni_potsdam.hpi.bpt.bp2014.eventhandling;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.FragmentInstance;

import javax.jms.*;

/**
 * Created by Jonas on 14.12.2015.
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
