package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.domain.ref.SystemProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by smckay on 3/6/2017.
 *
 * @author smckay
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:oeca-cgp-test.xml"})
@Transactional(transactionManager = "transactionManager")
@Rollback
public class SystemPropertyServiceImplTest {
    @Autowired
    SystemPropertyService systemPropertyService;

    @Test
    public void testRetrieve() {
        assertNotNull(systemPropertyService.retrieveProperty(ApplicationUtils.DISABLE_DISTRIBUTION_KEY));
    }
}
