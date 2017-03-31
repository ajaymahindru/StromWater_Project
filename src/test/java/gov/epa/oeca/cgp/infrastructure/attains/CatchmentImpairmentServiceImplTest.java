package gov.epa.oeca.cgp.infrastructure.attains;

import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author dfladung
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-cgp-test.xml"})
public class CatchmentImpairmentServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(CatchmentImpairmentServiceImplTest.class);

    @Resource(name = "catchmentImpairmentService")
    CatchmentImpairmentService catchmentImpairmentService;

    @Test
    public void retrieveWatersList() throws Exception {
        try {
            List<ReceivingWater> waters = catchmentImpairmentService
                    .retrieveWatersList(new BigDecimal("-76.97"), new BigDecimal("38.8803"));
            assertEquals(waters.size(), 5);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }


    @Test
    @Ignore
    public void retrieveWatersList_invalidGeomethry() throws Exception {
        try {
            List<ReceivingWater> waters = catchmentImpairmentService
                    .retrieveWatersList(new BigDecimal("-76.97"), new BigDecimal("388.8803"));
            fail("Should not have reached this point.");
        } catch (ApplicationException e) {
            assertEquals(ApplicationErrorCode.E_RemoteServiceError, e.getErrorCode());
            assertTrue(e.getMessage().startsWith("invalid wkt: geometry appears not to be geodetic"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

}