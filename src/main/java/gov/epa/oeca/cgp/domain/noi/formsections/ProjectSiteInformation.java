package gov.epa.oeca.cgp.domain.noi.formsections;

import com.fasterxml.jackson.annotation.JsonFormat;
import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author dfladung
 */
public class ProjectSiteInformation extends BaseValueObject {

    public static final List<String> horizontalReferenceDatumOptions = Arrays.asList("NAD 27", "NAD 83", "WGS 84");
    public static final List<String> siteConstructionTypeOptions = Arrays.asList("Single-Family Residential",
            "Multi-Family Residential", "Commercial", "Industrial", "Institutional", "Highway or Road", "Utility");

    private static final long serialVersionUID = 1L;

    String siteName;
    String siteAddress;
    String siteAddress2;
    String siteCity;
    String siteStateCode;
    String siteZipCode;
    String siteCounty;
    Location siteLocation;
    Boolean siteIndianCountry;
    String siteIndianCountryLands;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone="EST")
    Date siteProjectStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone="EST")
    Date siteProjectEnd;
    BigDecimal siteAreaDisturbed;
    List<String> siteConstructionTypes = new ArrayList<>();
    Boolean siteStructureDemolitionBefore1980;
    Boolean siteStructureDemolitionBefore198010kSquareFeet;
    Boolean sitePreDevelopmentAgricultural;
    Boolean siteEarthDisturbingActivitiesOccurrence;
    Boolean siteEmergencyRelated;
    Boolean sitePreviousNpdesPermit;
    String sitePreviousNpdesPermitId;
    Boolean siteCgpAuthorizationConfirmation;
    Boolean siteIndianCulturalProperty;
    String siteIndianCulturalPropertyTribe;
    String siteTerminationReason;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProjectSiteInformation that = (ProjectSiteInformation) o;

        return new EqualsBuilder()
                .append(siteName, that.siteName)
                .append(siteAddress, that.siteAddress)
                .append(siteAddress2, that.siteAddress2)
                .append(siteCity, that.siteCity)
                .append(siteStateCode, that.siteStateCode)
                .append(siteZipCode, that.siteZipCode)
                .append(siteCounty, that.siteCounty)
                .append(siteLocation, that.siteLocation)
                .append(siteIndianCountry, that.siteIndianCountry)
                .append(siteIndianCountryLands, that.siteIndianCountryLands)
                .append(siteProjectStart, that.siteProjectStart)
                .append(siteProjectEnd, that.siteProjectEnd)
                .append(siteAreaDisturbed, that.siteAreaDisturbed)
                .append(siteConstructionTypes, that.siteConstructionTypes)
                .append(siteStructureDemolitionBefore1980, that.siteStructureDemolitionBefore1980)
                .append(siteStructureDemolitionBefore198010kSquareFeet, that.siteStructureDemolitionBefore198010kSquareFeet)
                .append(sitePreDevelopmentAgricultural, that.sitePreDevelopmentAgricultural)
                .append(siteEarthDisturbingActivitiesOccurrence, that.siteEarthDisturbingActivitiesOccurrence)
                .append(siteEmergencyRelated, that.siteEmergencyRelated)
                .append(sitePreviousNpdesPermit, that.sitePreviousNpdesPermit)
                .append(sitePreviousNpdesPermitId, that.sitePreviousNpdesPermitId)
                .append(siteCgpAuthorizationConfirmation, that.siteCgpAuthorizationConfirmation)
                .isEquals();
    }

    @Override
    public String toString() {
        return "ProjectSiteInformation{" +
                "siteName='" + siteName + '\'' +
                ", siteAddress='" + siteAddress + '\'' +
                ", siteCity='" + siteCity + '\'' +
                ", siteStateCode='" + siteStateCode + '\'' +
                ", siteZipCode='" + siteZipCode + '\'' +
                ", siteCounty='" + siteCounty + '\'' +
                ", siteLocation=" + siteLocation +
                "} " + super.toString();
    }

    public Boolean getSiteCgpAuthorizationConfirmation() {
        return siteCgpAuthorizationConfirmation;
    }

    public void setSiteCgpAuthorizationConfirmation(Boolean siteCgpAuthorizationConfirmation) {
        this.siteCgpAuthorizationConfirmation = siteCgpAuthorizationConfirmation;
    }

    public String getSiteAddress2() {
        return siteAddress2;
    }

    public void setSiteAddress2(String siteAddress2) {
        this.siteAddress2 = siteAddress2;
    }

    public Location getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(Location siteLocation) {
        this.siteLocation = siteLocation;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSiteCity() {
        return siteCity;
    }

    public void setSiteCity(String siteCity) {
        this.siteCity = siteCity;
    }

    public String getSiteStateCode() {
        return siteStateCode;
    }

    public void setSiteStateCode(String siteStateCode) {
        this.siteStateCode = siteStateCode;
    }

    public String getSiteZipCode() {
        return siteZipCode;
    }

    public void setSiteZipCode(String siteZipCode) {
        this.siteZipCode = siteZipCode;
    }

    public String getSiteCounty() {
        return siteCounty;
    }

    public void setSiteCounty(String siteCounty) {
        this.siteCounty = siteCounty;
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

    public Date getSiteProjectStart() {
        return siteProjectStart;
    }

    public void setSiteProjectStart(Date siteProjectStart) {
        this.siteProjectStart = siteProjectStart;
    }

    public Date getSiteProjectEnd() {
        return siteProjectEnd;
    }

    public void setSiteProjectEnd(Date siteProjectEnd) {
        this.siteProjectEnd = siteProjectEnd;
    }

    public BigDecimal getSiteAreaDisturbed() {
        return siteAreaDisturbed;
    }

    public void setSiteAreaDisturbed(BigDecimal siteAreaDisturbed) {
        this.siteAreaDisturbed = siteAreaDisturbed;
    }

    public List<String> getSiteConstructionTypes() {
        return siteConstructionTypes;
    }

    public void setSiteConstructionTypes(List<String> siteConstructionTypes) {
        this.siteConstructionTypes = siteConstructionTypes;
    }

    public Boolean getSiteStructureDemolitionBefore1980() {
        return siteStructureDemolitionBefore1980;
    }

    public void setSiteStructureDemolitionBefore1980(Boolean siteStructureDemolitionBefore1980) {
        this.siteStructureDemolitionBefore1980 = siteStructureDemolitionBefore1980;
    }

    public Boolean getSiteStructureDemolitionBefore198010kSquareFeet() {
        return siteStructureDemolitionBefore198010kSquareFeet;
    }

    public void setSiteStructureDemolitionBefore198010kSquareFeet(Boolean siteStructureDemolitionBefore198010kSquareFeet) {
        this.siteStructureDemolitionBefore198010kSquareFeet = siteStructureDemolitionBefore198010kSquareFeet;
    }

    public Boolean getSitePreDevelopmentAgricultural() {
        return sitePreDevelopmentAgricultural;
    }

    public void setSitePreDevelopmentAgricultural(Boolean sitePreDevelopmentAgricultural) {
        this.sitePreDevelopmentAgricultural = sitePreDevelopmentAgricultural;
    }

    public Boolean getSiteEarthDisturbingActivitiesOccurrence() {
        return siteEarthDisturbingActivitiesOccurrence;
    }

    public void setSiteEarthDisturbingActivitiesOccurrence(Boolean siteEarthDisturbingActivitiesOccurrence) {
        this.siteEarthDisturbingActivitiesOccurrence = siteEarthDisturbingActivitiesOccurrence;
    }

    public Boolean getSiteEmergencyRelated() {
        return siteEmergencyRelated;
    }

    public void setSiteEmergencyRelated(Boolean siteEmergencyRelated) {
        this.siteEmergencyRelated = siteEmergencyRelated;
    }

    public Boolean getSitePreviousNpdesPermit() {
        return sitePreviousNpdesPermit;
    }

    public void setSitePreviousNpdesPermit(Boolean sitePreviousNpdesPermit) {
        this.sitePreviousNpdesPermit = sitePreviousNpdesPermit;
    }

    public String getSitePreviousNpdesPermitId() {
        return sitePreviousNpdesPermitId;
    }

    public void setSitePreviousNpdesPermitId(String sitePreviousNpdesPermitId) {
        this.sitePreviousNpdesPermitId = sitePreviousNpdesPermitId;
    }

    public String getSiteTerminationReason() {
        return siteTerminationReason;
    }

    public void setSiteTerminationReason(String siteTerminationReason) {
        this.siteTerminationReason = siteTerminationReason;
    }

    public Boolean getSiteIndianCulturalProperty() {
        return siteIndianCulturalProperty;
    }

    public void setSiteIndianCulturalProperty(Boolean siteIndianCulturalProperty) {
        this.siteIndianCulturalProperty = siteIndianCulturalProperty;
    }

    public String getSiteIndianCulturalPropertyTribe() {
        return siteIndianCulturalPropertyTribe;
    }

    public void setSiteIndianCulturalPropertyTribe(String siteIndianCulturalPropertyTribe) {
        this.siteIndianCulturalPropertyTribe = siteIndianCulturalPropertyTribe;
    }
}
