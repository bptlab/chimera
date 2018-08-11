package de.hpi.bpt.chimera.rest.beans.organization;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelOverviewJaxBean;
import de.hpi.bpt.chimera.rest.beans.usermanagement.UserOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagment.Organization;
import de.hpi.bpt.chimera.usermanagment.OrganizationManager;
import de.hpi.bpt.chimera.usermanagment.User;

@XmlRootElement
public class OrganizationDetailsJaxBean extends OrganizationOverviewJaxBean {
	private UserOverviewJaxBean[] members;
	private CaseModelOverviewJaxBean[] casemodels;
	private UserOverviewJaxBean[] owners;

	public OrganizationDetailsJaxBean(Organization org, User requester) {
		super(org);
		owners = org.getOwners().values().stream()
					.map(UserOverviewJaxBean::new)
					.toArray(UserOverviewJaxBean[]::new);
		members = org.getMembers().values().stream()
					.map(UserOverviewJaxBean::new)
					.toArray(UserOverviewJaxBean[]::new);
		casemodels = OrganizationManager.getCaseModels(org, requester).stream()
						.map(CaseModelOverviewJaxBean::new)
						.toArray(CaseModelOverviewJaxBean[]::new);
	}

	public UserOverviewJaxBean[] getMembers() {
		return members;
	}

	public void setMembers(UserOverviewJaxBean[] members) {
		this.members = members;
	}

	public CaseModelOverviewJaxBean[] getCasemodels() {
		return casemodels;
	}

	public void setCasemodels(CaseModelOverviewJaxBean[] casemodels) {
		this.casemodels = casemodels;
	}

	public UserOverviewJaxBean[] getOwners() {
		return owners;
	}

	public void setOwners(UserOverviewJaxBean[] owners) {
		this.owners = owners;
	}
}
