package gov.epa.oeca.cgp.infrastructure.icis;

import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;

/**
 * @author dfladung
 */
public interface IcisSubmissionService {

    CgpNoiForm submitToIcisNpdesDataflow(CgpNoiForm form, CgpNoiForm previous) throws ApplicationException;
}
