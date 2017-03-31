package gov.epa.oeca.cgp.interfaces.web;

/**
 * @author dfladung
 */

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/action/secured/cor")
public class CorAction extends BaseCgpAction {

    String formId;
    String formType;
    String npdesId;

    @DefaultHandler
    public Resolution home() {
        return new RedirectResolution(String.format("/action/secured/home#!/%s?formId=%s/cor", formType, formId)).addParameter("npdesId", npdesId);
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getNpdesId() {
        return npdesId;
    }

    public void setNpdesId(String npdesId) {
        this.npdesId = npdesId;
    }
}
