package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import de.hpi.bpt.chimera.parser.IllegalCaseModelException;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.List;

@Entity
public class WebServiceTask extends AbstractActivity {
	private String webServiceUrl;
	private String webServiceMethod;
	private String webServiceBody;
	private String webServiceHeader;
	private String contentType;

	/**
	 * Email Activities are executed automatically.
	 */
	@Override
	public boolean isAutomatic() {
		return true;
	}

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
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		if (!validateContentType(contentType)) {
			throw new IllegalCaseModelException("Content-Type not one of the valid options.");
		}
		this.contentType = contentType;
	}
	public void setWebServiceBody(String webServiceBody) {
		this.webServiceBody = webServiceBody;
	}
	public String getWebServiceHeader() {
		return webServiceHeader;
	}
	public void setWebServiceHeader(String webServiceHeader) {
		this.webServiceHeader = webServiceHeader;
	}

	private boolean validateContentType(String contentType) {
		List<String> allowedContentTypes = Arrays.asList("", "application/json", "application/x-www-form-urlencoded",
				"application/atom+xml", "application/octet-stream", "application/svg+xml", "application/xhtml+xml",
				"application/xml", "multipart/form-data", "text/html", "text/plain", "text/xml");
		return allowedContentTypes.contains(contentType);
	}
}
