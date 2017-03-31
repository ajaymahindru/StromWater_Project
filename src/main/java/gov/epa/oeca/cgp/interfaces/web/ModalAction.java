package gov.epa.oeca.cgp.interfaces.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * Created by smckay on 1/17/2017.
 */
@UrlBinding("/action/modal/{modalName}")
public class ModalAction extends BaseCgpAction {
    private String modalName;
    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(String.format("/modal/%s.jsp", modalName));
    }

    public String getModalName() {
        return modalName;
    }

    public void setModalName(String modalName) {
        this.modalName = modalName;
    }
}
