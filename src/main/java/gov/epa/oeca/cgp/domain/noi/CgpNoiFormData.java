package gov.epa.oeca.cgp.domain.noi;

import gov.epa.oeca.cgp.domain.noi.formsections.*;
import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author dfladung
 */
public class CgpNoiFormData extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    ProjectSiteInformation projectSiteInformation;
    OperatorInformation operatorInformation;
    DischargeInformation dischargeInformation;
    ChemicalTreatmentInformation chemicalTreatmentInformation;
    StormwaterPollutionPreventionPlanInformation stormwaterPollutionPreventionPlanInformation;
    EndangeredSpeciesProtectionInformation endangeredSpeciesProtectionInformation;
    HistoricPreservation historicPreservation;
    LowErosivityWaiver lowErosivityWaiver;

    public CgpNoiFormData() {
        projectSiteInformation = new ProjectSiteInformation();
        operatorInformation = new OperatorInformation();
        dischargeInformation = new DischargeInformation();
        chemicalTreatmentInformation = new ChemicalTreatmentInformation();
        stormwaterPollutionPreventionPlanInformation = new StormwaterPollutionPreventionPlanInformation();
        endangeredSpeciesProtectionInformation = new EndangeredSpeciesProtectionInformation();
        historicPreservation = new HistoricPreservation();
        lowErosivityWaiver = new LowErosivityWaiver();
    }

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CgpNoiFormData that = (CgpNoiFormData) o;

        return new EqualsBuilder()
                .append(projectSiteInformation, that.projectSiteInformation)
                .append(operatorInformation, that.operatorInformation)
                .append(dischargeInformation, that.dischargeInformation)
                .append(chemicalTreatmentInformation, that.chemicalTreatmentInformation)
                .append(stormwaterPollutionPreventionPlanInformation, that.stormwaterPollutionPreventionPlanInformation)
                .append(endangeredSpeciesProtectionInformation, that.endangeredSpeciesProtectionInformation)
                .append(historicPreservation, that.historicPreservation)
                .append(lowErosivityWaiver, that.lowErosivityWaiver)
                .isEquals();
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

    public LowErosivityWaiver getLowErosivityWaiver() {
        return lowErosivityWaiver;
    }

    public void setLowErosivityWaiver(LowErosivityWaiver lowErosivityWaiver) {
        this.lowErosivityWaiver = lowErosivityWaiver;
    }
}
