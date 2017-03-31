package gov.epa.oeca.cgp.infrastructure.icis;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.CgpNoiFormSet;
import gov.epa.oeca.cgp.domain.noi.FormType;
import gov.epa.oeca.cgp.domain.noi.Phase;
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

import java.time.ZonedDateTime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author dfladung
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-cgp-test.xml"})
@Transactional(transactionManager = "transactionManager")
@Rollback
public class IcisSubmissionServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(IcisSubmissionServiceImplTest.class);

    @Autowired
    ResourceLoader loader;

    @Autowired
    IcisSubmissionService icisSubmissionService;

    CgpNoiForm getForm(String file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(loader.getResource(file).getFile(), CgpNoiForm.class);
    }

    @Test
    public void marshallForm() throws Exception {
        try {

            CgpNoiForm completedForm = getForm("test-data/completed-noi-form.json");
            completedForm.setPhase(Phase.New);
            completedForm.setType(FormType.Notice_Of_Intent);
            completedForm.setCertifiedDate(ZonedDateTime.now());
            completedForm.setSubmittedDate(ZonedDateTime.now());

            CgpNoiFormSet set = new CgpNoiFormSet();
            set.setNpdesId("ALR10I004");
            set.setMasterPermitNumber("ALR10I000");
            set.getForms().add(completedForm);
            completedForm.setFormSet(set);

            completedForm = icisSubmissionService.submitToIcisNpdesDataflow(completedForm, null);
            assertNotNull(completedForm.getNodeTransactionId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

}