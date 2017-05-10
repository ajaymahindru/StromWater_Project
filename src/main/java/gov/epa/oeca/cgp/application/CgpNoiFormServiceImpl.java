package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.application.reference.ReferenceService;
import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchResult;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSetSearchCriteria;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.cgp.domain.noi.*;
import gov.epa.oeca.cgp.domain.noi.formsections.PointOfDischarge;
import gov.epa.oeca.cgp.domain.noi.formsections.Pollutant;
import gov.epa.oeca.cgp.domain.noi.formsections.Tier;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.node.TransactionStatus;
import gov.epa.oeca.cgp.domain.ref.*;
import gov.epa.oeca.cgp.infrastructure.certification.CromerrService;
import gov.epa.oeca.cgp.infrastructure.cor.CopyOfRecordGeneratorService;
import gov.epa.oeca.cgp.infrastructure.icis.IcisSubmissionService;
import gov.epa.oeca.cgp.infrastructure.persistence.AttachmentRepository;
import gov.epa.oeca.cgp.infrastructure.persistence.FormRepository;
import gov.epa.oeca.cgp.infrastructure.persistence.FormSetRepository;
import gov.epa.oeca.cgp.infrastructure.user.UserInformationService;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.infrastructure.notification.NotificationService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author dfladung
 */
@Service("formService")
@Transactional
public class CgpNoiFormServiceImpl implements CgpNoiFormService {

    private static final Logger logger = LoggerFactory.getLogger(CgpNoiFormServiceImpl.class);
    private static final List<Phase> phasesThatRequireReview = Arrays.asList(Phase.New, Phase.Change);
    private static final List<FormType> typesThatRequireReview = Collections.singletonList(FormType.Notice_Of_Intent);

    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;
    @Autowired
    FormRepository formRepository;
    @Autowired
    FormSetRepository formSetRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    ReferenceService referenceService;
    @Autowired
    CgpNoiFormValidationHelper validationHelper;
    @Autowired
    CgpNoiFormAssembler assembler;
    @Autowired
    CromerrService cromerrService;
    @Autowired
    IcisSubmissionService icisSubmissionService;
    @Autowired
    CopyOfRecordGeneratorService copyOfRecordGeneratorService;
    @Autowired
    UserInformationService userInformationService;
    @Autowired
    CgpNoiFormNotificationHelper notificationHelper;
    @Resource(name = "oecaGeneralConfiguration")
    Map<String, String> oecaGeneralConfiguration;
    @Autowired
    NotificationService notificationService;

