package gov.epa.oeca.cgp.interfaces.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/action/login")
public class LoginAction extends BaseCgpAction {
	
	@DefaultHandler
	public Resolution login() {
		return new ForwardResolution("/views/login.jsp");
	}
}
