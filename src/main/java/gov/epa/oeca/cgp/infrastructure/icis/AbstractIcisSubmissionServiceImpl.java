package gov.epa.oeca.cgp.infrastructure.icis;

import gov.epa.oeca.cgp.application.reference.ReferenceService;
import gov.epa.oeca.cgp.domain.noi.*;
import gov.epa.oeca.cgp.domain.noi.formsections.*;
import gov.epa.oeca.cgp.domain.ref.Tribe;
import gov.epa.oeca.common.ApplicationException;
import net.exchangenetwork.schema.icis._5._8.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dfladung
 */
public abstract class AbstractIcisSubmissionServiceImpl implements IcisSubmissionService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractIcisSubmissionServiceImpl.class);
    private static final String submitterId = "NET_CDX";
    private static final String expiration = "2022-02-15";
    private static final String sdfString = "yyyy-MM-dd";
    private static final String NA = "N/A";
    private static final HashMap<String, String> horizontalReferenceDatumCode = new HashMap<>();
    private static final HashMap<String, String> mapDataSources = new HashMap<>();
    private static final HashMap<AppendixDCriterion, String> speciesCriterion = new HashMap<>();
    private static final HashMap<String, String> constructionSiteCodes = new HashMap<>();
    private static final HashMap<Tier, String> tierCodes = new HashMap<>();

    static {
        horizontalReferenceDatumCode.put("NAD 27", "001");
        horizontalReferenceDatumCode.put("NAD 83", "002");
        horizontalReferenceDatumCode.put("WGS 84", "003");
        mapDataSources.put("Map", "018");
        mapDataSources.put("GPS", "028");
        speciesCriterion.put(AppendixDCriterion.Criterion_A, "NON");
        speciesCriterion.put(AppendixDCriterion.Criterion_B, "OTH");
        speciesCriterion.put(AppendixDCriterion.Criterion_C, "SPU");
        speciesCriterion.put(AppendixDCriterion.Criterion_D, "COO");
        speciesCriterion.put(AppendixDCriterion.Criterion_E, "SC7");
        speciesCriterion.put(AppendixDCriterion.Criterion_F, "S10");
        constructionSiteCodes.put("single", "SFR");
        constructionSiteCodes.put("multi", "MFR");
        constructionSiteCodes.put("commercial", "COM");
        constructionSiteCodes.put("industrial", "IND");
        constructionSiteCodes.put("institutional", "INS");
        constructionSiteCodes.put("road", "HOR");
        constructionSiteCodes.put("utility", "UTI");
        tierCodes.put(Tier.Tier_2, "2");
        tierCodes.put(Tier.Tier_2_5, "2.5");
        tierCodes.put(Tier.Tier_3, "3");
    }


    @Autowired
    protected IcisMarshallerFactory5_8 marshallerFactory;
    @Resource(name = "oecaGeneralConfiguration")
    protected Map<String, String> oecaGeneralConfiguration;
    @Autowired
    protected ReferenceService referenceService;

    String getIcisDataflow() {
        return oecaGeneralConfiguration.get("icisDataflow");
    }


    public File marshallForm(CgpNoiForm form, CgpNoiForm previous) throws ApplicationException {
        try {
            // setup the basic structure
            File icisXml = File.createTempFile(form.getFormSet().getNpdesId(), ".xml");
            Document doc = new Document();
            HeaderData headerData = new HeaderData();
            doc.setHeader(headerData);
            headerData.setId(submitterId);
            Property p = new Property();
            p.setName(NameType.SOURCE);
            p.setValue("FullBatch");
            headerData.getProperty().add(p);
            TransactionHeader th = new TransactionHeader();
            switch (form.getPhase()) {
                case New:
                    th.setTransactionType(TransactionType.N);
                    break;
                case Change:
                    th.setTransactionType(TransactionType.R);
                    break;
                case Terminate:
                    th.setTransactionType(TransactionType.C);
                    break;
            }

            // assemble the form
            if (FormType.Low_Erosivity_Waiver.equals(form.getType())) {
                assembleLew(form, doc, th);
            } else if (FormType.Notice_Of_Intent.equals(form.getType())) {
                assembleNoi(form, previous, doc, th);
            }

            // serialize the form
            Marshaller marshaller = marshallerFactory.getMarshaller();
            marshaller.marshal(doc, icisXml);
            return icisXml;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    void assembleLew(CgpNoiForm form, Document doc, TransactionHeader th) {
        if (!Phase.Terminate.equals(form.getPhase())) {
            assembleLewGeneralPermitPayload(form, doc, th);
            assembleLewStormWaterPayload(form, doc, th);
        } else {
            assembleDiscontinueLewGeneralPermitPayload(form, doc, th);
        }
    }

    void assembleNoi(CgpNoiForm form, CgpNoiForm previous, Document doc, TransactionHeader th) throws Exception {
        if (!Phase.Terminate.equals(form.getPhase())) {
            // add the general permit and stormwater
            assembleNoiGeneralPermitPayload(form, doc, th);
            assembleNoiStormWaterPayload(form, doc, th);
            // delete all of the old features
            if (Phase.Change.equals(form.getPhase())
                    && previous != null
                    && !CollectionUtils.isEmpty(previous.getFormData().getDischargeInformation().getDischargePoints())) {
                TransactionHeader delete = new TransactionHeader();
                delete.setTransactionType(TransactionType.D);
                assembleNoiPermittedFeaturePayload(previous, doc, delete);

            }
            if (!CollectionUtils.isEmpty(form.getFormData().getDischargeInformation().getDischargePoints())) {
                // add the new features
                TransactionHeader add = new TransactionHeader();
                add.setTransactionType(TransactionType.N);
                assembleNoiPermittedFeaturePayload(form, doc, add);
            }
        } else {
            assembleNoiPermitTerminationPayload(form, doc, th);
        }
    }

    void assembleNoiGeneralPermitPayload(CgpNoiForm form, Document doc, TransactionHeader th) throws Exception {
        OperatorInformation oi = form.getFormData().getOperatorInformation();

        // general permit
        GeneralPermit gp = assembleGeneralPermit(form, doc, th);
        gp.getNPDESDataGroupNumberCode().add("G2A");
        gp.getNPDESDataGroupNumberCode().add("G2B");
        CgpNoiForm initial = findInitial(form.getFormSet());
        XMLGregorianCalendar issue = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        issue.setDay(initial.getSubmittedDate().getDayOfMonth());
        issue.setMonth(initial.getSubmittedDate().getMonthValue());
        issue.setYear(initial.getSubmittedDate().getYear());
        gp.setPermitIssueDate(issue);
        gp.setPermitEffectiveDate(issue);
        XMLGregorianCalendar expiration = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        expiration.setDay(15);
        expiration.setMonth(2);
        expiration.setYear(2022);
        gp.setPermitExpirationDate(expiration);
        gp.setPermitIssuingOrganizationTypeName("U.S. EPA");
        gp.setApplicationReceivedDate(form.getCertifiedDate().toLocalDate().toString());
        gp.setPermitApplicationCompletionDate(form.getCertifiedDate().toLocalDate().toString());

        // permit contact
        PermitContact pc = new PermitContact();
        gp.setPermitContact(pc);
        oi.getOperatorPointOfContact().setOrganization(oi.getOperatorName());
        pc.getContact().add(assembleContact("PMA", oi.getOperatorPointOfContact()));
        pc.getContact().add(assembleContact("PRP", oi.getPreparer()));
        pc.getContact().add(assembleContact("SWF", oi.getCertifier()));

        // permit address
        assemblePermitAddress(gp, oi);

        // facility
        Facility facility = assembleFacility(form, gp);
        FacilityContact fc = new FacilityContact();
        facility.setFacilityContact(fc);
        fc.getContact().add(assembleContact("OPE", oi.getOperatorPointOfContact()));
        FacilityAddress fa = new FacilityAddress();
        facility.setFacilityAddress(fa);
        fa.getAddress().add(gp.getPermitAddress().getAddress().get(0));
    }

    void assembleLewGeneralPermitPayload(CgpNoiForm form, Document doc, TransactionHeader th) {
        OperatorInformation oi = form.getFormData().getOperatorInformation();

        // general permit
        GeneralPermit gp = assembleGeneralPermit(form, doc, th);
        gp.setPermitStatusCode(PermitStatusCodeType.NON);
        gp.getNPDESDataGroupNumberCode().add("G2D");
        gp.getNPDESDataGroupNumberCode().add("G2E");

        // permit contact
        PermitContact pc = new PermitContact();
        gp.setPermitContact(pc);
        oi.getOperatorPointOfContact().setOrganization(oi.getOperatorName());
        pc.getContact().add(assembleContact("PMA", oi.getOperatorPointOfContact()));
        pc.getContact().add(assembleContact("PRP", oi.getPreparer()));
        pc.getContact().add(assembleContact("SWF", oi.getCertifier()));

        // permit address
        assemblePermitAddress(gp, oi);

        // facility
        assembleFacility(form, gp);
    }

    void assembleNoiPermitTerminationPayload(CgpNoiForm form, Document doc, TransactionHeader th) throws Exception {
        PermitTermination pt = new PermitTermination();
        pt.setPermitIdentifier(form.getFormSet().getNpdesId());
        XMLGregorianCalendar terminationDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        terminationDate.setYear(form.getCertifiedDate().getYear());
        terminationDate.setMonth(form.getCertifiedDate().getMonthValue());
        terminationDate.setDay(form.getCertifiedDate().getDayOfMonth());
        pt.setPermitTerminationDate(terminationDate);
        PermitTerminationData ptd = new PermitTerminationData();
        ptd.setTransactionHeader(th);
        ptd.setPermitTermination(pt);
        PayloadData ptPayload = new PayloadData();
        ptPayload.setOperation(OperationType.PERMIT_TERMINATION_SUBMISSION);
        ptPayload.getPermitTerminationData().add(ptd);
        doc.getPayload().add(ptPayload);
    }

    void assembleDiscontinueLewGeneralPermitPayload(CgpNoiForm form, Document doc, TransactionHeader th) {
        GeneralPermit gp = assembleGeneralPermit(form, doc, th);
        gp.setElectronicSubmissionTypeCode(null);
        gp.setAssociatedMasterGeneralPermitIdentifier(null);
        gp.setPermitTypeCode(null);
        gp.setAgencyTypeCode(null);
        gp.getNPDESDataGroupNumberCode().clear();
        gp.setPermitCommentsText(String.format(
                "LEW Discontinued as of %s",
                form.getCertifiedDate().toLocalDate().toString()));
    }

    void assembleNoiStormWaterPayload(CgpNoiForm form, Document doc, TransactionHeader th) {
        SWConstructionPermit swPermit = assembleStormWaterConstructionPermit(form, doc, th);
        swPermit.setEstimatedStartDate(
                new SimpleDateFormat(sdfString).format(form.getFormData().getProjectSiteInformation().getSiteProjectStart()));
        swPermit.setEstimatedCompleteDate(
                new SimpleDateFormat(sdfString).format(form.getFormData().getProjectSiteInformation().getSiteProjectEnd()));
        swPermit.setEstimatedAreaDisturbedAcresNumber(
                form.getFormData().getProjectSiteInformation().getSiteAreaDisturbed().toString());

        StormWaterContact swContact = new StormWaterContact();
        Contact contact = assembleContact("SWS",
                form.getFormData().getStormwaterPollutionPreventionPlanInformation().getContactInformation());
        swContact.getContact().add(contact);
        swPermit.setStormWaterContact(swContact);

        // historic property
        HistoricPreservation hp = form.getFormData().getHistoricPreservation();
        HistoricPreservationData hpd = new HistoricPreservationData();
        swPermit.setHistoricPreservationData(hpd);
        hpd.setSubsurfaceEarthDisturbanceIndicator(getIndicatorType(hp.getAppendexEStep1()));
        if (BooleanUtils.isTrue(hp.getAppendexEStep1())) {
            hpd.setPriorSurveysEvaluationsIndicator(getIndicatorType(hp.getAppendexEStep2()));
            if (BooleanUtils.isFalse(hp.getAppendexEStep2())) {
                hpd.setSubsurfaceEarthDisturbanceControlIndicator(getIndicatorType(hp.getAppendexEStep3()));
                if (BooleanUtils.isFalse(hp.getAppendexEStep3())) {
                    swPermit.setHistoricPropertyIndicator(getIndicatorType(hp.getAppendexEStep4()));
                    if (BooleanUtils.isTrue(hp.getAppendexEStep4())) {
                        swPermit.setHistoricPropertyCriterionMetCode("WRA");
                    } else {
                        swPermit.setHistoricPropertyCriterionMetCode("CNR");
                    }
                } else {
                    swPermit.setHistoricPropertyCriterionMetCode("NEF");
                }
            } else {
                swPermit.setHistoricPropertyCriterionMetCode("NRB");
            }
        } else {
            swPermit.setHistoricPropertyCriterionMetCode("NSC");
        }

        // esp
        EndangeredSpeciesProtectionInformation esp = form.getFormData().getEndangeredSpeciesProtectionInformation();
        swPermit.setSpeciesCriticalHabitatIndicator(getIndicatorType(esp.getAppendixDCriteriaMet()));
        swPermit.setSpeciesCriterionMetCode(speciesCriterion.get(esp.getCriterion()));

        // signature date
        GPCFNoticeOfIntent gpcfNoticeOfIntent = new GPCFNoticeOfIntent();
        swPermit.setGPCFNoticeOfIntent(gpcfNoticeOfIntent);
        gpcfNoticeOfIntent.setNOISignatureDate(form.getCertifiedDate().toLocalDate().toString());
        gpcfNoticeOfIntent.setCompleteNOIReceivedDate(gpcfNoticeOfIntent.getNOISignatureDate());

        // project site
        ProjectSiteInformation psi = form.getFormData().getProjectSiteInformation();
        if (!CollectionUtils.isEmpty(psi.getSiteConstructionTypes())) {
            ConstructionSiteList constructionSiteList = new ConstructionSiteList();
            swPermit.setConstructionSiteList(constructionSiteList);
            for (String type : psi.getSiteConstructionTypes()) {
                if (constructionSiteCodes.containsKey(type)) {
                    constructionSiteList.getConstructionSiteCode().add(constructionSiteCodes.get(type));
                } else {
                    constructionSiteList.getConstructionSiteCode().add("OTH");
                    String otherText = StringUtils.length(type) > 100 ? StringUtils.abbreviate(type, 100) : type;
                    constructionSiteList.setConstructionSiteOtherText(otherText);
                }
            }
        }
        swPermit.setStructureDemolishedIndicator(getIndicatorType(psi.getSiteStructureDemolitionBefore1980()));
        if (BooleanUtils.isTrue(psi.getSiteStructureDemolitionBefore1980())) {
            swPermit.setStructureDemolishedFloorSpaceIndicator(getIndicatorType(psi.getSiteStructureDemolitionBefore198010kSquareFeet()));
        }
        swPermit.setPredevelopmentLandUseIndicator(getIndicatorType(psi.getSitePreDevelopmentAgricultural()));
        swPermit.setEarthDisturbingActivitiesIndicator(getIndicatorType(psi.getSiteEarthDisturbingActivitiesOccurrence()));
        if (BooleanUtils.isTrue(psi.getSiteEarthDisturbingActivitiesOccurrence())) {
            swPermit.setEarthDisturbingEmergencyIndicator(getIndicatorType(psi.getSiteEmergencyRelated()));
        }
        swPermit.setPreviousStormwaterDischargesIndicator(getIndicatorType(psi.getSitePreviousNpdesPermit()));
        if (BooleanUtils.isTrue(psi.getSitePreviousNpdesPermit())) {
            swPermit.setOtherPermitIdentifier(psi.getSitePreviousNpdesPermitId());
        }
        swPermit.setCGPIndicator(getIndicatorType(psi.getSiteCgpAuthorizationConfirmation()));


        // discharge information
        DischargeInformation di = form.getFormData().getDischargeInformation();
        swPermit.setMS4DischargeIndicator(getIndicatorType(di.getDischargeMunicipalSeparateStormSewerSystem()));
        swPermit.setWaterProximityIndicator(getIndicatorType(di.getDischargeUSWatersWithin50Feet()));
        swPermit.setAntidegradationIndicator(getIndicatorType(di.getDischargeAllowable()));

        // treatment chemicals
        ChemicalTreatmentInformation cti = form.getFormData().getChemicalTreatmentInformation();
        swPermit.setTreatmentChemicalsIndicator(getIndicatorType(cti.getPolymersFlocculantsOtherTreatmentChemicals()));
        if (cti.getCationicTreatmentChemicals() != null) {
            swPermit.setCationicChemicalsIndicator(getIndicatorType(cti.getCationicTreatmentChemicals()));
            if (BooleanUtils.isTrue(cti.getCationicTreatmentChemicals())) {
                swPermit.setCationicChemicalsAuthorizationIndicator(getIndicatorType(cti.getCationicTreatmentChemicalsAuthorization()));
            }
        }
        if (!CollectionUtils.isEmpty(cti.getTreatmentChemicals())) {
            TreatmentChemicalsList list = new TreatmentChemicalsList();
            swPermit.setTreatmentChemicalsList(list);
            for (String treatmentChemical : cti.getTreatmentChemicals()) {
                list.getTreatmentChemicalName().add(treatmentChemical);
            }
        }

        // swppp
        StormwaterPollutionPreventionPlanInformation swppp = form.getFormData().getStormwaterPollutionPreventionPlanInformation();
        swPermit.setSWPPPPreparedIndicator(getIndicatorType(swppp.getPreparationInAdvance()));

    }

    void assembleLewStormWaterPayload(CgpNoiForm form, Document doc, TransactionHeader th) {
        SWConstructionPermit swPermit = assembleStormWaterConstructionPermit(form, doc, th);
        swPermit.setEstimatedStartDate(
                new SimpleDateFormat(sdfString).format(form.getFormData().getLowErosivityWaiver().getLewProjectStart()));
        swPermit.setEstimatedCompleteDate(
                new SimpleDateFormat(sdfString).format(form.getFormData().getLowErosivityWaiver().getLewProjectEnd()));
        swPermit.setEstimatedAreaDisturbedAcresNumber(
                form.getFormData().getLowErosivityWaiver().getLewAreaDisturbed().toString());
    }


    GeneralPermit assembleGeneralPermit(CgpNoiForm form, Document doc, TransactionHeader th) {
        GeneralPermit gp = new GeneralPermit();
        gp.setPermitIdentifier(form.getFormSet().getNpdesId());
        gp.setAssociatedMasterGeneralPermitIdentifier(form.getFormSet().getMasterPermitNumber());
        gp.setPermitTypeCode("GPC");
        gp.setAgencyTypeCode("EP6");
        if (Source.Electronic.equals(form.getSource())) {
            gp.setElectronicSubmissionTypeCode("ES1");
        } else {
            gp.setElectronicSubmissionTypeCode("ES8");
            ElectronicReportingWaiverData erw = new ElectronicReportingWaiverData();
            erw.setElectronicReportingWaiverTypeCode("TMP");
            erw.setElectronicReportingWaiverEffectiveDate(form.getCertifiedDate().toLocalDate().toString());
            erw.setElectronicReportingWaiverExpirationDate(expiration);
            gp.setElectronicReportingWaiverData(erw);
        }

        GeneralPermitData gpd = new GeneralPermitData();
        gpd.setGeneralPermit(gp);
        gpd.setTransactionHeader(th);
        PayloadData gpPayload = new PayloadData();
        gpPayload.setOperation(OperationType.GENERAL_PERMIT_SUBMISSION);
        gpPayload.getGeneralPermitData().add(gpd);
        doc.getPayload().add(gpPayload);
        return gp;
    }

    SWConstructionPermit assembleStormWaterConstructionPermit(CgpNoiForm form, Document doc, TransactionHeader th) {
        SWConstructionPermit swPermit = new SWConstructionPermit();
        swPermit.setPermitIdentifier(form.getFormSet().getNpdesId());
        SWConstructionPermitData swData = new SWConstructionPermitData();
        swData.setSWConstructionPermit(swPermit);
        swData.setTransactionHeader(th);
        PayloadData stormWater = new PayloadData();
        stormWater.setOperation(OperationType.SW_CONSTRUCTION_PERMIT_SUBMISSION);
        stormWater.getSWConstructionPermitData().add(swData);
        doc.getPayload().add(stormWater);
        return swPermit;
    }

    void assembleNoiPermittedFeaturePayload(CgpNoiForm form, Document doc, TransactionHeader th) {
        PayloadData permittedFeaturePayload = new PayloadData();
        permittedFeaturePayload.setOperation(OperationType.PERMITTED_FEATURE_SUBMISSION);
        for (PointOfDischarge pod : form.getFormData().getDischargeInformation().getDischargePoints()) {
            PermittedFeature feature = new PermittedFeature();
            feature.setPermitIdentifier(form.getFormSet().getNpdesId());
            feature.setPermittedFeatureIdentifier(pod.getId());

            String pfd = StringUtils.length(pod.getDescription()) < 100 ?
                    StringUtils.trimToNull(pod.getDescription()) :
                    StringUtils.abbreviate(StringUtils.trimToNull(pod.getDescription()), 100);
            feature.setPermittedFeatureDescription(pfd);
            feature.setPermittedFeatureTypeCode("EXO");
            String rwName = pod.getFirstWater().getReceivingWaterName().length() > 50 ?
                    StringUtils.abbreviate(pod.getFirstWater().getReceivingWaterName(), 50)
                    : pod.getFirstWater().getReceivingWaterName();
            feature.setPermittedFeatureStateWaterBodyName(rwName);
            if (tierCodes.containsKey(pod.getTier())) {
                feature.setTierLevelName(tierCodes.get(pod.getTier()));
            }
            feature.setImpairedWaterIndicator(getIndicatorType(
                    CollectionUtils.exists(pod.getFirstWater().getPollutants(), o -> {
                        Pollutant p = (Pollutant) o;
                        return BooleanUtils.isTrue(p.getImpaired());
                    })));
            feature.setTMDLCompletedIndicator(getIndicatorType(
                    CollectionUtils.exists(pod.getFirstWater().getPollutants(), o -> {
                        Pollutant p = (Pollutant) o;
                        return p.getTmdl() != null
                                && !StringUtils.isEmpty(p.getTmdl().getId())
                                && !StringUtils.isEmpty(p.getTmdl().getName());
                    })));
            PollutantList pollutantList = new PollutantList();
            feature.setPollutantList(pollutantList);
            ImpairedWaterPollutants impairedWaterPollutants = null;
            for (Pollutant p : pod.getFirstWater().getPollutants()) {
                if (BooleanUtils.isTrue(p.getImpaired())) {
                    if (impairedWaterPollutants == null) {
                        impairedWaterPollutants = new ImpairedWaterPollutants();
                        pollutantList.setImpairedWaterPollutants(impairedWaterPollutants);
                    }
                    impairedWaterPollutants.getImpairedWaterPollutantCode().add(p.getPollutantCode().longValue());
                }
                if (p.getTmdl() != null && !StringUtils.isEmpty(p.getTmdl().getId()) && !StringUtils.isEmpty(p.getTmdl().getName())) {
                    TMDLPollutants tmdl = new TMDLPollutants();
                    tmdl.setTMDLIdentifier(p.getTmdl().getId());
                    tmdl.getTMDLPollutantCode().add(p.getPollutantCode().longValue());
                    tmdl.setTMDLName(p.getTmdl().getName());
                    pollutantList.getTMDLPollutants().add(tmdl);
                }
            }

            PermittedFeatureData featureData = new PermittedFeatureData();
            featureData.setPermittedFeature(feature);
            featureData.setTransactionHeader(th);
            permittedFeaturePayload.getPermittedFeatureData().add(featureData);
        }
        doc.getPayload().add(permittedFeaturePayload);
    }

    Facility assembleFacility(CgpNoiForm form, GeneralPermit gp) {
        // facility
        Facility facility = new Facility();
        gp.setFacility(facility);
        ProjectSiteInformation psi = form.getFormData().getProjectSiteInformation();
        facility.setFacilitySiteName(psi.getSiteName());
        facility.setLocationAddressText(psi.getSiteAddress());
        facility.setLocalityName(psi.getSiteCity());
        facility.setLocationStateCode(psi.getSiteStateCode());
        facility.setLocationZipCode(psi.getSiteZipCode());
        facility.setLocationCountryCode("US");
        if (!StringUtils.isEmpty(psi.getSiteIndianCountryLands())) {
            Tribe t = referenceService.retrieveTribeByLandNameAndStateCode(psi.getSiteIndianCountryLands(), psi.getSiteStateCode());
            // the original design does not support tribes with non-unique codes, so we're treating any
            // negative tribal code as -999 for ICIS purposes
            String tribalLandCode = (StringUtils.startsWith(t.getTribalCode(), "-")) ? "-999" : t.getTribalCode();
            facility.setTribalLandCode(tribalLandCode);
        }
        GeographicCoordinates coordinates = new GeographicCoordinates();
        facility.setGeographicCoordinates(coordinates);
        coordinates.setLatitudeMeasure(psi.getSiteLocation().getLatitude().setScale(4, RoundingMode.HALF_UP).toString());
        coordinates.setLongitudeMeasure(psi.getSiteLocation().getLongitude().setScale(4, RoundingMode.HALF_UP).toString());
        coordinates.setHorizontalReferenceDatumCode(
                horizontalReferenceDatumCode.get((psi.getSiteLocation().getHorizontalReferenceDatum())));
        String mapSource = mapDataSources.containsKey(psi.getSiteLocation().getLatLongDataSource()) ?
                mapDataSources.get(psi.getSiteLocation().getLatLongDataSource()) :
                "027";
        coordinates.setHorizontalCollectionMethodCode(mapSource);
        return facility;
    }

    Contact assembleContact(String affiliationType, gov.epa.oeca.cgp.domain.Contact cgpContact) {
        Contact contact = new Contact();
        contact.setAffiliationTypeText(affiliationType);
        contact.setFirstName(StringUtils.trimToNull(cgpContact.getFirstName()));
        contact.setLastName(StringUtils.trimToNull(cgpContact.getLastName()));
        contact.setMiddleName(StringUtils.trimToNull(cgpContact.getMiddleInitial()));
        contact.setIndividualTitleText(StringUtils.isEmpty(cgpContact.getTitle()) ? NA : cgpContact.getTitle());
        contact.setOrganizationFormalName(StringUtils.isEmpty(cgpContact.getOrganization()) ? NA : cgpContact.getOrganization());
        contact.setElectronicAddressText(StringUtils.trimToNull(cgpContact.getEmail()));
        Telephone telephone = new Telephone();
        contact.getTelephone().add(telephone);
        telephone.setTelephoneNumberTypeCode("OFF");
        telephone.setTelephoneNumber(getTelephoneNumber(cgpContact.getPhone()));
        if (!StringUtils.isEmpty(cgpContact.getPhoneExtension())) {
            telephone.setTelephoneExtensionNumber(cgpContact.getPhoneExtension());
        }
        return contact;
    }

    void assemblePermitAddress(GeneralPermit gp, OperatorInformation oi) {
        PermitAddress pa = new PermitAddress();
        gp.setPermitAddress(pa);
        Address a = new Address();
        pa.getAddress().add(a);
        a.setAffiliationTypeText("PMA");
        a.setOrganizationFormalName(oi.getOperatorName());
        a.setMailingAddressText(oi.getOperatorAddress());
        a.setMailingAddressCityName(oi.getOperatorCity());
        a.setMailingAddressStateCode(oi.getOperatorStateCode());
        a.setMailingAddressZipCode(oi.getOperatorZipCode());
        a.setMailingAddressCountryCode("US");
        a.setCountyName(StringUtils.trimToNull(oi.getOperatorCounty()));
    }

    String getIndicatorType(Boolean value) {
        Validate.notNull(value, "Can not determine indicator type");
        return value ? "Y" : "N";
    }

    String getTelephoneNumber(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        } else {
            return phone.replaceAll("[^0-9+]", "");
        }
    }

    CgpNoiForm findInitial(CgpNoiFormSet formSet) {
        return (CgpNoiForm) CollectionUtils.find(formSet.getForms(), o -> ((CgpNoiForm) o).getPhase().equals(Phase.New));
    }
}
