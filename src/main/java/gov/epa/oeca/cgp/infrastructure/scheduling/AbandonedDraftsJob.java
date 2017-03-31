package gov.epa.oeca.cgp.infrastructure.scheduling;

import gov.epa.oeca.cgp.application.CgpNoiFormService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author dfladung
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class AbandonedDraftsJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(AbandonedDraftsJob.class);

    @Autowired
    CgpNoiFormService formService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            logger.info("Executing abandoned drafts job");
            formService.notifyAbandonedDrafts();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new JobExecutionException(e);
        }
    }
}
