package gov.epa.oeca.cgp.domain.noi.formsections;

import com.fasterxml.jackson.annotation.JsonFormat;
import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dfladung
 */
public class LowErosivityWaiver extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone="EST")
    Date lewProjectStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone="EST")
    Date lewProjectEnd;

    Boolean lewLessThan5Acres;
    Boolean lewRFactorLessThan5;

    BigDecimal lewAreaDisturbed;
    BigDecimal lewRFactor;
    String lewRFactorCalculationMethod;
    Boolean interimSiteStabilizationMeasures;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LowErosivityWaiver that = (LowErosivityWaiver) o;

        return new EqualsBuilder()
                .append(lewProjectStart, that.lewProjectStart)
                .append(lewProjectEnd, that.lewProjectEnd)
                .append(lewLessThan5Acres, that.lewLessThan5Acres)
                .append(lewRFactorLessThan5, that.lewRFactorLessThan5)
                .append(lewAreaDisturbed, that.lewAreaDisturbed)
                .append(lewRFactor, that.lewRFactor)
                .append(lewRFactorCalculationMethod, that.lewRFactorCalculationMethod)
                .append(interimSiteStabilizationMeasures, that.interimSiteStabilizationMeasures)
                .isEquals();
    }

    @Override
    public String toString() {
        return "LowErosivityWaiver{" +
                "lewProjectStart=" + lewProjectStart +
                ", lewProjectEnd=" + lewProjectEnd +
                ", lewLessThan5Acres=" + lewLessThan5Acres +
                ", lewRFactorLessThan5=" + lewRFactorLessThan5 +
                ", lewAreaDisturbed=" + lewAreaDisturbed +
                ", lewRFactor=" + lewRFactor +
                ", lewRFactorCalculationMethod='" + lewRFactorCalculationMethod + '\'' +
                ", interimSiteStabilizationMeasures=" + interimSiteStabilizationMeasures +
                "} " + super.toString();
    }

    public Date getLewProjectStart() {
        return lewProjectStart;
    }

    public void setLewProjectStart(Date lewProjectStart) {
        this.lewProjectStart = lewProjectStart;
    }

    public Date getLewProjectEnd() {
        return lewProjectEnd;
    }

    public void setLewProjectEnd(Date lewProjectEnd) {
        this.lewProjectEnd = lewProjectEnd;
    }

    public BigDecimal getLewAreaDisturbed() {
        return lewAreaDisturbed;
    }

    public void setLewAreaDisturbed(BigDecimal lewAreaDisturbed) {
        this.lewAreaDisturbed = lewAreaDisturbed;
    }

    public BigDecimal getLewRFactor() {
        return lewRFactor;
    }

    public void setLewRFactor(BigDecimal lewRFactor) {
        this.lewRFactor = lewRFactor;
    }

    public String getLewRFactorCalculationMethod() {
        return lewRFactorCalculationMethod;
    }

    public void setLewRFactorCalculationMethod(String lewRFactorCalculationMethod) {
        this.lewRFactorCalculationMethod = lewRFactorCalculationMethod;
    }

    public Boolean getInterimSiteStabilizationMeasures() {
        return interimSiteStabilizationMeasures;
    }

    public void setInterimSiteStabilizationMeasures(Boolean interimSiteStabilizationMeasures) {
        this.interimSiteStabilizationMeasures = interimSiteStabilizationMeasures;
    }

    public Boolean getLewLessThan5Acres() {
        return lewLessThan5Acres;
    }

    public void setLewLessThan5Acres(Boolean lewLessThan5Acres) {
        this.lewLessThan5Acres = lewLessThan5Acres;
    }

    public Boolean getLewRFactorLessThan5() {
        return lewRFactorLessThan5;
    }

    public void setLewRFactorLessThan5(Boolean lewRFactorLessThan5) {
        this.lewRFactorLessThan5 = lewRFactorLessThan5;
    }
}
