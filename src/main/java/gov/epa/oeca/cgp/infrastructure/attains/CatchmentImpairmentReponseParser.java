package gov.epa.oeca.cgp.infrastructure.attains;

import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;

import java.util.List;

/**
 * @author dfladung
 */
public interface CatchmentImpairmentReponseParser {

    List<ReceivingWater> parse(String json);
}
