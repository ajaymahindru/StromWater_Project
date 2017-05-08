package gov.epa.oeca.cgp.infrastructure.user;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.NewUserProfile;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationOrganization;

import java.util.List;

/**
 * @author dfladung
 */
public interface UserInformationService {

    List<NewUserProfile> retrieveRegionalAuthority(Integer region) throws ApplicationException;

    NewUserProfile retrieveCertifier(String certifierId) throws ApplicationException;

    RegistrationOrganization retrievePrimaryOrganization(String userId) throws ApplicationException;
}
