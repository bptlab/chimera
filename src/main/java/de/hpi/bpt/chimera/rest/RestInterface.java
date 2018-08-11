package de.hpi.bpt.chimera.rest;

import de.hpi.bpt.chimera.util.PropertyLoader;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class implements the REST interface of the JEngine core.
 * The core module provides methods to begin PCM instances
 * and to access the date inside the engine.
 * This REST interface provides methods to access this information
 * and to control the instances.
 * Methods which are necessary for the controlling can be found
 * inside the {@link ExecutionService}.
 * This class will use {@link ConnectionWrapper}
 * to access the database directly.
 */
@OpenAPIDefinition(
tags = {
	@Tag(name = "", description = "")
})
@SecurityScheme(
		name = "BasicAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "basic")
@Path("interface/v2")
public class RestInterface {
	/**
	 * Method for checking whether an address specified in the griffin editor
	 * links to a valid running chimera instance
	 *
	 * @return Response containing the version.
	 */
	@GET
	@Path("version")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVersion() {
		String version = PropertyLoader.getProperty("webapp.version");

		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity("{\"version\": \"" + version + "\"}").build();
	}
}

