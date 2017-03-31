package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.application.SystemPropertyService;
import gov.epa.oeca.cgp.domain.ref.SystemProperty;
import gov.epa.oeca.cgp.infrastructure.persistence.SystemPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by smckay on 3/6/2017.
 *
 * @author smckay
 */
@Service("systemPropertyService")
@Transactional
public class SystemPropertyServiceImpl implements SystemPropertyService {

    @Autowired
    SystemPropertyRepository systemPropertyRepository;

    @Override
    @Transactional(readOnly =  true)
    public SystemProperty retrieveProperty(String key) {
        return systemPropertyRepository.retrieve(key);
    }
}
