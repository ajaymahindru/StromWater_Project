package gov.epa.oeca.cgp.application;

import freemarker.template.Configuration;
import gov.epa.oeca.cgp.application.reference.ReferenceService;
import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.domain.noi.Phase;
import gov.epa.oeca.cgp.domain.ref.State;
import gov.epa.oeca.cgp.domain.ref.Subscriber;
import gov.epa.oeca.cgp.infrastructure.user.UserInformationService;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.registration.NewUserProfile;
import gov.epa.oeca.common.domain.registration.User;
import gov.epa.oeca.common.infrastructure.notification.NotificationService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

/**
 * @author dfladung
 */
@Component
public class CgpNoiFormNotificationHelper {

    private static final Logger logger = LoggerFactory.getLogger(CgpNoiFormNotificationHelper.class);
    private static final String defaultReason = "No reason provided.";

    @Autowired
    NotificationService notificationService;
    @Autowired
    Configuration freemarkerConfiguration;
    @Resource(name = "cgpExternalUrls")
    Map<String, String> cgpExternalUrls;
    @Resource(name = "additionalMailConfiguration")
    Map<String, String> additionalMailConfiguration;
    @Autowired
    UserInformationService userInformationService;
    @Autowired
    ReferenceService referenceService;
    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;
    @Autowired
    CgpNoiFormService cgpNoiFormService;

    String getNotificationPath(FormType type, Phase phase, String template) {
        return getNotificationPath(type, phase.toString().toLowerCase(), template);

    }

    String getNotificationPath(FormType type, String notificationType, String template) {
        String typePath = (FormType.Notice_Of_Intent.equals(type)) ? "noi" : "lew";
        return "notifications/" + typePath + "/" + notificationType + "/" + template;

    }

