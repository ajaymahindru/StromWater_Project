package gov.epa.oeca.cgp.application;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author dfladung
 */
public abstract class BaseService {

    @Resource(name = "oecaServicesEndpointConfiguration")
    Map<String, String> oecaServicesEndpointConfiguration;

    protected String getResourceUrl(String base, String resource) {
        return oecaServicesEndpointConfiguration.get(base) + oecaServicesEndpointConfiguration.get(resource);
    }

}
