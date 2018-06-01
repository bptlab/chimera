package de.hpi.bpt.chimera.execution;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Rule;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.rest.EventRestService;

public class Unicorn extends JerseyTest {

	private WebTarget base;
	private final int port = 8081;
	private final String unicornPathDeploy = "/Unicorn-unicorn_BP15_dev/webapi/REST/EventQuery/REST";
	@Rule
	public MockServerRule mockServerRule = new MockServerRule(this, port);

	private MockServerClient unicorn;

	@Override
	protected Application configure() {
		return new ResourceConfig(EventRestService.class);
	}

	@SuppressWarnings("resource")
	public void setUpTest() {
		base = target("eventdispatcher");
		String host = "localhost";

		unicorn = new MockServerClient(host, port).reset();
		EventDispatcher.setUrl(String.format("http://%s:%d", host, port));
		setCatchEventResponse();
	}

	private void setCatchEventResponse() {
		unicorn.when(
				HttpRequest.request()
					.withMethod("POST")
					.withPath(unicornPathDeploy))
			.respond(
				HttpResponse.response()
					.withStatusCode(201)
					.withBody(catchEventResponse()));
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		unicorn.close();
	}

	public String catchEventResponse() {
		return "1";
	}

	public WebTarget getBase() {
		return base;
	}

	public void setBase(WebTarget base) {
		this.base = base;
	}
}
