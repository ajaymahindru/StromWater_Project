package gov.epa.oeca.cgp.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Mocks the handoff process by setting a user in the spring context prior to the rest of the chain.
 *
 * @author dfladung
 */
@Component("mockHandoff")
public class MockHandoffFilter implements Filter {

    private static final Log logger = LogFactory.getLog(MockHandoffFilter.class);

    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpSession session = httpReq.getSession();
        applicationSecurityUtils.mockCertifier("OECA.TEST");
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        logger.warn("Mock handoff filter destroyed.");
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        logger.warn("Mock handoff filter initialized. This should only be used for local web testing.");
    }

}
