package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.application.reference.ReferenceService;
import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.AttachmentCategory;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.domain.noi.formsections.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dfladung
 */
@Component
public class CgpNoiFormValidationHelper {

    private static final String ineligibleTreatmentChemicals = "You are ineligible for coverage under this permit unless you notify your " +
            "applicable EPA Regional Office in advance, and the EPA office authorizes coverage under this permit after you " +
            "have included appropriate controls and implementation procedures designed to ensure that your use of cationic " +
            "treatment chemicals will not lead to a violation of water quality standards.";
    private static final String ineligibleSwwpp = "You may not submit your NOI for coverage under the CGP if you have " +
            "not developed your Stormwater Pollution Prevention Plan (SWPPP). For information about what information is " +
            "required in your SWPPP, see Part 7 of the permit.";
    private static final String ineligibleEsp = "You must meet one of the criteria listed in Appendix D to be eligible " +
            "to be covered by this permit.";
    private static final String ineligibleHp = "You must complete the screening process as required in Appendix E to be " +
            "eligible to be covered by this permit.";
    @Autowired
    ReferenceService referenceService;

    public void validateNoiEligibility(CgpNoiForm form) throws IllegalArgumentException {
        Validate.notNull(form.getFormData(), "Form data is required.");
        Validate.notNull(form.getFormData().getProjectSiteInformation(), "Project site information is required.");
        Validate.notNull(form.getFormData().getOperatorInformation(), "Operator information is required.");
        Validate.notNull(form.getFormData().getChemicalTreatmentInformation(), "Chemical treatment information is required.");
        Validate.notNull(form.getFormData().getStormwaterPollutionPreventionPlanInformation(), "SWWPP information is required.");
        Validate.notNull(form.getFormData().getEndangeredSpeciesProtectionInformation(), "ESP information is required.");
        Validate.notNull(form.getFormData().getHistoricPreservation(), "Historic Preservation information is required.");

        ProjectSiteInformation psi = form.getFormData().getProjectSiteInformation();
        OperatorInformation oi = form.getFormData().getOperatorInformation();
        ChemicalTreatmentInformation cti = form.getFormData().getChemicalTreatmentInformation();
        StormwaterPollutionPreventionPlanInformation swwpp = form.getFormData().getStormwaterPollutionPreventionPlanInformation();
        EndangeredSpeciesProtectionInformation esp = form.getFormData().getEndangeredSpeciesProtectionInformation();
        HistoricPreservation hp = form.getFormData().getHistoricPreservation();

        // state
        Validate.notEmpty(psi.getSiteStateCode(), "Project site state code is required.");

        // indian country
        Validate.notNull(psi.getSiteIndianCountry(), "Indian country indicator is required.");
        if (psi.getSiteIndianCountry()) {
            Validate.notEmpty(psi.getSiteIndianCountryLands(), "Indian country lands is required.");
        } else {
            Validate.notNull(oi.getOperatorFederal(), "Federal operator indicator is required.");
        }

        // previous npdes
        Validate.notNull(psi.getSitePreviousNpdesPermit(), "Previous NPDES permit indicator is required.");
        if (psi.getSitePreviousNpdesPermit()) {
            Validate.notEmpty(psi.getSitePreviousNpdesPermitId(), "Previous NPDES permit ID is required.");
        }

        // chemicals
        Validate.notNull(cti.getPolymersFlocculantsOtherTreatmentChemicals(), "Construction site chemicals indicator is required.");
        if (BooleanUtils.isTrue(cti.getPolymersFlocculantsOtherTreatmentChemicals())) {
            Validate.notNull(
                    cti.getCationicTreatmentChemicals(),
                    "Cationic treatment chemicals indicator is required.");
            if (cti.getCationicTreatmentChemicals()) {
                Validate.notNull(
                        cti.getCationicTreatmentChemicalsAuthorization(),
                        "Cationic treatment chemicals authorization is required.");
                Validate.isTrue(
                        cti.getCationicTreatmentChemicalsAuthorization(), ineligibleTreatmentChemicals);
            }
        }

        // swwpp
        Validate.notNull(swwpp.getPreparationInAdvance(), "SWWPP preparation indicator is required.");
        Validate.isTrue(swwpp.getPreparationInAdvance(), ineligibleSwwpp);

        // esp
        Validate.notNull(esp.getAppendixDCriteriaMet(), "Appendix D criteria indicator is required.");
        Validate.isTrue(esp.getAppendixDCriteriaMet(), ineligibleEsp);

        // historic preservation
        Validate.notNull(hp.getScreeningCompleted(), "Historic preservation screening indicator is required.");
        Validate.isTrue(hp.getScreeningCompleted(), ineligibleHp);

        // cgp authorization confirmation
        Validate.notNull(psi.getSiteCgpAuthorizationConfirmation(), "CGP authorization indicator is required.");
    }

