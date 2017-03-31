package gov.epa.oeca.cgp.interfaces.rest.v1;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.springframework.stereotype.Component;

import gov.epa.oeca.common.security.ApplicationUser;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author smckay
 */
@Component
@Path("/user/v1")
@Api(
		value = "User Resource",
		consumes = MediaType.APPLICATION_JSON,
		produces = MediaType.APPLICATION_JSON
)
public class UserResource extends BaseResource {
	@POST
	@Path("/authenticate")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Authenticates a user with the server.")
	public Response authenticate(ApplicationUser user, @Context HttpServletRequest request) {
		request.getSession(true).setAttribute("user", user);
		return Response.ok().location(
					UriBuilder.fromPath("/action/secured/home").build()
				).build();
	}
}
