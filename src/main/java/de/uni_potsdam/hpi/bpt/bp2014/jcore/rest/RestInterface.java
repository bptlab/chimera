package de.uni_potsdam.hpi.bpt.bp2014.jcore.rest;

import de.uni_potsdam.hpi.bpt.bp2014.database.ConnectionWrapper;
import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

/**
 * This class implements the REST interface of the JEngine core.
 * The core module provides methods to execute PCM instances
 * and to access the date inside the engine.
 * This REST interface provides methods to access this information
 * and to control the instances.
 * Methods which are necessary for the controlling can be found
 * inside the {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService}.
 * This class will use {@link ConnectionWrapper}
 * to access the database directly.
 */
@Path("interface/v2") public class RestInterface {
	/**
	 * Method for checking whether an address specified in the griffin editor
	 * links to a valid running chimera instance
	 * @return Response containing the version.
     */
	@GET
	@Path("version")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVersion() {
		String version = PropertyLoader.getProperty("webapp.version");

		return Response.status(Response.Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity("{\"version\": \""
						+ version + "\"}")
				.build();
	}
}

