package gov.epa.oeca.cgp.domain.noi;

import gov.epa.oeca.cgp.domain.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * I know, this is weird. I really want to serialize/deserialize the form as JSON. However, we need to search a number
 * of elements, and we don't have a document database or ElasticSearch available to us. So, instead, I'm creating a
 * separate entity for searchable fields that will be synchronized on update using reflection.
 *
 * @author dfladung
 */
@Entity
@Table(name = "cgp_noi_form_data_index")
public class CgpNoiFormDataIndex extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "site_name", length = 255)
    String siteName;

    @Column(name = "site_state_code", length = 2)
    String siteStateCode;

    @Column(name = "site_city")
    String siteCity;

    @Column(name = "site_zip_code")
    String siteZipCode;

    @Column(name = "site_indian_country_ind", length = 1)
    @Type(type = "yes_no")
    Boolean siteIndianCountry;

    @Column(name = "site_indian_country_lands", length = 4000)
    String siteIndianCountryLands;

    @Column(name = "operator_name", length = 4000)
    String operatorName;

    @Column(name = "operator_federal_ind", length = 1)
    @Type(type = "yes_no")
    Boolean operatorFederal;

    @Column(name = "preparer")
    String preparer;

    @Column(name = "certifier")
    String certifier;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteStateCode() {
        return siteStateCode;
    }

    public void setSiteStateCode(String siteStateCode) {
        this.siteStateCode = siteStateCode;
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

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Boolean getOperatorFederal() {
        return operatorFederal;
    }

    public void setOperatorFederal(Boolean operatorFederal) {
        this.operatorFederal = operatorFederal;
    }

    public String getSiteCity() {
        return siteCity;
    }

    public void setSiteCity(String siteCity) {
        this.siteCity = siteCity;
    }

    public String getSiteZipCode() {
        return siteZipCode;
    }

    public void setSiteZipCode(String siteZipCode) {
        this.siteZipCode = siteZipCode;
    }

    public String getPreparer() {
        return preparer;
    }

    public void setPreparer(String preparer) {
        this.preparer = preparer;
    }

    public String getCertifier() {
        return certifier;
    }

    public void setCertifier(String certifier) {
        this.certifier = certifier;
    }
}
