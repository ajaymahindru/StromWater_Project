package gov.epa.oeca.cgp.application.registration;

import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.notification.NewAccountConfirmation;

/**
 * @author labieva (linera.abieva@cgifederal.com)
 */
public interface CgpRegistrationService {

    Boolean isEmailAvailable(String email) throws ApplicationException;

    NewAccountConfirmation sendConfirmation(NewAccountConfirmation confirmation) throws ApplicationException;

    void validateConfirmationCode(NewAccountConfirmation src, NewAccountConfirmation target) throws ApplicationException;
}
