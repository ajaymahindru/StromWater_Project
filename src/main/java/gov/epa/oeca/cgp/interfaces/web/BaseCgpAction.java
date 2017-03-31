package gov.epa.oeca.cgp.interfaces.web;

import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.interfaces.web.BaseAction;
import gov.epa.oeca.common.security.ApplicationUser;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.Map;

public class BaseCgpAction extends BaseAction {
    @SpringBean("cgpExternalUrls")
    protected Map<String, String> cgpUrls;

    private String npdesId;

    @SpringBean
    ApplicationSecurityUtils applicationSecurityUtils;

    public ApplicationUser getUser() {
        try {
            return applicationSecurityUtils.getCurrentApplicationUser();
        } catch (ApplicationException e) {
            return null;
        }
    }

    public Map<String, String> getCgpUrls() {
        return cgpUrls;
    }

    public void setCgpUrls(Map<String, String> cgpUrls) {
        this.cgpUrls = cgpUrls;
    }

    public String getNpdesId() {
        return npdesId;
    }

    public void setNpdesId(String npdesId) {
        this.npdesId = npdesId;
    }

    public String getDefaultRole() {
        return ApplicationSecurityUtils.defaultRoleName;
    }

    public String getPreparerRole() {
        return ApplicationSecurityUtils.preparerRoleName;
    }

    public String getCertifierRole() {
        return ApplicationSecurityUtils.certifierRoleName;
    }

    public String getRegAuthRole() {
        return ApplicationSecurityUtils.regAuthRoleName;
    }

    public String getHelpdeskRole() {
        return ApplicationSecurityUtils.helpdeskRoleName;
    }
}
