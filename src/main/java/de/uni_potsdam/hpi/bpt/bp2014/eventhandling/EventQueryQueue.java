package de.uni_potsdam.hpi.bpt.bp2014.eventhandling;

import com.google.gson.Gson;

import javax.jms.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.ws.rs.core.Response;

/**
 * Receiving Events from Event Queries via ActiveMQ and JMS.
 * Register a Query and receive Events that to Unicorn EventQueries.
 */
public class EventQueryQueue {

	private String uuid = "";
	public boolean hasReceived = false;
	private final String MQ_HOST = "bpt.hpi.uni-potsdam.de";
	private final String MQ_PORT = "61616";
	private final String REST_PATH = "Unicorn/webapi/REST/EventQuery";

	public EventQueryQueue() {
	}

	/**
	 * Registers an Event Query on the Event Processing Platform.
	 * Receives a Uuid for identifying the Message Queue that query sends to.
	 * If the Query could not be registered, uuid
	 * @param title Title of the Query
	 * @param queryString The actual Query
	 * @param email E-Mail-Address of the user the Query is registered for
	 * @param url URL of the Server Unicorn is running on
	 */
	public void registerQuery(String title, String queryString, String email, String url) {
		EventQueryJson json = new EventQueryJson();
		json.setTitle(title);
		json.setQueryString(queryString);
		json.setEmail(email);

		Gson gson = new Gson();
		String postJson = gson.toJson(json);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url).path(REST_PATH);

		Response response = target.request().post(Entity.json(postJson));
		if (response.getStatus() == 200) {
			uuid = response.readEntity(String.class);
		}
	}

	/**
	 * Listens to the Message Queue and receives an Event that is selected by the query.
	 * @return The event string.
	 */
	public void receiveEvent() {
		if (uuid.isEmpty()) {
			throw new RuntimeException("No Event Query registered or registration failed.");
		} else {
			try {
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
						String.format("tcp://%s:%s", MQ_HOST, MQ_PORT));

				final Connection connection = connectionFactory.createConnection();
				connection.start();

				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				Destination destination = session.createQueue(uuid);

				MessageConsumer consumer = session.createConsumer(destination);

				EventMessageListener listener = new EventMessageListener(this, connection);

				consumer.setMessageListener(listener);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getUuid() {
		return uuid;
	}

	/**
	 * Json Class for parsing Event Queries
	 * received via REST POST
	 */
	private class EventQueryJson {
		private String title;
		private String queryString;
		private String email;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getQueryString() {
			return queryString;
		}

		public void setQueryString(String queryString) {
			this.queryString = queryString;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}
}
