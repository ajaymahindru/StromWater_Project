package gov.epa.oeca.cgp.domain.noi.formsections;

import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.List;

/**
 * @author dfladung
 */
public class ChemicalTreatmentInformation extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    Boolean polymersFlocculantsOtherTreatmentChemicals;
    Boolean cationicTreatmentChemicals;
    Boolean cationicTreatmentChemicalsAuthorization;
    List<String> treatmentChemicals;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ChemicalTreatmentInformation that = (ChemicalTreatmentInformation) o;

        return new EqualsBuilder()
                .append(polymersFlocculantsOtherTreatmentChemicals, that.polymersFlocculantsOtherTreatmentChemicals)
                .append(cationicTreatmentChemicals, that.cationicTreatmentChemicals)
                .append(cationicTreatmentChemicalsAuthorization, that.cationicTreatmentChemicalsAuthorization)
                .append(treatmentChemicals, that.treatmentChemicals)
                .isEquals();
    }


    public Boolean getPolymersFlocculantsOtherTreatmentChemicals() {
        return polymersFlocculantsOtherTreatmentChemicals;
    }

    public void setPolymersFlocculantsOtherTreatmentChemicals(Boolean polymersFlocculantsOtherTreatmentChemicals) {
        this.polymersFlocculantsOtherTreatmentChemicals = polymersFlocculantsOtherTreatmentChemicals;
    }

    public Boolean getCationicTreatmentChemicals() {
        return cationicTreatmentChemicals;
    }

    public void setCationicTreatmentChemicals(Boolean cationicTreatmentChemicals) {
        this.cationicTreatmentChemicals = cationicTreatmentChemicals;
    }

    public Boolean getCationicTreatmentChemicalsAuthorization() {
        return cationicTreatmentChemicalsAuthorization;
    }

    public void setCationicTreatmentChemicalsAuthorization(Boolean cationicTreatmentChemicalsAuthorization) {
        this.cationicTreatmentChemicalsAuthorization = cationicTreatmentChemicalsAuthorization;
    }

    public List<String> getTreatmentChemicals() {
        return treatmentChemicals;
    }

    public void setTreatmentChemicals(List<String> treatmentChemicals) {
        this.treatmentChemicals = treatmentChemicals;
    }
}
