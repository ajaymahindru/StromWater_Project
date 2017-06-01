package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.infrastructure.export.CgpFormExportService;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-cgp-test.xml"})
@Transactional(transactionManager = "transactionManager")
@Rollback
public class CgpFormExportServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(CgpFormExportServiceImplTest.class);

    @Resource(name = "applicationSecurityUtils")
    ApplicationSecurityUtils applicationSecurityUtils;
    @Resource(name = "formService")
    CgpNoiFormService formService;
    @Resource(name = "formExportService")
    CgpFormExportService exportService;

    CgpNoiFormSearchCriteria getCriteria(){
        CgpNoiFormSearchCriteria cr = new CgpNoiFormSearchCriteria();
        cr.setType(FormType.Notice_Of_Intent);
        cr.setOwner("LABIEVA34");
        return cr;
    }

    @Test
    public void testExportToExcel() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("LABIEVA34", "linera.abieva@cgifederal.com", "Linera", "Abieva");
            List<CgpNoiForm> forms = formService.retrieveForms(getCriteria());
            File excel = exportService.generateExcelExport(forms);
            assertTrue(excel.exists());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testExportToHtml() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("LABIEVA34", "linera.abieva@cgifederal.com", "Linera", "Abieva");
            List<CgpNoiForm> forms = formService.retrieveForms(getCriteria());
            File html = exportService.generateHtmlExport(forms);
            assertTrue(html.exists());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
}
