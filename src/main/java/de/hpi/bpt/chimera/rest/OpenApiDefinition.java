package de.hpi.bpt.chimera.rest;

import javax.ws.rs.Path;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
	info = @Info(
		title = "Chimera",
		version = "1.4",
		description = "Chimera Case Engine"),
	tags = {
		@Tag(name = "organizations", description = "Requests concerning organizations."),
		@Tag(name = "users", description = "Requests concerning users."),
		@Tag(name = "casemodels", description = "Requests concerning casemodels."),
		@Tag(name = "cases", description = "Requests concerning cases."),
		@Tag(name = "dataobjects", description = "Requests concerning dataobjects.")},
	servers = {
		@Server(url = "https://bpt-lab.org/chimera-dev/api"),
		@Server(url = "http://localhost:8080/Chimera/api")
	})
@SecurityScheme(
	name = "BasicAuth",
	type = SecuritySchemeType.HTTP,
	scheme = "basic")
@Path("v3")
public class OpenApiDefinition {

}
