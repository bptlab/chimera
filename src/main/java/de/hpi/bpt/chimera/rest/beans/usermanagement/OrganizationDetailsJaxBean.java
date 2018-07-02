package de.hpi.bpt.chimera.rest.beans.usermanagement;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagment.Organization;

@XmlRootElement
public class OrganizationDetailsJaxBean extends OrganizationOverviewJaxBean {
	private UserOverviewJaxBean[] members;
	private CaseModelOverviewJaxBean[] casemodels;
	private UserOverviewJaxBean[] owners;

	public OrganizationDetailsJaxBean(Organization organization) {
		super(organization);
		List<UserOverviewJaxBean> ownersList = organization.getMembers().values().stream()
													.map(UserOverviewJaxBean::new)
													.collect(Collectors.toList());
		UserOverviewJaxBean[] ownersArray = ownersList.toArray(new UserOverviewJaxBean[ownersList.size()]);
		setOwners(ownersArray);

		List<UserOverviewJaxBean> membersList = organization.getMembers().values().stream()
														.map(UserOverviewJaxBean::new)
														.collect(Collectors.toList());
		UserOverviewJaxBean[] membersArray = membersList.toArray(new UserOverviewJaxBean[membersList.size()]);
		setMembers(membersArray);
		
		List<CaseModelOverviewJaxBean> cmList = organization.getCaseModels().values().stream()
													.map(CaseModelOverviewJaxBean::new)
													.collect(Collectors.toList());
		CaseModelOverviewJaxBean[] cmArray = cmList.toArray(new CaseModelOverviewJaxBean[cmList.size()]);
		setCasemodels(cmArray);
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
