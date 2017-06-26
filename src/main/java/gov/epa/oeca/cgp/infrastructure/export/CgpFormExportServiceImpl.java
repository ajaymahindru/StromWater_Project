package gov.epa.oeca.cgp.infrastructure.export;

import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.domain.noi.formsections.*;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author labieva (linera.abieva@cgifederal.com)
 */
@Service("formExportService")
@Transactional
public class CgpFormExportServiceImpl implements CgpFormExportService {
    private static final Logger logger = LoggerFactory.getLogger(CgpFormExportServiceImpl.class);

    private static final String NEW_LINE_SEPARATOR = "\n";
    //CSV file header
    private static final Object [] FORM_LIST_FILE_HEADER = {
            "ID",
            "Type",
            "NPDES ID",
            "Master Permit Number",
            "Tracking Number",
            "Owner",
            "Status",
            "Last Modified",
            "Created Date",
            "Review Expiration Date",
            "Certified Date",
            "Submitted Date",
            "Operator Name",
            "Operator Address",
            "Operator City",
            "Operator State",
            "Operator Zip",
            "Operator County",
            "Operator Point of Contact",
            "Project/Site Name",
            "Project/Site Address",
            "Project/Site City",
            "Project/Site State",
            "Project/Site Zip",
            "Project/Site County",
            "Project Location Latitude",
            "Project Location Longitude",
            "Location Data Source",
            "Location Horizontal Reference Datum",
            "Project/Site Start Date",
            "Project/Site End Date",
            "P/S Area to be Disturbed (acres)",
            "Type of Construction Site",
            "Site Structure Demolition before 1980",
            "Site Demolition before 1980 >=10k sq ft",
            "Pre-development agricultural",
            "Earth-disturbing activities",
            "Emergency-related project",
            "Previous NPDES ID",
            "Site Indian Cultural Property",
            "Site Cultural Significance Indian Tribe",
            "Termination Reason",
            "Discharge into MS4",
            "Discharge: US Waters Within 50ft",
            "Discharge Points",
            "Treatment Chemicals Usage Y/N",
            "Cationic Treatment Chemicals Y/N",
            "Cationic Treatment Chem. Authorization",
            "Treatment Chemicals",
            "SWPPP Contact",
            "Endangered Species Protection Criterion",
            "ESP Criterion Basis Summary",
            "Other Operator NPDES ID",
            "Species/Habitat in Action Area",
            "Species/Habitat Distance from Site (mi)",
            "Historic Preservation Screening Completed",
            "HP Step 1",
            "HP Step 2",
            "HP Step 3",
            "HP Step 4",
            "HP Step 4 Response",
            "LEW Project Start Date",
            "LEW Project End Date",
            "LEW Area to be Disturbed",
            "LEW R-Factor",
            "R-Factor Calculation Method",
            "Interim Site Stabilization Measures (Y/N)"
    };

