package gov.epa.oeca.cgp.application.registration;

import gov.epa.oeca.cgp.application.BaseService;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.OecaConstants;
import gov.epa.oeca.common.domain.notification.NewAccountConfirmation;
import gov.epa.oeca.common.infrastructure.cdx.register.Assembler;
import gov.epa.oeca.common.infrastructure.cdx.register.RegistrationHelper;
import gov.epa.oeca.common.infrastructure.cdx.register.StreamlinedRegistrationClient;
import gov.epa.oeca.common.infrastructure.notification.NotificationService;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationRole;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationRoleSearchCriteria;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;

@Service
public class CgpRegistrationServiceImpl extends BaseService implements CgpRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(CgpRegistrationServiceImpl.class);

    @Autowired
    StreamlinedRegistrationClient streamlinedRegistrationClient;
    @Autowired
    RegistrationHelper helper;
    @Autowired
    Assembler assembler;
    @Autowired
    NotificationService notificationService;
    @Resource(name = "oecaGeneralConfiguration")
    Map<String, String> oecaGeneralConfiguration;

    @Override
    public Boolean isEmailAvailable(String email) throws ApplicationException {
        try {
            // if email uniqueness isn't enabled, just return true
            if (!Boolean.parseBoolean(oecaGeneralConfiguration.get("emailUniquenessEnabled"))) {
                return true;
            }

            // get the CDX service configuration
            URL url = new URL(helper.getStreamlinedRegistrationServiceEndpoint());
            String token = getStreamlinedRegistrationToken(url);

            // initialize the result
            List<RegistrationRole> roles = new ArrayList<>();

            // look up by email and dataflow category
            RegistrationRoleSearchCriteria criteria = new RegistrationRoleSearchCriteria();
            criteria.getDataflowCategories().addAll(Arrays.asList("NeT", "NetDMR"));
			criteria.setEmail(email);
            roles = streamlinedRegistrationClient.retrieveRolesByCriteria(url, token, criteria);
            return CollectionUtils.isEmpty(roles);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public NewAccountConfirmation sendConfirmation(NewAccountConfirmation confirmation) throws ApplicationException {
        try {
            Validate.notNull(confirmation, "Confirmation is required.");
            Validate.notEmpty(confirmation.getUser(), "Confirmation user is required.");
            Validate.notEmpty(confirmation.getEmail(), "Confirmation email is required.");
            Validate.isTrue(
                    confirmation.getEmail().matches(OecaConstants.VALID_EMAIL_REGEX),
                    String.format("%s is not a valid email address.", confirmation.getEmail()));
            // generate and send the notification
            String confirmationCode = UUID.randomUUID().toString();
            confirmation.setConfirmationCode(confirmationCode);
            notificationService.sendNewAccountConfirmationNotification(confirmation);
            return confirmation;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public void validateConfirmationCode(NewAccountConfirmation confirmation, NewAccountConfirmation target) throws ApplicationException {
        try {
            Validate.notNull(target, "Could not locate confirmation code in user's session.");
            Validate.notNull(confirmation, "Confirmation is required.");
            Validate.notEmpty(confirmation.getUser(), "Confirmation user is required.");
            Validate.notEmpty(confirmation.getEmail(), "Confirmation email is required.");
            Validate.isTrue(
                    confirmation.getEmail().matches(OecaConstants.VALID_EMAIL_REGEX),
                    String.format("%s is not a valid email address.", confirmation.getEmail()));
            Validate.notEmpty(confirmation.getConfirmationCode(), "Confirmation code is required.");
            Validate.isTrue(confirmation.getUser().equals(target.getUser()), "User's ID does not match.");
            Validate.isTrue(confirmation.getEmail().equals(target.getEmail()), "User's email address does not match.");
            Validate.isTrue(confirmation.getConfirmationCode().equals(target.getConfirmationCode()), "User's confirmation code does not match.");
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
