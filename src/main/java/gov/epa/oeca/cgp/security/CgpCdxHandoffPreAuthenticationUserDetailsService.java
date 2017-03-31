package gov.epa.oeca.cgp.security;

import gov.epa.oeca.common.security.cdx.CdxHandoffPreAuthenticationUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author dfladung
 */
@Service
public class CgpCdxHandoffPreAuthenticationUserDetailsService extends CdxHandoffPreAuthenticationUserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CgpCdxHandoffPreAuthenticationUserDetailsService.class);

    @Override
    protected Collection<SimpleGrantedAuthority> getRoles(Map<String, String> userProperties) {
        Long roleId = nullSafeLong(userProperties.get(ROLE_ID));
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        switch (roleId.intValue()) {
            case 120410:
                roles.add(ApplicationSecurityUtils.certifier);
                roles.add(ApplicationSecurityUtils.defaultRole);
                break;
            case 120420:
                roles.add(ApplicationSecurityUtils.preparer);
                roles.add(ApplicationSecurityUtils.defaultRole);
                break;
            case 120430:
                roles.add(ApplicationSecurityUtils.regAuth);
                roles.add(ApplicationSecurityUtils.defaultRole);
                break;
            case 120440:
                roles.add(ApplicationSecurityUtils.helpdesk);
                roles.add(ApplicationSecurityUtils.defaultRole);
                break;
            default:
                logger.warn("Handoff occurred with unmapped role ID " + roleId);
                break;
        }
        return roles;
    }

}
