package gov.epa.oeca.cgp.interfaces.rest.v1;

import gov.epa.oeca.cgp.application.ApplicationUtils;
import gov.epa.oeca.cgp.application.reference.ReferenceService;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.domain.noi.Status;
import gov.epa.oeca.cgp.domain.ref.County;
import gov.epa.oeca.cgp.domain.ref.State;
import gov.epa.oeca.cgp.domain.ref.Tribe;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Component
@Path("/public/v1/reference")
@Api(
        value = "Public CGP reference values.",
        consumes = MediaType.APPLICATION_JSON,
        produces = MediaType.APPLICATION_JSON
)
public class PublicReferenceResource extends BaseResource {

    @Autowired
    ReferenceService referenceService;

    @GET
    @Path("formType")
    @ApiOperation(value = "Retrieve all CGP permit types")
    public List<FormType> retrieveAllFormTypes() {
        return Arrays.asList(FormType.values());
    }

    @GET
    @Path("formStatus")
    @ApiOperation(value = "Retrieves all CGP permit statuses")
    public List<Status> retrieveAllStatuses() {
        return ApplicationUtils.PUBLIC_STATUSES;
    }

    @GET
    @Path("states")
    @ApiOperation(value = "Retrieves all states")
    public List<State> retrieveStates() {
        return referenceService.retrieveStates();
    }

    @GET
    @Path("tribes/{stateCode}")
    @ApiOperation(value = "Retrieves all tribes for specified state")
    public List<Tribe> retrieveTribes(@PathParam("stateCode") String stateCode) {
        return referenceService.retrieveTribes(stateCode);
    }

    @GET
    @Path("counties/{stateCode}")
    @ApiOperation(value = "Retrieves all counties for specified state")
    public List<County> retrieveCounties(@PathParam("stateCode") String stateCode) {
        return referenceService.retrieveCounties(stateCode);
    }
}
