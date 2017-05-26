package gov.epa.oeca.cgp.interfaces.rest.v1;

import gov.epa.oeca.cgp.application.reference.ReferenceService;
import gov.epa.oeca.cgp.domain.dto.Lookup;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.domain.noi.Status;
import gov.epa.oeca.cgp.domain.noi.formsections.Pollutant;
import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import gov.epa.oeca.cgp.domain.ref.*;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/lookup/v1")
@Api(
        value = "Lookup Resource",
        consumes = MediaType.APPLICATION_JSON,
        produces = MediaType.APPLICATION_JSON
)
public class LookupResource extends BaseResource {
    @Autowired
    ReferenceService referenceService;

    @GET
    @Path("formType")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieve all form types")
    public List<Lookup> retrieveAllFormTypes() {
        List<Lookup> formTypes = new ArrayList<>();
        for (FormType type : FormType.values()) {
            formTypes.add(new Lookup(type.name(), type.getValue()));
        }
        return formTypes;
    }

    @GET
    @Path("formStatus")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all form statuses")
    public List<Lookup> retrieveAllStatuses(@QueryParam("shownToUser") Boolean shownToUser) {
        List<Lookup> formStatuses = new ArrayList<>();
        Status[] statuses = BooleanUtils.isTrue(shownToUser) ? new Status[]{
            Status.Draft, Status.Submitted, Status.Active, Status.Terminated, Status.Discontinued,
                Status.OnHold
        } : Status.values();
        for (Status status : statuses) {
            formStatuses.add(new Lookup(status.name(), status.getValue()));
        }
        return formStatuses;
    }

    @GET
    @Path("states")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all states")
    public List<State> retrieveStates() {
        return referenceService.retrieveStates();
    }

    @GET
    @Path("tribes")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all tribes")
    public List<Tribe> retrieveTribes() {
        return referenceService.retrieveTribes();
    }


    @GET
    @Path("biaTribes")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all BIA tribes")
    public List<BiaTribe> retrieveBiaTribes() {
        return referenceService.retrieveBiaTribes();
    }

    @GET
    @Path("counties")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all counties")
    public List<County> retrieveCounties() {
        return referenceService.retrieveAllCounties();
    }

    @GET
    @Path("counties/{stateCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all counties for passed in state")
    public List<County> retrieveCounties(@PathParam("stateCode") String stateCode) {
        return referenceService.retrieveCounties(stateCode);
    }

    @GET
    @Path("chemicals")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all chemicals")
    public List<Chemical> retrieveChemicals() {
        return referenceService.retrieveChemicals();
    }

    @GET
    @Path("chemicals/{criteria}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Searches for chemicals.  At least 3 characters are required for the criteria to work.")
    public List<Chemical> retrieveChemicals(@PathParam("criteria") String criteria) {
        return referenceService.retrieveChemicals(criteria);
    }

    @GET
    @Path("pollutants/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Searches for pollutatns.  At least 3 characters are required for the criteria to work.")
    public List<Pollutant> retrievePollutants(@QueryParam("criteria") String criteria) {
        return referenceService.retrievePollutants(criteria);
    }

    @GET
    @Path("waters")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Searches for receiving waters.  At least 3 characters are required for the criteria to work.")
    public List<ReceivingWater> retrieveReceivingWaters(@QueryParam("criteria") String criteria) {
        return referenceService.retrieveReceivingWaters(criteria);
    }
}
