package de.hpi.bpt.chimera.rest;

import javax.ws.rs.container.ContainerRequestContext;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.beans.miscellaneous.MessageJaxBean;
import de.hpi.bpt.chimera.rest.filters.BasicAuth;
import de.hpi.bpt.chimera.usermanagment.User;
import de.hpi.bpt.chimera.usermanagment.UserManager;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 *
 */
@ApiResponse(
		responseCode = "400", description = "Test",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class)))
@ApiResponse(
		responseCode = "402", description = "Test",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class)))
public class AbstractRestService {
	protected final User retrieveUser(ContainerRequestContext requestContext) {
		try {
			String auth = requestContext.getHeaderString("authorization");
			// If the user does not have the right (does not provide any HTTP
			// Basic
			// Auth)
			// lap : loginAndPassword
			String[] lap = BasicAuth.decode(auth);
			return UserManager.authenticateUser(lap[0], lap[1]);
		} catch (Exception e) {
			throw e;
		}
	}

	protected final String buildError(String text) {
		JSONArray result = new JSONArray();
		JSONObject content = new JSONObject();
		content.put("text", text);
		content.put("type", "danger");
		result.put(content);
		return result.toString();
	}

	protected final String buildMessage(String text) {
		MessageJaxBean bean = new MessageJaxBean(text);
		JSONObject message = new JSONObject(bean);
		return message.toString();
	}
}
