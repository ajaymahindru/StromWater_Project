package gov.epa.oeca.cgp.infrastructure.icis;

import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.node.Transaction;

import java.util.List;

/**
 * @author dfladung
 */
public interface IcisSubmissionService {

    CgpNoiForm submitToIcisNpdesDataflow(CgpNoiForm form, CgpNoiForm previous) throws ApplicationException;

    Transaction getTransactionDetail(String transactionId) throws ApplicationException;

    List<Document> downloadTransactionDocs(String transactionId) throws ApplicationException;
}
