package gov.epa.oeca.cgp.interfaces.web;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.Map;

@UrlBinding("/action/secured/home")
public class HomeAction extends BaseCgpAction {
	@SpringBean("cgpExternalUrls")
	protected Map<String, String> cgpUrls;
	
	@DefaultHandler
	public Resolution home() {
		return new ForwardResolution("/views/home.jsp");
	}

	public Map<String, String> getCgpUrls() {
		return cgpUrls;
	}

	public void setCgpUrls(Map<String, String> cgpUrls) {
		this.cgpUrls = cgpUrls;
	}
}