    @Override
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public Boolean isEligible(CgpNoiForm form) throws ApplicationException {
        try {
            return !StringUtils.isEmpty(determineMasterPermitNumber(form));
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public CgpNoiForm createNewNoticeOfIntent(CgpNoiForm form) throws ApplicationException {
        try {
            // validate the input
            CgpNoiForm newForm = assembler.assembleNewNoi(form);
            validationHelper.validateNoiEligibility(newForm);

            // create the form set
            String masterPermitNumber = determineMasterPermitNumber(newForm);
            if (StringUtils.isEmpty(masterPermitNumber)) {
                throw new ApplicationException(ApplicationErrorCode.E_Ineligible, "Your project is ineligible.");
            }
            CgpNoiFormSet newFormSet = new CgpNoiFormSet(
                    masterPermitNumber,
                    applicationSecurityUtils.getCurrentUserId());
            formSetRepository.save(newFormSet);

            // create the form
            newForm.setFormSet(newFormSet);
            updateFormDataIndex(newForm);
            newFormSet.getForms().add(newForm);
            formRepository.save(newForm);
            formSetRepository.update(newFormSet);
            return newForm;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public CgpNoiForm changeNoticeOfIntent(Long previousNoiFormId) throws ApplicationException {
        try {
            // validate
            CgpNoiForm toClone = formRepository.find(previousNoiFormId);
            Validate.isTrue(
                    toClone.getFormSet().getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                    "Only the owner can change an NOI.");
            Validate.isTrue(toClone.getActiveRecord(), "Only the most recent version can be changed.");
            Validate.isTrue(
                    Status.Active.equals(toClone.getStatus()),
                    "Can not change an NOI until previous submission is active.");
            Validate.isTrue(!CollectionUtils.exists(toClone.getFormSet().getForms(), o -> {
                CgpNoiForm form = (CgpNoiForm) o;
                return Phase.Terminate.equals(form.getPhase())
                        && Arrays.asList(Status.Submitted, Status.Terminated).contains(form.getStatus());
            }), "This NOI has already been terminated and can't be changed.");

            // update the previous form
            toClone.setActiveRecord(false);
            toClone.setLastUpdatedDate(ZonedDateTime.now());
            toClone.setStatus(Status.ActivePendingChange);
            formRepository.update(toClone);

            // create the form
            CgpNoiForm newForm = assembler.assembleNoiChange(toClone);
            CgpNoiFormSet formSet = toClone.getFormSet();
            newForm.setFormSet(formSet);
            updateFormDataIndex(newForm);
            formSet.getForms().add(newForm);
            formRepository.save(newForm);
            formSetRepository.update(formSet);
            // clone the attachments
            cloneAttachments(toClone, newForm);
            formRepository.update(newForm);
            return newForm;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName,
            ApplicationSecurityUtils.regAuthRoleName})
    public CgpNoiForm terminateNoticeOfIntent(Long previousNoiFormId, CgpNoiForm updatedForm) throws ApplicationException {
        try {
            // validate
            CgpNoiForm toClone = formRepository.find(previousNoiFormId);
            Validate.isTrue(
                    toClone.getFormSet().getOwner().equals(applicationSecurityUtils.getCurrentUserId())
                            || applicationSecurityUtils.hasRole(ApplicationSecurityUtils.regAuth),
                    "Only the owner can terminate an NOI.");
            Validate.isTrue(toClone.getActiveRecord(), "Only the most recent version can be changed.");
            Validate.isTrue(
                    Status.Active.equals(toClone.getStatus()),
                    "Can not terminate an NOI until previous submission is active.");
            Validate.isTrue(!CollectionUtils.exists(toClone.getFormSet().getForms(), o -> {
                CgpNoiForm form = (CgpNoiForm) o;
                return Phase.Terminate.equals(form.getPhase())
                        && Arrays.asList(Status.Submitted, Status.Terminated).contains(form.getStatus());
            }), "This NOI has already been terminated.");
            Validate.notEmpty(updatedForm.getFormData().getProjectSiteInformation().getSiteTerminationReason(),
                    "Termination reason is required.");
            if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.helpdesk)) {
                Validate.isTrue(
                        StringUtils.isNotEmpty(updatedForm.getFormData().getOperatorInformation().getCertifier().getFirstName()) &&
                                StringUtils.isNotEmpty(updatedForm.getFormData().getOperatorInformation().getCertifier().getLastName()) &&
                                StringUtils.isNotEmpty(updatedForm.getFormData().getOperatorInformation().getCertifier().getTitle()) &&
                                StringUtils.isNotEmpty(updatedForm.getFormData().getOperatorInformation().getCertifier().getEmail()),
                        "Certifier information is required.");
                Validate.notNull(updatedForm.getCertifiedDate(),
                        "Termination date is required.");
            }


            // update the previous form
            toClone.setActiveRecord(false);
            toClone.setLastUpdatedDate(ZonedDateTime.now());
            formRepository.update(toClone);

            // create the form
            CgpNoiForm newForm = assembler.assembleNoiTermination(toClone, updatedForm);
            CgpNoiFormSet formSet = toClone.getFormSet();
            newForm.setFormSet(formSet);
            updateFormDataIndex(newForm);
            formSet.getForms().add(newForm);
            formRepository.save(newForm);
            formSetRepository.update(formSet);
            // clone the attachments
            cloneAttachments(toClone, newForm);
            formRepository.update(newForm);
            return newForm;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public CgpNoiForm createNewLowErosivityWaiver(CgpNoiForm form) throws ApplicationException {
        try {
            // validate the input
            CgpNoiForm newForm = assembler.assembleNewLew(form);
            validationHelper.validateLewEligibility(newForm);

            // create the form set
            String masterPermitNumber = determineMasterPermitNumber(newForm);
            CgpNoiFormSet newFormSet = new CgpNoiFormSet(
                    masterPermitNumber,
                    applicationSecurityUtils.getCurrentUserId());
            formSetRepository.save(newFormSet);

            // create the form
            newForm.setFormSet(newFormSet);
            updateFormDataIndex(newForm);
            newFormSet.getForms().add(newForm);
            formRepository.save(newForm);
            formSetRepository.update(newFormSet);
            return newForm;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public CgpNoiForm changeLowErosivityWaiver(Long previousLewFormId) throws ApplicationException {
        try {
            // validate
            CgpNoiForm toClone = formRepository.find(previousLewFormId);
            Validate.isTrue(
                    toClone.getFormSet().getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                    "Only the owner can change a LEW.");
            Validate.isTrue(toClone.getActiveRecord(), "Only the most recent version can be changed.");
            Validate.isTrue(
                    Status.Active.equals(toClone.getStatus()),
                    "Can not change a LEW until previous submission is active.");
            Validate.isTrue(!CollectionUtils.exists(toClone.getFormSet().getForms(), o -> {
                CgpNoiForm form = (CgpNoiForm) o;
                return Phase.Terminate.equals(form.getPhase())
                        && Arrays.asList(Status.Submitted, Status.Discontinued).contains(form.getStatus());
            }), "This LEW has already been discontinued and can't be changed.");

            // update the previous form
            toClone.setActiveRecord(false);
            toClone.setLastUpdatedDate(ZonedDateTime.now());
            toClone.setStatus(Status.ActivePendingChange);
            formRepository.update(toClone);

            // create the form
            CgpNoiForm newForm = assembler.assembleLewChange(toClone);
            CgpNoiFormSet formSet = toClone.getFormSet();
            newForm.setFormSet(formSet);
            updateFormDataIndex(newForm);
            formSet.getForms().add(newForm);
            formRepository.save(newForm);
            formSetRepository.update(formSet);
            return newForm;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName,
            ApplicationSecurityUtils.regAuthRoleName})
    public CgpNoiForm discontinueLowErosivityWaiver(Long previousLewFormId, CgpNoiForm updatedForm) throws ApplicationException {
        try {
            // validate
            CgpNoiForm toClone = formRepository.find(previousLewFormId);
            Validate.isTrue(
                    toClone.getFormSet().getOwner().equals(applicationSecurityUtils.getCurrentUserId())
                            || applicationSecurityUtils.hasRole(ApplicationSecurityUtils.regAuth),
                    "Only the owner can discontinue a LEW.");
            Validate.isTrue(toClone.getActiveRecord(), "Only the most recent version can be changed.");
            Validate.isTrue(
                    Status.Active.equals(toClone.getStatus()),
                    "Can not discontinue a LEW until previous submission is active.");
            Validate.isTrue(!CollectionUtils.exists(toClone.getFormSet().getForms(), o -> {
                CgpNoiForm form = (CgpNoiForm) o;
                return Phase.Terminate.equals(form.getPhase())
                        && Arrays.asList(Status.Submitted, Status.Discontinued).contains(form.getStatus());
            }), "This LEW has already been discontinued.");
            if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.helpdesk)) {
                Validate.isTrue(
                        StringUtils.isNotEmpty(updatedForm.getFormData().getOperatorInformation().getCertifier().getFirstName()) &&
                                StringUtils.isNotEmpty(updatedForm.getFormData().getOperatorInformation().getCertifier().getLastName()) &&
                                StringUtils.isNotEmpty(updatedForm.getFormData().getOperatorInformation().getCertifier().getTitle()) &&
                                StringUtils.isNotEmpty(updatedForm.getFormData().getOperatorInformation().getCertifier().getEmail()),
                        "Certifier information is required.");
                Validate.notNull(updatedForm.getCertifiedDate(),
                        "Termination date is required.");
            }

            // update the previous form
            toClone.setActiveRecord(false);
            toClone.setLastUpdatedDate(ZonedDateTime.now());
            formRepository.update(toClone);

            // create the form
            CgpNoiForm newForm = assembler.assembleLewDiscontinuation(toClone, updatedForm);
            CgpNoiFormSet formSet = toClone.getFormSet();
            newForm.setFormSet(formSet);
            updateFormDataIndex(newForm);
            formSet.getForms().add(newForm);
            formRepository.save(newForm);
            formSetRepository.update(formSet);
            return newForm;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public void assignFormSet(Long formSetId, String ownerUserId) throws ApplicationException {
        try {
            CgpNoiFormSet toAssign = formSetRepository.find(formSetId);
            if (!applicationSecurityUtils.getCurrentApplicationUser().getAuthorities()
                    .contains(ApplicationSecurityUtils.helpdesk)) {
                Validate.isTrue(
                        toAssign.getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                        "Only the owner can assign a form.");
            }
            toAssign.setOwner(ownerUserId);
            formSetRepository.update(toAssign);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Secured({ApplicationSecurityUtils.preparerRoleName, ApplicationSecurityUtils.certifierRoleName})
    public void routeToCertifier(Long formId, String certifierId) throws ApplicationException {
        try {
            // validate for routing
            Validate.notEmpty(certifierId, "Certifier ID is required.");
            CgpNoiForm toCertify = formRepository.find(formId);
            CgpNoiFormSet formSet = toCertify.getFormSet();
            Validate.isTrue(
                    formSet.getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                    "Only the owner can route a form for certification.");

            // check to see if this is even necessary
            if (certifierId.equals(toCertify.getFormSet().getOwner())
                    && applicationSecurityUtils.hasRole(ApplicationSecurityUtils.certifier)) {
                // don't do anything if the current user is a certifier and the owner
                return;
            }

            // change ownership
            formSet.setOwner(certifierId);
            formSetRepository.update(formSet);

            // notify the certifier
            notificationHelper.sendRouteToCertifier(toCertify, certifierId);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName})
    public void rejectForm(Long formId, String reason) throws ApplicationException {
        try {
            // validate for rejection
            CgpNoiForm forRejection = formRepository.find(formId);
            CgpNoiFormSet formSet = forRejection.getFormSet();
            Validate.isTrue(
                    formSet.getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                    "Only the owner can reject an NOI/LEW.");
            Validate.isTrue(Status.Draft.equals(forRejection.getStatus()), "Only drafts can be rejected.");
            Validate.notEmpty(reason, "A reason is required for rejection.");

            // change ownership
            formSet.setOwner(forRejection.getFormData().getOperatorInformation().getPreparer().getUserId());
            formSetRepository.update(formSet);

            // notify the preparer
            notificationHelper.sendRejectedByCertifier(forRejection, reason);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public void updateForm(Long formId, CgpNoiForm form) throws ApplicationException {
        try {
            CgpNoiForm forUpdate = formRepository.find(formId);
            Validate.isTrue(
                    forUpdate.getFormSet().getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                    "Only the owner can update a form.");
            Validate.isTrue(Status.Draft.equals(forUpdate.getStatus()), "Can not update a form in non-draft status.");
            Validate.isTrue(forUpdate.getActiveRecord(), "Only the active record can be changed.");
            forUpdate = assembler.assembleForm(forUpdate, form);
            if (FormType.Notice_Of_Intent.equals(forUpdate.getType())) {
                validationHelper.validateNoiEligibility(forUpdate);
            } else {
                validationHelper.validateLewEligibility(forUpdate);
            }
            updateFormDataIndex(forUpdate);
            forUpdate.setLastUpdatedDate(ZonedDateTime.now());
            formRepository.update(forUpdate);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public void withdrawForm(Long formId) throws ApplicationException {
        try {
            CgpNoiForm forUpdate = formRepository.find(formId);
            Validate.isTrue(
                    forUpdate.getFormSet().getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                    "Only the owner can withdraw a form.");
            Validate.isTrue(forUpdate.getActiveRecord(), "Only the most recent version can be withdrawn.");
            Validate.isTrue(Arrays.asList(Status.Draft, Status.Submitted).contains(forUpdate.getStatus()),
                    "Can not withdraw form unless it is in Draft or Submitted status.");

            // reset most recent to active record
            if (forUpdate.getFormSet().getForms().size() > 1) {
                CgpNoiForm previous = findPreviousForm(forUpdate.getFormSet());
                previous.setActiveRecord(true);
                previous.setLastUpdatedDate(ZonedDateTime.now());
                setFinalFormStatus(previous);
                formRepository.update(previous);
            }

            // delete the form
            formRepository.delete(forUpdate);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }


    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public void addAttachment(Long formId, Attachment attachment) throws ApplicationException {
        try {
            CgpNoiForm forUpdate = formRepository.find(formId);

            // validate form state
            Validate.isTrue(
                    forUpdate.getFormSet().getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                    "Only the owner can add an attachment.");
            Validate.isTrue(
                    forUpdate.getActiveRecord(),
                    "Only the most recent version can have attachments added.");
            Validate.isTrue(
                    Status.Draft.equals(forUpdate.getStatus()),
                    "Can not add an attachment to a form with non-draft status.");
            // validate attachment
            Validate.notEmpty(attachment.getName(), "Attachment name is required.");
            Validate.notNull(attachment.getCategory(), "Attachment category is required.");
            Validate.isTrue(
                    attachment.getData().exists() && FileUtils.sizeOf(attachment.getData()) > 1,
                    "Attachment data can't be empty.");
            // add the attachment
            Attachment toAdd = new Attachment(forUpdate, attachment.getName(), attachment.getCategory(),
                    attachment.getData(), ZonedDateTime.now());
            forUpdate.getAttachments().add(toAdd);
            attachmentRepository.save(toAdd);
            formRepository.update(forUpdate);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public void addAttachments(Long formId, List<Attachment> attachments) throws ApplicationException {
        if (attachments == null) {
            return;
        }
        for (Attachment attachment : attachments) {
            try {
                addAttachment(formId, attachment);
            } catch (ApplicationException e) {
                throw ApplicationException.asApplicationException(e, e.getErrorCode(), String.format("Issue uploading attachment: %s.  %s", attachment.getName(), e.getMessage()));
            }
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName})
    public void removeAttachment(Long attachmentId) throws ApplicationException {
        try {
            // validate attachment state
            Attachment toRemove = attachmentRepository.find(attachmentId);
            Validate.notNull(toRemove, "Could not find attachment with this ID");
            // validate form state
            CgpNoiForm forUpdate = toRemove.getForm();
            Validate.isTrue(
                    forUpdate.getFormSet().getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                    "Only the owner can remove an attachment.");
            Validate.isTrue(
                    forUpdate.getActiveRecord(),
                    "Only the most recent version can have attachments removed.");
            Validate.isTrue(
                    Status.Draft.equals(forUpdate.getStatus()),
                    "Can not remove an attachment to a form with non-draft status.");
            // remove the attachment
            forUpdate.getAttachments().remove(toRemove);
            attachmentRepository.delete(toRemove);
            formRepository.update(forUpdate);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.helpdeskRoleName,
            ApplicationSecurityUtils.regAuthRoleName})
    public void certifyForm(Long formId, String activityId) throws ApplicationException {
        try {
            // validate for certification
            CgpNoiForm forCertification = formRepository.find(formId);
            CgpNoiFormSet formSet = forCertification.getFormSet();
            Validate.isTrue(forCertification.getActiveRecord(), "Only the most recent version can be certified.");
            Validate.isTrue(Status.Draft.equals(forCertification.getStatus()), "Only drafts can be certified.");
            if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.regAuth)) {
                Validate.isTrue(
                        Phase.Terminate.equals(forCertification.getPhase()),
                        "RAs can only certify terminations/discontinuations.");
                activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
                forCertification.setCromerrActivityId(activityId);
            } else {
                Validate.isTrue(
                        forCertification.getFormSet().getOwner().equals(applicationSecurityUtils.getCurrentUserId()),
                        "Only the owner can certify an NOI/LEW.");
            }
            Validate.notEmpty(activityId, "CROMERR activity ID must be created in advance.");

            // assign a NPDES ID if it hasn't already been created
            if (StringUtils.isEmpty(formSet.getNpdesId())) {
                formSet.setNpdesId(generateNpdesId(formSet.getMasterPermitNumber()));
                formSetRepository.update(formSet);
            }

            // update the form
            forCertification.setStatus(Status.Submitted);
            forCertification.setCertifiedDate(ZonedDateTime.now());
            forCertification.setLastUpdatedDate(ZonedDateTime.now());
            forCertification.setReviewExpiration(getReviewExpiriation(forCertification));
            forCertification.setCromerrActivityId(activityId);
            forCertification.getFormData().getOperatorInformation().setCertifier(assembler.getCurrentUserAsContact());
            if (FormType.Notice_Of_Intent.equals(forCertification.getType())) {
                validationHelper.validateNoiForCertification(forCertification);
            } else {
                validationHelper.validateLewForCertification(forCertification);
            }

            // add CoR as an attachment
            Attachment toAdd = copyOfRecordGeneratorService.generateCorFromForm(forCertification);
            forCertification.getAttachments().add(toAdd);
            attachmentRepository.save(toAdd);

            // avoid ConcurrentModificationException by iterating over a non-hibernate backed list
            List<Attachment> attachments = new ArrayList<>(forCertification.getAttachments());
            // get all of the attachment content
            for (Attachment a : attachments) {
                a.setData(attachmentRepository.findData(a.getId()));
            }
            // sign all of the attachments
            cromerrService.signAttachments(forCertification, attachments);
            for (Attachment a : attachments) {
                a.setData(null);
                attachmentRepository.deleteData(a); // remove the blob since it's in CROMERR now
                attachmentRepository.update(a); // save the cromerr document ID from certification
            }
            // update the form
            formRepository.update(forCertification);

            // send notifications
            //get copy of record
            Document attachment = new Document();
            for (Attachment a : forCertification.getAttachments()) {
                if (AttachmentCategory.CoR.equals(a.getCategory())) {
                    attachment.setName(a.getName());
                    attachment.setContent(retrieveAttachmentData(a.getId()));
                    break; // there is only one
                }
            }
            notificationHelper.sendCertificationToCertifier(forCertification, attachment);
            if (FormType.Notice_Of_Intent.equals(forCertification.getType()) && !Phase.Terminate.equals(forCertification.getPhase())) {
                notificationHelper.sendCertificationToServices(forCertification, attachment);
                if (Phase.New.equals(forCertification.getPhase())) {
                    notificationHelper.sendCertificationToRegulatoryAuthority(forCertification, attachment);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }


    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.regAuthRoleName})
    public void denyForm(Long formId, String reason) throws ApplicationException {
        try {
            CgpNoiForm toDeny = formRepository.find(formId);
            Validate.isTrue(toDeny.getActiveRecord(), "Only the most recent version can be denied.");
            Validate.isTrue(Arrays.asList(Status.Submitted, Status.OnHold).contains(toDeny.getStatus()),
                    "Can not deny a form unless it has been submitted or is on hold.");

            // reset the attachments to be certified again later
            List<Attachment> attachments = new ArrayList<>(toDeny.getAttachments());
            for (Attachment a : attachments) {
                if (!AttachmentCategory.CoR.equals(a.getCategory())) {
                    a.setData(retrieveAttachmentData(a.getId()));
                    a.setCromerrAttachmentId(null);
                    attachmentRepository.update(a);
                } else {
                    // remove the CoR since it's not longer valid
                    toDeny.getAttachments().remove(a);
                    attachmentRepository.delete(a);
                }
            }

            // update the form
            toDeny.setStatus(Status.Draft);
            toDeny.setCertifiedDate(null);
            toDeny.setCromerrActivityId(null);
            toDeny.setLastUpdatedDate(ZonedDateTime.now());
            formRepository.update(toDeny);
            if (FormType.Notice_Of_Intent.equals(toDeny.getType())) {
                notificationHelper.sendRejectedByRegulatoryAuthority(toDeny, reason);
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.regAuthRoleName})
    public void holdForm(Long formId) throws ApplicationException {
        try {
            CgpNoiForm toHold = formRepository.find(formId);
            Validate.isTrue(toHold.getActiveRecord(), "Only the most recent version can be held.");
            Validate.isTrue(
                    Status.Submitted.equals(toHold.getStatus()),
                    "Can not hold a form unless it has been submitted.");
            toHold.setStatus(Status.OnHold);
            toHold.setLastUpdatedDate(ZonedDateTime.now());
            formRepository.update(toHold);
            if (FormType.Notice_Of_Intent.equals(toHold.getType())) {
                notificationHelper.sendOnHoldByRegulatoryAuthority(toHold);
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.regAuthRoleName})
    public void releaseForm(Long formId) throws ApplicationException {
        try {
            CgpNoiForm toRelease = formRepository.find(formId);
            Validate.isTrue(toRelease.getActiveRecord(), "Only the most recent version can be released.");
            Validate.isTrue(
                    Status.OnHold.equals(toRelease.getStatus()),
                    "Can not release a form unless it has been held.");
            toRelease.setStatus(Status.Submitted);
            toRelease.setLastUpdatedDate(ZonedDateTime.now());
            formRepository.update(toRelease);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }


    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.helpdeskRoleName})
    public void submitForm(Long formId) throws ApplicationException {
        try {
            // validate
            CgpNoiForm toSubmit = formRepository.find(formId);
            CgpNoiFormSet formSet = toSubmit.getFormSet();
            Validate.isTrue(toSubmit.getActiveRecord(), "Only the most recent version can be submitted.");
            Validate.isTrue(
                    Status.Draft.equals(toSubmit.getStatus()),
                    "Only drafts can be submitted.");

            // assign a NPDES ID if it hasn't already been created
            if (StringUtils.isEmpty(formSet.getNpdesId())) {
                formSet.setNpdesId(generateNpdesId(formSet.getMasterPermitNumber()));
                formSetRepository.update(formSet);
            }

            // update the form
            toSubmit.setStatus(Status.Submitted);
            toSubmit.setLastUpdatedDate(ZonedDateTime.now());
            toSubmit.setReviewExpiration(getReviewExpiriation(toSubmit));
            if (FormType.Notice_Of_Intent.equals(toSubmit.getType())) {
                validationHelper.validateNoiForSubmission(toSubmit);
            } else {
                validationHelper.validateLewForSubmission(toSubmit);
            }
            formRepository.update(toSubmit);

            // send notifications
            if (FormType.Notice_Of_Intent.equals(toSubmit.getType()) && !Phase.Terminate.equals(toSubmit.getPhase())) {
                //get copy of record
                Document attachment = new Document();
                for (Attachment a : toSubmit.getAttachments()) {
                    if (AttachmentCategory.CoR.equals(a.getCategory())) {
                        attachment.setName(a.getName());
                        attachment.setContent(retrieveAttachmentData(a.getId()));
                        break; // there is only one
                    }
                }
                notificationHelper.sendCertificationToServices(toSubmit, attachment);
                if (Phase.New.equals(toSubmit.getPhase())) {
                    notificationHelper.sendCertificationToRegulatoryAuthority(toSubmit, attachment);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.systemRoleName})
    public void activateForm(Long formId) throws ApplicationException {
        try {
            // activate the form
            CgpNoiForm toActivate = formRepository.find(formId);
            Validate.isTrue(
                    Status.Submitted.equals(toActivate.getStatus()),
                    "Can not activate a form unless it has been submitted.");
            Validate.isTrue(
                    ZonedDateTime.now().compareTo(toActivate.getReviewExpiration()) > 0,
                    "Can not activate a form unless the review expiration has expired.");
            toActivate.setSubmittedDate(ZonedDateTime.now());
            toActivate.setLastUpdatedDate(ZonedDateTime.now());
            setFinalFormStatus(toActivate);
            formRepository.update(toActivate);

            // archive the previous form
            CgpNoiForm previous = toActivate.getFormSet().getForms().size() > 1 ?
                    findPreviousForm(toActivate.getFormSet()) :
                    null;
            if (previous != null) {
                previous.setStatus(Status.Archived);
                formRepository.update(previous);
            }


            // find the CoR for notifications
            Document attachment = new Document();
            for (Attachment a : toActivate.getAttachments()) {
                if (AttachmentCategory.CoR.equals(a.getCategory())) {
                    attachment.setName(a.getName());
                    attachment.setContent(retrieveAttachmentData(a.getId()));
                    break; // there is only one
                }
            }

            // send notifications
            notificationHelper.sendAcceptedByIcis(toActivate, attachment);
            if (FormType.Notice_Of_Intent.equals(toActivate.getType()) && !Phase.Terminate.equals(toActivate.getPhase())) {
                if (hasTierDesignation(toActivate)) {
                    notificationHelper.sendTierDesignation(toActivate);
                }
                if (hasAdjacentWaters(toActivate)) {
                    notificationHelper.sendAdjacentUsWaters(toActivate);
                }
                if (hasTreatmentChemicals(toActivate)) {
                    notificationHelper.sendChemicalTreatment(toActivate);
                }
                if (hasImpairedWaters(toActivate)) {
                    notificationHelper.sendImpairedUsWaters(toActivate);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }

    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.systemRoleName})
    public void distributeForm(Long formId) throws ApplicationException {
        try {
            CgpNoiForm toDistribute = formRepository.find(formId);
            Validate.isTrue(
                    ApplicationUtils.FINAL_STATUSES.contains(toDistribute.getStatus()),
                    "Can not distribute a form unless it has been activated.");
            CgpNoiForm previous = toDistribute.getFormSet().getForms().size() > 1 ?
                    findPreviousForm(toDistribute.getFormSet()) :
                    null;
            toDistribute.setNodeTransactionStatus(TransactionStatus.UNKNOWN);
            icisSubmissionService.submitToIcisNpdesDataflow(toDistribute, previous);
            formRepository.update(toDistribute);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void notifyAbandonedDrafts() throws ApplicationException {
        try {
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            criteria.setStatus(Status.Draft);
            criteria.setCreatedTo(ZonedDateTime.now().minusDays(30));
            List<CgpNoiForm> forms = formRepository.findAll(criteria);
            for (CgpNoiForm form : forms) {
                notificationHelper.sendAbandonedDrafts(form);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CgpNoiForm retrieveForm(Long formId) throws ApplicationException {
        try {
            return formRepository.find(formId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CgpNoiFormSet retrieveFormSet(Long formSetId) throws ApplicationException {
        try {
            return formSetRepository.find(formSetId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CgpNoiFormSet> retrieveFormSet(CgpNoiFormSetSearchCriteria criteria) throws ApplicationException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<CgpNoiForm> retrieveForms(CgpNoiFormSearchCriteria criteria) throws ApplicationException {
        try {
            validateCgpNoiFormSearchCriteria(criteria);
            return formRepository.findAll(criteria);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CgpNoiFormSearchResult retrieveSearchResult(DataTableCriteria<CgpNoiFormSearchCriteria> search) throws ApplicationException {
        try {
            CgpNoiFormSearchCriteria criteria = search.getCriteria();
            validateCgpNoiFormSearchCriteria(criteria);
            return formRepository.findPaginated(search);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName,
            ApplicationSecurityUtils.regAuthRoleName,
            ApplicationSecurityUtils.systemRoleName})
    public File retrieveAttachmentData(Long attachmentId) throws ApplicationException {
        try {
            // validate attachment state
            Attachment toRetrieve = attachmentRepository.find(attachmentId);
            Validate.notNull(toRetrieve, "Could not find attachment with this ID");
            if (StringUtils.isEmpty(toRetrieve.getCromerrAttachmentId())) {
                return attachmentRepository.findData(attachmentId);
            } else {
                return cromerrService.retrieveAttachmentData(toRetrieve.getForm().getCromerrActivityId(),
                        toRetrieve.getCromerrAttachmentId());
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public Attachment retrieveAttachment(Long attachmentId) throws ApplicationException {
        try {
            return attachmentRepository.find(attachmentId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Attachment> retrieveAttachments(Long formId) throws ApplicationException {
        return attachmentRepository.findAll(formId);
    }

    @Override
    @Transactional(readOnly = true)
    public CgpNoiForm retrievePublicForm(Long id) throws ApplicationException {
        try {
            Validate.notNull(id, "Form ID is required.");
            CgpNoiForm result = formRepository.find(id);
            Validate.isTrue(ApplicationUtils.PUBLIC_STATUSES.contains(result.getStatus()),
                    "This form does not have an appropriate status for public retrieval.");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CgpNoiForm> retrievePublicForms(CgpNoiFormSearchCriteria criteria) throws ApplicationException {
        try {
            Validate.isTrue(criteria.getStatus() == null || !Status.Draft.equals(criteria.getStatus()),
                    "Draft NOIs are not searchable.");
            criteria.setPublicSearch(true);
            return formRepository.findAll(criteria);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Attachment retrievePublicAttachment(Long id) throws ApplicationException {
        try {
            Attachment toRetrieve = attachmentRepository.find(id);
            Validate.notNull(toRetrieve, "Could not find attachment with this ID");
            CgpNoiForm form = toRetrieve.getForm();
            Validate.isTrue(ApplicationUtils.PUBLIC_STATUSES.contains(form.getStatus()),
                    "This form does not have an appropriate status for public retrieval.");
            return toRetrieve;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public File retrievePublicAttachmentData(Long formId, Long attachmentId) throws ApplicationException {
        try {
            // validate attachment state
            Attachment toRetrieve = attachmentRepository.find(attachmentId);
            Validate.notNull(toRetrieve, "Could not find attachment with this ID");
            Validate.isTrue(
                    formId.equals(toRetrieve.getForm().getId()),
                    "Attachment does not belong to this form.");
            Validate.isTrue(
                    !Status.Draft.equals(toRetrieve.getForm().getStatus()),
                    "Can not retrieve non-public attachments.");
            if (StringUtils.isEmpty(toRetrieve.getCromerrAttachmentId())) {
                return attachmentRepository.findData(attachmentId);
            } else {
                return cromerrService.retrieveAttachmentData(toRetrieve.getForm().getCromerrActivityId(),
                        toRetrieve.getCromerrAttachmentId());
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void inviteUser(String role, Contact contact) throws ApplicationException {
        try {
            String email = contact.getEmail();
            String firstName = contact.getFirstName();
            String lastName = contact.getLastName();
            Validate.notEmpty(email, "Email address is required.");
            Validate.notEmpty(firstName, "First name address is required.");
            Validate.notEmpty(lastName, "Last name is required.");
            Validate.notEmpty(role, "Intended user role is required.");
            notificationHelper.sendInvite(email, firstName, lastName, role);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void sendIcisTransactionFailure(CgpNoiForm form, List<Document> documents, String statusDetail) throws  ApplicationException {
        try {
            Validate.notEmpty(form.getNodeTransactionId(),
                    "Transaction ID is required");
            Validate.isTrue(
                    TransactionStatus.FAILED.equals(form.getNodeTransactionStatus()),
                    "Transaction Status must be FAILED to proceed to notification.");
            notificationHelper.sendIcisError(form, documents, statusDetail);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    String generateNpdesId(String masterPermitNumber) {
        NpdesSequence seq = referenceService.retrieveNextNpdesSequence(masterPermitNumber);
        String result = masterPermitNumber.substring(0, masterPermitNumber.length() - 3) + seq.getNpdesAlphaStart();
        char[] str = seq.getNpdesAlphaStart().toCharArray();
        seq.setNpdesAlphaStart(NpdesSequence.incrementNpdesSeq(str));
        referenceService.updateNpdesSequence(seq);
        return result;
    }

    String determineMasterPermitNumber(CgpNoiForm form) {
        String masterGeneralPermitNumber = null;
        // find the mgp rule for this state
        Validate.notEmpty(form.getFormData().getProjectSiteInformation().getSiteStateCode(), "State code is required");
        String stateCode = form.getFormData().getProjectSiteInformation().getSiteStateCode();
        MgpRule mgpRule = referenceService.retrieveRule(stateCode);
        logger.info(String.format("found rule %s, for form with ID %s", mgpRule, form.getId()));
        if (mgpRule == null) {
            throw new IllegalArgumentException(String.format("Could not find MGP rule for state %s", stateCode));
        }
        Validate.notNull(
                form.getFormData().getProjectSiteInformation().getSiteIndianCountry(),
                "Indian country status is required.");
        Boolean indianCountry = form.getFormData().getProjectSiteInformation().getSiteIndianCountry();
        if (indianCountry) {
            masterGeneralPermitNumber = mgpRule.getIndianCountryMgpNumber();
        } else {
            Validate.notNull(
                    form.getFormData().getOperatorInformation().getOperatorFederal(),
                    "Federal operator indicator is required.");
            Boolean federalOperatorIndicator = form.getFormData().getOperatorInformation().getOperatorFederal();
            if ((federalOperatorIndicator && mgpRule.getFederalFacilityEligibleOutsideIndianCountry()) ||
                    (!federalOperatorIndicator && mgpRule.getNonFederalfacilityEligibleOutsideIndianCountry())) {
                masterGeneralPermitNumber = mgpRule.getMgpNumber();
            }
        }

        // find if there is an override for this tribe
        if (indianCountry) {
            Validate.notEmpty(
                    form.getFormData().getProjectSiteInformation().getSiteIndianCountryLands(),
                    "Indian tribal code is required.");
            String tribalLandName = form.getFormData().getProjectSiteInformation().getSiteIndianCountryLands();
            Tribe t = referenceService.retrieveTribeByLandNameAndStateCode(tribalLandName, stateCode);
            TribalOverride override = referenceService.retrieveOverride(t.getTribalCode(), stateCode);
            logger.info(String.format("found override %s, for form with ID %s", override, form.getId()));
            if (override != null) {
                masterGeneralPermitNumber = override.getMgpNumber();
            }
        }
        return masterGeneralPermitNumber;

    }

    void updateFormDataIndex(CgpNoiForm form) {
        BeanUtils.copyProperties(form.getFormData().getOperatorInformation(), form.getIndex());
        BeanUtils.copyProperties(form.getFormData().getProjectSiteInformation(), form.getIndex());
        if (form.getFormData().getOperatorInformation().getPreparer() != null) {
            form.getIndex().setPreparer(form.getFormData().getOperatorInformation().getPreparer().getUserId());
        }
        if (form.getFormData().getOperatorInformation().getCertifier() != null) {
            form.getIndex().setCertifier(form.getFormData().getOperatorInformation().getCertifier().getUserId());
        }
    }

    boolean requiresReview(CgpNoiForm form) {
        try {
            if (typesThatRequireReview.contains(form.getType()) && phasesThatRequireReview.contains(form.getPhase())) {
                if (Phase.New.equals(form.getPhase())) {
                    return true;
                } else if (Phase.Change.equals(form.getPhase())) {
                    CgpNoiForm previous = findPreviousForm(form.getFormSet());
                    if (FormType.Notice_Of_Intent.equals(form.getType())) {
                        return !form.getFormData().getHistoricPreservation()
                                .sameValueAs(previous.getFormData().getHistoricPreservation())
                                || !form.getFormData().getEndangeredSpeciesProtectionInformation()
                                .sameValueAs(previous.getFormData().getEndangeredSpeciesProtectionInformation())
                                || !form.getFormData().getChemicalTreatmentInformation()
                                .sameValueAs(previous.getFormData().getChemicalTreatmentInformation())
                                || form.getFormData().getDischargeInformation().hasNewReceivingWater(previous.getFormData().getDischargeInformation())
                                || BooleanUtils.isTrue(form.getFormData().getProjectSiteInformation().getSiteStructureDemolitionBefore1980())
                                != (BooleanUtils.isTrue(previous.getFormData().getProjectSiteInformation().getSiteStructureDemolitionBefore1980()))
                                || form.getFormData().getProjectSiteInformation().getSiteAreaDisturbed()
                                .subtract(previous.getFormData().getProjectSiteInformation().getSiteAreaDisturbed())
                                .compareTo(new BigDecimal("1")) > 0;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            logger.warn("Could not determine if form requires review, so defaulting to true.", e);
            return true;
        }
    }

    ZonedDateTime getReviewExpiriation(CgpNoiForm form) {
        Integer holdSeconds = Integer.parseInt(oecaGeneralConfiguration.get("holdPeriodSeconds"));
        return requiresReview(form) ?
                ZonedDateTime.now().plus(holdSeconds, ChronoUnit.SECONDS)
                : ZonedDateTime.now();
    }

    void cloneAttachments(CgpNoiForm toClone, CgpNoiForm newForm) {
        // clone the attachments
        for (Attachment a : toClone.getAttachments()) {
            // don't clone the CoR, though
            if (!AttachmentCategory.CoR.equals(a.getCategory())) {
                Attachment toAdd = new Attachment();
                toAdd.setForm(newForm);
                toAdd.setCreatedDate(ZonedDateTime.now());
                toAdd.setName(a.getName());
                toAdd.setSize(a.getSize());
                toAdd.setCategory(a.getCategory());
                toAdd.setData(retrieveAttachmentData(a.getId()));
                newForm.getAttachments().add(toAdd);
                attachmentRepository.save(toAdd);
            }
        }
    }

    Boolean hasTierDesignation(CgpNoiForm form) {
        if (!CollectionUtils.isEmpty(form.getFormData().getDischargeInformation().getDischargePoints())) {
            for (PointOfDischarge pod : form.getFormData().getDischargeInformation().getDischargePoints()) {
                if (pod.getTier() != null && !Tier.NA.equals(pod.getTier())) {
                    return true;
                }
            }
        }
        return false;
    }

    Boolean hasAdjacentWaters(CgpNoiForm form) {
        return BooleanUtils.isTrue(form.getFormData().getDischargeInformation().getDischargeUSWatersWithin50Feet());
    }

    Boolean hasTreatmentChemicals(CgpNoiForm form) {
        return BooleanUtils.isTrue(form.getFormData().getChemicalTreatmentInformation().getPolymersFlocculantsOtherTreatmentChemicals());
    }

    Boolean hasImpairedWaters(CgpNoiForm form) {
        if (!CollectionUtils.isEmpty(form.getFormData().getDischargeInformation().getDischargePoints())) {
            for (PointOfDischarge pod : form.getFormData().getDischargeInformation().getDischargePoints()) {
                if (pod.getFirstWater() != null
                        && !CollectionUtils.isEmpty(pod.getFirstWater().getPollutants())
                        && CollectionUtils.exists(pod.getFirstWater().getPollutants(), o -> ((Pollutant) o).getImpaired())) {
                    return true;
                }
            }
        }
        return false;
    }

    CgpNoiForm findPreviousForm(CgpNoiFormSet formSet) {
        List<CgpNoiForm> previousForms = new ArrayList<>(formSet.getForms());
        Validate.isTrue(previousForms.size() > 1, "There is only one form.");
        previousForms.sort(Comparator.comparing(CgpNoiForm::getLastUpdatedDate));
        return previousForms.get(previousForms.size() - 2);
    }

    void setFinalFormStatus(CgpNoiForm form) {
        if (Phase.Terminate.equals(form.getPhase())) {
            if (FormType.Notice_Of_Intent.equals(form.getType())) {
                form.setStatus(Status.Terminated);
            } else {
                form.setStatus(Status.Discontinued);
            }
        } else {
            form.setStatus(Status.Active);
        }
    }

    void validateCgpNoiFormSearchCriteria(CgpNoiFormSearchCriteria criteria) {
        if (BooleanUtils.isTrue(criteria.getRegulatoryAuthoritySearch())) {
            Validate.isTrue(applicationSecurityUtils.hasRole(ApplicationSecurityUtils.regAuth),
                    "Regulatory authority search requires the RA role.");
            String region = applicationSecurityUtils.getCurrentApplicationUser().getClientId();
            if (!StringUtils.isEmpty(region)) {
                List<String> stateCodes = new ArrayList<>();
                try {
                    List<State> states = referenceService.retrieveStates(Integer.parseInt(region));
                    for (State state : states) {
                        stateCodes.add(state.getStateCode());
                    }
                    criteria.setSiteStateCodes(stateCodes);
                } catch (NumberFormatException e) {
                    logger.warn("Region was not in the expected format: " + region);
                }
            }
        }
    }
}
