package gov.epa.oeca.cgp.infrastructure.icis;

import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.node.TransactionStatus;

import java.util.List;

/**
 * @author dfladung
 */
public interface IcisSubmissionService {

    CgpNoiForm submitToIcisNpdesDataflow(CgpNoiForm form, CgpNoiForm previous) throws ApplicationException;

    TransactionStatus getTransactionStatus(String transactionId) throws ApplicationException;

    String getTransactionStatusDetail(String transactionId) throws ApplicationException;

    List<Document> downloadTransactionDocs(String transactionId) throws ApplicationException;
}
