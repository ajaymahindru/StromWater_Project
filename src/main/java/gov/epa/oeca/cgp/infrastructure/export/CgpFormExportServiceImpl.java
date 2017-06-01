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
    private static final Object [] FILE_HEADER = {
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
            "Operator Point of Contact",
            "Project/Site Name",
            "Project/Site Address",
            "Project/Site Location",
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
            "Cultural Significance Indian Tribe",
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
    public File generateCsvExtract (List<CgpNoiForm> formList) throws ApplicationException {
        try {
            File csvFile = File.createTempFile("EPACGP", ".csv");
            FileWriter fileWriter = new FileWriter(csvFile.getAbsolutePath());
            CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withQuote('"');
            CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(FILE_HEADER);

            for (CgpNoiForm form : formList) {
                FormType type = form.getType();
                Boolean noi = FormType.Notice_Of_Intent.equals(type);
                OperatorInformation operator = form.getFormData().getOperatorInformation();
                ProjectSiteInformation site = form.getFormData().getProjectSiteInformation();
                DischargeInformation discharge = form.getFormData().getDischargeInformation();
                ChemicalTreatmentInformation chem = form.getFormData().getChemicalTreatmentInformation();
                StormwaterPollutionPreventionPlanInformation swppp = form.getFormData().getStormwaterPollutionPreventionPlanInformation();
                EndangeredSpeciesProtectionInformation esp = form.getFormData().getEndangeredSpeciesProtectionInformation();
                AppendixDCriterion espCrit = esp.getCriterion();
                HistoricPreservation hp = form.getFormData().getHistoricPreservation();
                LowErosivityWaiver lew = form.getFormData().getLowErosivityWaiver();
                List formRecord = Arrays.asList(
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
                        assembleOperatorAddress(operator),
                        assembleContactString(operator.getOperatorPointOfContact()),
                        site.getSiteName(),
                        assembleSiteAddress(site),
                        assembleLocation(site.getSiteLocation()),
                        noi ? site.getSiteProjectStart().toString() : "N/A",
                        noi ? site.getSiteProjectEnd().toString() : "N/A",
                        noi ? site.getSiteAreaDisturbed().toString() : "N/A",
                        noi ? assembleListString(site.getSiteConstructionTypes()) : "N/A",
                        assembleYNString(site.getSiteStructureDemolitionBefore1980()),
                        assembleYNString(site.getSiteStructureDemolitionBefore198010kSquareFeet()),
                        assembleYNString(site.getSitePreDevelopmentAgricultural()),
                        assembleYNString(site.getSiteEarthDisturbingActivitiesOccurrence()),
                        assembleYNString(site.getSiteEmergencyRelated()),
                        noi ? site.getSitePreviousNpdesPermitId()  : "N/A",
                        noi ? (site.getSiteIndianCulturalProperty() ? site.getSiteIndianCulturalPropertyTribe() : "N/A") : "N/A",
                        site.getSiteTerminationReason(),
                        assembleYNString(discharge.getDischargeMunicipalSeparateStormSewerSystem()),
                        assembleYNString(discharge.getDischargeUSWatersWithin50Feet()),
                        assembleDischargePoints(discharge),
                        assembleYNString(chem.getPolymersFlocculantsOtherTreatmentChemicals()),
                        assembleYNString(chem.getCationicTreatmentChemicals()),
                        assembleYNString(chem.getCationicTreatmentChemicalsAuthorization()),
                        assembleListString(chem.getTreatmentChemicals()),
                        assembleContactString(swppp.getContactInformation()),
                        noi ? espCrit.getValue() : "N/A",
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
                        !noi ? lew.getLewAreaDisturbed().toString() : "N/A",
                        !noi ? lew.getLewRFactor().toString() : "N/A",
                        !noi ? lew.getLewRFactorCalculationMethod() : "N/A",
                        assembleYNString(lew.getInterimSiteStabilizationMeasures())
                );
                csvFilePrinter.printRecord(formRecord);
            }

            FileOutputStream fos = new FileOutputStream(csvFile);
            fos.close();
            fileWriter.flush();
            fileWriter.close();
            csvFilePrinter.close();

            return csvFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }

    }

    String getZonedDateString(ZonedDateTime date) {
        return date != null ? DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a").format(date) : "";
    }

    String getDateString(Date date) {
        return date != null ? date.toString() : "";
    }

    String assembleOperatorAddress(OperatorInformation operatorInformation) {
        return String.format("%s, %s, %s, %s, County/Division: %s",
                operatorInformation.getOperatorAddress(), operatorInformation.getOperatorCity(),
                operatorInformation.getOperatorStateCode(), operatorInformation.getOperatorZipCode(),
                operatorInformation.getOperatorCounty());

    }

    String assembleSiteAddress(ProjectSiteInformation site) {
        return String.format("%s, %s, %s, %s, County/Division: %s",
                site.getSiteAddress(), site.getSiteCity(),
                site.getSiteStateCode(), site.getSiteZipCode(),
                site.getSiteCounty());
    }

    String assembleContactString(Contact contact) {
        if (contact != null) {
            return String.format("%s %s %s, %s, %s ext. %s, %s",
                    contact.getFirstName(), contact.getMiddleInitial(), contact.getLastName(),
                    contact.getTitle(), contact.getPhone(), contact.getPhoneExtension(), contact.getEmail());
        }
        return "";
    }

    String assembleLocation(Location l) {
        return String.format("(%s, %s), Data Source: %s, Horizontal Reference Datum: %s",
                l.getLatitude().toString(), l.getLongitude().toString(),
                l.getLatLongDataSource(), l.getHorizontalReferenceDatum());
    }

    String assembleListString(List<String> list) {
        if (!CollectionUtils.isEmpty(list)) {
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s);
            }
            return sb.toString();
        }
        return "";
    }

    String assembleYNString(Boolean b) {
        if (b != null) {
            return b ? "Yes" : "No";
        } else {
            return "";
        }
    }

    String assembleDischargePoints(DischargeInformation discharge) {
        StringBuilder sb = new StringBuilder();
        List<PointOfDischarge> points = discharge.getDischargePoints();
        for (PointOfDischarge p : points) {
            sb.append(String.format("ID: %s, %s, %s; ",
                    p.getId(), p.getFirstWater().getReceivingWaterName(), p.getTier().getValue()));
        }
        return sb.toString();
    }
}