    public void validateNoiForCertification(CgpNoiForm form) throws IllegalArgumentException {
        validateNoiForSubmission(form);
        Validate.notEmpty(form.getCromerrActivityId(), "CROMERR activity ID is required.");
    }

    public void validateNoiForSubmission(CgpNoiForm form) throws IllegalArgumentException {
        validateNoiEligibility(form);
        validateOperatorInformation(form);
        validateProjectSiteInformation(form);
        validateDischargeInformation(form);
        validateSwpppInformation(form);
        validateEndangeredSpeciesProtection(form);
        validateHistoricPreservation(form);
        validateChemicalTreatmentInformation(form);
        Validate.isTrue(
                StringUtils.isNotEmpty(form.getFormData().getOperatorInformation().getPreparer().getFirstName()) &&
                        StringUtils.isNotEmpty(form.getFormData().getOperatorInformation().getPreparer().getLastName()),
                "Preparer information is required.");
        Validate.isTrue(
                StringUtils.isNotEmpty(form.getFormData().getOperatorInformation().getCertifier().getFirstName()) &&
                        StringUtils.isNotEmpty(form.getFormData().getOperatorInformation().getCertifier().getLastName()),
                "Certifier information is required.");
    }


    public void validateLewEligibility(CgpNoiForm form) throws IllegalArgumentException {
        Validate.notNull(form.getFormData(), "Form data is required.");
        Validate.notNull(form.getFormData().getProjectSiteInformation(), "Project site information is required.");
        Validate.notNull(form.getFormData().getOperatorInformation(), "Operator information is required.");
        Validate.notNull(form.getFormData().getLowErosivityWaiver(), "Low Erosivity Waiver information is required.");

        ProjectSiteInformation psi = form.getFormData().getProjectSiteInformation();
        OperatorInformation oi = form.getFormData().getOperatorInformation();
        LowErosivityWaiver lew = form.getFormData().getLowErosivityWaiver();

        // state
        Validate.notEmpty(psi.getSiteStateCode(), "Project site state code is required.");

        // indian country
        Validate.notNull(psi.getSiteIndianCountry(), "Indian country indicator is required.");
        Validate.notNull(oi.getOperatorFederal(), "Federal operator indicator is required.");

        // lew
        Validate.notNull(lew.getLewLessThan5Acres(), "Indicator of construction activity acreage is required.");
        Validate.notNull(lew.getLewRFactorLessThan5(), "Indicator of erosivity factor is required.");
    }

    public void validateLewForCertification(CgpNoiForm form) throws IllegalArgumentException {
        validateLewForSubmission(form);
        Validate.notEmpty(form.getCromerrActivityId(), "CROMERR activity ID is required.");
    }

    public void validateLewForSubmission(CgpNoiForm form) throws IllegalArgumentException {
        validateLewEligibility(form);
        validateOperatorInformation(form);
        validateProjectSiteInformation(form);
        LowErosivityWaiver lew = form.getFormData().getLowErosivityWaiver();
        Validate.notNull(lew.getLewProjectStart(), "LEW project start date is required.");
        Validate.notNull(lew.getLewProjectEnd(), "LEW project end date is required.");
        Validate.isTrue(
                lew.getLewProjectEnd().compareTo(lew.getLewProjectStart()) >= 0,
                "LEW project end date may not be before start date.");
        Validate.notNull(lew.getLewAreaDisturbed(), "LEW area to be disturbed is required.");
        Validate.isTrue(
                lew.getLewAreaDisturbed().compareTo(new BigDecimal("5.0")) < 0,
                "LEW area to be disturbed must be less than 5");
        Validate.notNull(lew.getLewRFactor(), "LEW R-Factor is required.");
        Validate.isTrue(
                lew.getLewRFactor().compareTo(new BigDecimal("5.0")) < 0,
                "LEW R-Factor must be less than 5");
        Validate.isTrue(
                StringUtils.isNotEmpty(form.getFormData().getOperatorInformation().getPreparer().getFirstName()) &&
                        StringUtils.isNotEmpty(form.getFormData().getOperatorInformation().getPreparer().getLastName()),
                "Preparer information is required.");
        Validate.isTrue(
                StringUtils.isNotEmpty(form.getFormData().getOperatorInformation().getCertifier().getFirstName()) &&
                        StringUtils.isNotEmpty(form.getFormData().getOperatorInformation().getCertifier().getLastName()),
                "Certifier information is required.");
    }

