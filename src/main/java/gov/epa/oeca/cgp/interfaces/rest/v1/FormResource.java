package gov.epa.oeca.cgp.interfaces.rest.v1;

import gov.epa.oeca.cgp.application.ApplicationUtils;
import gov.epa.oeca.cgp.application.CgpNoiFormAssembler;
import gov.epa.oeca.cgp.application.CgpNoiFormService;
import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchResult;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.cgp.domain.noi.*;
import gov.epa.oeca.cgp.infrastructure.export.CgpFormExportService;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Path("/form/v1")
@Api(
        value = "Form Resource",
        consumes = MediaType.APPLICATION_JSON,
        produces = MediaType.APPLICATION_JSON
)
public class FormResource extends BaseResource {

    private static final Logger logger = LoggerFactory.getLogger(FormResource.class);

    protected static final String EXCEL = "excel";
    protected static final String HTML = "html";

    @Autowired
    protected CgpNoiFormService cgpNoiFormService;
    @Autowired
    protected CgpFormExportService cgpFormExportService;
    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;
    @Autowired
    ApplicationUtils applicationUtils;
    @Autowired
    CgpNoiFormAssembler assembler;
    @Autowired
    CgpFormExportService exportService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves a blank form")
    public CgpNoiForm blankForm() {
        try {
            return new CgpNoiForm();
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("noi")
    @ApiOperation(value = "Creates a new noi")
    public CgpNoiForm createNoiForm(CgpNoiForm form) {
        try {
            return cgpNoiFormService.createNewNoticeOfIntent(form);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("lew")
    @ApiOperation(value = "Creates a new lew")
    public CgpNoiForm createLewForm(CgpNoiForm form) {
        try {
            return cgpNoiFormService.createNewLowErosivityWaiver(form);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{formId}")
    @ApiOperation(value = "Retrieves a form by id")
    public CgpNoiForm retrieveForm(@PathParam("formId") Long formId) {
        try {
            CgpNoiForm form = cgpNoiFormService.retrieveForm(formId);
            CgpNoiFormData data = form.getFormData();
            return form;
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{formId}")
    @ApiOperation(value = "Saves a form")
    public void saveForm(@PathParam("formId") Long formId, CgpNoiForm form) {
        try {
            cgpNoiFormService.updateForm(formId, form);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @DELETE
    @Path("{formId}")
    @ApiOperation(value = "Deletes a form")
    public void deleteForm(@PathParam("formId") Long formId) {
        try {
            cgpNoiFormService.withdrawForm(formId);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{formId}/sign")
    @ApiOperation(value = "Signs a form in cromerr")
    public void sign(@PathParam("formId") Long formId, String activityId) {
        try {
            cgpNoiFormService.certifyForm(formId, activityId);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{formId}/submit")
    @ApiOperation(value = "Submits a form without certifying.")
    public void submit(@PathParam("formId") Long formId) {
        try {
            cgpNoiFormService.submitForm(formId);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{formId}/change")
    @ApiOperation(value = "Allows a certified form to be certified")
    public CgpNoiForm change(@PathParam("formId") Long formId) {
        try {
            CgpNoiForm form = cgpNoiFormService.retrieveForm(formId);
            if (form.getType() == FormType.Notice_Of_Intent) {
                return cgpNoiFormService.changeNoticeOfIntent(formId);
            } else {
                return cgpNoiFormService.changeLowErosivityWaiver(formId);
            }
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{formId}/terminate/{certifier}")
    @ApiOperation(value = "If the form is a noi this will terminate the form.  If the form is a lew it will discontinue the form.  " +
            "This only creates a pending version of the form the user will need to sign this form to complete the process")
    public CgpNoiForm terminate(
            @PathParam("formId") Long formId,
            @PathParam("certifier") String certifier,
            CgpNoiForm updatedForm) {
        try {
            CgpNoiForm form = cgpNoiFormService.retrieveForm(formId);
            if (form.getType() == FormType.Low_Erosivity_Waiver) {
                form = cgpNoiFormService.discontinueLowErosivityWaiver(formId, updatedForm);
            } else if (form.getType() == FormType.Notice_Of_Intent) {
                form = cgpNoiFormService.terminateNoticeOfIntent(formId, updatedForm);
            }
            if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.preparer) && StringUtils.isNotEmpty(certifier)) {
                cgpNoiFormService.routeToCertifier(formId, certifier);
            } else if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.certifier)) {
                cgpNoiFormService.routeToCertifier(formId, applicationSecurityUtils.getCurrentUserId());
            }
            return form;
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("{formId}/deny")
    @ApiOperation(value = "Deny form.  Must have RA role.")
    public void deny(@PathParam("formId") Long formId, String reason) {
        try {
            cgpNoiFormService.denyForm(formId, reason);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("{formId}/reject")
    @ApiOperation(value = "Reject form and send back to preparer.  Must have Certifier Role")
    public void reject(@PathParam("formId") Long formId, String reason) {
        try {
            cgpNoiFormService.rejectForm(formId, reason);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("{formId}/release")
    @ApiOperation(value = "Release a form.  This moves the back back into the hold period.  Must have RA role.")
    public void release(@PathParam("formId") Long formId) {
        try {
            cgpNoiFormService.releaseForm(formId);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("{formId}/hold")
    @ApiOperation(value = "Puts a form on hold.  This suspends the form for review.  Must have RA role.")
    public void hold(@PathParam("formId") Long formId) {
        try {
            cgpNoiFormService.holdForm(formId);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{formId}/route")
    @ApiOperation(value = "Routes a form set")
    public void route(@PathParam("formId") Long formId, String userId) {
        try {
            cgpNoiFormService.routeToCertifier(formId, userId);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{formSetId}/assign")
    @ApiOperation(value = "Assigns a form set to another user")
    public void assign(@PathParam("formSetId") Long formSetId, String userId) {
        try {
            cgpNoiFormService.assignFormSet(formSetId, userId);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("{formId}/attachment")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns a list of attachments for a form")
    public List<Attachment> retrieveAttachments(@PathParam("formId") Long formId) {
        return cgpNoiFormService.retrieveAttachments(formId);
    }

    @PUT
    @Path("{formId}/attachment")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Saves an attachment")
    public void upload(@PathParam("formId") Long formId, @FormDataParam("meta") FormDataBodyPart meta, FormDataMultiPart formData) throws IOException {
        try {
            Validate.notNull(meta);
            meta.setMediaType(MediaType.APPLICATION_JSON_TYPE);
            Attachment metaData = meta.getValueAs(Attachment.class);
            List<Attachment> attachments = new ArrayList<>();
            for (FormDataBodyPart part : formData.getFields("file[]")) {
                InputStream is = part.getEntityAs(InputStream.class);
                Attachment attachment = new Attachment();
                attachment.setCategory(metaData.getCategory());
                attachment.setName(part.getFormDataContentDisposition().getFileName());
                File temp = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
                FileUtils.copyInputStreamToFile(is, temp);
                if (FileUtils.sizeOf(temp) > 3 * FileUtils.ONE_MB) {
                    throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, String.format("Attachments must be less than 3MB in size.  Your attachment %s was %dMB", attachment.getName(), FileUtils.sizeOf(temp) / FileUtils.ONE_MB));
                }
                attachment.setSize(temp.length());
                attachment.setData(temp);
                attachments.add(attachment);
            }
            cgpNoiFormService.addAttachments(formId, attachments);
            return;
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @DELETE
    @Path("{formId}/attachment/{id}")
    @ApiOperation("Delete an attachment")
    public void deleteAttachment(@PathParam("id") Long id) {
        cgpNoiFormService.removeAttachment(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    @ApiOperation(value = "Filters the currently logged in user's list of forms based on the search criteria posted.  " +
            "If the user is logged in as a preparer or certifier the listed will be restricted to forms they are the " +
            "owner of.")
    public List<CgpNoiForm> searchFormList(CgpNoiFormSearchCriteria criteria) {
        if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.preparer, ApplicationSecurityUtils.certifier)) {
            criteria.setAssociatedUser(applicationSecurityUtils.getCurrentUserId());
        }
        if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.regAuth)) {
            criteria.setRegulatoryAuthoritySearch(true);
        }
        return cgpNoiFormService.retrieveForms(criteria);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("search")
    @ApiOperation(value = "Filters the currently logged in user's list of forms based on the search criteria posted.  " +
            "If the user is logged in as a preparer or certifier the listed will be restricted to forms they are the " +
            "owner of.")
    public CgpNoiFormSearchResult formSearchResult(DataTableCriteria<CgpNoiFormSearchCriteria> search) {
        if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.preparer, ApplicationSecurityUtils.certifier)) {
            search.getCriteria().setAssociatedUser(applicationSecurityUtils.getCurrentUserId());
        }
        if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.regAuth)) {
            search.getCriteria().setRegulatoryAuthoritySearch(true);
        }
        return cgpNoiFormService.retrieveSearchResult(search);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("eligibility")
    @ApiOperation(value = "Checks if a form is eligible")
    public Boolean isEligible(CgpNoiForm form) {
        return cgpNoiFormService.isEligible(form);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("invite/{role}")
    @ApiOperation("Invite certifier or preparer to register with CDX.")
    public void invite(@PathParam("role") String role, Contact contact) {
        try {
            cgpNoiFormService.inviteUser(role, contact);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("export/{format}")
    @ApiOperation(value = "Filters the currently logged in user's list of forms based on the search criteria posted " +
            "and returns an excel file of the searched forms.")
    public Response exportFormList(
            final @PathParam("format") String format,
            @ApiParam(value = "The owner user ID of the form.")
            @QueryParam("owner") String owner,
            @ApiParam(value = "The NPDES ID of the form.")
            @QueryParam("npdesId") String npdesId,
            @ApiParam(value = "The Master General Permit (MGP) of the form.")
            @QueryParam("masterGeneralPermit") String masterGeneralPermit,
            @ApiParam(value = "The unique tracking number of the form.")
            @QueryParam("trackingNumber") String trackingNumber,
            @ApiParam(value = "The form type.")
            @QueryParam("type") String type,
            @ApiParam(value = "Form status.")
            @QueryParam("status") String status,
            @ApiParam(value = "The name of the project operator.")
            @QueryParam("operatorName") String operatorName,
            @ApiParam(value = "The project's name.")
            @QueryParam("siteName") String siteName,
            @ApiParam(value = "The project's region.")
            @QueryParam("siteRegion") Long siteRegion,
            @ApiParam(value = "The two digit state code of the project site.")
            @QueryParam("siteStateCode") String siteStateCode,
            @ApiParam(value = "The city of the project site.")
            @QueryParam("siteCity") String siteCity,
            @ApiParam(value = "The zip code of the project site.")
            @QueryParam("siteZipCode") String siteZipCode,
            @ApiParam(value = "Indicator to search for facilities on tribal lands.")
            @QueryParam("siteIndianCountry") Boolean siteIndianCountry,
            @ApiParam(value = "The name of Indian country lands to search by.")
            @QueryParam("siteIndianCountryLands") String siteIndianCountryLands,
            @ApiParam(value = "Indicator to search for federally operated facilities.")
            @QueryParam("operatorFederal") Boolean operatorFederal,
            @ApiParam(value = "An ISO 8601 formatted review expiration date.")
            @QueryParam("reviewExpiration") String reviewExpiration,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("submittedFrom") String submittedFrom,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("submittedTo") String submittedTo,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("updatedFrom") String updatedFrom,
            @ApiParam(value = "An ISO 8601 formatted date string.")
            @QueryParam("updatedTo") String updatedTo,
            @ApiParam(value = "Active record search indicator.")
            @QueryParam("activeRecord") Boolean activeRecord,
            @ApiParam(value = "Result limit.")
            @QueryParam("resultLimit") Long resultLimit
    ) {
        try {
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            criteria.setOwner(owner);
            criteria.setNpdesId(npdesId);
            criteria.setMasterGeneralPermit(masterGeneralPermit);
            criteria.setTrackingNumber(trackingNumber);
            if (!StringUtils.isEmpty(type)) {
                FormType formType = FormType.valueOf(type);
                criteria.setType(formType);
            }
            if (!StringUtils.isEmpty(status)) {
                Status s = Status.valueOf(status);
                criteria.setStatus(s);
            }
            criteria.setOperatorName(operatorName);
            criteria.setSiteName(siteName);
            criteria.setSiteRegion(siteRegion);
            if (!StringUtils.isEmpty(siteStateCode)) {
                Validate.isTrue(siteStateCode.length() == 2, "Project state should be a 2-digit code.");
                criteria.setSiteStateCode(siteStateCode);
            }
            criteria.setSiteCity(siteCity);
            criteria.setSiteZipCode(siteZipCode);
            if (siteIndianCountry != null) {
                criteria.setSiteIndianCountry(siteIndianCountry);
            }
            criteria.setSiteIndianCountryLands(siteIndianCountryLands);
            if (operatorFederal != null) {
                criteria.setOperatorFederal(operatorFederal);
            }
            criteria.setReviewExpiration(applicationUtils.fromString(reviewExpiration));
            criteria.setSubmittedFrom(applicationUtils.fromString(submittedFrom));
            criteria.setSubmittedTo(applicationUtils.fromString(submittedTo));
            criteria.setUpdatedFrom(applicationUtils.fromString(updatedFrom));
            criteria.setUpdatedTo(applicationUtils.fromString(updatedTo));
            criteria.setActiveRecord(activeRecord);
            criteria.setResultLimit(resultLimit);

            if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.preparer, ApplicationSecurityUtils.certifier)) {
                criteria.setAssociatedUser(applicationSecurityUtils.getCurrentUserId());
            }
            if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.regAuth)) {
                criteria.setRegulatoryAuthoritySearch(true);
            }
            List<CgpNoiForm> formList = cgpNoiFormService.retrieveForms(criteria);
            if (EXCEL.equals(format)) {
                File excel = cgpFormExportService.generateExcelExport(formList);
                tracker.track(excel, excel);
                return Response.ok(excel, MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Disposition", "attachment; filename=\"" + excel.getName() + "\"")
                        .build();
            } else if (HTML.equals(format)) {
                File html = cgpFormExportService.generateHtmlExport(formList);
                tracker.track(html, html);
                return Response.ok(html, MediaType.TEXT_HTML)
                        .header("Content-Disposition", "inline; filename=\"" + html.getName() + "\"")
                        .build();
            } else {
                return Response.serverError().entity("ERROR: incorrect export format specified.").build();
            }

        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/csv")
    @Consumes("application/json")
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
            List<CgpNoiForm> forms = cgpNoiFormService.retrieveForms(criteria);

            File csv = exportService.generateCsvExtract(forms);
            tracker.track(csv, csv);
            return Response.ok(csv, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "inline; filename=\"" + csv.getName() + "\"")
                    .build();

        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw translateException(new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage()));
        }
        catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }

    @GET
    @Path("/csv/{formId}")
    @Consumes("application/json")
    @ApiOperation(value = "Retrieves CSV data of a form with the specified ID.")
    public Response retrieveFormCsv(
            @ApiParam(value = "The tracking number of the form.")
            @PathParam("formId") Long formId) {
        try {
            CgpNoiForm result = cgpNoiFormService.retrieveForm(formId);
            File csv = exportService.generateFormCsv(result);
            tracker.track(csv, csv);
            return Response.ok(csv, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "inline; filename=\"" + csv.getName() + "\"")
                    .build();
        }
        catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw translateException(e);
        }
    }
}
