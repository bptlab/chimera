package de.hpi.bpt.chimera.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.rest.beans.datamodel.DataObjectJaxBean;
import de.hpi.bpt.chimera.rest.beans.datamodel.MultipleDataObjectsJaxBean;
import de.hpi.bpt.chimera.rest.beans.exception.DangerExceptionJaxBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "data")
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
@Path("v3/organizations/{organizationId}/casemodels/{casemodelId}/cases/{caseId}/dataobjects")
public class DataObjectRestService extends AbstractRestService {
	private static final Logger log = Logger.getLogger(DataObjectRestService.class);

	@GET
	@Path("")
	@Operation(
		summary = "Receive all dataobjects of a case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the dataobjects.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultipleDataObjectsJaxBean.class)))})
	public Response getDataObjects(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @DefaultValue("") @QueryParam("filter") String filterString) {
		CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(caseId);

		DataManager dataManager = caseExecutioner.getDataManager();
		List<DataObject> dataObjects = dataManager.getDataObjects();

		if (!filterString.isEmpty()) {
			dataObjects = dataObjects.stream().filter(instance -> instance.getId().contains(filterString)).collect(Collectors.toList());
		}

		JSONObject result = new JSONObject(new MultipleDataObjectsJaxBean(dataObjects));
		return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("{dataObjectId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
		summary = "Receive a specific dataobject of a case",
		responses = {
			@ApiResponse(
				responseCode = "200", description = "Successfully requested the dataobject.",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataObjectJaxBean.class)))})
	public Response getDataObject(@PathParam("organizationId") String orgId, @PathParam("casemodelId") String cmId, @PathParam("caseId") String caseId, @PathParam("dataObjectId") String dataObjectId) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(caseId);

			DataManager dataManager = caseExecutioner.getDataManager();
			DataObject dataObject = dataManager.getDataObjectById(dataObjectId);

			JSONObject result = new JSONObject(new DataObjectJaxBean(dataObject));
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException e) {
			log.error(e);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(e.getMessage())).build();
		}
	}
}
