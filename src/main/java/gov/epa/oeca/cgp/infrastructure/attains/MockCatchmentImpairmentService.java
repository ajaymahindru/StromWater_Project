package gov.epa.oeca.cgp.infrastructure.attains;

import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import gov.epa.oeca.common.ApplicationException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author dfladung
 */
public class MockCatchmentImpairmentService implements CatchmentImpairmentService {

    private static final Logger logger = LoggerFactory.getLogger(MockCatchmentImpairmentService.class);

    @Autowired
    ResourceLoader loader;
    @Autowired
    CatchmentImpairmentReponseParser parser;

    @Override
    public List<ReceivingWater> retrieveWatersList(BigDecimal latitude, BigDecimal longitude) throws ApplicationException {
        try {
            String attainsJson = IOUtils.toString(
                    loader.getResource("classpath:attains/attains-response.json").getInputStream(),
                    Charset.defaultCharset());
            return parser.parse(attainsJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }
}
