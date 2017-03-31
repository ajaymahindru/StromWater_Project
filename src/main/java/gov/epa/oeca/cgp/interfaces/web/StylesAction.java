package gov.epa.oeca.cgp.interfaces.web;

import gov.epa.oeca.common.interfaces.web.BaseAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/action/styles")
public class StylesAction extends BaseCgpAction{
	@DefaultHandler
	public Resolution view() {
		return new ForwardResolution("/views/styles.jsp");
	}
}
