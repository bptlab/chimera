package de.hpi.bpt.chimera.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import de.hpi.bpt.chimera.rest.history.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "history")
@ApiResponses(value = {
	@ApiResponse(
		responseCode = "400", description = "A problem occured during the processing.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))),
	@ApiResponse(
		responseCode = "401", description = "A problem occured during the authentication.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))),
	@ApiResponse(
		responseCode = "404", description = "An unassigned identifier was used.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DangerExceptionJaxBean.class))) })
@SecurityRequirement(name = "BasicAuth")
@Path("v3/organizations/{organizationId}/casemodels/{casemodelId}/history")
public class CaseModelHistoryRestService extends AbstractRestService {
	private static final Logger log = Logger.getLogger(CaseModelHistoryRestService.class);

	@GET
	@Path("export")
	@Produces(MediaType.APPLICATION_XML)
	@Operation(
		summary = "Receive all logs of a case model as xml",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested all logs.",
				content = @Content(mediaType = "application/xml"))})
	public Response exportToXml(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId) throws TransformerConfigurationException {
		HistoryService service = new HistoryService();
		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);
			Document doc = service.getTracesForScenarioId(cm);
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_XML).entity(doc).build();
		} catch (ParserConfigurationException e) {
			log.error(e);
			return Response.status(Response.Status.BAD_REQUEST).entity("Error processing the xml").build();
		}
	}
}
