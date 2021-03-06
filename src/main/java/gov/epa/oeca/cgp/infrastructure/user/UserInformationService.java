package gov.epa.oeca.cgp.infrastructure.user;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.NewUserProfile;
import gov.epa.oeca.common.domain.registration.Organization;

import java.util.List;

/**
 * @author dfladung
 */
public interface UserInformationService {

    List<NewUserProfile> retrieveRegionalAuthority(Integer region) throws ApplicationException;

    NewUserProfile retrieveCertifier(String certifierId) throws ApplicationException;

    Organization retrievePrimaryOrganization(String userId) throws ApplicationException;

    List<NewUserProfile> retrieveUserById(String userId) throws ApplicationException;
}
