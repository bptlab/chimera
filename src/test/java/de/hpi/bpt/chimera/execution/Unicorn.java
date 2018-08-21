package de.hpi.bpt.chimera.execution;

import java.util.UUID;

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
import de.hpi.bpt.chimera.rest.v2.EventRestService;
import de.hpi.bpt.chimera.util.PropertyLoader;

public class Unicorn extends JerseyTest {

	private WebTarget base;
	private final int port = 8081;
	private final String registerCatchEventDeployPath = String.format("%s%s", PropertyLoader.getProperty("unicorn.path.deploy"), PropertyLoader.getProperty("unicorn.path.query.rest"));
	// TODO: set up a response for throw events
	private final String throwEventDeploy = "/Unicorn-unicorn_BP15_dev/webapi/REST/Event";
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
		setRegisterCatchEventResponse();
		// setUnregisterCatchEventResponse();
	}

	private void setRegisterCatchEventResponse() {
		unicorn.when(
				HttpRequest.request()
					.withMethod("POST")
					.withPath(registerCatchEventDeployPath))
			.respond(
				HttpResponse.response()
					.withStatusCode(201)
					.withBody(registerCatchEventResponse()));
	}

	private void setUnregisterCatchEventResponse(String notificationRuleId) {
		unicorn.when(
				HttpRequest.request()
					.withMethod("DELETE")
						.withPath(String.format("%s/%s", registerCatchEventDeployPath, notificationRuleId)))
			.respond(
				HttpResponse.response()
					.withStatusCode(201));
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		unicorn.close();
	}

	public String registerCatchEventResponse() {
		String notificationRuleId = UUID.randomUUID().toString().replace("-", "");
		setUnregisterCatchEventResponse(notificationRuleId);
		return notificationRuleId;
	}

	public WebTarget getBase() {
		return base;
	}

	public void setBase(WebTarget base) {
		this.base = base;
	}
}
