package gov.epa.oeca.cgp.security;

import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.security.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * @author dfladung
 */
@Component("applicationSecurityUtils")
public class ApplicationSecurityUtils {

    public static final String defaultRoleName = "ROLE_CGP_USER";
    public static final String preparerRoleName = "ROLE_CGP_PREPARER";
    public static final String certifierRoleName = "ROLE_CGP_CERTIFIER";
    public static final String helpdeskRoleName = "ROLE_CGP_HELPDESK";
    public static final String regAuthRoleName = "ROLE_CGP_REGAUTH";
    public static final String systemRoleName = "ROLE_CGP_SYSTEM";

    public static final SimpleGrantedAuthority defaultRole = new SimpleGrantedAuthority(defaultRoleName);
    public static final SimpleGrantedAuthority preparer = new SimpleGrantedAuthority(preparerRoleName);
    public static final SimpleGrantedAuthority certifier = new SimpleGrantedAuthority(certifierRoleName);
    public static final SimpleGrantedAuthority helpdesk = new SimpleGrantedAuthority(helpdeskRoleName);
    public static final SimpleGrantedAuthority regAuth = new SimpleGrantedAuthority(regAuthRoleName);
    public static final SimpleGrantedAuthority system = new SimpleGrantedAuthority(systemRoleName);

    private static final Logger logger = LoggerFactory.getLogger(ApplicationSecurityUtils.class);

    public ApplicationUser getCurrentApplicationUser() {
        return (ApplicationUser) getCurrentPrincipal();
    }

    public boolean hasRole(SimpleGrantedAuthority... roles) {
        for (SimpleGrantedAuthority role : roles) {
            if (getCurrentRoles().contains(role)) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentUserId() {
        return getCurrentApplicationUser().getUsername();
    }

    public void mockPreparer(String userId) {
        mockPreparer(userId, "preparer@cgifederal.com", "John", "Doe");
    }

    public void mockPreparer(String userId, String email, String firstName, String lastName) {
        ApplicationUser user = new ApplicationUser(userId, Collections.singletonList(preparer));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        addUserToSecurityContext(user);
    }

    public void mockCertifier(String userId) {
        mockCertifier(userId, "certifier@cgifederal.com", "Bruce", "Wayne");
    }

    public void mockCertifier(String userId, String email, String firstName, String lastName) {
        ApplicationUser user = new ApplicationUser(userId, Collections.singletonList(certifier));
        user.setUserRoleId(new Long(114141L));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setOrganization("CGI");
        user.setTitle("Boss");
        user.setPhoneNumber("1234567890");
        addUserToSecurityContext(user);
    }

    public void mockRegionalAuthority(String userId) {
        mockRegionalAuthority(userId, "ra@cgifederal.com", "Bruce", "Wayne");
    }

    public void mockRegionalAuthority(String userId, String email, String firstName, String lastName) {
        ApplicationUser user = new ApplicationUser(userId, Collections.singletonList(regAuth));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setClientId("1");
        addUserToSecurityContext(user);
    }

    public void mockHelpDesk(String userId) {
        ApplicationUser user = new ApplicationUser(userId, Collections.singletonList(helpdesk));
        user.setEmail("hd@cgifederal.com");
        addUserToSecurityContext(user);
    }

    public void addSystemUser(){
        ApplicationUser user = new ApplicationUser("CGP_SYSTEM", Collections.singletonList(system));
        addUserToSecurityContext(user);
    }

    void addUserToSecurityContext(ApplicationUser appUser) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                appUser, null, appUser.getAuthorities()));
        logger.info(
                String.format("User %s explicitly added to security context.", appUser.getUsername()));
    }

    Object getCurrentPrincipal() {
        // checking security context
        checkSecurityContext();
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    Collection<? extends GrantedAuthority> getCurrentRoles() {
        checkSecurityContext();
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    void checkSecurityContext() throws ApplicationException {
        if (SecurityContextHolder.getContext() == null ||
                SecurityContextHolder.getContext().getAuthentication() == null ||
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
            throw new ApplicationException(
                    ApplicationErrorCode.E_Security,
                    "Security Context, authentication or principal is empty.");

        }
    }

}
