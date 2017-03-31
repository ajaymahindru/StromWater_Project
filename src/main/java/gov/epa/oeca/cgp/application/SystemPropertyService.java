package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.domain.ref.SystemProperty;

/**
 * Created by smckay on 3/6/2017.
 *
 * @author smckay
 */
public interface SystemPropertyService {
    SystemProperty retrieveProperty(String key);
}
