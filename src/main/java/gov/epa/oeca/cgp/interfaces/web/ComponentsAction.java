package gov.epa.oeca.cgp.interfaces.web;

import gov.epa.oeca.common.interfaces.web.BaseAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/action/components/{componentName}")
public class ComponentsAction extends BaseCgpAction{
	private String componentName;
	@DefaultHandler
	public Resolution view() {
		return new ForwardResolution(String.format("/components/%s.jsp", componentName));
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
}
