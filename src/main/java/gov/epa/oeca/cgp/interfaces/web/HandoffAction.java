package gov.epa.oeca.cgp.interfaces.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * @author dfladung
 */

@UrlBinding("/action/secured/handoff")
public class HandoffAction extends BaseCgpAction {

    @DefaultHandler
    public Resolution home() {
        return new RedirectResolution(HomeAction.class);
    }
}
