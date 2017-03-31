package gov.epa.oeca.cgp.domain.noi.formsections;

import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Maps to Endanged Species Protection.
 *
 * @author dfladung
 */
public class EndangeredSpeciesProtectionInformation extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    Boolean appendixDCriteriaMet;
    //option A, B, C, D, E, or F
    AppendixDCriterion criterion;
    //summary applies to any criterion selected
    String criteriaSelectionSummary;

    // option B
    String otherOperatorNpdesId;

    // option C
    String speciesAndHabitatInActionArea;
    String distanceFromSite;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EndangeredSpeciesProtectionInformation that = (EndangeredSpeciesProtectionInformation) o;

        return new EqualsBuilder()
                .append(appendixDCriteriaMet, that.appendixDCriteriaMet)
                .append(criterion, that.criterion)
                .append(criteriaSelectionSummary, that.criteriaSelectionSummary)
                .append(otherOperatorNpdesId, that.otherOperatorNpdesId)
                .append(speciesAndHabitatInActionArea, that.speciesAndHabitatInActionArea)
                .append(distanceFromSite, that.distanceFromSite)
                .isEquals();
    }

    public Boolean getAppendixDCriteriaMet() {
        return appendixDCriteriaMet;
    }

    public void setAppendixDCriteriaMet(Boolean appendixDCriteriaMet) {
        this.appendixDCriteriaMet = appendixDCriteriaMet;
    }

    public AppendixDCriterion getCriterion() {
        return criterion;
    }

    public void setCriterion(AppendixDCriterion criterion) {
        this.criterion = criterion;
    }

    public String getCriteriaSelectionSummary() {
        return criteriaSelectionSummary;
    }

    public void setCriteriaSelectionSummary(String criteriaSelectionSummary) {
        this.criteriaSelectionSummary = criteriaSelectionSummary;
    }

    public String getOtherOperatorNpdesId() {
        return otherOperatorNpdesId;
    }

    public void setOtherOperatorNpdesId(String otherOperatorNpdesId) {
        this.otherOperatorNpdesId = otherOperatorNpdesId;
    }

    public String getSpeciesAndHabitatInActionArea() {
        return speciesAndHabitatInActionArea;
    }

    public void setSpeciesAndHabitatInActionArea(String speciesAndHabitatInActionArea) {
        this.speciesAndHabitatInActionArea = speciesAndHabitatInActionArea;
    }

    public String getDistanceFromSite() {
        return distanceFromSite;
    }

    public void setDistanceFromSite(String distanceFromSite) {
        this.distanceFromSite = distanceFromSite;
    }

}
