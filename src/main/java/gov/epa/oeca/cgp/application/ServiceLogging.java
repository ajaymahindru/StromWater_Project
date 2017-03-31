package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.domain.ServiceEvent;
import gov.epa.oeca.cgp.infrastructure.persistence.ServiceLoggingRepository;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Aspect that logs calls to CgpNoiForm services.
 *
 * @author ktucker
 */
@Component
@Aspect
@Order(2)
public class ServiceLogging {

    private static final Log logger = LogFactory.getLog(ServiceLogging.class);

    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;
    @Autowired
    ServiceLoggingRepository serviceLoggingRepository;

    @Around("execution(public * gov.epa.oeca.cgp.application.CgpNoiFormService.*(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        boolean exception = false;
        long start = System.currentTimeMillis();
        String errorCode = null;
        String errorMessage = null;
        String userId = null;

        try {
            return pjp.proceed();
        } catch (Exception e) {
            exception = true;
            if (e instanceof ApplicationException) {
                errorCode = ((ApplicationException) e).getErrorCode().toString();
            }
            errorMessage = e.getMessage();
            throw e;
        } finally {
            // Get user ID from Spring Security context
            try {
                userId = applicationSecurityUtils.getCurrentApplicationUser() != null ?
                        applicationSecurityUtils.getCurrentUserId() : "userId N/A";
            } catch (ApplicationException e) {
                userId = "N/A";
            }

            // Get service name (e.g. CgiNoiFormService) and method
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            String[] fqn = signature.getDeclaringTypeName().split("\\.");
            String type = fqn[fqn.length - 1];
            String method = signature.getMethod().getName();

            // Get parameter names
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < signature.getParameterNames().length; i++) {
                sb.append(signature.getParameterTypes()[i].getSimpleName()).append(" ");
                sb.append(signature.getParameterNames()[i]).append(", ");
            }
            String parameters = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "";

            // Output method information to log file
            long time = System.currentTimeMillis() - start;
            String status = exception ? "error" : "success";
            if (exception) {
                logger.info(String.format("[%s] %s.%s(%s): %s, %sms [%s: %s]", userId, type, method, parameters, status, time, errorCode, errorMessage));
            } else {
                logger.info(String.format("[%s] %s.%s(%s): %s, %sms", userId, type, method, parameters, status, time));
            }

            // Output parameters to log file
            for (int i = 0; i < signature.getParameterNames().length; i++) {
                String value = pjp.getArgs()[i] == null ? null : StringUtils.abbreviate(pjp.getArgs()[i].toString(), 100);
                logger.info(signature.getParameterNames()[i] + ": " + value);
            }

            // Save service event in database
            ServiceEvent event = new ServiceEvent();
            event.setUserId(userId);
            event.setService(type);
            event.setMethod(String.format("%s(%s)", method, parameters));
            event.setStatus(status);
            event.setErrorCode(errorCode);
            event.setErrorMessage(errorMessage);
            serviceLoggingRepository.createServiceEvent(event);
        }
    }

}
