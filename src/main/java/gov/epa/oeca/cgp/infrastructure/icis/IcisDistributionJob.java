package gov.epa.oeca.cgp.infrastructure.icis;

import gov.epa.oeca.cgp.application.ApplicationUtils;
import gov.epa.oeca.cgp.application.CgpNoiFormService;
import gov.epa.oeca.cgp.application.SystemPropertyService;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.Status;
import gov.epa.oeca.cgp.domain.ref.SystemProperty;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import org.apache.commons.lang.BooleanUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author dfladung
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class IcisDistributionJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(IcisDistributionJob.class);

    @Autowired
    CgpNoiFormService formService;

    @Autowired
    SystemPropertyService systemPropertyService;

    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing ICIS distribution job");
        applicationSecurityUtils.addSystemUser();
        CgpNoiFormSearchCriteria criteria = new CgpNoiFormSearchCriteria();
        criteria.setSubmittedToIcis(false);
        criteria.setReviewExpiration(ZonedDateTime.now());
        criteria.setStatuses(ApplicationUtils.DISTRIBUTION_STATUSES);
        List<CgpNoiForm> submitttedForms = formService.retrieveForms(criteria);
        SystemProperty prop = systemPropertyService.retrieveProperty(ApplicationUtils.DISABLE_DISTRIBUTION_KEY);
        Boolean skip = prop != null && BooleanUtils.toBoolean(prop.getValue());

        for (CgpNoiForm submittedForm : submitttedForms) {
            try {
                // distribution and activation are separate steps to allow for possibility that distribution might fail
                if (!ApplicationUtils.FINAL_STATUSES.contains(submittedForm.getStatus())) {
                    // don't activate forms that have already been activated, but not distributed
                    formService.activateForm(submittedForm.getId());
                }
                if (!skip) {
                    logger.info(String.format("Distributing form with ID %s", submittedForm.getId()));
                    formService.distributeForm(submittedForm.getId());
                } else {
                    logger.info(String.format("Skipping distribution of form with ID %s", submittedForm.getId()));
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                // trapping the exception so we can continue with other forms
            }
        }
    }
}