    public void sendRouteToCertifier(CgpNoiForm form, String certifierId) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            String to = userInformationService.retrieveCertifier(certifierId).getOrganization().getEmail();
            List<String> cc = Collections.singletonList(applicationSecurityUtils.getCurrentApplicationUser().getEmail());
            List<String> raEmails = getRaEmails(form);
            String ra = StringUtils.join(raEmails, ", ");

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());
            model.put("applicationLink", cgpExternalUrls.get("applicationLink"));
            model.put("eReporting", cgpExternalUrls.get("eReporting"));
            model.put("stormwaterCgp", cgpExternalUrls.get("stormwaterCgp"));
            model.put("raEmail", ra);
            model.put("netHelpCenter", cgpExternalUrls.get("netHelpCenter"));
            model.put("npdesEmail", additionalMailConfiguration.get("NpdesEmail"));
            String subjectTemplate = getNotificationPath(form.getType(), form.getPhase(), "route_to_certifier-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), form.getPhase(), "route_to_certifier-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Collections.singletonList(to), cc, null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendRejectedByCertifier(CgpNoiForm form, String reason) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            String to = form.getFormData().getOperatorInformation().getPreparer().getEmail();
            List<String> cc = Collections.singletonList(applicationSecurityUtils.getCurrentApplicationUser().getEmail());
            Contact certifier = new Contact();
            certifier.setFirstName(applicationSecurityUtils.getCurrentApplicationUser().getFirstName());
            certifier.setLastName(applicationSecurityUtils.getCurrentApplicationUser().getLastName());
            certifier.setEmail(applicationSecurityUtils.getCurrentApplicationUser().getEmail());

            List<String> raEmails = getRaEmails(form);
            String ra = StringUtils.join(raEmails, ", ");

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());
            model.put("certifier", certifier);
            model.put("reason", StringUtils.isEmpty(reason) ? defaultReason : reason);
            model.put("netHelpCenter", cgpExternalUrls.get("netHelpCenter"));
            model.put("npdesEmail", additionalMailConfiguration.get("NpdesEmail"));
            model.put("raEmail", ra);
            String subjectTemplate = getNotificationPath(form.getType(), form.getPhase(), "reject_by_certifier-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), form.getPhase(), "reject_by_certifier-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Collections.singletonList(to), cc, null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendCertificationToCertifier(CgpNoiForm form, Document attachment) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            String to = applicationSecurityUtils.getCurrentApplicationUser().getEmail();
            List<String> cc = Collections.singletonList(form.getFormData().getOperatorInformation().getPreparer().getEmail());
            List<String> raEmails = getRaEmails(form);
            String ra = StringUtils.join(raEmails, ", ");

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());
            model.put("raEmail", ra);
            model.put("eReporting", cgpExternalUrls.get("eReporting"));
            model.put("stormwaterCgp", cgpExternalUrls.get("stormwaterCgp"));
            model.put("netHelpCenter", cgpExternalUrls.get("netHelpCenter"));
            model.put("npdesEmail", additionalMailConfiguration.get("NpdesEmail"));
            model.put("eEnterprisePortal", cgpExternalUrls.get("eEnterpriseEnvironment"));
            String subjectTemplate = getNotificationPath(form.getType(), form.getPhase(), "certified_to_certifier-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), form.getPhase(), "certified_to_certifier-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);

            // send the notification
            notificationService.sendNotificationWithAttachments(from, Collections.singletonList(to), cc, null,
                    subject, body, Collections.singletonList(attachment));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendCertificationToRegulatoryAuthority(CgpNoiForm form, Document attachment) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            List<String> to = getRaEmails(form);
            if (to.size() < 1) {
                logger.warn("Could not determine RA for form: " + form.getId());
                return;
            }
            Contact certifier = new Contact();
            certifier.setFirstName(applicationSecurityUtils.getCurrentApplicationUser().getFirstName());
            certifier.setLastName(applicationSecurityUtils.getCurrentApplicationUser().getLastName());
            certifier.setEmail(applicationSecurityUtils.getCurrentApplicationUser().getEmail());

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());
            model.put("certifier", certifier);
            model.put("applicationLink", cgpExternalUrls.get("applicationLink"));
            String subjectTemplate = getNotificationPath(form.getType(), form.getPhase(), "certified_to_ra-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), form.getPhase(), "certified_to_ra-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendNotificationWithAttachments(from, to, null, null,
                    subject, body, Collections.singletonList(attachment));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendCertificationToServices(CgpNoiForm form, Document attachment) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            List<String> raEmails = getRaEmails(form);
            String ra = StringUtils.join(raEmails, ", ");
            String esaCriterion = form.getFormData().getEndangeredSpeciesProtectionInformation().getCriterion().getValue();
            String latitude = form.getFormData().getProjectSiteInformation().getSiteLocation().getLatitude().toString();
            String longitude = form.getFormData().getProjectSiteInformation().getSiteLocation().getLongitude().toString();

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());
            model.put("raEmail", ra);
            model.put("esaCriterion", esaCriterion);
            model.put("latitude", latitude);
            model.put("longitude", longitude);
            model.put("stormwaterCgp", cgpExternalUrls.get("stormwaterCgp"));
            String subjectTemplate = getNotificationPath(form.getType(), form.getPhase(), "certified_to_services-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), form.getPhase(), "certified_to_services-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            //get subscribers' emails
            List<Subscriber> subscribers = referenceService.retrieveSubscribersByMgp(form.getFormSet().getMasterPermitNumber());
            List<String> to = new ArrayList<>();
            for (Subscriber s : subscribers) {
                to.add(s.getEmail());
            }

            //send the notification
            notificationService.sendNotificationWithAttachments(from, to, null, null,
                    subject, body, Collections.singletonList(attachment));

        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendOnHoldByRegulatoryAuthority(CgpNoiForm form) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            List<String> raEmails = getRaEmails(form);
            String ra = StringUtils.join(raEmails, ", ");
            List<String> cc = CollectionUtils.isEmpty(raEmails) ? null : raEmails;
            String certifier = form.getFormData().getOperatorInformation().getCertifier().getEmail();
            String preparer = form.getFormData().getOperatorInformation().getPreparer().getEmail();

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());
            model.put("raEmail", ra);
            String subjectTemplate = getNotificationPath(form.getType(), form.getPhase(), "on_hold_by_ra-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), form.getPhase(), "on_hold_by_ra-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Arrays.asList(certifier, preparer), cc,
                    null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendRejectedByRegulatoryAuthority(CgpNoiForm form, String reason) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            List<String> raEmails = getRaEmails(form);
            String ra = StringUtils.join(raEmails, ", ");
            List<String> cc = CollectionUtils.isEmpty(raEmails) ? null : raEmails;
            String certifier = form.getFormData().getOperatorInformation().getCertifier().getEmail();
            String preparer = form.getFormData().getOperatorInformation().getPreparer().getEmail();

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());
            model.put("raEmail", ra);
            model.put("reason", StringUtils.isEmpty(reason) ? defaultReason : reason);
            String subjectTemplate = getNotificationPath(form.getType(), form.getPhase(), "reject_by_ra-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), form.getPhase(), "reject_by_ra-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Arrays.asList(certifier, preparer), cc,
                    null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendAcceptedByIcis(CgpNoiForm form, Document attachment) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            List<String> raEmails = getRaEmails(form);
            String ra = StringUtils.join(raEmails, ", ");
            List<String> cc = CollectionUtils.isEmpty(raEmails) ? null : raEmails;
            String certifier = form.getFormData().getOperatorInformation().getCertifier().getEmail();
            String preparer = form.getFormData().getOperatorInformation().getPreparer().getEmail();
            List<String> bcc = new ArrayList<>();
            if (!Phase.Change.equals(form.getPhase())) {
                //get bcc emails for all non-Change forms
                List<Subscriber> subscribers = referenceService.retrieveSubscribersByCatSubcat("bcc", "accepted_by_icis");
                for (Subscriber s : subscribers) {
                    bcc.add(s.getEmail());
                }
            }
            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());
            model.put("raEmail", ra);
            model.put("applicationLink", cgpExternalUrls.get("applicationLink"));
            model.put("stormwaterCgp", cgpExternalUrls.get("stormwaterCgp"));
            model.put("submittedDate", form.getSubmittedDate().toLocalDate());
            model.put("netHelpCenter", cgpExternalUrls.get("netHelpCenter"));
            model.put("npdesEmail", additionalMailConfiguration.get("NpdesEmail"));
            String subjectTemplate = getNotificationPath(form.getType(), form.getPhase(), "accepted_by_icis-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), form.getPhase(), "accepted_by_icis-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendNotificationWithAttachments(from, Arrays.asList(certifier, preparer), cc,
                    bcc, subject, body, Collections.singletonList(attachment));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendAbandonedDrafts(CgpNoiForm form) throws ApplicationException {
        try {
            if (StringUtils.isEmpty(form.getFormData().getProjectSiteInformation().getSiteName())
                    || StringUtils.isEmpty(form.getFormData().getOperatorInformation().getPreparer().getFirstName())
                    || StringUtils.isEmpty(form.getFormData().getOperatorInformation().getPreparer().getLastName())) {
                // can't send the notification
                return;
            }
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            String owner = form.getFormSet().getOwner();
            String ownerEmail = userInformationService.retrievePrimaryOrganization(owner).getEmail();
            User ownerProfile = userInformationService.retrieveUserById(owner).get(0).getUser();
            List<String> raEmails = getRaEmails(form);
            String ra = StringUtils.join(raEmails, ", ");

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("ownerFirstName", ownerProfile.getFirstName());
            model.put("ownerLastName", ownerProfile.getLastName());
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());
            model.put("applicationLink", cgpExternalUrls.get("applicationLink"));
            model.put("eReporting", cgpExternalUrls.get("eReporting"));
            model.put("netHelpCenter", cgpExternalUrls.get("netHelpCenter"));
            model.put("npdesEmail", additionalMailConfiguration.get("NpdesEmail"));
            model.put("raEmail", ra);
            String subjectTemplate = getNotificationPath(form.getType(), form.getPhase(), "abandoned_draft-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), form.getPhase(), "abandoned_draft-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Collections.singletonList(ownerEmail), null,
                    null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendTierDesignation(CgpNoiForm form) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            String certifier = form.getFormData().getOperatorInformation().getCertifier().getEmail();
            String preparer = form.getFormData().getOperatorInformation().getPreparer().getEmail();

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());

            String subjectTemplate = getNotificationPath(form.getType(), "tier", "tier_designation-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), "tier", "tier_designation-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Arrays.asList(certifier, preparer), null,
                    null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendAdjacentUsWaters(CgpNoiForm form) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            String certifier = form.getFormData().getOperatorInformation().getCertifier().getEmail();
            String preparer = form.getFormData().getOperatorInformation().getPreparer().getEmail();

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());

            String subjectTemplate = getNotificationPath(form.getType(), "waters", "adjacent_us_waters-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), "waters", "adjacent_us_waters-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Arrays.asList(certifier, preparer), null,
                    null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendImpairedUsWaters(CgpNoiForm form) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            String certifier = form.getFormData().getOperatorInformation().getCertifier().getEmail();
            String preparer = form.getFormData().getOperatorInformation().getPreparer().getEmail();

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());

            String subjectTemplate = getNotificationPath(form.getType(), "waters", "impaired_waters-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), "waters", "impaired_waters-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Arrays.asList(certifier, preparer), null,
                    null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendChemicalTreatment(CgpNoiForm form) throws ApplicationException {
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            String certifier = form.getFormData().getOperatorInformation().getCertifier().getEmail();
            String preparer = form.getFormData().getOperatorInformation().getPreparer().getEmail();

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("currentDate", LocalDate.now().toString());

            String subjectTemplate = getNotificationPath(form.getType(), "chemicals", "chemical_treatment-subject.fm");
            String bodyTemplate = getNotificationPath(form.getType(), "chemicals", "chemical_treatment-body.fm");
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Arrays.asList(certifier, preparer), null,
                    null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendInvite(String email, String firstName, String lastName, String role) throws ApplicationException {
        try {

            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");

            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("invitedByFirstName", applicationSecurityUtils.getCurrentApplicationUser().getFirstName());
            model.put("invitedByLastName", applicationSecurityUtils.getCurrentApplicationUser().getLastName());
            model.put("invitedById", applicationSecurityUtils.getCurrentUserId());
            model.put("firstName", firstName);
            model.put("lastName", lastName);
            model.put("role", role);
            model.put("currentDate", LocalDate.now().toString());
            model.put("applicationLink", cgpExternalUrls.get("applicationLink"));
            model.put("eReporting", cgpExternalUrls.get("eReporting"));
            String subjectTemplate = "notifications/invite/invite-subject.fm";
            String bodyTemplate = "notifications/invite/invite-body.fm";
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            // send the notification
            notificationService.sendGenericNotification(from, Collections.singletonList(email), null, null, subject, body);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendIcisError(CgpNoiForm form, List<Document> txDocs, String statusDetail) throws ApplicationException{
        try {
            // get the basic mail information
            String from = additionalMailConfiguration.get("DoNotReplyEmail");
            //get subscribers' emails
            List<Subscriber> subscribers = referenceService.retrieveSubscribersByCatSubcat("icis", "failed");
            List<String> to = new ArrayList<>();
            for (Subscriber s : subscribers) {
                to.add(s.getEmail());
            }
            // merge model
            Map<String, Object> model = new HashMap<>();
            model.put("formType", form.getType().getValue());
            model.put("siteName", form.getFormData().getProjectSiteInformation().getSiteName());
            model.put("npdesId", form.getFormSet().getNpdesId());
            model.put("currentDate", LocalDate.now().toString());
            model.put("formPhase", form.getPhase());
            model.put("txStatus", form.getNodeTransactionStatus());
            model.put("txId", form.getNodeTransactionId());
            model.put("displayError", StringUtils.isNotEmpty(statusDetail));
            model.put("statusDetail", statusDetail);
            String subjectTemplate = "notifications/icis/icis-fail-subject.fm";
            String bodyTemplate = "notifications/icis/icis-fail-body.fm";
            String subject = mergeTemplate(subjectTemplate, model);
            String body = mergeTemplate(bodyTemplate, model);
            notificationService.sendNotificationWithAttachments(from, to, null,
                    null, subject, body, txDocs);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }


    String mergeTemplate(String template, Map<String, Object> model) {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration
                    .getTemplate(template), model);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_Messaging, e.getMessage());
        }
    }

    Integer getRegion(CgpNoiForm form) {
        State state = referenceService.retrieveState(form.getFormData().getProjectSiteInformation().getSiteStateCode());
        return StringUtils.isEmpty(state.getRegionCode()) ?
                null :
                Integer.parseInt(StringUtils.removeStart(state.getRegionCode(), "0"));
    }

    List<String> getRaEmails(CgpNoiForm form) {
        Integer region = getRegion(form);
        if (region == null) {
            return new ArrayList<>();
        }

        List<NewUserProfile> ras = userInformationService.retrieveRegionalAuthority(region);
        Set<String> emails = new HashSet<>();
        for (NewUserProfile ra : ras) {
            emails.add(ra.getOrganization().getEmail());
        }
        return new ArrayList<>(emails);
    }
}