    private static final Object [] SINGLE_FORM_FILE_HEADER = {
            "Field Name",
            "Field Value"
    };

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName,
            ApplicationSecurityUtils.regAuthRoleName})
    public File generateExcelExport (List<CgpNoiForm> formList) throws ApplicationException {
        try {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Forms");

            Iterator<CgpNoiForm> iterator = formList.iterator();
            int rowIndex = 0;
            while (iterator.hasNext()) {
                if (rowIndex == 0) {
                    Row row = sheet.createRow(rowIndex++);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellValue("Type");
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue("NPDES ID");
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue("Master Permit Number");
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue("Tracking Number");
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue("Site State");
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue("Operator");
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue("Site Name");
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue("Owner");
                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue("Status");
                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue("Last Modified");
                } else {
                    CgpNoiForm form = iterator.next();
                    Row r = sheet.createRow(rowIndex++);
                    Cell c0 = r.createCell(0);
                    c0.setCellValue(form.getType().getValue());
                    Cell c1 = r.createCell(1);
                    c1.setCellValue(form.getFormSet().getNpdesId());
                    Cell c2 = r.createCell(2);
                    c2.setCellValue(form.getFormSet().getMasterPermitNumber());
                    Cell c3 = r.createCell(3);
                    c3.setCellValue(form.getTrackingNumber());
                    Cell c4 = r.createCell(4);
                    c4.setCellValue(form.getFormData().getProjectSiteInformation().getSiteStateCode());
                    Cell c5 = r.createCell(5);
                    c5.setCellValue(form.getFormData().getOperatorInformation().getOperatorName());
                    Cell c6 = r.createCell(6);
                    c6.setCellValue(form.getFormData().getProjectSiteInformation().getSiteName());
                    Cell c7 = r.createCell(7);
                    c7.setCellValue(form.getFormSet().getOwner());
                    Cell c8 = r.createCell(8);
                    c8.setCellValue(form.getStatus().getValue());
                    Cell c9 = r.createCell(9);
                    c9.setCellValue(getZonedDateString(form.getLastUpdatedDate()));
                }
            }
            File excelFile = File.createTempFile("EPACGP", ".xls");
            FileOutputStream fos = new FileOutputStream(excelFile);
            workbook.write(fos);
            fos.close();

            return excelFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }

    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.certifierRoleName,
            ApplicationSecurityUtils.preparerRoleName,
            ApplicationSecurityUtils.helpdeskRoleName,
            ApplicationSecurityUtils.regAuthRoleName})
    public File generateHtmlExport (List<CgpNoiForm> formList) throws ApplicationException {
        try {
            StringBuilder buf = new StringBuilder();
            buf.append("<html>" +
                    "<header>EPA CGP</header>" +
                    "<body onload=\"window.print()\">" +
                    "<table>" +
                    "<tr>" +
                    "<th>Type</th>" +
                    "<th>NPDES ID</th>" +
                    "<th>Master Permit Number</th>" +
                    "<th>Tracking Number</th>" +
                    "<th>Site State</th>" +
                    "<th>Operator</th>" +
                    "<th>Site Name</th>" +
                    "<th>Owner</th>" +
                    "<th>Status</th>" +
                    "<th>Last Modified</th>" +
                    "</tr>");
            Iterator<CgpNoiForm> iterator = formList.iterator();
            while (iterator.hasNext()) {
                CgpNoiForm form = iterator.next();
                buf.append("<tr><td>")
                        .append(form.getType().getValue())
                        .append("</td><td>")
                        .append(!StringUtils.isEmpty(form.getFormSet().getNpdesId()) ? form.getFormSet().getNpdesId() : "")
                        .append("</td><td>")
                        .append(form.getFormSet().getMasterPermitNumber())
                        .append("</td><td>")
                        .append(!StringUtils.isEmpty(form.getTrackingNumber()) ? form.getTrackingNumber() : "")
                        .append("</td><td>")
                        .append(form.getFormData().getProjectSiteInformation().getSiteStateCode())
                        .append("</td><td>")
                        .append(!StringUtils.isEmpty(form.getFormData().getOperatorInformation().getOperatorName()) ? form.getFormData().getOperatorInformation().getOperatorName() : "")
                        .append("</td><td>")
                        .append(!StringUtils.isEmpty(form.getFormData().getProjectSiteInformation().getSiteName()) ? form.getFormData().getProjectSiteInformation().getSiteName() : "")
                        .append("</td><td>")
                        .append(form.getFormSet().getOwner())
                        .append("</td><td>")
                        .append(form.getStatus().getValue())
                        .append("</td><td>")
                        .append(getZonedDateString(form.getLastUpdatedDate()))
                        .append("</td></tr>");
            }
            buf.append("</table>" +
                    "</body>" +
                    "</html>");
            String html = buf.toString();
            File htmlFile = File.createTempFile("EPACGP", ".html");
            FileOutputStream fos = new FileOutputStream(htmlFile);
            fos.write(html.getBytes());
            fos.close();
            return htmlFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }

    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.helpdeskRoleName})
    public File generateCsvExtract (List<CgpNoiForm> formList) throws ApplicationException {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        try {
            File csvFile = File.createTempFile("EPACGP", ".csv");
            fileWriter = new FileWriter(csvFile.getAbsolutePath());
            CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withQuote('"');
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(FORM_LIST_FILE_HEADER);

            for (CgpNoiForm form : formList) {
                FormType type = form.getType();
                Boolean noi = FormType.Notice_Of_Intent.equals(type);
                OperatorInformation operator = form.getFormData().getOperatorInformation();
                ProjectSiteInformation site = form.getFormData().getProjectSiteInformation();
                Location l = site.getSiteLocation();
                DischargeInformation discharge = form.getFormData().getDischargeInformation();
                ChemicalTreatmentInformation chem = form.getFormData().getChemicalTreatmentInformation();
                StormwaterPollutionPreventionPlanInformation swppp = form.getFormData().getStormwaterPollutionPreventionPlanInformation();
                EndangeredSpeciesProtectionInformation esp = form.getFormData().getEndangeredSpeciesProtectionInformation();
                AppendixDCriterion espCrit = esp.getCriterion();
                HistoricPreservation hp = form.getFormData().getHistoricPreservation();
                LowErosivityWaiver lew = form.getFormData().getLowErosivityWaiver();
                List formRecord = Arrays.asList(
                        form.getId(),
                        type.getValue(),
                        form.getFormSet().getNpdesId(),
                        form.getFormSet().getMasterPermitNumber(),
                        form.getTrackingNumber(),
                        form.getFormSet().getOwner(),
                        form.getStatus().getValue(),
                        getZonedDateString(form.getLastUpdatedDate()),
                        getZonedDateString(form.getCreatedDate()),
                        getZonedDateString(form.getReviewExpiration()),
                        getZonedDateString(form.getCertifiedDate()),
                        getZonedDateString(form.getSubmittedDate()),
                        operator.getOperatorName(),
                        operator.getOperatorAddress(),
                        operator.getOperatorCity(),
                        operator.getOperatorStateCode(),
                        operator.getOperatorZipCode(),
                        operator.getOperatorCounty(),
                        assembleContactString(operator.getOperatorPointOfContact()),
                        site.getSiteName(),
                        site.getSiteAddress(),
                        site.getSiteCity(),
                        site.getSiteStateCode(),
                        site.getSiteZipCode(),
                        site.getSiteCounty(),
                        l != null ? l.getLatitude() : "",
                        l != null ? l.getLongitude() : "",
                        l != null ? l.getLatLongDataSource() : "",
                        l != null ? l.getHorizontalReferenceDatum() : "",
                        noi ? getDateString(site.getSiteProjectStart()) : "N/A",
                        noi ? getDateString(site.getSiteProjectEnd()) : "N/A",
                        noi ? site.getSiteAreaDisturbed() : "N/A",
                        noi ? assembleListString(site.getSiteConstructionTypes()) : "N/A",
                        assembleYNString(site.getSiteStructureDemolitionBefore1980()),
                        assembleYNString(site.getSiteStructureDemolitionBefore198010kSquareFeet()),
                        assembleYNString(site.getSitePreDevelopmentAgricultural()),
                        assembleYNString(site.getSiteEarthDisturbingActivitiesOccurrence()),
                        assembleYNString(site.getSiteEmergencyRelated()),
                        noi ? site.getSitePreviousNpdesPermitId()  : "N/A",
                        noi ? site.getSiteIndianCulturalProperty()  : "N/A",
                        noi ? site.getSiteIndianCulturalPropertyTribe() : "N/A",
                        site.getSiteTerminationReason(),
                        assembleYNString(discharge.getDischargeMunicipalSeparateStormSewerSystem()),
                        assembleYNString(discharge.getDischargeUSWatersWithin50Feet()),
                        assembleDischargePoints(discharge),
                        assembleYNString(chem.getPolymersFlocculantsOtherTreatmentChemicals()),
                        assembleYNString(chem.getCationicTreatmentChemicals()),
                        assembleYNString(chem.getCationicTreatmentChemicalsAuthorization()),
                        assembleListString(chem.getTreatmentChemicals()),
                        assembleContactString(swppp.getContactInformation()),
                        (noi && espCrit != null) ? espCrit.getValue() : "N/A",
                        esp.getCriteriaSelectionSummary(),
                        (AppendixDCriterion.Criterion_B.equals(espCrit) ? esp.getOtherOperatorNpdesId() : "N/A"),
                        (AppendixDCriterion.Criterion_C.equals(espCrit) ? esp.getSpeciesAndHabitatInActionArea() : "N/A"),
                        (AppendixDCriterion.Criterion_C.equals(espCrit) ? esp.getDistanceFromSite() : "N/A"),
                        assembleYNString(hp.getScreeningCompleted()),
                        assembleYNString(hp.getAppendexEStep1()),
                        assembleYNString(hp.getAppendexEStep2()),
                        assembleYNString(hp.getAppendexEStep3()),
                        assembleYNString(hp.getAppendexEStep4()),
                        hp.getAppendexEStep4Response(),
                        getDateString(lew.getLewProjectStart()),
                        getDateString(lew.getLewProjectEnd()),
                        (!noi && lew.getLewAreaDisturbed() != null) ? lew.getLewAreaDisturbed().toString() : "N/A",
                        (!noi && lew.getLewRFactor() != null) ? lew.getLewRFactor().toString() : "N/A",
                        !noi ? lew.getLewRFactorCalculationMethod() : "N/A",
                        assembleYNString(lew.getInterimSiteStabilizationMeasures())
                );
                csvFilePrinter.printRecord(formRecord);
            }

            return csvFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        } finally {
            IOUtils.closeQuietly(csvFilePrinter);
            IOUtils.closeQuietly(fileWriter);
        }

    }

    @Override
    @Transactional
    @Secured({ApplicationSecurityUtils.helpdeskRoleName})
    public File generateFormCsv (CgpNoiForm form) throws ApplicationException {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        try {
            File csvFile = File.createTempFile("EPACGP", ".csv");
            fileWriter = new FileWriter(csvFile.getAbsolutePath());
            CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withQuote('"');
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(SINGLE_FORM_FILE_HEADER);

            FormType type = form.getType();
            OperatorInformation operator = form.getFormData().getOperatorInformation();
            ProjectSiteInformation site = form.getFormData().getProjectSiteInformation();
            Location location = site.getSiteLocation();

            List r1 = Arrays.asList("ID", form.getId());
            csvFilePrinter.printRecord(r1);
            List r2 = Arrays.asList("Type", type.getValue());
            csvFilePrinter.printRecord(r2);
            List r3 = Arrays.asList("NPDES ID", form.getFormSet().getNpdesId());
            csvFilePrinter.printRecord(r3);
            List r4 = Arrays.asList("Master Permit Number", form.getFormSet().getMasterPermitNumber());
            csvFilePrinter.printRecord(r4);
            List r5 = Arrays.asList("Tracking Number", form.getTrackingNumber());
            csvFilePrinter.printRecord(r5);
            List r6 = Arrays.asList("Status", form.getStatus());
            csvFilePrinter.printRecord(r6);
            List r7 = Arrays.asList("Last Modified", getZonedDateString(form.getLastUpdatedDate()));
            csvFilePrinter.printRecord(r7);
            List r8 = Arrays.asList("Review Expiration Date", getZonedDateString(form.getReviewExpiration()));
            csvFilePrinter.printRecord(r8);
            List r9 = Arrays.asList("Certified Date", getZonedDateString(form.getCertifiedDate()));
            csvFilePrinter.printRecord(r9);
            List r10 = Arrays.asList("Operator Name", operator.getOperatorName());
            csvFilePrinter.printRecord(r10);
            List opAd1 = Arrays.asList("Operator Address", operator.getOperatorAddress());
            csvFilePrinter.printRecord(opAd1);
            List opAd2 = Arrays.asList("Operator City", operator.getOperatorCity());
            csvFilePrinter.printRecord(opAd2);
            List opAd3 = Arrays.asList("Operator State", operator.getOperatorStateCode());
            csvFilePrinter.printRecord(opAd3);
            List opAd4 = Arrays.asList("Operator Zip", operator.getOperatorZipCode());
            csvFilePrinter.printRecord(opAd4);
            List opAd5 = Arrays.asList("Operator County", operator.getOperatorCounty());
            csvFilePrinter.printRecord(opAd5);
            List r12 = Arrays.asList("Operator Point of Contact", assembleContactString(operator.getOperatorPointOfContact()));
            csvFilePrinter.printRecord(r12);
            List r13 = Arrays.asList("Project/Site Name", site.getSiteName());
            csvFilePrinter.printRecord(r13);
            List prAd1 = Arrays.asList("Project/Site Address", site.getSiteAddress());
            csvFilePrinter.printRecord(prAd1);
            List prAd2 = Arrays.asList("Project/Site City", site.getSiteCity());
            csvFilePrinter.printRecord(prAd2);
            List prAd3 = Arrays.asList("Project/Site State", site.getSiteStateCode());
            csvFilePrinter.printRecord(prAd3);
            List prAd4 = Arrays.asList("Project/Site Zip", site.getSiteZipCode());
            csvFilePrinter.printRecord(prAd4);
            List prAd5 = Arrays.asList("Project/Site County", site.getSiteCounty());
            csvFilePrinter.printRecord(prAd5);
            if (location != null) {
                List loc1 = Arrays.asList("Project Location Latitude", location.getLatitude());
                csvFilePrinter.printRecord(loc1);
                List loc2 = Arrays.asList("Project Location Longitude", location.getLongitude());
                csvFilePrinter.printRecord(loc2);
                List loc3 = Arrays.asList("Project Location Data Source", location.getLatLongDataSource());
                csvFilePrinter.printRecord(loc3);
                List loc4 = Arrays.asList("Location Horizontal Reference Datum", location.getHorizontalReferenceDatum());
                csvFilePrinter.printRecord(loc4);
            } else {
                List loc = Arrays.asList("Project Location", "not specified");
                csvFilePrinter.printRecord(loc);
            }
            if (FormType.Notice_Of_Intent.equals(type)) {
                DischargeInformation discharge = form.getFormData().getDischargeInformation();
                ChemicalTreatmentInformation chem = form.getFormData().getChemicalTreatmentInformation();
                StormwaterPollutionPreventionPlanInformation swppp = form.getFormData().getStormwaterPollutionPreventionPlanInformation();
                EndangeredSpeciesProtectionInformation esp = form.getFormData().getEndangeredSpeciesProtectionInformation();
                AppendixDCriterion espCrit = esp.getCriterion();
                HistoricPreservation hp = form.getFormData().getHistoricPreservation();

                List r16 = Arrays.asList("Project Start Date", getDateString(site.getSiteProjectStart()));
                csvFilePrinter.printRecord(r16);
                List r17 = Arrays.asList("Project End Date", getDateString(site.getSiteProjectEnd()));
                csvFilePrinter.printRecord(r17);
                List r18 = Arrays.asList("Project Area to be Disturbed (acre)", site.getSiteAreaDisturbed());
                csvFilePrinter.printRecord(r18);
                List r19 = Arrays.asList("Site Construction Type(s)", assembleListString(site.getSiteConstructionTypes()));
                csvFilePrinter.printRecord(r19);
                List r20 = Arrays.asList("Site Structure Demolition before 1980?",
                        assembleYNString(site.getSiteStructureDemolitionBefore1980()));
                csvFilePrinter.printRecord(r20);
                List r21 = Arrays.asList("Site Structure Demolition b/f 1980 >=10k sq ft?",
                        assembleYNString(site.getSiteStructureDemolitionBefore198010kSquareFeet()));
                csvFilePrinter.printRecord(r21);
                List r22 = Arrays.asList("Pre-development Agricultural?",
                        assembleYNString(site.getSitePreDevelopmentAgricultural()));
                csvFilePrinter.printRecord(r22);
                List r23 = Arrays.asList("Earth-disturbing Activities?",
                        assembleYNString(site.getSiteEarthDisturbingActivitiesOccurrence()));
                csvFilePrinter.printRecord(r23);
                List r24 = Arrays.asList("Emergency-related Project?", assembleYNString(site.getSiteEmergencyRelated()));
                csvFilePrinter.printRecord(r24);
                List r25 = Arrays.asList("Previous NPDES Permit Present?",
                        assembleYNString(site.getSitePreviousNpdesPermit()));
                csvFilePrinter.printRecord(r25);
                List r26 = Arrays.asList("Previous NPDES ID", site.getSitePreviousNpdesPermitId());
                csvFilePrinter.printRecord(r26);
                List r27 = Arrays.asList("Religious/Cultural Indian Property?",
                        assembleYNString(site.getSiteIndianCulturalProperty()));
                csvFilePrinter.printRecord(r27);
                List r28 = Arrays.asList("Religious/Cultural Indian Tribe", site.getSiteIndianCulturalPropertyTribe());
                csvFilePrinter.printRecord(r28);
                List r29 = Arrays.asList("Termination Reason", site.getSiteTerminationReason());
                csvFilePrinter.printRecord(r29);
                List r30 = Arrays.asList("Discharge Municipal Separated Storm Sewer System",
                        assembleYNString(discharge.getDischargeMunicipalSeparateStormSewerSystem()));
                csvFilePrinter.printRecord(r30);
                List r31 = Arrays.asList("Discharge: US Waters within 50ft",
                        assembleYNString(discharge.getDischargeUSWatersWithin50Feet()));
                csvFilePrinter.printRecord(r31);
                //parse through points of discharge
                for (PointOfDischarge pod : discharge.getDischargePoints()) {
                    ReceivingWater water = pod.getFirstWater();
                    List dr = Arrays.asList(
                            "Point Of Discharge ID " + pod.getId(),
                            "Description: " + pod.getDescription(),
                            "Tier: " + (pod.getTier() != null ? pod.getTier().getValue() : "N/A"),
                            "Impared: " + assembleYNString(pod.getImpaired()),
                            "TMDL completed: " + assembleYNString(pod.getTmdlCompleted()),
                            "Receiving Water ID: " + water.getId(),
                            "Receiving Water Name: " + water.getReceivingWaterName()
                    );
                    csvFilePrinter.printRecord(dr);
                    List<Pollutant> pollutants = water.getPollutants();
                    for (Pollutant p : pollutants) {
                        List pol = Arrays.asList(
                                String.format("PoD %s Pollutant", pod.getId()),
                                "Code: " + p.getId() != null ? p.getId() : "",
                                "Name: " + p.getPollutantName(),
                                "Impared:" + assembleYNString(p.getImpaired()),
                                String.format("TMDL ID: %s, Name: %s", p.getTmdl().getId(), p.getTmdl().getName())
                        );
                        csvFilePrinter.printRecord(pol);
                    }
                }
                List r32 = Arrays.asList("Treatment Chemicals Usage", assembleYNString(chem.getPolymersFlocculantsOtherTreatmentChemicals()));
                csvFilePrinter.printRecord(r32);
                List r33 = Arrays.asList("Cationic Treatment Chemicals", assembleYNString(chem.getCationicTreatmentChemicals()));
                csvFilePrinter.printRecord(r33);
                List r34 = Arrays.asList("Cationic Treatment Chem. Authorization", assembleYNString(chem.getCationicTreatmentChemicalsAuthorization()));
                csvFilePrinter.printRecord(r34);
                List r35 = Arrays.asList("Treatment Chemicals", assembleListString(chem.getTreatmentChemicals()));
                csvFilePrinter.printRecord(r35);
                List r36 = Arrays.asList("SWPPP Contact", assembleContactString(swppp.getContactInformation()));
                csvFilePrinter.printRecord(r36);
                List r37 = Arrays.asList("Endangered Species Protection Criterion", espCrit != null ? espCrit.getValue() : "");
                csvFilePrinter.printRecord(r37);
                List r38 = Arrays.asList("ESP Criterion Basis Summary", esp.getCriteriaSelectionSummary());
                csvFilePrinter.printRecord(r38);
                if (AppendixDCriterion.Criterion_B.equals(espCrit)) {
                    List otherNpdes = Arrays.asList("Other Operator NPDES ID", esp.getOtherOperatorNpdesId());
                    csvFilePrinter.printRecord(otherNpdes);
                }
                if (AppendixDCriterion.Criterion_C.equals(espCrit)) {
                    List sh = Arrays.asList("Species/Habitat in Action Area", esp.getSpeciesAndHabitatInActionArea());
                    csvFilePrinter.printRecord(sh);
                    List shDistance = Arrays.asList("Species/Habitat Distance from Site (mi)", esp.getDistanceFromSite());
                    csvFilePrinter.printRecord(shDistance);
                }
                List r39 = Arrays.asList("Historic Preservation Screening Completed", assembleYNString(hp.getScreeningCompleted()));
                csvFilePrinter.printRecord(r39);
                List r40 = Arrays.asList("HP Step 1: stormwater controls requiring subsurface earth disturbance", assembleYNString(hp.getAppendexEStep1()));
                csvFilePrinter.printRecord(r40);
                if (hp.getAppendexEStep1() != null && hp.getAppendexEStep1()) {
                    List r41 = Arrays.asList("HP Step 2: historic properties do not exist", assembleYNString(hp.getAppendexEStep2()));
                    csvFilePrinter.printRecord(r41);
                    if (hp.getAppendexEStep2() != null && !hp.getAppendexEStep2()) {
                        List r42 = Arrays.asList("HP Step 3: stormwater controls have no effect on historic properties", assembleYNString(hp.getAppendexEStep3()));
                        csvFilePrinter.printRecord(r42);
                        if (hp.getAppendexEStep3() != null && !hp.getAppendexEStep3()) {
                            List r43 = Arrays.asList("HP Step 4: SHPO/THPO indicated whether subsurface earth disturbances affect historic properties", assembleYNString(hp.getAppendexEStep4()));
                            csvFilePrinter.printRecord(r43);
                            if (hp.getAppendexEStep4() != null && hp.getAppendexEStep4()) {
                                List r44 = Arrays.asList("HP Step 4 Response", hp.getAppendexEStep4Response());
                                csvFilePrinter.printRecord(r44);
                            }
                        }
                    }
                }
            }
            else
            {
                LowErosivityWaiver lew = form.getFormData().getLowErosivityWaiver();
                List r16 = Arrays.asList("LEW Project Start Date", getDateString(lew.getLewProjectStart()));
                csvFilePrinter.printRecord(r16);
                List r17 = Arrays.asList("LEW Project End Date", getDateString(lew.getLewProjectEnd()));
                csvFilePrinter.printRecord(r17);
                List r18 = Arrays.asList("LEW Area to be Disturbed", lew.getLewAreaDisturbed());
                csvFilePrinter.printRecord(r18);
                List r19 = Arrays.asList("LEW R-Factor", lew.getLewRFactor());
                csvFilePrinter.printRecord(r19);
                List r20 = Arrays.asList("R-Factor Calculation Method", lew.getLewRFactorCalculationMethod());
                csvFilePrinter.printRecord(r20);
                List r21 = Arrays.asList("Interim Site Stabilization Measures", assembleYNString(lew.getInterimSiteStabilizationMeasures()));
                csvFilePrinter.printRecord(r21);
            }

            return csvFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        } finally {
            IOUtils.closeQuietly(csvFilePrinter);
            IOUtils.closeQuietly(fileWriter);
        }

    }

    String getZonedDateString(ZonedDateTime date) {
        return date != null ? DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a").format(date) : "";
    }

    String getDateString(Date date) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return date != null ? df.format(date) : "";
    }

    String assembleContactString(Contact contact) {
        if (contact != null) {
            return String.format("%s %s %s, %s, %s ext. %s, %s",
                    contact.getFirstName(), contact.getMiddleInitial() != null ? contact.getMiddleInitial() : "",
                    contact.getLastName(), contact.getTitle(), contact.getPhone(),
                    contact.getPhoneExtension() != null ? contact.getPhoneExtension() : "", contact.getEmail());
        }
        return "";
    }

    String assembleListString(List<String> list) {
        if (!CollectionUtils.isEmpty(list)) {
            return StringUtils.join(list, ", ");
        } else {
            return "";
        }
    }

    String assembleYNString(Boolean b) {
        if (b != null) {
            return b ? "Yes" : "No";
        } else {
            return "";
        }
    }

    String assembleDischargePoints(DischargeInformation discharge) {
        List<PointOfDischarge> points = discharge.getDischargePoints();
        if (!CollectionUtils.isEmpty(points)) {
            StringBuilder sb = new StringBuilder();
            for (PointOfDischarge p : points) {
                sb.append(String.format("ID: %s, Receiving Water: %s, Tier: %s; ",
                        p.getId(), p.getFirstWater() != null ? p.getFirstWater().getReceivingWaterName() : "not specified", p.getTier() != null ? p.getTier().getValue() : "N/A"));
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
