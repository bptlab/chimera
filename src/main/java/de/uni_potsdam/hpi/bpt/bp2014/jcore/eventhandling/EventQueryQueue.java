package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import com.google.gson.Gson;

import javax.jms.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractEvent;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.ws.rs.core.Response;

/**
 * Receiving Events from Event Queries via ActiveMQ and JMS.
 * Register a Query and receive Events that to Unicorn EventQueries.
 */
public class EventQueryQueue {

	private final String mqHost;
	private final String mqPort;
	private final String restPath;

    public EventQueryQueue(String mqHost, String mqPort, String restPath) {
        this.mqHost = mqHost;
        this.mqPort = mqPort;
        this.restPath = restPath;
    }

	/**
	 * Registers an Event Query on the Event Processing Platform.
	 * Receives a Uuid for identifying the Message Queue that query sends to.
	 * If the Query could not be registered, uuid
	 * @param title Title of the Query
	 * @param queryString The actual Query
	 * @param email E-Mail-Address of the user the Query is registered for
	 * @param url URL of the Server Unicorn is running on
     * @return returns UUID of registered query.
     */
	public String registerQuery(String title, String queryString, String email, String url) {
		EventQueryJson json = new EventQueryJson();
		json.setTitle(title);
		json.setQueryString(queryString);
		json.setEmail(email);

		Gson gson = new Gson();
		String postJson = gson.toJson(json);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url).path(this.restPath);

		Response response = target.request().post(Entity.json(postJson));
		if (response.getStatus() == 200) {
			return response.readEntity(String.class);
		} else {
            throw new IllegalArgumentException("Invalid query string");
        }
	}

    /**
     * This method creates an event listener for a previously registered query,
     * which calls terminate on the event passed as parameter, as soon as an event is received.
     * @param event the event control node which listens to the event.
     * @param uuid the id of the registered event.
     */
    public void listenToEvent(AbstractEvent event, String uuid) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                String.format("tcp://%s:%s", this.mqHost, this.mqPort));

        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(uuid);
            MessageConsumer consumer = session.createConsumer(destination);

            EventMessageListener listener = new EventMessageListener(connection, event);
            consumer.setMessageListener(listener);

            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
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
