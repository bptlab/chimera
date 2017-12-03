package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

public class WebServiceTask extends AbstractActivity {
	private String webServiceUrl;
	private String webServiceMethod;
	private String webServiceBody;

	public String getWebServiceUrl() {
		return webServiceUrl;
	}
	public void setWebServiceUrl(String webServiceUrl) {
		this.webServiceUrl = webServiceUrl;
	}
	public String getWebServiceMethod() {
		return webServiceMethod;
	}
	public void setWebServiceMethod(String webServiceMethod) {
		this.webServiceMethod = webServiceMethod;
	}
	public String getWebServiceBody() {
		return webServiceBody;
	}
	public void setWebServiceBody(String webServiceBody) {
		this.webServiceBody = webServiceBody;
	}
}
