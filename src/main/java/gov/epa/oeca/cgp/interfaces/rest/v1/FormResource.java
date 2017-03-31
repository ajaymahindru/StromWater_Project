package gov.epa.oeca.cgp.interfaces.rest.v1;

import gov.epa.oeca.cgp.application.CgpNoiFormService;
import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchResult;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.CgpNoiFormData;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.interfaces.rest.BaseResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    protected CgpNoiFormService cgpNoiFormService;
    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;

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
}
