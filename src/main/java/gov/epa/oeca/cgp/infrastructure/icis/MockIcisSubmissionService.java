package gov.epa.oeca.cgp.infrastructure.icis;

import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.node.Transaction;
import gov.epa.oeca.common.domain.node.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author dfladung
 */
public class MockIcisSubmissionService extends AbstractIcisSubmissionServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(MockIcisSubmissionService.class);

    @Autowired
    ResourceLoader loader;

    @Override
    public CgpNoiForm submitToIcisNpdesDataflow(CgpNoiForm form, CgpNoiForm previous) throws ApplicationException {
        // marshall the form to make sure it validates
        marshallForm(form, previous);
        String txId = "_" + UUID.randomUUID().toString();
        form.setNodeTransactionId(txId);
        return form;
    }

    @Override
    public Transaction getTransactionDetail(String transactionId) throws ApplicationException {
        Transaction tx = new Transaction("_" + UUID.randomUUID().toString(), TransactionStatus.FAILED,
                "E_InternalError: all files failed validation");
        return tx;
    }

    @Override
    public List<Document> downloadTransactionDocs(String transactionId) throws ApplicationException {
        try {
            Document doc = new Document();
            doc.setContent(loader.getResource("logback.xml").getFile());
            doc.setName("submission-metadata.xml");
            return Collections.singletonList(doc);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }
}
