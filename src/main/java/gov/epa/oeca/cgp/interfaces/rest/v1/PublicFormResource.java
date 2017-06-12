package gov.epa.oeca.cgp.interfaces.rest.v1;

import gov.epa.oeca.cgp.application.ApplicationUtils;
import gov.epa.oeca.cgp.application.CgpNoiFormAssembler;
import gov.epa.oeca.cgp.application.CgpNoiFormService;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.dto.publik.PublicNoiForm;
import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.domain.noi.Status;
import gov.epa.oeca.cgp.infrastructure.export.CgpFormExportService;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * @author dfladung
 */
@Component
@Path("/public/v1/form")
@Api(
        value = "Public CGP Form API"
)
public class PublicFormResource extends BaseResource {

    private static final Logger logger = LoggerFactory.getLogger(PublicFormResource.class);

    @Autowired
    CgpNoiFormService cgpNoiFormService;
    @Autowired
    CgpNoiFormAssembler assembler;
    @Autowired
    ApplicationUtils applicationUtils;
    @Autowired
    CgpFormExportService exportService;

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves list of forms for the specified criteria.")
    public List<PublicNoiForm> retrieveForms(
            @ApiParam(value = "The Master General Permit (MGP) of the form.")
            @QueryParam("masterPermitNumber") String masterPermitNumber,
            @ApiParam(value = "The NPDES ID of the form.")
            @QueryParam("npdesId") String npdesId,
            @ApiParam(value = "The unique tracking number of the form.")
            @QueryParam("trackingNumber") String trackingNumber,
            @ApiParam(value = "The application type.")
            @QueryParam("applicationType") String applicationType,
            @ApiParam(value = "The project's name.")
            @QueryParam("projectSiteName") String projectSiteName,
            @ApiParam(value = "The name of the project operator.")
            @QueryParam("operatorName") String operatorName,
            @ApiParam(value = "The two digit state code of the project site.")
            @QueryParam("projectState") String projectState,
            @ApiParam(value = "The county of the project site.")
            @QueryParam("projectCounty") String projectCounty,
            @ApiParam(value = "The city of the project site.")
            @QueryParam("projectCity") String projectCity,
            @ApiParam(value = "The zip code of the project site.")
            @QueryParam("projectZip") String projectZip,
            @ApiParam(value = "The status of the project")
            @QueryParam("projectStatus") String projectStatus,
            @ApiParam(value = "Indicator to search for federally operated facilities.")
            @QueryParam("federalIndicator") Boolean federalIndicator,
            @ApiParam(value = "Indicator to search for facilities on tribal lands.")
            @QueryParam("tribalIndicator") Boolean tribalIndicator,
            @ApiParam(value = "The name of a tribe to search by.")
            @QueryParam("tribalName") String tribalName,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("submittedDateFrom") String submittedDateFrom,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("submittedDateTo") String submittedDateTo,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("updatedDateFrom") String updatedDateFrom,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("updatedDateTo") String updatedDateTo) {
        try {
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            criteria.setTrackingNumber(trackingNumber);
            criteria.setNpdesId(npdesId);
            criteria.setMasterGeneralPermit(masterPermitNumber);
            if (!StringUtils.isEmpty(applicationType)) {
                FormType type = FormType.valueOf(applicationType);
                criteria.setType(type);
            }
            criteria.setOperatorName(operatorName);
            criteria.setSiteName(projectSiteName);
            if (!StringUtils.isEmpty(projectState)) {
                Validate.isTrue(projectState.length() == 2, "Project state should be a 2-digit code.");
                criteria.setSiteStateCode(projectState);
            }
            criteria.setSiteCity(projectCity);
            criteria.setSiteZipCode(projectZip);
            criteria.setSiteCounty(projectCounty);
            if (!StringUtils.isEmpty(projectStatus)) {
                Status status = Status.valueOf(projectStatus);
                criteria.setStatus(status);
            }

            criteria.setOperatorFederal(federalIndicator);
            criteria.setSiteIndianCountry(tribalIndicator);
            criteria.setSiteIndianCountryLands(tribalName);

            criteria.setSubmittedFrom(applicationUtils.fromString(submittedDateFrom));
            criteria.setSubmittedTo(applicationUtils.fromString(submittedDateTo));
            criteria.setUpdatedFrom(applicationUtils.fromString(updatedDateFrom));
            criteria.setUpdatedTo(applicationUtils.fromString(updatedDateTo));
            List<CgpNoiForm> forms = cgpNoiFormService.retrievePublicForms(criteria);
            return assembler.assemblePublicForms(forms);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw translateException(new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage()));
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/csv")
    @Consumes("application/json")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Extracts a CSV format copy of forms for the specified criteria.")
    public Response extractFormsCsv(
            @ApiParam(value = "The Master General Permit (MGP) of the form.")
            @QueryParam("masterPermitNumber") String masterPermitNumber,
            @ApiParam(value = "The NPDES ID of the form.")
            @QueryParam("npdesId") String npdesId,
            @ApiParam(value = "The unique tracking number of the form.")
            @QueryParam("trackingNumber") String trackingNumber,
            @ApiParam(value = "The application type.")
            @QueryParam("applicationType") String applicationType,
            @ApiParam(value = "The project's name.")
            @QueryParam("projectSiteName") String projectSiteName,
            @ApiParam(value = "The name of the project operator.")
            @QueryParam("operatorName") String operatorName,
            @ApiParam(value = "The two digit state code of the project site.")
            @QueryParam("projectState") String projectState,
            @ApiParam(value = "The county of the project site.")
            @QueryParam("projectCounty") String projectCounty,
            @ApiParam(value = "The city of the project site.")
            @QueryParam("projectCity") String projectCity,
            @ApiParam(value = "The zip code of the project site.")
            @QueryParam("projectZip") String projectZip,
            @ApiParam(value = "The status of the project")
            @QueryParam("projectStatus") String projectStatus,
            @ApiParam(value = "Indicator to search for federally operated facilities.")
            @QueryParam("federalIndicator") Boolean federalIndicator,
            @ApiParam(value = "Indicator to search for facilities on tribal lands.")
            @QueryParam("tribalIndicator") Boolean tribalIndicator,
            @ApiParam(value = "The name of a tribe to search by.")
            @QueryParam("tribalName") String tribalName,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("submittedDateFrom") String submittedDateFrom,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("submittedDateTo") String submittedDateTo,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("updatedDateFrom") String updatedDateFrom,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("updatedDateTo") String updatedDateTo) {
        try {
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            criteria.setTrackingNumber(trackingNumber);
            criteria.setNpdesId(npdesId);
            criteria.setMasterGeneralPermit(masterPermitNumber);
            if (!StringUtils.isEmpty(applicationType)) {
                FormType type = FormType.valueOf(applicationType);
                criteria.setType(type);
            }
            criteria.setOperatorName(operatorName);
            criteria.setSiteName(projectSiteName);
            if (!StringUtils.isEmpty(projectState)) {
                Validate.isTrue(projectState.length() == 2, "Project state should be a 2-digit code.");
                criteria.setSiteStateCode(projectState);
            }
            criteria.setSiteCity(projectCity);
            criteria.setSiteZipCode(projectZip);
            criteria.setSiteCounty(projectCounty);
            if (!StringUtils.isEmpty(projectStatus)) {
                Status status = Status.valueOf(projectStatus);
                criteria.setStatus(status);
            }

            criteria.setOperatorFederal(federalIndicator);
            criteria.setSiteIndianCountry(tribalIndicator);
            criteria.setSiteIndianCountryLands(tribalName);

            criteria.setSubmittedFrom(applicationUtils.fromString(submittedDateFrom));
            criteria.setSubmittedTo(applicationUtils.fromString(submittedDateTo));
            criteria.setUpdatedFrom(applicationUtils.fromString(updatedDateFrom));
            criteria.setUpdatedTo(applicationUtils.fromString(updatedDateTo));
            criteria.setResultLimit(1000L);
            List<CgpNoiForm> forms = cgpNoiFormService.retrievePublicForms(criteria);

            File csv = exportService.generateCsvExtract(forms);
            return Response.ok()
                    .entity(new String(Files.readAllBytes(csv.toPath())))
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw translateException(new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage()));
        } catch (IOException io) {
            logger.error(io.getMessage(), io);
            throw translateException(new ApplicationException(ApplicationErrorCode.E_InvalidArgument, io.getMessage()));
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/{formId}")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Retrieves a form with the specified ID.")
    public PublicNoiForm retrieveForm(
            @ApiParam(value = "The tracking number of the form.")
            @PathParam("formId") Long formId) {
        try {
            CgpNoiForm result = cgpNoiFormService.retrievePublicForm(formId);
            return assembler.assemblePublicForm(result);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/csv/{formId}")
    @Consumes("application/json")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Retrieves CSV data of a form with the specified ID.")
    public Response retrieveFormCsv(
            @ApiParam(value = "The tracking number of the form.")
            @PathParam("formId") Long formId) {
        try {
            CgpNoiForm result = cgpNoiFormService.retrievePublicForm(formId);
            File csv = exportService.generateFormCsv(result);

            return Response.ok()
                    .entity(new String(Files.readAllBytes(csv.toPath())))
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        } catch (IOException io) {
            logger.error(io.getMessage(), io);
            throw translateException(new ApplicationException(ApplicationErrorCode.E_InvalidArgument, io.getMessage()));
        }
        catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }


    @GET
    @Path("/{formId}/attachment/{attachmentId}")
    @Consumes("application/json")
    @Produces("application/octet-stream")
    @ApiOperation(value = "Retrieves the document with the specified ID as binary content.")
    public Response retrieveAttachmentContent(
            @ApiParam(value = "The ID of the form.")
            @PathParam("formId")
                    Long formId,
            @ApiParam(value = "The ID of the attachment.")
            @PathParam("attachmentId")
                    Long attachmentId) {
        try {
            File content = cgpNoiFormService.retrievePublicAttachmentData(formId, attachmentId);
            Attachment att = cgpNoiFormService.retrievePublicAttachment(attachmentId);
            return Response.ok(content, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + att.getName() + "\"")
                    .build();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }
}
