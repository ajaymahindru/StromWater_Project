package gov.epa.oeca.cgp.infrastructure.attains;

import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import gov.epa.oeca.common.ApplicationException;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dfladung
 */
public interface CatchmentImpairmentService {

    List<ReceivingWater> retrieveWatersList(BigDecimal latitude, BigDecimal longitude) throws ApplicationException;
}
