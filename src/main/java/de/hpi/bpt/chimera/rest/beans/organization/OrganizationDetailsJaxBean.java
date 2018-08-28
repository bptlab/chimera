package de.hpi.bpt.chimera.rest.beans.organization;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagement.Organization;
import de.hpi.bpt.chimera.usermanagement.OrganizationManager;
import de.hpi.bpt.chimera.usermanagement.User;

@XmlRootElement
public class OrganizationDetailsJaxBean extends OrganizationOverviewJaxBean {
	private List<UserOverviewJaxBean> members;
	private List<CaseModelOverviewJaxBean> casemodels;
	private List<UserOverviewJaxBean> owners;

	public OrganizationDetailsJaxBean(Organization org, User requester) {
		super(org);
		setOwners(org.getOwners().values().stream()
					.map(UserOverviewJaxBean::new)
					.collect(Collectors.toList()));
		setMembers(org.getMembers().values().stream()
					.map(UserOverviewJaxBean::new)
					.collect(Collectors.toList()));
		setCasemodels(OrganizationManager.getCaseModels(org, requester).stream()
						.map(CaseModelOverviewJaxBean::new)
						.collect(Collectors.toList()));
	}

	public List<UserOverviewJaxBean> getMembers() {
		return members;
	}

	public void setMembers(List<UserOverviewJaxBean> members) {
		this.members = members;
	}

	public List<CaseModelOverviewJaxBean> getCasemodels() {
		return casemodels;
	}

	public void setCasemodels(List<CaseModelOverviewJaxBean> casemodels) {
		this.casemodels = casemodels;
	}

	public List<UserOverviewJaxBean> getOwners() {
		return owners;
	}

	public void setOwners(List<UserOverviewJaxBean> owners) {
		this.owners = owners;
	}
}
