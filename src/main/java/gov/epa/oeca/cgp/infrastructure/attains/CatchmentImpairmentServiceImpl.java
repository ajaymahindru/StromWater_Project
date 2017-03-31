package gov.epa.oeca.cgp.infrastructure.attains;

import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import gov.epa.oeca.common.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author dfladung
 */
public class CatchmentImpairmentServiceImpl implements CatchmentImpairmentService {

    private static final Logger logger = LoggerFactory.getLogger(CatchmentImpairmentServiceImpl.class);

    @Resource(name = "catchmentServiceConfiguration")
    Map<String, String> catchmentServiceConfiguration;
    @Autowired
    CatchmentImpairmentReponseParser parser;

    @Override
    public List<ReceivingWater> retrieveWatersList(BigDecimal latitude, BigDecimal longitude) throws ApplicationException {
        try {
            String submissionId = UUID.randomUUID().toString();
            logger.info(String.format("Requesting catchment data [%s] for %s/%s", submissionId, latitude, longitude));
            String baseUrl = catchmentServiceConfiguration.get("serviceUrl");
            URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("pGeometry", String.format("POINT(%s %s)", latitude, longitude))
                    .queryParam("pGeometryMod", "WKT,SRID=8265")
                    .queryParam("optSubmissionID", submissionId)
                    .queryParam("optOutPrettyPrint", "0")
                    .build()
                    .toUri();
            String json = new RestTemplate().getForObject(targetUrl, String.class);
            return parser.parse(json);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }
}
