package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.cgp.domain.dto.publik.PublicAttachment;
import gov.epa.oeca.cgp.domain.dto.publik.PublicNoiForm;
import gov.epa.oeca.cgp.domain.noi.*;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.security.ApplicationUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author dfladung
 */
@Component
public class CgpNoiFormAssembler {

    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;

    public CgpNoiForm assembleNewNoi(CgpNoiForm input) {
        return assembleNew(FormType.Notice_Of_Intent, Phase.New, input);
    }

    public CgpNoiForm assembleNoiChange(CgpNoiForm input) {
        CgpNoiForm result = assembleNew(FormType.Notice_Of_Intent, Phase.Change, input);
        result.getFormData().getOperatorInformation().setCertifier(null);
        return result;
    }

    public CgpNoiForm assembleNoiTermination(CgpNoiForm input, CgpNoiForm updatedForm) {
        CgpNoiForm result = assembleNew(FormType.Notice_Of_Intent, Phase.Terminate, input);
        if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.helpdesk)) {
            result.getFormData().getOperatorInformation().setCertifier(updatedForm.getFormData().getOperatorInformation().getCertifier());
            result.setCertifiedDate(updatedForm.getCertifiedDate());
        } else {
            result.getFormData().getOperatorInformation().setCertifier(null);
        }
        result.getFormData().getProjectSiteInformation().setSiteTerminationReason(updatedForm.getFormData().getProjectSiteInformation().getSiteTerminationReason());
        return result;
    }

    public CgpNoiForm assembleNewLew(CgpNoiForm input) {
        return assembleNew(FormType.Low_Erosivity_Waiver, Phase.New, input);
    }

    public CgpNoiForm assembleLewChange(CgpNoiForm input) {
        CgpNoiForm result = assembleNew(FormType.Low_Erosivity_Waiver, Phase.Change, input);
        result.getFormData().getOperatorInformation().setCertifier(null);
        return result;
    }

    public CgpNoiForm assembleLewDiscontinuation(CgpNoiForm input, CgpNoiForm updatedForm) {
        CgpNoiForm result = assembleNew(FormType.Low_Erosivity_Waiver, Phase.Terminate, input);
        if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.helpdesk)) {
            result.getFormData().getOperatorInformation().setCertifier(updatedForm.getFormData().getOperatorInformation().getCertifier());
            result.setCertifiedDate(updatedForm.getCertifiedDate());
        } else {
            result.getFormData().getOperatorInformation().setCertifier(null);
        }
        result.getFormData().getProjectSiteInformation().setSiteTerminationReason(updatedForm.getFormData().getProjectSiteInformation().getSiteTerminationReason());
        return result;
    }

    private CgpNoiForm assembleNew(FormType type, Phase phase, CgpNoiForm input) {
        CgpNoiForm newForm = new CgpNoiForm();
        newForm.setTrackingNumber(UUID.randomUUID().toString());
        newForm.setCreatedDate(ZonedDateTime.now());
        newForm.setLastUpdatedDate(ZonedDateTime.now());
        newForm.setStatus(Status.Draft);
        newForm.setType(type);
        newForm.setPhase(phase);
        newForm.setActiveRecord(true);
        newForm.setSource(getSource());
        newForm = assembleForm(newForm, input);
        if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.certifier, ApplicationSecurityUtils.preparer)) {
            newForm.getFormData().getOperatorInformation().setPreparer(getCurrentUserAsContact());
        }
        return newForm;
    }

    public Contact getCurrentUserAsContact() {
        ApplicationUser user = applicationSecurityUtils.getCurrentApplicationUser();
        Contact contact = new Contact();
        contact.setFirstName(user.getFirstName());
        contact.setLastName(user.getLastName());
        contact.setEmail(user.getEmail());
        contact.setMiddleInitial(user.getMiddleInitial());
        contact.setUserId(user.getUsername());
        contact.setPhone(user.getPhoneNumber());
        contact.setOrganization(user.getOrganization());
        contact.setTitle(StringUtils.isEmpty(user.getJobTitle()) ? user.getTitle() : user.getJobTitle());
        return contact;
    }

    public CgpNoiForm assembleForm(CgpNoiForm target, CgpNoiForm input) {
        switch (target.getType()) {
            case Notice_Of_Intent:
                BeanUtils.copyProperties(
                        input.getFormData().getProjectSiteInformation(),
                        target.getFormData().getProjectSiteInformation());
                BeanUtils.copyProperties(
                        input.getFormData().getOperatorInformation(),
                        target.getFormData().getOperatorInformation());
                BeanUtils.copyProperties(
                        input.getFormData().getChemicalTreatmentInformation(),
                        target.getFormData().getChemicalTreatmentInformation());
                BeanUtils.copyProperties(
                        input.getFormData().getEndangeredSpeciesProtectionInformation(),
                        target.getFormData().getEndangeredSpeciesProtectionInformation());
                BeanUtils.copyProperties(
                        input.getFormData().getHistoricPreservation(),
                        target.getFormData().getHistoricPreservation());
                BeanUtils.copyProperties(
                        input.getFormData().getStormwaterPollutionPreventionPlanInformation(),
                        target.getFormData().getStormwaterPollutionPreventionPlanInformation());
                BeanUtils.copyProperties(
                        input.getFormData().getDischargeInformation(),
                        target.getFormData().getDischargeInformation());
                break;
            case Low_Erosivity_Waiver:
                BeanUtils.copyProperties(
                        input.getFormData().getProjectSiteInformation(),
                        target.getFormData().getProjectSiteInformation());
                BeanUtils.copyProperties(
                        input.getFormData().getOperatorInformation(),
                        target.getFormData().getOperatorInformation());
                BeanUtils.copyProperties(
                        input.getFormData().getLowErosivityWaiver(),
                        target.getFormData().getLowErosivityWaiver());
                break;
            default:
                break;
        }
        if (applicationSecurityUtils.hasRole(ApplicationSecurityUtils.helpdesk)) {
            target.setCertifiedDate(input.getCertifiedDate());
        }
        return target;
    }

    public PublicNoiForm assemblePublicForm(CgpNoiForm form) {
        PublicNoiForm result = new PublicNoiForm();
        result.setId(form.getId());
        result.setMasterPermitNumber(form.getFormSet().getMasterPermitNumber());
        result.setNpdesId(form.getFormSet().getNpdesId());
        result.setTrackingNumber(form.getTrackingNumber());
        result.setCreatedDate(form.getCertifiedDate());
        result.setCertifiedDate(form.getCertifiedDate());
        result.setLastUpdatedDate(form.getLastUpdatedDate());
        result.setReviewExpiration(form.getReviewExpiration());
        result.setSubmittedDate(form.getReviewExpiration());
        result.setType(form.getType());
        result.setStatus(form.getStatus());

        result.setChemicalTreatmentInformation(form.getFormData().getChemicalTreatmentInformation());
        result.setDischargeInformation(form.getFormData().getDischargeInformation());
        result.setEndangeredSpeciesProtectionInformation(form.getFormData().getEndangeredSpeciesProtectionInformation());
        result.setHistoricPreservation(form.getFormData().getHistoricPreservation());
        result.setOperatorInformation(form.getFormData().getOperatorInformation());
        result.getOperatorInformation().setPreparer(null);
        result.getOperatorInformation().setCertifier(null);
        result.setProjectSiteInformation(form.getFormData().getProjectSiteInformation());
        result.setStormwaterPollutionPreventionPlanInformation(form.getFormData().getStormwaterPollutionPreventionPlanInformation());
        result.setLowErosivityWaiver(form.getFormData().getLowErosivityWaiver());

        // attachments
        for (Attachment att : form.getAttachments()) {
            PublicAttachment pa = new PublicAttachment();
            pa.setCreatedDate(att.getCreatedDate());
            pa.setName(att.getName());
            pa.setId(att.getId());
            pa.setCategory(att.getCategory());
            pa.setSize(att.getSize());
            result.getAttachments().add(pa);
        }
        return result;
    }

    public List<PublicNoiForm> assemblePublicForms(List<CgpNoiForm> forms) {
        List<PublicNoiForm> results = new ArrayList<>();
        for (CgpNoiForm form : forms) {
            results.add(assemblePublicForm(form));
        }
        return results;
    }

    Source getSource() {
        return applicationSecurityUtils.hasRole(ApplicationSecurityUtils.helpdesk) ? Source.Paper : Source.Electronic;
    }
}