    void validateOperatorInformation(CgpNoiForm form) {
        OperatorInformation oi = form.getFormData().getOperatorInformation();
        Validate.notEmpty(oi.getOperatorName(), "Operator name is required.");
        Validate.notEmpty(oi.getOperatorAddress(), "Operator mailing address is required.");
        Validate.notEmpty(oi.getOperatorCity(), "Operator city is required.");
        Validate.notEmpty(oi.getOperatorStateCode(), "Operator state is required.");
        Validate.notEmpty(oi.getOperatorZipCode(), "Operator zip is required.");
        if (!CollectionUtils.isEmpty(referenceService.retrieveCounties(oi.getOperatorStateCode()))) {
            Validate.notEmpty(oi.getOperatorCounty(), "Operator county is required.");
        }
        Validate.notEmpty(oi.getOperatorPointOfContact().getFirstName(), "Operator first name is required.");
        Validate.notEmpty(oi.getOperatorPointOfContact().getLastName(), "Operator last name is required.");
        Validate.notEmpty(oi.getOperatorPointOfContact().getPhone(), "Operator phone is required.");
        Validate.notEmpty(oi.getOperatorPointOfContact().getEmail(), "Operator email is required.");
        Validate.notEmpty(oi.getOperatorPointOfContact().getTitle(), "Operator title is required.");
    }

    void validateProjectSiteInformation(CgpNoiForm form) {
        ProjectSiteInformation psi = form.getFormData().getProjectSiteInformation();
        Validate.notEmpty(psi.getSiteName(), "Project site name is required.");
        Validate.notEmpty(psi.getSiteAddress(), "Project site address is required.");
        Validate.notEmpty(psi.getSiteCity(), "Project site city is required.");
        Validate.notEmpty(psi.getSiteStateCode(), "Project site state is required.");
        Validate.notEmpty(psi.getSiteZipCode(), "Project site zip is required.");
        if (!CollectionUtils.isEmpty(referenceService.retrieveCounties(psi.getSiteStateCode()))) {
            Validate.notEmpty(psi.getSiteCounty(), "Project site county is required.");
        }
        if (FormType.Notice_Of_Intent.equals(form.getType())) {
            Validate.notNull(psi.getSiteLocation(), "Project location is required.");
            Validate.notNull(psi.getSiteLocation().getLatitude(), "Project latitude is required.");
            Validate.notNull(psi.getSiteLocation().getLongitude(), "Project longitude is required.");
            Validate.notNull(
                    psi.getSiteLocation().getLatLongDataSource(),
                    "Project latitude/longitude data source is required.");
            Validate.notNull(
                    psi.getSiteLocation().getHorizontalReferenceDatum(),
                    "Project reference datum is required.");
            Validate.notNull(psi.getSiteProjectStart(), "Project start date is required.");
            Validate.notNull(psi.getSiteProjectEnd(), "Project end date is required.");
            Validate.isTrue(
                    psi.getSiteProjectEnd().compareTo(psi.getSiteProjectStart()) >= 0,
                    "Project end date may not be before project start date.");
            Validate.notNull(
                    psi.getSiteStructureDemolitionBefore1980(),
                    "Indication of structure demolition or renovation before 1980 is required.");
            if (BooleanUtils.isTrue(psi.getSiteStructureDemolitionBefore1980())) {
                Validate.notNull(
                        psi.getSiteStructureDemolitionBefore198010kSquareFeet(),
                        "Indication of floor space is required.");
            }
            Validate.notNull(
                    psi.getSitePreDevelopmentAgricultural(),
                    "Indication of pre-developed use as agricultural land is required.");
            Validate.notNull(
                    psi.getSiteEarthDisturbingActivitiesOccurrence(),
                    "Indication of whether earth disturbing activities have commenced is required.");
            if (BooleanUtils.isTrue(psi.getSiteEarthDisturbingActivitiesOccurrence())) {
                Validate.notNull(
                        psi.getSiteEmergencyRelated(),
                        "Indication whether project is an \"emergency-related project\" is required.");
            }
            Validate.notNull(
                    psi.getSiteIndianCulturalProperty(),
                    "Indication of whether the site is located on property of religious or cultural significance is " +
                            "required");
            if (BooleanUtils.isTrue(psi.getSiteIndianCulturalProperty())) {
                Validate.notNull(psi.getSiteIndianCulturalPropertyTribe(),
                        "Indian Tribe for land of religious or cultural significance is required.");
            }
        }
    }

