package gov.epa.oeca.cgp.interfaces.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * @author labieva (linera.abieva@cgifederal.com)
 */

@UrlBinding("/action/registration")
public class RegistrationAction extends BaseCgpAction {

    @DefaultHandler
    public Resolution start() {
        return new ForwardResolution("/views/register.jsp");
    }
}
