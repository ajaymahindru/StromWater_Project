package gov.epa.oeca.cgp.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gov.epa.oeca.cgp.domain.noi.Source;
import gov.epa.oeca.cgp.domain.util.DatePickerZonedDateTimeDeserializer;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.domain.noi.Status;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author dfladung
 */
public class CgpNoiFormSearchCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private String owner;
    private String npdesId;
    private String masterGeneralPermit;
    private String trackingNumber;
    private FormType type;
    private Status status;
    private List<Status> statuses;
    private String operatorName;
    private String siteName;
    private Long siteRegion;
    private String siteStateCode;
    private List<String> siteStateCodes;
    private String siteCity;
    private String siteZipCode;
    private String siteCounty;
    private Boolean siteIndianCountry;
    private String siteIndianCountryLands;
    private Boolean operatorFederal;
    private ZonedDateTime reviewExpiration;
    @JsonDeserialize(using = DatePickerZonedDateTimeDeserializer.class)
    private ZonedDateTime submittedFrom;
    @JsonDeserialize(using = DatePickerZonedDateTimeDeserializer.class)
    private ZonedDateTime submittedTo;
    @JsonDeserialize(using = DatePickerZonedDateTimeDeserializer.class)
    private ZonedDateTime updatedFrom;
    @JsonDeserialize(using = DatePickerZonedDateTimeDeserializer.class)
    private ZonedDateTime updatedTo;
    @JsonDeserialize(using = DatePickerZonedDateTimeDeserializer.class)
    private ZonedDateTime createdFrom;
    @JsonDeserialize(using = DatePickerZonedDateTimeDeserializer.class)
    private ZonedDateTime createdTo;
    private Boolean publicSearch;
    private Boolean regulatoryAuthoritySearch;
    private Boolean activeRecord;
    private String associatedUser;
    private Boolean submittedToIcis;
    private Boolean icisSubmissionInProgress;
    private Long resultLimit;
    private Source source;

    public CgpNoiFormSearchCriteria() {

    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ZonedDateTime getSubmittedFrom() {
        return submittedFrom;
    }

    public void setSubmittedFrom(ZonedDateTime submittedFrom) {
        this.submittedFrom = submittedFrom;
    }

    public ZonedDateTime getSubmittedTo() {
        return submittedTo;
    }

    public void setSubmittedTo(ZonedDateTime submittedTo) {
        this.submittedTo = submittedTo;
    }

    public ZonedDateTime getUpdatedFrom() {
        return updatedFrom;
    }

    public void setUpdatedFrom(ZonedDateTime updatedFrom) {
        this.updatedFrom = updatedFrom;
    }

    public ZonedDateTime getUpdatedTo() {
        return updatedTo;
    }

    public void setUpdatedTo(ZonedDateTime updatedTo) {
        this.updatedTo = updatedTo;
    }

    public String getSiteZipCode() {
        return siteZipCode;
    }

    public void setSiteZipCode(String siteZipCode) {
        this.siteZipCode = siteZipCode;
    }

    public Long getSiteRegion() {
        return siteRegion;
    }

    public void setSiteRegion(Long siteRegion) {
        this.siteRegion = siteRegion;
    }

    public String getSiteStateCode() {
        return siteStateCode;
    }

    public void setSiteStateCode(String siteStateCode) {
        this.siteStateCode = siteStateCode;
    }

    public String getSiteCity() {
        return siteCity;
    }

    public void setSiteCity(String siteCity) {
        this.siteCity = siteCity;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getNpdesId() {
        return npdesId;
    }

    public void setNpdesId(String npdesId) {
        this.npdesId = npdesId;
    }

    public ZonedDateTime getReviewExpiration() {
        return reviewExpiration;
    }

    public void setReviewExpiration(ZonedDateTime reviewExpiration) {
        this.reviewExpiration = reviewExpiration;
    }

    public FormType getType() {
        return type;
    }

    public void setType(FormType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getPublicSearch() {
        return publicSearch;
    }

    public void setPublicSearch(Boolean publicSearch) {
        this.publicSearch = publicSearch;
    }

    public String getMasterGeneralPermit() {
        return masterGeneralPermit;
    }

    public void setMasterGeneralPermit(String masterGeneralPermit) {
        this.masterGeneralPermit = masterGeneralPermit;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Boolean getSiteIndianCountry() {
        return siteIndianCountry;
    }

    public void setSiteIndianCountry(Boolean siteIndianCountry) {
        this.siteIndianCountry = siteIndianCountry;
    }

    public String getSiteIndianCountryLands() {
        return siteIndianCountryLands;
    }

    public void setSiteIndianCountryLands(String siteIndianCountryLands) {
        this.siteIndianCountryLands = siteIndianCountryLands;
    }

    public Boolean getOperatorFederal() {
        return operatorFederal;
    }

    public void setOperatorFederal(Boolean operatorFederal) {
        this.operatorFederal = operatorFederal;
    }

    public List<String> getSiteStateCodes() {
        return siteStateCodes;
    }

    public void setSiteStateCodes(List<String> siteStateCodes) {
        this.siteStateCodes = siteStateCodes;
    }

    public Boolean getRegulatoryAuthoritySearch() {
        return regulatoryAuthoritySearch;
    }

    public void setRegulatoryAuthoritySearch(Boolean regulatoryAuthoritySearch) {
        this.regulatoryAuthoritySearch = regulatoryAuthoritySearch;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public ZonedDateTime getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(ZonedDateTime createdFrom) {
        this.createdFrom = createdFrom;
    }

    public ZonedDateTime getCreatedTo() {
        return createdTo;
    }

    public void setCreatedTo(ZonedDateTime createdTo) {
        this.createdTo = createdTo;
    }

    public Boolean getActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(Boolean activeRecord) {
        this.activeRecord = activeRecord;
    }

    public String getAssociatedUser() {
        return associatedUser;
    }

    public void setAssociatedUser(String associatedUser) {
        this.associatedUser = associatedUser;
    }

    public Boolean getSubmittedToIcis() {
        return submittedToIcis;
    }

    public void setSubmittedToIcis(Boolean submittedToIcis) {
        this.submittedToIcis = submittedToIcis;
    }

    public Boolean getIcisSubmissionInProgress() {
        return icisSubmissionInProgress;
    }

    public void setIcisSubmissionInProgress(Boolean icisSubmissionInProgress) {
        this.icisSubmissionInProgress = icisSubmissionInProgress;
    }

    public Long getResultLimit() {
        return resultLimit;
    }

    public void setResultLimit(Long resultLimit) {
        this.resultLimit = resultLimit;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getSiteCounty() {
        return siteCounty;
    }

    public void setSiteCounty(String siteCounty) {
        this.siteCounty = siteCounty;
    }

    @Override
    public String toString() {
        return "CgpNoiFormSearchCriteria{" +
                "owner='" + owner + '\'' +
                ", npdesId='" + npdesId + '\'' +
                ", masterGeneralPermit='" + masterGeneralPermit + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", siteName='" + siteName + '\'' +
                ", siteIndianCountry=" + siteIndianCountry +
                ", siteIndianCountryLands=" + siteIndianCountryLands +
                ", operatorFederal=" + operatorFederal +
                ", updatedFrom='" + updatedFrom + '\'' +
                "}";
    }
}
