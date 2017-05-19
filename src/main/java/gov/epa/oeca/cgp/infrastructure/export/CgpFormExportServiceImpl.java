package gov.epa.oeca.cgp.infrastructure.export;

import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationException;
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
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

/**
 * @author labieva (linera.abieva@cgifederal.com)
 */
@Service("formExportService")
@Transactional
public class CgpFormExportServiceImpl implements CgpFormExportService {
    private static final Logger logger = LoggerFactory.getLogger(CgpFormExportServiceImpl.class);

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
                    c9.setCellValue(form.getLastUpdatedDate() != null ? DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a").format(form.getLastUpdatedDate()) : "");
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
                    "<body>" +
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
                        .append(form.getLastUpdatedDate() != null ? DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a").format(form.getLastUpdatedDate()) : "")
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
}
