package gov.epa.oeca.cgp.application;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.AttachmentCategory;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.infrastructure.export.CgpFormExportService;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
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
    @Autowired
    ResourceLoader loader;

    CgpNoiFormSearchCriteria getCriteria(){
        CgpNoiFormSearchCriteria cr = new CgpNoiFormSearchCriteria();
        cr.setOwner("LABIEVA34");
        return cr;
    }

    CgpNoiForm getForm(String file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(loader.getResource(file).getFile(), CgpNoiForm.class);
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

    @Test
     public void testExtractCsv() throws Exception {
        try {
            applicationSecurityUtils.mockCertifier("LABIEVA34", "linera.abieva@cgifederal.com", "Linera", "Abieva");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            newForm = getForm("test-data/new-noi-form.json");
            formService.updateForm(id, newForm);
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);

            List<CgpNoiForm> forms = formService.retrieveForms(getCriteria());
            File csv = exportService.generateCsvExtract(forms);
            assertTrue(csv.exists());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testExtractFormCsv() throws Exception {
        try {
            applicationSecurityUtils.mockCertifier("LABIEVA34", "linera.abieva@cgifederal.com", "Linera", "Abieva");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            newForm = getForm("test-data/new-noi-form.json");
            formService.updateForm(id, newForm);
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);

            CgpNoiForm form = formService.retrieveForm(id);
            File csv = exportService.generateFormCsv(form);
            assertTrue(csv.exists());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
}
