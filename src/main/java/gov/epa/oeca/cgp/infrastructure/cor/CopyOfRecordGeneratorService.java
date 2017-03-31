package gov.epa.oeca.cgp.infrastructure.cor;

import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;

/**
 * @author dfladung
 */
public interface CopyOfRecordGeneratorService {

    Attachment generateCorFromForm(CgpNoiForm form) throws ApplicationException;
}
