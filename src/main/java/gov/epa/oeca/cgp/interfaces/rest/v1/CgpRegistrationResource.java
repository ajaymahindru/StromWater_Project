package gov.epa.oeca.cgp.interfaces.rest.v1;

import gov.epa.oeca.cgp.application.registration.CgpRegistrationService;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.notification.NewAccountConfirmation;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import gov.epa.oeca.common.security.jwt.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.Collections;

@Component
@Path("/registration/v1")
@Api(
        value = "Registration API",
        consumes = "application/json",
        produces = "application/json"
)
public class CgpRegistrationResource extends BaseResource {
    private static final Logger logger = LoggerFactory.getLogger(CgpRegistrationResource.class);
    private static final String confirmationSessionKey = "confirmationSession";

    @Autowired
    CgpRegistrationService cgpRegistrationService;
    @Autowired
    JwtTokenUtil tokenUtil;

    @POST
    @Path("/email_available")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Checks if email is available for new account registration")
    public Boolean isEmailAvailable(String email, @Context HttpServletRequest req) {
        try {
            return cgpRegistrationService.isEmailAvailable(email);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("/new_account_confirmation")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Sends a new account confirmation email")
    public NewAccountConfirmation sendConfirmation(NewAccountConfirmation confirmation, @Context HttpServletRequest req) {
        try {
            NewAccountConfirmation result = cgpRegistrationService.sendConfirmation(confirmation);
            // bind it to the session so we know it's for the correct user
            req.getSession().setAttribute(confirmationSessionKey, result);
            return result;
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Path("/confirmation_code_validation")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Validate as confirmation code.")
    public void validateConfirmationCode(NewAccountConfirmation confirmation, @Context HttpServletRequest req, @Context HttpServletResponse resp) {
        try {
            NewAccountConfirmation target = (NewAccountConfirmation) req.getSession().getAttribute(confirmationSessionKey);
            cgpRegistrationService.validateConfirmationCode(confirmation, target);
            // remove it from the session if successful
            req.getSession().removeAttribute(confirmationSessionKey);
            // the user is effectively authenticated at this point, so get a token and set it as a cookie
            tokenUtil.insertTokenIntoCookie(tokenUtil.createTokenForUser(
                    target.getUser(),
                    Collections.singletonList("ROLE_OECA_USER")), resp);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }
}
