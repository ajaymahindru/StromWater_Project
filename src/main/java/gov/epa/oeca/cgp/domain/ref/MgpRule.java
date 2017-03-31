package gov.epa.oeca.cgp.domain.ref;

import gov.epa.oeca.cgp.domain.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author dfladung
 */
@Entity
@Table(name = "cgp_ref_mgp",
        indexes = {@Index(name = "idx_mgp_rules", columnList = "state_code")})
public class MgpRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "region_code", length = 2, nullable = false)
    String regionCode;

    @Column(name = "state_code", length = 2, nullable = false)
    String stateCode;

    @Column(name = "non_fed_fac_eligible", length = 1, nullable = false)
    @Type(type = "yes_no")
    Boolean nonFederalfacilityEligibleOutsideIndianCountry;

    @Column(name = "fed_fac_eligible", length = 1, nullable = false)
    @Type(type = "yes_no")
    Boolean federalFacilityEligibleOutsideIndianCountry;

    @Column(name = "mgp_number")
    String mgpNumber;

    @Column(name = "ind_country_mgp_number")
    String indianCountryMgpNumber;

    @Override
    public String toString() {
        return "MgpRule{" +
                "regionCode='" + regionCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", nonFederalfacilityEligibleOutsideIndianCountry=" + nonFederalfacilityEligibleOutsideIndianCountry +
                ", federalFacilityEligibleOutsideIndianCountry=" + federalFacilityEligibleOutsideIndianCountry +
                ", mgpNumber='" + mgpNumber + '\'' +
                ", indianCountryMgpNumber='" + indianCountryMgpNumber + '\'' +
                "} " + super.toString();
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Boolean getFederalFacilityEligibleOutsideIndianCountry() {
        return federalFacilityEligibleOutsideIndianCountry;
    }

    public void setFederalFacilityEligibleOutsideIndianCountry(Boolean federalFacilityEligibleOutsideIndianCountry) {
        this.federalFacilityEligibleOutsideIndianCountry = federalFacilityEligibleOutsideIndianCountry;
    }

    public Boolean getNonFederalfacilityEligibleOutsideIndianCountry() {
        return nonFederalfacilityEligibleOutsideIndianCountry;
    }

    public void setNonFederalfacilityEligibleOutsideIndianCountry(Boolean nonFederalfacilityEligibleOutsideIndianCountry) {
        this.nonFederalfacilityEligibleOutsideIndianCountry = nonFederalfacilityEligibleOutsideIndianCountry;
    }

    public String getMgpNumber() {
        return mgpNumber;
    }

    public void setMgpNumber(String mgpNumber) {
        this.mgpNumber = mgpNumber;
    }

    public String getIndianCountryMgpNumber() {
        return indianCountryMgpNumber;
    }

    public void setIndianCountryMgpNumber(String indianCountryMgpNumber) {
        this.indianCountryMgpNumber = indianCountryMgpNumber;
    }
}
