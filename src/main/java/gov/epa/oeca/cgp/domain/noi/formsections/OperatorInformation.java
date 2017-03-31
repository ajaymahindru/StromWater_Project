package gov.epa.oeca.cgp.domain.noi.formsections;

import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author dfladung
 */
public class OperatorInformation extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    String operatorName;
    Boolean operatorFederal;
    String operatorAddress;
    String operatorAddress2;
    String operatorCity;
    String operatorStateCode;
    String operatorZipCode;
    String operatorCounty;
    Contact operatorPointOfContact;
    Contact preparer;
    Contact certifier;

    public OperatorInformation() {
        operatorPointOfContact = new Contact();
        preparer = new Contact();
        certifier = new Contact();
    }

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OperatorInformation that = (OperatorInformation) o;

        return new EqualsBuilder()
                .append(operatorName, that.operatorName)
                .append(operatorFederal, that.operatorFederal)
                .append(operatorAddress, that.operatorAddress)
                .append(operatorAddress2, that.operatorAddress2)
                .append(operatorCity, that.operatorCity)
                .append(operatorStateCode, that.operatorStateCode)
                .append(operatorZipCode, that.operatorZipCode)
                .append(operatorCounty, that.operatorCounty)
                .append(operatorPointOfContact, that.operatorPointOfContact)
                .append(preparer, that.preparer)
                .append(certifier, that.certifier)
                .isEquals();
    }


    @Override
    public String toString() {
        return "OperatorInformation{" +
                "operatorName='" + operatorName + '\'' +
                ", operatorFederal=" + operatorFederal +
                ", operatorAddress='" + operatorAddress + '\'' +
                ", operatorCity='" + operatorCity + '\'' +
                ", operatorStateCode='" + operatorStateCode + '\'' +
                ", operatorZipCode='" + operatorZipCode + '\'' +
                ", operatorCounty='" + operatorCounty + '\'' +
                ", operatorPointOfContact=" + operatorPointOfContact +
                ", preparer=" + preparer +
                ", certifier=" + certifier +
                "} " + super.toString();
    }

    public Contact getCertifier() {
        return certifier;
    }

    public void setCertifier(Contact certifier) {
        this.certifier = certifier;
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

    public String getOperatorAddress() {
        return operatorAddress;
    }

    public void setOperatorAddress(String operatorAddress) {
        this.operatorAddress = operatorAddress;
    }

    public String getOperatorAddress2() {
        return operatorAddress2;
    }

    public void setOperatorAddress2(String operatorAddress2) {
        this.operatorAddress2 = operatorAddress2;
    }

    public String getOperatorCity() {
        return operatorCity;
    }

    public void setOperatorCity(String operatorCity) {
        this.operatorCity = operatorCity;
    }

    public String getOperatorStateCode() {
        return operatorStateCode;
    }

    public void setOperatorStateCode(String operatorStateCode) {
        this.operatorStateCode = operatorStateCode;
    }

    public String getOperatorZipCode() {
        return operatorZipCode;
    }

    public void setOperatorZipCode(String operatorZipCode) {
        this.operatorZipCode = operatorZipCode;
    }

    public String getOperatorCounty() {
        return operatorCounty;
    }

    public void setOperatorCounty(String operatorCounty) {
        this.operatorCounty = operatorCounty;
    }

    public Contact getOperatorPointOfContact() {
        return operatorPointOfContact;
    }

    public void setOperatorPointOfContact(Contact operatorPointOfContact) {
        this.operatorPointOfContact = operatorPointOfContact;
    }

    public Contact getPreparer() {
        return preparer;
    }

    public void setPreparer(Contact preparer) {
        this.preparer = preparer;
    }


}
