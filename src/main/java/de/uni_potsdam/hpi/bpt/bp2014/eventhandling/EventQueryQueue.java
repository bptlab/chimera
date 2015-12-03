package de.uni_potsdam.hpi.bpt.bp2014.eventhandling;

import com.google.gson.Gson;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Receiving Events from Event Queries via ActiveMQ and JMS.
 * Register a Query and receive Events that to Unicorn EventQueries.
 */
public class EventQueryQueue {

	private String uuid = "";
	private final String host = "bpt.hpi.uni-potsdam.de";
	private final String port = "61616";

	/**
	 * Registers an Event Query on the Event Processing Platform.
	 * Receives a Uuid for identifying the Message Queue that query sends to.
	 * If the Query could not be registered, uuid
	 * @param title Title of the Query
	 * @param queryString The actual Query
	 * @param email E-Mail-Address of the user the Query is registered for
	 */
	public void registerQuery(String title, String queryString, String email) {
		EventQueryJson json = new EventQueryJson();
		json.setTitle(title);
		json.setQueryString(queryString);
		json.setEmail(email);

		Gson gson = new Gson();
		String postJson = gson.toJson(json);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080").path("Unicorn/webapi/REST/EventQuery");

		String response = target.request().post(Entity.json(postJson)).readEntity(String.class);
		if (!response.startsWith("EPException")) {
			uuid = response;
		}
	}

	/**
	 * Listens to the Message Queue and receives an Event that is selected by the query.
	 * @return The event string.
	 */
	public String receiveEvent() {
		if (uuid.isEmpty()) {
			return "No Event Query registered or registration failed.";
		} else {
			String event;
			try {
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
						String.format("tcp://%s:%s", host, port));

				Connection connection = connectionFactory.createConnection();
				connection.start();

				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				Destination destination = session.createQueue(uuid);

				MessageConsumer consumer = session.createConsumer(destination);

				Message message = consumer.receive(0);

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					event = textMessage.getText();
				} else {
					event = message.toString();
				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return event;
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