    void validateDischargeInformation(CgpNoiForm form) {
        DischargeInformation di = form.getFormData().getDischargeInformation();
        Validate.notNull(
                di.getDischargeMunicipalSeparateStormSewerSystem(),
                "Indication of whether site discharges stormwater into a municipal sewer system is required.");
        Validate.notNull(
                di.getDischargeUSWatersWithin50Feet(),
                "Indication of whether there are any U.S. waters within 50 feet of your project is required.");
        if (!CollectionUtils.isEmpty(di.getDischargePoints())) {
            for (PointOfDischarge pod : di.getDischargePoints()) {
                // make sure there is a receiving water
                Validate.notNull(pod.getFirstWater(), "Receiving water is required for a discharge point.");
                // then there must be one pollutant marked as impaired
                if (BooleanUtils.isTrue(pod.getImpaired())) {
                    Validate.isTrue(
                            CollectionUtils.exists(pod.getFirstWater().getPollutants(), o -> ((Pollutant) o).getImpaired()),
                            String.format("%s must have at least one pollutant causing impairment.", pod.getFirstWater().getReceivingWaterName()));
                }
                // then there must be one pollutant with a TMDL
                if (BooleanUtils.isTrue(pod.getTmdlCompleted())) {
                    Validate.isTrue(
                            CollectionUtils.exists(pod.getFirstWater().getPollutants(), o -> {
                                Pollutant p = (Pollutant) o;
                                return !(p.getTmdl() == null
                                        || StringUtils.isEmpty(p.getTmdl().getId())
                                        || StringUtils.isEmpty(p.getTmdl().getName()));
                            }),
                            String.format("%s must have at least one TMDL.", pod.getFirstWater().getReceivingWaterName()));
                }
            }
        }
    }

    void validateSwpppInformation(CgpNoiForm form) {
        StormwaterPollutionPreventionPlanInformation swppp = form.getFormData().getStormwaterPollutionPreventionPlanInformation();
        Validate.notNull(swppp.getContactInformation(), "SWPPP contact is required.");
        Validate.notEmpty(swppp.getContactInformation().getFirstName(), "SWPPP contact first name is required.");
        Validate.notEmpty(swppp.getContactInformation().getLastName(), "SWPPP contact last name is required.");
        Validate.notEmpty(swppp.getContactInformation().getLastName(), "SWPPP contact last name is required.");
        Validate.notEmpty(swppp.getContactInformation().getPhone(), "SWPPP contact phone is required.");
        Validate.notEmpty(swppp.getContactInformation().getEmail(), "SWPPP contact email is required.");
        Validate.notEmpty(swppp.getContactInformation().getTitle(), "SWPPP contact title is required.");
    }

    void validateEndangeredSpeciesProtection(CgpNoiForm form) {
        EndangeredSpeciesProtectionInformation esp = form.getFormData().getEndangeredSpeciesProtectionInformation();
        Validate.notNull(esp.getCriterion(), "One of endangered species criterion must be chosen.");
        Validate.notEmpty(esp.getCriteriaSelectionSummary(), "Criteria selection summary is required.");
        if (AppendixDCriterion.Criterion_B.equals(esp.getCriterion())) {
            Validate.notEmpty(esp.getOtherOperatorNpdesId(), "The other operator's NPDES ID is required.");
        } else if (AppendixDCriterion.Criterion_C.equals(esp.getCriterion())) {
            Validate.notEmpty(
                    esp.getSpeciesAndHabitatInActionArea(),
                    "Federally-listed species or federally-designated critical habitat are required.");
            Validate.notEmpty(esp.getDistanceFromSite(), "Distance from species or habitat is required.");
        }
    }

    void validateHistoricPreservation(CgpNoiForm form) {
        HistoricPreservation hp = form.getFormData().getHistoricPreservation();
        Validate.notNull(hp.getAppendexEStep1(), "Indication of Appendix E step 1 is required.");
        if (BooleanUtils.isTrue(hp.getAppendexEStep1())) {
            Validate.notNull(hp.getAppendexEStep2(), "Indication of Appendix E step 2 is required.");
            if (BooleanUtils.isFalse(hp.getAppendexEStep2())) {
                Validate.notNull(hp.getAppendexEStep3(), "Indication of Appendix E step 3 is required.");
                if (BooleanUtils.isFalse(hp.getAppendexEStep3())) {
                    Validate.notNull(hp.getAppendexEStep4(), "Indication of Appendix E step 4 is required.");
                    if (BooleanUtils.isTrue(hp.getAppendexEStep4())) {
                        Validate.notEmpty(hp.getAppendexEStep4Response(), "Appendix E step 4 response is required.");
                    }
                }
            }
        }
    }

    void validateChemicalTreatmentInformation(CgpNoiForm form) {
        ChemicalTreatmentInformation cti = form.getFormData().getChemicalTreatmentInformation();
        if (BooleanUtils.isTrue(cti.getCationicTreatmentChemicals())) {
            Validate.isTrue(
                    CollectionUtils.isNotEmpty(cti.getTreatmentChemicals()),
                    "Please indicate the treatment chemicals that you will use.");
            int count = CollectionUtils.countMatches(form.getAttachments(), o -> {
                Attachment att = (Attachment) o;
                return AttachmentCategory.Chemical_Treatment_Information.equals(att.getCategory());
            });
            Validate.isTrue(count > 0, "Cationic treatment chemicals authorization letter is required.");
        }
    }

}
