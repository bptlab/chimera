package de.hpi.bpt.chimera.rest.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

@Provider
public class ResponseFilter implements ContainerResponseFilter {
	private static Logger log = Logger.getLogger(ResponseFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		// TODO Auto-generated method stub
		DomainModelPersistenceManager.getAllDataLock().unlock();
		log.info("RESPONSE");

	}

}