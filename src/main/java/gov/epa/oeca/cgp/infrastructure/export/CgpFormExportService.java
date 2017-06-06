package gov.epa.oeca.cgp.infrastructure.export;

import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;

import java.io.File;
import java.util.List;

/**
 * @author labieva (linera.abieva@cgifederal.com)
 */
public interface CgpFormExportService {

    File generateExcelExport(List<CgpNoiForm> formList) throws ApplicationException;

    File generateHtmlExport (List<CgpNoiForm> formList) throws ApplicationException;

    File generateCsvExtract (List<CgpNoiForm> formList) throws ApplicationException;

    File generateFormCsv (CgpNoiForm form) throws ApplicationException;
}
