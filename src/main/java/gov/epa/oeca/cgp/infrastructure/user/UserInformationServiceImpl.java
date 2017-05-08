package gov.epa.oeca.cgp.infrastructure.user;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.registration.NewUserProfile;
import gov.epa.oeca.common.domain.registration.Organization;
import gov.epa.oeca.common.infrastructure.cdx.register.Assembler;
import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import gov.epa.oeca.common.infrastructure.cdx.register.StreamlinedRegistrationClient;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationRoleType;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationUser;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationUserSearchCriteria;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.List;

/**
 * @author dfladung
 */

public class UserInformationServiceImpl implements UserInformationService {

    private static final Logger logger = LoggerFactory.getLogger(UserInformationServiceImpl.class);

    @Autowired
    StreamlinedRegistrationClient streamlinedRegistrationClient;
    @Autowired
    RegistrationHelper helper;
    @Autowired
    Assembler assembler;

    @Override
    public List<NewUserProfile> retrieveRegionalAuthority(Integer region) throws ApplicationException {
        try {
            // validate
            Validate.notNull(region, "Region is required.");
            Validate.isTrue(region > 0 && region < 12, "Valid region values are from 1-11");
            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            RegistrationUserSearchCriteria criteria = new RegistrationUserSearchCriteria();
            criteria.setSubject(region.toString());
            RegistrationRoleType role = new RegistrationRoleType();
            role.setCode(120430L); // RA role
            criteria.setRoleType(role);
            return assembler.assembleNewUserProfiles(streamlinedRegistrationClient.retrieveUsersByCriteria(url, token, criteria));
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public NewUserProfile retrieveCertifier(String certifierId) throws ApplicationException {
        try {
            // validate
            Validate.notNull(certifierId, "Certifier ID is required.");

            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            RegistrationUserSearchCriteria criteria = new RegistrationUserSearchCriteria();
            criteria.setUserId(certifierId);
            RegistrationRoleType role = new RegistrationRoleType();
            role.setCode(120410L); // certifier role
            criteria.setRoleType(role);
            List<NewUserProfile> results = assembler.assembleNewUserProfiles(
                    streamlinedRegistrationClient.retrieveUsersByCriteria(url, token, criteria));
            Validate.isTrue(results.size() == 1, "Expecting one certifer result.");
            return results.get(0);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public Organization retrievePrimaryOrganization(String userId) throws ApplicationException {
        try {
            // validate
            Validate.notNull(userId, "User ID is required.");

            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);
            RegistrationUser user = new RegistrationUser();
            user.setUserId(userId);
            return assembler.assembleOrg(streamlinedRegistrationClient.retrievePrimaryOrganization(url, token, user));
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    String getStreamlinedRegistrationToken(URL url) throws Exception {
        return streamlinedRegistrationClient.authenticate(
                url,
                helper.getStreamlinedRegisterOperatorUser(),
                helper.getStreamlinedRegisterOperatorPassword());
    }


}
