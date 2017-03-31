package gov.epa.oeca.cgp.infrastructure.icis;

import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;

import java.util.UUID;

/**
 * @author dfladung
 */
public class MockIcisSubmissionService extends AbstractIcisSubmissionServiceImpl {

    @Override
    public CgpNoiForm submitToIcisNpdesDataflow(CgpNoiForm form, CgpNoiForm previous) throws ApplicationException {
        // marshall the form to make sure it validates
        marshallForm(form, previous);
        String txId = "_" + UUID.randomUUID().toString();
        form.setNodeTransactionId(txId);
        return form;
    }
}
