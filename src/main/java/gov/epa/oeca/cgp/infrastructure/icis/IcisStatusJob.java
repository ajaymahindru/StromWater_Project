package gov.epa.oeca.cgp.infrastructure.icis;

import gov.epa.oeca.cgp.application.CgpNoiFormService;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.domain.node.Transaction;
import gov.epa.oeca.common.domain.node.TransactionStatus;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class IcisStatusJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(IcisStatusJob.class);

    @Autowired
    CgpNoiFormService formService;
    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;
    @Autowired
    IcisSubmissionService icisSubmissionService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing ICIS Transaction Status job");
        applicationSecurityUtils.addSystemUser();
        CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
        criteria.setSubmittedToIcis(true);
        criteria.setIcisSubmissionInProgress(true);
        List<CgpNoiForm> submittedForms = formService.retrieveForms(criteria);

        for (CgpNoiForm submittedForm : submittedForms) {
            try {
                String txId = submittedForm.getNodeTransactionId();
                Transaction transaction = icisSubmissionService.getTransactionDetail(txId);
                TransactionStatus txStatus = transaction.getStatus();
                String statusDetail = transaction.getStatusDetail();
                Long formId = submittedForm.getId();
                if (!txStatus.equals(submittedForm.getNodeTransactionStatus())) {
                    formService.updateFormTxStatus(formId, txStatus);
                    logger.debug(String.format("Transaction Status of form %s was changed to %s",
                            formId, submittedForm.getNodeTransactionStatus().getValue()));
                    if (TransactionStatus.FAILED.equals(submittedForm.getNodeTransactionStatus())) {
                        List<Document> docs = icisSubmissionService.downloadTransactionDocs(txId);
                        formService.sendIcisTransactionFailure(formId, docs, statusDetail);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                // trapping the exception so we can continue with other forms
            }
        }
    }
}
