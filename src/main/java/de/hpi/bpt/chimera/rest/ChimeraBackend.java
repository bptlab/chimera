package de.hpi.bpt.chimera.rest;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * This is the start point for the JAX-RS application.
 * It defines all packages that contain REST resources to be made available via the REST interface.
 * This class is configured in the web.xml and mapped to the URL pattern /api/*.
 * @author Marcin.Hewelt
 *
 */
public class ChimeraBackend extends ResourceConfig {
	
	public ChimeraBackend() {
		super(MultiPartFeature.class);
		packages("de.hpi.bpt.chimera.rest;"
				+ "de.hpi.bpt.chimera.configuration.rest;"
				+ "de.hpi.bpt.chimera.history.rest;");
	}
}
