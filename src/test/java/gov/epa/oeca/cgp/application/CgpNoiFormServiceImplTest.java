package gov.epa.oeca.cgp.application;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableConfig;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableOrder;
import gov.epa.oeca.cgp.domain.noi.*;
import gov.epa.oeca.cgp.domain.noi.formsections.AppendixDCriterion;
import gov.epa.oeca.cgp.domain.ref.NpdesSequence;
import gov.epa.oeca.cgp.infrastructure.certification.CromerrService;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author dfladung
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-cgp-test.xml"})
@Transactional(transactionManager = "transactionManager")
@Rollback
//@Commit
public class CgpNoiFormServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(CgpNoiFormServiceImplTest.class);

    @Autowired
    ResourceLoader loader;
    @Resource(name = "formService")
    CgpNoiFormService formService;
    @Resource(name = "applicationSecurityUtils")
    ApplicationSecurityUtils applicationSecurityUtils;
    @Autowired
    CromerrService cromerrService;

    @BeforeClass
    static public void init() {
        System.getProperties().setProperty("suppressNotifications", Boolean.TRUE.toString());
    }

    CgpNoiForm getForm(String file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(loader.getResource(file).getFile(), CgpNoiForm.class);
    }

    DataTableConfig getDataTableConfig(String file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(loader.getResource(file).getFile(), DataTableConfig.class);
    }

    Contact getCertifier() {
        Contact certifier = new Contact();
        certifier.setFirstName("Linera");
        certifier.setLastName("Abieva");
        certifier.setTitle("Bawse");
        certifier.setEmail("linera.abieva@cgifederal.com");
        return certifier;
    }

    @Test
    public void testIncrementNpdesId() throws Exception {
        try {
            assertEquals("00A", NpdesSequence.incrementNpdesSeq("009".toCharArray()));
            assertEquals("010", NpdesSequence.incrementNpdesSeq("00Z".toCharArray()));
            assertEquals("01A", NpdesSequence.incrementNpdesSeq("019".toCharArray()));
            assertEquals("020", NpdesSequence.incrementNpdesSeq("01Z".toCharArray()));
            assertEquals("A00", NpdesSequence.incrementNpdesSeq("9ZZ".toCharArray()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateInitialForm() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            CgpNoiForm form = formService.retrieveForm(id);

            assertNotNull(form);
            assertEquals("MA", form.getFormData().getProjectSiteInformation().getSiteStateCode());
            assertEquals("MAR10I000", form.getFormSet().getMasterPermitNumber());
            assertNotNull(form.getTrackingNumber());
            assertEquals(Source.Electronic, form.getSource());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateInitialFormHelpDesk() throws Exception {
        try {
            applicationSecurityUtils.mockHelpDesk("DFLADUNG-HD");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            CgpNoiForm form = formService.retrieveForm(id);

            assertNotNull(form);
            assertEquals("MA", form.getFormData().getProjectSiteInformation().getSiteStateCode());
            assertEquals("MAR10I000", form.getFormSet().getMasterPermitNumber());
            assertNotNull(form.getTrackingNumber());
            assertEquals(Source.Paper, form.getSource());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateInitialFormIndianCountry() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form-indian.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            CgpNoiForm form = formService.retrieveForm(id);

            assertNotNull(form);
            assertEquals("MA", form.getFormData().getProjectSiteInformation().getSiteStateCode());
            assertEquals("MAR10I000", form.getFormSet().getMasterPermitNumber());
            assertNotNull(form.getTrackingNumber());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateInitialFormIndianCountryWithOverride() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form-indian-override.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            CgpNoiForm form = formService.retrieveForm(id);

            assertNotNull(form);
            assertEquals("ID", form.getFormData().getProjectSiteInformation().getSiteStateCode());
            assertEquals("NVR10I000", form.getFormSet().getMasterPermitNumber());
            assertNotNull(form.getTrackingNumber());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateInitialForm_ineligible() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/ineligible.json");
            formService.createNewNoticeOfIntent(newForm).getId();
            fail("Should not have reached this point.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_Ineligible, e.getErrorCode());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRouteToCertifier() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            formService.routeToCertifier(id, "TEST.CERTIFIER");

            CgpNoiForm form = formService.retrieveForm(id);
            assertNotNull(form);
            assertEquals("TEST.CERTIFIER", form.getFormSet().getOwner());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRejectForm() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            formService.routeToCertifier(id, "TEST.CERTIFIER");
            applicationSecurityUtils.mockCertifier("TEST.CERTIFIER");
            formService.rejectForm(id, "Because I said so!");

            CgpNoiForm form = formService.retrieveForm(id);
            assertNotNull(form);
            assertEquals("DFLADUNG", form.getFormSet().getOwner());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateInitialForm_ineligibleOverride() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/ineligible-override.json");
            formService.createNewNoticeOfIntent(newForm).getId();
            fail("Should not have reached this point.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_Ineligible, e.getErrorCode());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testIsEligible() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            assertTrue(formService.isEligible(newForm));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testIsNotEligible() throws Exception {
        try {
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/ineligible.json");
            assertFalse(formService.isEligible(newForm));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testAttachment() throws Exception {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            // test adding the attachment
            formService.addAttachment(id, attachment);
            newForm = formService.retrieveForm(id);
            assertEquals(newForm.getAttachments().size(), 1);
            attachment = newForm.getAttachments().get(0);
            assertEquals(attachment.getName(), "logback.xml");
            assertEquals(attachment.getCategory(), AttachmentCategory.Default);
            assertNull(attachment.getCromerrAttachmentId());
            assertTrue(attachment.getCreatedDate().compareTo(start) >= 0);
            // test the content is correct
            File data = formService.retrieveAttachmentData(attachment.getId());
            File src = loader.getResource("logback.xml").getFile();
            assertTrue(FileUtils.contentEquals(src, data));
            // test remove
            formService.removeAttachment(attachment.getId());
            newForm = formService.retrieveForm(id);
            assertEquals(newForm.getAttachments().size(), 0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCertify() throws Exception {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            CgpNoiForm certified = formService.retrieveForm(id);
            assertEquals(Status.Submitted, certified.getStatus());
            //assertEquals("MAR10I001", certified.getFormSet().getNpdesId());
            assertNotNull(certified.getFormSet().getNpdesId());
            assertTrue(certified.getCertifiedDate().compareTo(start) > 0);
            assertTrue(certified.getLastUpdatedDate().compareTo(start) > 0);
            assertTrue(certified.getReviewExpiration().compareTo(start) > 0);
            // validate the attachment
            assertEquals(certified.getAttachments().size(), 2);
            attachment = certified.getAttachments().get(0);
            assertNull(attachment.getData());
            assertNotNull(attachment.getCromerrAttachmentId());
            File data = formService.retrieveAttachmentData(attachment.getId());
            File src = loader.getResource("logback.xml").getFile();
            assertTrue(FileUtils.contentEquals(src, data));
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            CgpNoiForm activated = formService.retrieveForm(id);
            assertEquals(Status.Active, activated.getStatus());
            formService.distributeForm(id);
            CgpNoiForm distributed = formService.retrieveForm(id);
            assertNotNull(distributed.getNodeTransactionId());
            assertNotNull(distributed.getSubmittedDate());
            // test retrieving by associated users
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            criteria.setAssociatedUser("DFLADUNG");
            List<CgpNoiForm> forms = formService.retrieveForms(criteria);
            assertEquals(1, forms.size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testTerminateRa() throws Exception {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            formService.distributeForm(id);

            // initiate a termination
            applicationSecurityUtils.mockRegionalAuthority("DFLADUNG-RA");
            CgpNoiForm certified = formService.retrieveForm(id);
            CgpNoiForm updatedForm = getForm("test-data/new-noi-form.json");
            updatedForm.getFormData().getProjectSiteInformation().setSiteTerminationReason("Test reason");
            updatedForm.setCertifiedDate(start);
            updatedForm.getFormData().getOperatorInformation().setCertifier(getCertifier());
            CgpNoiForm noticeOfTermination = formService.terminateNoticeOfIntent(
                    certified.getId(), updatedForm);
            assertEquals(noticeOfTermination.getPhase(), Phase.Terminate);
            assertEquals(Status.Draft, noticeOfTermination.getStatus());
            assertEquals(FormType.Notice_Of_Intent, noticeOfTermination.getType());
            assertNull(noticeOfTermination.getCromerrActivityId());
            assertNull(noticeOfTermination.getCertifiedDate());
            assertEquals(noticeOfTermination.getAttachments().size(), 1);
            assertTrue(noticeOfTermination.getActiveRecord());

            // certify the change
            formService.certifyForm(noticeOfTermination.getId(), noticeOfTermination.getCromerrActivityId());
            Thread.sleep(1500);// for the expiration period.
            assertEquals(Status.Submitted, noticeOfTermination.getStatus());
            //assertEquals("MAR10I001", noticeOfTermination.getFormSet().getNpdesId());
            assertNotNull(noticeOfTermination.getFormSet().getNpdesId());
            assertTrue(noticeOfTermination.getCertifiedDate().compareTo(start) > 0);
            assertTrue(noticeOfTermination.getLastUpdatedDate().compareTo(start) > 0);
            assertTrue(noticeOfTermination.getReviewExpiration().compareTo(start) > 0);
            // validate the attachment
            assertEquals(noticeOfTermination.getAttachments().size(), 2);

            // distribute the change
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(noticeOfTermination.getId());
            formService.distributeForm(noticeOfTermination.getId());
            CgpNoiForm distributed = formService.retrieveForm(noticeOfTermination.getId());
            assertEquals(Status.Terminated, distributed.getStatus());
            assertNotNull(distributed.getNodeTransactionId());
            assertNotNull(distributed.getSubmittedDate());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDiscontinueRa() throws Exception {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-lew-form.json");
            Long id = formService.createNewLowErosivityWaiver(newForm).getId();
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            formService.distributeForm(id);

            // initiate a termination
            applicationSecurityUtils.mockRegionalAuthority("DFLADUNG-RA");
            CgpNoiForm updatedForm = formService.retrieveForm(id);
            updatedForm.setCertifiedDate(start);
            updatedForm.getFormData().getOperatorInformation().setCertifier(getCertifier());
            CgpNoiForm lewDiscontinuation = formService.discontinueLowErosivityWaiver(updatedForm.getId(), updatedForm);
            assertEquals(lewDiscontinuation.getPhase(), Phase.Terminate);
            assertEquals(Status.Draft, lewDiscontinuation.getStatus());
            assertEquals(FormType.Low_Erosivity_Waiver, lewDiscontinuation.getType());
            assertNull(lewDiscontinuation.getCromerrActivityId());
            assertNull(lewDiscontinuation.getCertifiedDate());
            assertEquals(lewDiscontinuation.getAttachments().size(), 0);
            assertTrue(lewDiscontinuation.getActiveRecord());

            // certify the change
            formService.certifyForm(lewDiscontinuation.getId(), lewDiscontinuation.getCromerrActivityId());
            Thread.sleep(1500);// for the expiration period.
            assertEquals(Status.Submitted, lewDiscontinuation.getStatus());
            //assertEquals("VAR10I178", lewDiscontinuation.getFormSet().getNpdesId());
            assertNotNull(lewDiscontinuation.getFormSet().getNpdesId());
            assertTrue(lewDiscontinuation.getCertifiedDate().compareTo(start) > 0);
            assertTrue(lewDiscontinuation.getLastUpdatedDate().compareTo(start) > 0);
            assertTrue(lewDiscontinuation.getReviewExpiration().compareTo(start) > 0);
            // validate the attachment
            assertEquals(lewDiscontinuation.getAttachments().size(), 1);

            // distribute the change
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(lewDiscontinuation.getId());
            formService.distributeForm(lewDiscontinuation.getId());
            CgpNoiForm distributed = formService.retrieveForm(lewDiscontinuation.getId());
            assertEquals(Status.Discontinued, distributed.getStatus());
            assertNotNull(distributed.getNodeTransactionId());
            assertNotNull(distributed.getSubmittedDate());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testDenyForm() {
        try {
            // setup
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.

            // deny the form
            applicationSecurityUtils.mockRegionalAuthority("DFLADUNG-RA");
            formService.denyForm(id, "Cuz I said so.");
            CgpNoiForm denied = formService.retrieveForm(id);
            assertNull(denied.getCromerrActivityId());
            assertNull(denied.getCertifiedDate());
            assertEquals(Status.Draft, denied.getStatus());
            assertEquals(denied.getAttachments().size(), 1);
            attachment = denied.getAttachments().get(0);
            assertEquals(attachment.getName(), "logback.xml");
            assertEquals(attachment.getCategory(), AttachmentCategory.Default);
            assertNull(attachment.getCromerrAttachmentId());
            // test the content is correct
            File data = formService.retrieveAttachmentData(attachment.getId());
            File src = loader.getResource("logback.xml").getFile();
            assertTrue(FileUtils.contentEquals(src, data));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testHoldForm() {
        try {
            // setup
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            // hold the form
            applicationSecurityUtils.mockRegionalAuthority("DFLADUNG-RA");
            formService.holdForm(id);
            CgpNoiForm held = formService.retrieveForm(id);
            assertEquals(Status.OnHold, held.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testReleaseForm() {
        try {
            // setup
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.

            // release the form
            applicationSecurityUtils.mockRegionalAuthority("DFLADUNG-RA");
            formService.holdForm(id);
            CgpNoiForm held = formService.retrieveForm(id);
            ZonedDateTime expiration = ZonedDateTime.from(held.getReviewExpiration());
            formService.releaseForm(id);
            CgpNoiForm released = formService.retrieveForm(id);
            assertEquals(Status.Submitted, released.getStatus());
            assertTrue(released.getReviewExpiration().compareTo(expiration) == 0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }


    @Test
    public void testRetrieveForms() {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            newForm = getForm("test-data/new-noi-form.json");
            formService.updateForm(id, newForm);
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);

            // search by owner
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            criteria.setOwner("DFLADUNG");
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setOwner("dfladun");
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setOwner("dfladung1");
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by mgp
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setMasterGeneralPermit("MAR10I000");
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setMasterGeneralPermit("MAR100000");
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by tracking number
            newForm = formService.retrieveForm(id);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setTrackingNumber(newForm.getTrackingNumber());
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setTrackingNumber(UUID.randomUUID().toString());
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by type
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setType(FormType.Notice_Of_Intent);
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setType(FormType.Low_Erosivity_Waiver);
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by status
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setStatus(Status.Draft);
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setStatus(Status.Submitted);
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by operator name
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setOperatorName("CGI NOI");
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setOperatorName("CGI");
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setOperatorName("bogus");
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by operator name
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setSiteName("CGI NOI Site");
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setSiteName("CGI");
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setOperatorName("bogus");
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by updated date
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setUpdatedFrom(start);
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setUpdatedFrom(ZonedDateTime.now().plusDays(1));
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by tribal indicator
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setSiteIndianCountry(true);
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setSiteIndianCountry(false);
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by tribal lands name
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setSiteIndianCountryLands("MASHPEE WAMPANOAG");
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setSiteIndianCountryLands("BOGUS LANDS");
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by federal indicator
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setOperatorFederal(true);
            assertEquals(1, formService.retrieveForms(criteria).size());
            criteria.setOperatorFederal(false);
            assertEquals(0, formService.retrieveForms(criteria).size());

            // search by region
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setSiteRegion(2L);
            assertEquals(0, formService.retrieveForms(criteria).size());
            criteria.setSiteRegion(1L);
            assertEquals(1, formService.retrieveForms(criteria).size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveSearchResult() {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            //form 1
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm1 = getForm("test-data/new-noi-form.json");
            Long id1 = formService.createNewNoticeOfIntent(newForm1).getId();
            newForm1 = getForm("test-data/new-noi-form.json");
            formService.updateForm(id1, newForm1);
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id1, attachment);
            //form 2
            CgpNoiForm newForm2 = getForm("test-data/new-lew-form.json");
            Long id2 = formService.createNewLowErosivityWaiver(newForm2).getId();
            newForm2 = getForm("test-data/new-lew-form.json");
            formService.updateForm(id2, newForm2);
            formService.addAttachment(id2, attachment);
            //form 3
            applicationSecurityUtils.mockHelpDesk("LABIEVA34");
            CgpNoiForm newForm3 = getForm("test-data/new-noi-form.json");
            Long id3 = formService.createNewNoticeOfIntent(newForm3).getId();
            newForm3 = getForm("test-data/new-noi-form.json");
            formService.updateForm(id3, newForm3);
            formService.addAttachment(id3, attachment);
            //form 4
            CgpNoiForm newForm4 = getForm("test-data/new-noi-form.json");
            Long id4 = formService.createNewNoticeOfIntent(newForm4).getId();
            newForm4 = getForm("test-data/new-noi-form.json");
            formService.updateForm(id4, newForm4);
            formService.addAttachment(id4, attachment);
            //form 5
            CgpNoiForm newForm5 = getForm("test-data/new-noi-form.json");
            Long id5 = formService.createNewNoticeOfIntent(newForm5).getId();
            newForm5 = getForm("test-data/new-noi-form.json");
            formService.updateForm(id5, newForm5);
            formService.addAttachment(id5, attachment);
            //form 6
            CgpNoiForm newForm6 = getForm("test-data/new-noi-form.json");
            Long id6 = formService.createNewNoticeOfIntent(newForm6).getId();
            newForm6 = getForm("test-data/new-noi-form.json");
            formService.updateForm(id6, newForm6);
            formService.addAttachment(id6, attachment);

            // max result=5, start with 1, order by form type asc
            DataTableConfig config = getDataTableConfig("test-data/noi-search-config.json");
            DataTableOrder byType = new DataTableOrder();
            byType.setColumn(1);
            byType.setDir(DataTableOrder.DataTablesOrderEnum.asc);
            config.setOrder(new ArrayList<>());
            config.getOrder().add(byType);
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            DataTableCriteria<CgpNoiFormSearchCriteria> search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(5, formService.retrieveSearchResult(search).getData().size());
            assertEquals(FormType.Low_Erosivity_Waiver, formService.retrieveSearchResult(search).getData().get(0).getType());

            //second page with 1 result (starting at 6th)
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setStart(5L);
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(1, formService.retrieveSearchResult(search).getData().size());

            //by owner
            config = getDataTableConfig("test-data/noi-search-config.json");
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setOwner("DFLADUNG");
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(2, formService.retrieveSearchResult(search).getData().size());

            //by MGP
            config = getDataTableConfig("test-data/noi-search-config.json");
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setMasterGeneralPermit("MAR10I000");
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(5, formService.retrieveSearchResult(search).getData().size());

            // search by tracking number
            config = getDataTableConfig("test-data/noi-search-config.json");
            newForm1 = formService.retrieveForm(id1);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setTrackingNumber(newForm1.getTrackingNumber());
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(1, formService.retrieveSearchResult(search).getData().size());
            search.getCriteria().setTrackingNumber(UUID.randomUUID().toString());
            assertEquals(0, formService.retrieveSearchResult(search).getData().size());

            // search by type
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setLength(10L);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setType(FormType.Notice_Of_Intent);
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(5, formService.retrieveSearchResult(search).getData().size());

            // search by status
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setLength(10L);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setStatus(Status.Draft);
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(6, formService.retrieveSearchResult(search).getData().size());
            search.getCriteria().setStatus(Status.Submitted);
            assertEquals(0, formService.retrieveSearchResult(search).getData().size());

            // search by operator name
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setLength(10L);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setOperatorName("CGI Operator");
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(1, formService.retrieveSearchResult(search).getData().size());
            criteria.setOperatorName("CGI");
            assertEquals(6, formService.retrieveSearchResult(search).getData().size());
            criteria.setOperatorName("bogus");
            assertEquals(0, formService.retrieveSearchResult(search).getData().size());

            // search by site name
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setLength(10L);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setSiteName("CGI Site");
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(1, formService.retrieveSearchResult(search).getData().size());
            criteria.setSiteName("CGI");
            assertEquals(6, formService.retrieveSearchResult(search).getData().size());
            criteria.setOperatorName("bogus");
            assertEquals(0, formService.retrieveSearchResult(search).getData().size());

            // search by updated date
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setLength(10L);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setUpdatedFrom(start);
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(6, formService.retrieveSearchResult(search).getData().size());
            criteria.setUpdatedFrom(ZonedDateTime.now().plusDays(1));
            assertEquals(0, formService.retrieveSearchResult(search).getData().size());

            // search by tribal indicator
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setLength(10L);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setSiteIndianCountry(true);
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(6, formService.retrieveSearchResult(search).getData().size());
            criteria.setSiteIndianCountry(false);
            assertEquals(0, formService.retrieveSearchResult(search).getData().size());

            // search by tribal lands name
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setLength(10L);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setSiteIndianCountryLands("PAMUNKEY RESERVATION (STATE)");
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(1, formService.retrieveSearchResult(search).getData().size());
            criteria.setSiteIndianCountryLands("MASHPEE WAMPANOAG RESERVATION (STATE)");
            assertEquals(5, formService.retrieveSearchResult(search).getData().size());

            // search by federal indicator
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setLength(10L);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setOperatorFederal(true);
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(6, formService.retrieveSearchResult(search).getData().size());
            criteria.setOperatorFederal(false);
            assertEquals(0, formService.retrieveSearchResult(search).getData().size());

            // search by source
            config = getDataTableConfig("test-data/noi-search-config.json");
            config.setLength(10L);
            criteria = new CgpNoiFormSearchCriteria();
            criteria.setSource(Source.Paper);
            search = new DataTableCriteria<>();
            search.setConfig(config);
            search.setCriteria(criteria);
            assertEquals(4, formService.retrieveSearchResult(search).getData().size());
            criteria.setSource(Source.Electronic);
            search.setCriteria(criteria);
            assertEquals(2, formService.retrieveSearchResult(search).getData().size());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveRaForms() {
        try {
            // setup
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.

            // search by owner
            applicationSecurityUtils.mockRegionalAuthority("DFLADUNG");
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            criteria.setRegulatoryAuthoritySearch(true);
            List<CgpNoiForm> forms = formService.retrieveForms(criteria);
            assertTrue(forms.size() > 0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrieveRaForms_drafts() {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockPreparer("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);


            // search by owner
            applicationSecurityUtils.mockRegionalAuthority("DFLADUNG");
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            criteria.setRegulatoryAuthoritySearch(true);
            List<CgpNoiForm> forms = formService.retrieveForms(criteria);
            assertEquals(0, forms.size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrievePublicForms() {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            newForm = getForm("test-data/new-noi-form.json");
            formService.updateForm(id, newForm);
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);

            // should be empty since it's in draft
            CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
            assertEquals(0, formService.retrievePublicForms(criteria).size());

            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            formService.retrieveForm(id);
            // retrieve the form
            assertEquals(1, formService.retrievePublicForms(criteria).size());
            // retrieve the attachment
            attachment = formService.retrievePublicForms(criteria).get(0).getAttachments().get(0);
            File data = formService.retrievePublicAttachmentData(id, attachment.getId());
            File src = loader.getResource("logback.xml").getFile();
            assertTrue(FileUtils.contentEquals(src, data));
            // retrieve by ID
            newForm = formService.retrievePublicForm(id);
            assertNotNull(newForm);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRetrievePublicForms_nonPublicAttachment() {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            newForm = getForm("test-data/new-noi-form.json");
            formService.updateForm(id, newForm);
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // retrieve the attachment
            attachment = formService.retrieveForm(id).getAttachments().get(0);
            formService.retrievePublicAttachmentData(id, attachment.getId());
            fail("Should not have reached this point.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_InvalidArgument, e.getErrorCode());
            assertEquals("Can not retrieve non-public attachments.", e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }


    @Test
    public void testRouteChangeToCertifier() throws Exception {
        try {
            // setup
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            formService.distributeForm(id);

            // initiate a change
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm certified = formService.retrieveForm(id);
            CgpNoiForm changeNoi = formService.changeNoticeOfIntent(certified.getId());
            assertEquals(changeNoi.getPhase(), Phase.Change);
            assertNull(changeNoi.getCromerrActivityId());
            assertNull(changeNoi.getCertifiedDate());
            assertNull(changeNoi.getNodeTransactionId());
            assertNull(changeNoi.getSubmittedDate());
            assertEquals(changeNoi.getAttachments().size(), 1);
            assertTrue(changeNoi.getActiveRecord());
            assertEquals(Status.Draft, changeNoi.getStatus());
            assertEquals(FormType.Notice_Of_Intent, changeNoi.getType());
            changeNoi.getFormData().getOperatorInformation().setOperatorAddress2("Address 2 value");
            formService.updateForm(changeNoi.getId(), changeNoi);

            // route to certifier
            formService.routeToCertifier(changeNoi.getId(), "TEST.CERTIFIER");
            CgpNoiForm form = formService.retrieveForm(changeNoi.getId());
            assertNotNull(form);
            assertEquals("TEST.CERTIFIER", form.getFormSet().getOwner());

            // reject by certifier
            applicationSecurityUtils.mockCertifier("TEST.CERTIFIER");
            formService.rejectForm(changeNoi.getId(), "Because I said so!");
            form = formService.retrieveForm(changeNoi.getId());
            assertNotNull(form);
            assertEquals("DFLADUNG", form.getFormSet().getOwner());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCertifyChange() throws Exception {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            formService.distributeForm(id);

            // initiate a change
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm certified = formService.retrieveForm(id);
            CgpNoiForm changeNoi = formService.changeNoticeOfIntent(certified.getId());
            assertEquals(changeNoi.getPhase(), Phase.Change);
            assertNull(changeNoi.getCromerrActivityId());
            assertNull(changeNoi.getCertifiedDate());
            assertNull(changeNoi.getNodeTransactionId());
            assertNull(changeNoi.getSubmittedDate());
            assertEquals(changeNoi.getAttachments().size(), 1);
            assertTrue(changeNoi.getActiveRecord());
            assertEquals(Status.Draft, changeNoi.getStatus());
            assertEquals(FormType.Notice_Of_Intent, changeNoi.getType());
            changeNoi.getFormData().getEndangeredSpeciesProtectionInformation().setCriterion(AppendixDCriterion.Criterion_C);
            changeNoi.getFormData().getEndangeredSpeciesProtectionInformation().setCriteriaSelectionSummary("testing");
            changeNoi.getFormData().getEndangeredSpeciesProtectionInformation().setSpeciesAndHabitatInActionArea("dogs");
            changeNoi.getFormData().getEndangeredSpeciesProtectionInformation().setDistanceFromSite("1 mile");
            formService.updateForm(changeNoi.getId(), changeNoi);

            // certify the change
            activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            formService.certifyForm(changeNoi.getId(), activityId);
            Thread.sleep(1500);// for the expiration period.
            assertEquals(Status.Submitted, changeNoi.getStatus());
            //assertEquals("MAR10I001", changeNoi.getFormSet().getNpdesId());
            assertNotNull(changeNoi.getFormSet().getNpdesId());
            assertTrue(changeNoi.getCertifiedDate().compareTo(start) > 0);
            assertTrue(changeNoi.getLastUpdatedDate().compareTo(start) > 0);
            assertTrue(changeNoi.getReviewExpiration().compareTo(start) > 0);
            // validate the attachment
            assertEquals(changeNoi.getAttachments().size(), 2);
            attachment = changeNoi.getAttachments().get(0);
            assertNull(attachment.getData());
            assertNotNull(attachment.getCromerrAttachmentId());
            File data = formService.retrieveAttachmentData(attachment.getId());
            File src = loader.getResource("logback.xml").getFile();
            assertTrue(FileUtils.contentEquals(src, data));

            // distribute the change
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(changeNoi.getId());
            formService.distributeForm(changeNoi.getId());
            CgpNoiForm distributed = formService.retrieveForm(changeNoi.getId());
            assertEquals(Status.Active, distributed.getStatus());
            assertNotNull(distributed.getNodeTransactionId());
            assertNotNull(distributed.getSubmittedDate());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRouteNoTToCertifier() throws Exception {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            formService.distributeForm(id);

            // initiate an NoT
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm certified = formService.retrieveForm(id);
            CgpNoiForm updatedForm = getForm("test-data/new-noi-form.json");
            updatedForm.getFormData().getProjectSiteInformation().setSiteTerminationReason("reason");
            updatedForm.setCertifiedDate(start);
            updatedForm.getFormData().getOperatorInformation().setCertifier(getCertifier());
            CgpNoiForm noticeOfTermination = formService.terminateNoticeOfIntent(certified.getId(), updatedForm);
            assertEquals(noticeOfTermination.getPhase(), Phase.Terminate);
            assertNull(noticeOfTermination.getCromerrActivityId());
            assertNull(noticeOfTermination.getCertifiedDate());
            assertNull(noticeOfTermination.getNodeTransactionId());
            assertNull(noticeOfTermination.getSubmittedDate());
            assertEquals(noticeOfTermination.getAttachments().size(), 1);
            assertTrue(noticeOfTermination.getActiveRecord());
            assertEquals(Status.Draft, noticeOfTermination.getStatus());
            assertEquals(FormType.Notice_Of_Intent, noticeOfTermination.getType());

            // route to certifier
            formService.routeToCertifier(noticeOfTermination.getId(), "TEST.CERTIFIER");
            CgpNoiForm form = formService.retrieveForm(noticeOfTermination.getId());
            assertNotNull(form);
            assertEquals("TEST.CERTIFIER", form.getFormSet().getOwner());

            // reject by certifier
            applicationSecurityUtils.mockCertifier("TEST.CERTIFIER");
            formService.rejectForm(noticeOfTermination.getId(), "Because I said so!");
            form = formService.retrieveForm(noticeOfTermination.getId());
            assertNotNull(form);
            assertEquals("DFLADUNG", form.getFormSet().getOwner());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCertifyNoT() throws Exception {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            Attachment attachment = new Attachment();
            attachment.setName("logback.xml");
            attachment.setCategory(AttachmentCategory.Default);
            attachment.setData(loader.getResource("logback.xml").getFile());
            formService.addAttachment(id, attachment);
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            formService.distributeForm(id);

            // initiate a termination
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm certified = formService.retrieveForm(id);
            CgpNoiForm updatedForm = getForm("test-data/new-noi-form.json");
            updatedForm.getFormData().getProjectSiteInformation().setSiteTerminationReason("reason");
            updatedForm.setCertifiedDate(start);
            updatedForm.getFormData().getOperatorInformation().setCertifier(getCertifier());
            CgpNoiForm noticeOfTermination = formService.terminateNoticeOfIntent(certified.getId(), updatedForm);
            assertEquals(noticeOfTermination.getPhase(), Phase.Terminate);
            assertNull(noticeOfTermination.getCromerrActivityId());
            assertNull(noticeOfTermination.getCertifiedDate());
            assertNull(noticeOfTermination.getNodeTransactionId());
            assertNull(noticeOfTermination.getSubmittedDate());
            assertEquals(noticeOfTermination.getAttachments().size(), 1);
            assertTrue(noticeOfTermination.getActiveRecord());
            assertEquals(Status.Draft, noticeOfTermination.getStatus());
            assertEquals(FormType.Notice_Of_Intent, noticeOfTermination.getType());

            // certify the change
            activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            formService.certifyForm(noticeOfTermination.getId(), activityId);
            Thread.sleep(1500);// for the expiration period.
            assertEquals(Status.Submitted, noticeOfTermination.getStatus());
            //assertEquals("MAR10I001", noticeOfTermination.getFormSet().getNpdesId());
            assertNotNull(noticeOfTermination.getFormSet().getNpdesId());
            assertTrue(noticeOfTermination.getCertifiedDate().compareTo(start) > 0);
            assertTrue(noticeOfTermination.getLastUpdatedDate().compareTo(start) > 0);
            assertTrue(noticeOfTermination.getReviewExpiration().compareTo(start) > 0);
            // validate the attachment
            assertEquals(noticeOfTermination.getAttachments().size(), 2);

            // distribute the change
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(noticeOfTermination.getId());
            formService.distributeForm(noticeOfTermination.getId());
            CgpNoiForm distributed = formService.retrieveForm(noticeOfTermination.getId());
            assertEquals(Status.Terminated, distributed.getStatus());
            assertNotNull(distributed.getNodeTransactionId());
            assertNotNull(distributed.getSubmittedDate());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRouteLewToCertifier() throws Exception {
        try {
            // setup
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-lew-form.json");
            Long id = formService.createNewLowErosivityWaiver(newForm).getId();
            CgpNoiForm form = formService.retrieveForm(id);
            assertNotNull(form);
            assertEquals(FormType.Low_Erosivity_Waiver, form.getType());
            assertEquals(Phase.New, form.getPhase());

            // route to certifier
            formService.routeToCertifier(id, "TEST.CERTIFIER");
            formService.retrieveForm(id);
            assertEquals("TEST.CERTIFIER", form.getFormSet().getOwner());

            // reject by certifier
            applicationSecurityUtils.mockCertifier("TEST.CERTIFIER");
            formService.rejectForm(id, "Because I said so!");
            form = formService.retrieveForm(id);
            assertNotNull(form);
            assertEquals("DFLADUNG", form.getFormSet().getOwner());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCertifyLew() throws Exception {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-lew-form.json");
            Long id = formService.createNewLowErosivityWaiver(newForm).getId();
            CgpNoiForm form = formService.retrieveForm(id);
            assertNotNull(form);
            assertEquals(FormType.Low_Erosivity_Waiver, form.getType());
            assertEquals(Phase.New, form.getPhase());
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the LEW
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            CgpNoiForm certified = formService.retrieveForm(id);
            assertEquals(Status.Submitted, certified.getStatus());
            assertNotNull(certified.getCromerrActivityId());
            //assertEquals("VAR10I178", certified.getFormSet().getNpdesId());
            assertNotNull(certified.getFormSet().getNpdesId());
            assertTrue(certified.getCertifiedDate().compareTo(start) > 0);
            assertTrue(certified.getLastUpdatedDate().compareTo(start) > 0);
            assertTrue(certified.getReviewExpiration().compareTo(start) > 0);
            // validate the attachment
            assertEquals(certified.getAttachments().size(), 1);
            Attachment attachment = certified.getAttachments().get(0);
            assertNull(attachment.getData());
            assertNotNull(attachment.getCromerrAttachmentId());
            assertEquals(AttachmentCategory.CoR, attachment.getCategory());
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            formService.distributeForm(id);
            CgpNoiForm distributed = formService.retrieveForm(id);
            assertEquals(Status.Active, distributed.getStatus());
            assertNotNull(distributed.getNodeTransactionId());
            assertNotNull(distributed.getSubmittedDate());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRouteLewChangeToCertifier() throws Exception {
        try {
            // setup
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-lew-form.json");
            Long id = formService.createNewLowErosivityWaiver(newForm).getId();
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            formService.distributeForm(id);

            // initiate a change
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm certified = formService.retrieveForm(id);
            CgpNoiForm changeLew = formService.changeLowErosivityWaiver(certified.getId());
            assertEquals(changeLew.getPhase(), Phase.Change);
            assertNull(changeLew.getCromerrActivityId());
            assertNull(changeLew.getCertifiedDate());
            assertNull(changeLew.getNodeTransactionId());
            assertNull(changeLew.getSubmittedDate());
            assertEquals(changeLew.getAttachments().size(), 0);
            assertTrue(changeLew.getActiveRecord());
            assertEquals(Status.Draft, changeLew.getStatus());
            assertEquals(FormType.Low_Erosivity_Waiver, changeLew.getType());
            formService.updateForm(changeLew.getId(), changeLew);

            // route to certifier
            formService.routeToCertifier(changeLew.getId(), "TEST.CERTIFIER");
            CgpNoiForm form = formService.retrieveForm(changeLew.getId());
            assertNotNull(form);
            assertEquals("TEST.CERTIFIER", form.getFormSet().getOwner());

            // reject by certifier
            applicationSecurityUtils.mockCertifier("TEST.CERTIFIER");
            formService.rejectForm(changeLew.getId(), "Because I said so!");
            form = formService.retrieveForm(changeLew.getId());
            assertNotNull(form);
            assertEquals("DFLADUNG", form.getFormSet().getOwner());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCertifyLewChange() throws Exception {
        try {
            // setup
            ZonedDateTime start = ZonedDateTime.now();
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-lew-form.json");
            Long id = formService.createNewLowErosivityWaiver(newForm).getId();
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            Thread.sleep(1500);// for the expiration period.
            // distribute the form
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(id);
            formService.distributeForm(id);

            // initiate a change
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm certified = formService.retrieveForm(id);
            CgpNoiForm changeLew = formService.changeLowErosivityWaiver(certified.getId());
            assertEquals(changeLew.getPhase(), Phase.Change);
            assertNull(changeLew.getCromerrActivityId());
            assertNull(changeLew.getCertifiedDate());
            assertNull(changeLew.getNodeTransactionId());
            assertNull(changeLew.getSubmittedDate());
            assertEquals(changeLew.getAttachments().size(), 0);
            assertTrue(changeLew.getActiveRecord());
            assertEquals(Status.Draft, changeLew.getStatus());
            assertEquals(FormType.Low_Erosivity_Waiver, changeLew.getType());
            formService.updateForm(changeLew.getId(), changeLew);

            // certify the change
            activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            formService.certifyForm(changeLew.getId(), activityId);
            Thread.sleep(1500);// for the expiration period.
            assertEquals(Status.Submitted, changeLew.getStatus());
            //assertEquals("VAR10I178", changeLew.getFormSet().getNpdesId());
            assertNotNull(changeLew.getFormSet().getNpdesId());
            assertTrue(changeLew.getCertifiedDate().compareTo(start) > 0);
            assertTrue(changeLew.getLastUpdatedDate().compareTo(start) > 0);
            assertTrue(changeLew.getReviewExpiration().compareTo(start) > 0);
            // validate the attachment
            assertEquals(changeLew.getAttachments().size(), 1);

            // distribute the change
            applicationSecurityUtils.addSystemUser();
            formService.activateForm(changeLew.getId());
            formService.distributeForm(changeLew.getId());
            CgpNoiForm distributed = formService.retrieveForm(changeLew.getId());
            assertEquals(Status.Active, distributed.getStatus());
            assertNotNull(distributed.getNodeTransactionId());
            assertNotNull(distributed.getSubmittedDate());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCertifyNpdesGeneration() throws Exception {
        try {
            // setup
            applicationSecurityUtils.mockCertifier("DFLADUNG", "david.fladung@cgifederal.com", "David", "Fladung");
            CgpNoiForm newForm = getForm("test-data/new-noi-form-deterministic-npdesid.json");
            Long id = formService.createNewNoticeOfIntent(newForm).getId();
            // simulate the activity creation step
            String activityId = cromerrService.createActivity(applicationSecurityUtils.getCurrentApplicationUser());
            // certify the NOI
            formService.certifyForm(id, activityId);
            CgpNoiForm certified = formService.retrieveForm(id);
            assertEquals("NMR10I001", certified.getFormSet().getNpdesId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

}