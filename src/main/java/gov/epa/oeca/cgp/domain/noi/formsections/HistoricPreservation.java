package gov.epa.oeca.cgp.domain.noi.formsections;

import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author dfladung
 */
public class HistoricPreservation extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    Boolean screeningCompleted;
    Boolean appendexEStep1;
    Boolean appendexEStep2;
    Boolean appendexEStep3;
    Boolean appendexEStep4;
    String appendexEStep4Response;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HistoricPreservation that = (HistoricPreservation) o;

        return new EqualsBuilder()
                .append(screeningCompleted, that.screeningCompleted)
                .append(appendexEStep1, that.appendexEStep1)
                .append(appendexEStep2, that.appendexEStep2)
                .append(appendexEStep3, that.appendexEStep3)
                .append(appendexEStep4, that.appendexEStep4)
                .append(appendexEStep4Response, that.appendexEStep4Response)
                .isEquals();
    }

    public Boolean getScreeningCompleted() {
        return screeningCompleted;
    }

    public void setScreeningCompleted(Boolean screeningCompleted) {
        this.screeningCompleted = screeningCompleted;
    }

    public Boolean getAppendexEStep1() {
        return appendexEStep1;
    }

    public void setAppendexEStep1(Boolean appendexEStep1) {
        this.appendexEStep1 = appendexEStep1;
    }

    public Boolean getAppendexEStep2() {
        return appendexEStep2;
    }

    public void setAppendexEStep2(Boolean appendexEStep2) {
        this.appendexEStep2 = appendexEStep2;
    }

    public Boolean getAppendexEStep3() {
        return appendexEStep3;
    }

    public void setAppendexEStep3(Boolean appendexEStep3) {
        this.appendexEStep3 = appendexEStep3;
    }

    public Boolean getAppendexEStep4() {
        return appendexEStep4;
    }

    public void setAppendexEStep4(Boolean appendexEStep4) {
        this.appendexEStep4 = appendexEStep4;
    }

    public String getAppendexEStep4Response() {
        return appendexEStep4Response;
    }

    public void setAppendexEStep4Response(String appendexEStep4Response) {
        this.appendexEStep4Response = appendexEStep4Response;
    }
}
