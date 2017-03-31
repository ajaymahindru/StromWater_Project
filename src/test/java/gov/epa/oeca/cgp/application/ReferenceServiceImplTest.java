package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.application.reference.ReferenceService;
import gov.epa.oeca.cgp.domain.noi.formsections.Pollutant;
import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import gov.epa.oeca.cgp.domain.ref.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author dfladung
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-cgp-test.xml"})
@Transactional(transactionManager = "transactionManager")
@Rollback
public class ReferenceServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceServiceImplTest.class);

    @Resource(name = "referenceService")
    ReferenceService referenceService;

    @Test
    public void retrieveStates() throws Exception {
        try {
            List<State> states = referenceService.retrieveStates();
            assertEquals(states.size(), 64);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retrieveTribes() throws Exception {
        try {
            List<Tribe> tribes = referenceService.retrieveTribes();
            assertEquals(tribes.size(), 750);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retrieveTribesByState() throws Exception {
        try {
            List<Tribe> tribes = referenceService.retrieveTribes("CA");
            assertEquals(tribes.size(), 124);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retrieveBiaTribes() throws Exception {
        try {
            List<BiaTribe> tribes = referenceService.retrieveBiaTribes();
            assertEquals(tribes.size(), 571);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retrieveBiaTribesByState() throws Exception {
        try {
            List<BiaTribe> tribes = referenceService.retrieveBiaTribes("AK");
            assertEquals(tribes.size(), 229);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retriveTribesByLandName() throws Exception {
        try {
            List<Tribe> tribes = referenceService.retriveTribesByLandName("NAVAJO RESERVATION");
            assertEquals(tribes.size(), 3);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retrieveCountiesByState() throws Exception {
        try {
            List<County> counties = referenceService.retrieveCounties("MD");
            assertEquals(counties.size(), 24);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retrieveChemicals() throws Exception {
        try {
            List<Chemical> chemicals = referenceService.retrieveChemicals();
            assertEquals(chemicals.size(), 3160);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retrieveChemicalsByName() throws Exception {
        try {
            List<Chemical> chemicals = referenceService.retrieveChemicals("METH");
            assertEquals(chemicals.size(), 289);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retrievePollutantsByName() throws Exception {
        try {
            List<Pollutant> pollutants = referenceService.retrievePollutants("cHlorine");
            assertEquals(pollutants.size(), 10);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void retrieveReceivingWatersByName() throws Exception {
        try {
            List<ReceivingWater> receivingWaters = referenceService.retrieveReceivingWaters("anacostia");
            assertEquals(receivingWaters.size(), 0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
}