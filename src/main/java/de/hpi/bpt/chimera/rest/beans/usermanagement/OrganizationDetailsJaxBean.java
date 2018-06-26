package de.hpi.bpt.chimera.rest.beans.usermanagement;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelOverviewJaxBean;
import de.hpi.bpt.chimera.usermanagment.Organization;

@XmlRootElement
public class OrganizationDetailsJaxBean extends OrganizationOverviewJaxBean {
	private MemberOverviewJaxBean[] members;
	private CaseModelOverviewJaxBean[] casemodels;
	private MemberOverviewJaxBean owner;

	public OrganizationDetailsJaxBean(Organization organization) {
		super(organization);
		setOwner(new MemberOverviewJaxBean(organization.getOwner()));
		List<MemberOverviewJaxBean> membersList = organization.getMembers().values().stream()
														.map(MemberOverviewJaxBean::new)
														.collect(Collectors.toList());
		MemberOverviewJaxBean[] membersArray = membersList.toArray(new MemberOverviewJaxBean[membersList.size()]);
		setMembers(membersArray);
		
		List<CaseModelOverviewJaxBean> cmList = organization.getCaseModels().values().stream()
													.map(CaseModelOverviewJaxBean::new)
													.collect(Collectors.toList());
		CaseModelOverviewJaxBean[] cmArray = cmList.toArray(new CaseModelOverviewJaxBean[cmList.size()]);
		setCasemodels(cmArray);
	}

	public MemberOverviewJaxBean[] getMembers() {
		return members;
	}

	public void setMembers(MemberOverviewJaxBean members[]) {
		this.members = members;
	}

	public CaseModelOverviewJaxBean[] getCasemodels() {
		return casemodels;
	}

	public void setCasemodels(CaseModelOverviewJaxBean casemodels[]) {
		this.casemodels = casemodels;
	}

	public MemberOverviewJaxBean getOwner() {
		return owner;
	}

	public void setOwner(MemberOverviewJaxBean owner) {
		this.owner = owner;
	}
}
