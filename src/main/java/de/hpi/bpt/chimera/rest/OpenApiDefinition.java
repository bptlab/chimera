package de.hpi.bpt.chimera.rest;

import javax.ws.rs.Path;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
		@Tag(name = "data", description = "Requests concerning dataobjects and dataclasses."),
		@Tag(name = "activities", description = "Requests concerning activities."),
		@Tag(name = "events", description = "Requests concerning events."),
		@Tag(name = "emails", description = "Requests concerning email tasks."),
		@Tag(name = "members", description = "Requests concerning members of an organization.") }
// There is an undesired behavior in the automatic generation of the server
// object in swagger because the attribute 'allowedValues' in @ServerVariable
// always results in 'enum' in the swagger json.
// This means there is always a dropdown menu and not a textfield.
// To resolve this the server attribute of the open api definition is described
// in {basedir}/src/main/webapp/WEB-INF/openapi-configuration.json
)
@SecurityScheme(
	name = "BasicAuth",
	type = SecuritySchemeType.HTTP,
	scheme = "basic")
@Path("v3")
public class OpenApiDefinition {

}
