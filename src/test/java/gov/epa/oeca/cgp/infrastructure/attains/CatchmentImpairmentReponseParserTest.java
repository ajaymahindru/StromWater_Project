package gov.epa.oeca.cgp.infrastructure.attains;

import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author dfladung
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-cgp-test.xml"})
public class CatchmentImpairmentReponseParserTest {

    private static final Logger logger = LoggerFactory.getLogger(CatchmentImpairmentReponseParserTest.class);

    @Autowired
    CatchmentImpairmentReponseParser parser;
    @Autowired
    ResourceLoader loader;

    @Test
    public void testParse() throws Exception {
        try {
            String attainsJson = IOUtils.toString(
                    loader.getResource("classpath:attains/attains-response.json").getInputStream(),
                    Charset.defaultCharset());
            List<ReceivingWater> receivingWaters = parser.parse(attainsJson);
            assertNotNull(receivingWaters);
            assertEquals(receivingWaters.size(), 5);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

}