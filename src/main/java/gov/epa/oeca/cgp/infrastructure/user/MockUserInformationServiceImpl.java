package gov.epa.oeca.cgp.infrastructure.user;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.NewUserProfile;
import gov.epa.oeca.common.domain.registration.Organization;

import java.util.Collections;
import java.util.List;

/**
 * @author dfladung
 */
public class MockUserInformationServiceImpl implements UserInformationService {


    NewUserProfile getTest() {
        NewUserProfile result = new NewUserProfile();
        Organization org = new Organization();
        org.setEmail("david.fladung@cgifederal.com");
        result.setOrganization(org);
        return result;
    }

    @Override
    public List<NewUserProfile> retrieveRegionalAuthority(Integer region) throws ApplicationException {
        return Collections.singletonList(getTest());
    }

    @Override
    public NewUserProfile retrieveCertifier(String certifierId) throws ApplicationException {
        return getTest();
    }

    @Override
    public Organization retrievePrimaryOrganization(String userId) throws ApplicationException {
        return getTest().getOrganization();
    }
}
