package de.hpi.bpt.chimera.rest.beans.usermanagement;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreateOrganizationJaxBean extends UpdateOrganizationJaxBean {

	public CreateOrganizationJaxBean(String body) {
		super(body);
	}
}
