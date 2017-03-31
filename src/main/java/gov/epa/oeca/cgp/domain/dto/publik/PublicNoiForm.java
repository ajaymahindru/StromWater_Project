package gov.epa.oeca.cgp.domain.dto.publik;

import com.fasterxml.jackson.annotation.JsonFormat;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.domain.noi.Status;
import gov.epa.oeca.cgp.domain.noi.formsections.*;
import gov.epa.oeca.common.domain.BaseValueObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dfladung
 */
public class PublicNoiForm extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    Long id;
    String masterPermitNumber;
    String npdesId;
    String trackingNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime lastUpdatedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime reviewExpiration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime certifiedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime submittedDate;
    Status status;
    FormType type;

    ProjectSiteInformation projectSiteInformation;
    OperatorInformation operatorInformation;
    DischargeInformation dischargeInformation;
    ChemicalTreatmentInformation chemicalTreatmentInformation;
    StormwaterPollutionPreventionPlanInformation stormwaterPollutionPreventionPlanInformation;
    EndangeredSpeciesProtectionInformation endangeredSpeciesProtectionInformation;
    HistoricPreservation historicPreservation;
    LowErosivityWaiver lowErosivityWaiver;

    List<PublicAttachment> attachments;

    public PublicNoiForm() {
        attachments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMasterPermitNumber() {
        return masterPermitNumber;
    }

    public void setMasterPermitNumber(String masterPermitNumber) {
        this.masterPermitNumber = masterPermitNumber;
    }

    public String getNpdesId() {
        return npdesId;
    }

    public void setNpdesId(String npdesId) {
        this.npdesId = npdesId;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public ZonedDateTime getReviewExpiration() {
        return reviewExpiration;
    }

    public void setReviewExpiration(ZonedDateTime reviewExpiration) {
        this.reviewExpiration = reviewExpiration;
    }

    public ZonedDateTime getCertifiedDate() {
        return certifiedDate;
    }

    public void setCertifiedDate(ZonedDateTime certifiedDate) {
        this.certifiedDate = certifiedDate;
    }

    public ZonedDateTime getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(ZonedDateTime submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public FormType getType() {
        return type;
    }

    public void setType(FormType type) {
        this.type = type;
    }

    public ProjectSiteInformation getProjectSiteInformation() {
        return projectSiteInformation;
    }

    public void setProjectSiteInformation(ProjectSiteInformation projectSiteInformation) {
        this.projectSiteInformation = projectSiteInformation;
    }

    public OperatorInformation getOperatorInformation() {
        return operatorInformation;
    }

    public void setOperatorInformation(OperatorInformation operatorInformation) {
        this.operatorInformation = operatorInformation;
    }

    public DischargeInformation getDischargeInformation() {
        return dischargeInformation;
    }

    public void setDischargeInformation(DischargeInformation dischargeInformation) {
        this.dischargeInformation = dischargeInformation;
    }

    public ChemicalTreatmentInformation getChemicalTreatmentInformation() {
        return chemicalTreatmentInformation;
    }

    public void setChemicalTreatmentInformation(ChemicalTreatmentInformation chemicalTreatmentInformation) {
        this.chemicalTreatmentInformation = chemicalTreatmentInformation;
    }

    public StormwaterPollutionPreventionPlanInformation getStormwaterPollutionPreventionPlanInformation() {
        return stormwaterPollutionPreventionPlanInformation;
    }

    public void setStormwaterPollutionPreventionPlanInformation(StormwaterPollutionPreventionPlanInformation stormwaterPollutionPreventionPlanInformation) {
        this.stormwaterPollutionPreventionPlanInformation = stormwaterPollutionPreventionPlanInformation;
    }

    public EndangeredSpeciesProtectionInformation getEndangeredSpeciesProtectionInformation() {
        return endangeredSpeciesProtectionInformation;
    }

    public void setEndangeredSpeciesProtectionInformation(EndangeredSpeciesProtectionInformation endangeredSpeciesProtectionInformation) {
        this.endangeredSpeciesProtectionInformation = endangeredSpeciesProtectionInformation;
    }

    public HistoricPreservation getHistoricPreservation() {
        return historicPreservation;
    }

    public void setHistoricPreservation(HistoricPreservation historicPreservation) {
        this.historicPreservation = historicPreservation;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List<PublicAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<PublicAttachment> attachments) {
        this.attachments = attachments;
    }

    public LowErosivityWaiver getLowErosivityWaiver() {
        return lowErosivityWaiver;
    }

    public void setLowErosivityWaiver(LowErosivityWaiver lowErosivityWaiver) {
        this.lowErosivityWaiver = lowErosivityWaiver;
    }


}
