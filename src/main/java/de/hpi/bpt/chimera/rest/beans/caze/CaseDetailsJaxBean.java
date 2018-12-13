package de.hpi.bpt.chimera.rest.beans.caze;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.rest.beans.activity.ActivitiesByStatesJaxBean;
import de.hpi.bpt.chimera.rest.beans.casemodel.CaseModelDetailsJaxBean;
import de.hpi.bpt.chimera.rest.beans.datamodel.DataObjectJaxBean;

import java.util.List;
import java.util.stream.Collectors;

public class CaseDetailsJaxBean extends CaseOverviewJaxBean {
    private CaseModelDetailsJaxBean casemodel;
    private List<DataObjectJaxBean> dataobjects;
    private ActivitiesByStatesJaxBean activities;

    public CaseDetailsJaxBean(CaseExecutioner caseExecutioner) {
        super(caseExecutioner);
        setCasemodel(new CaseModelDetailsJaxBean(caseExecutioner.getCaseModel()));
        setDataobjects(caseExecutioner.getDataManager().getDataObjects().stream()
                        .map(DataObjectJaxBean::new)
                        .collect(Collectors.toList()));
        setActivities(new ActivitiesByStatesJaxBean(caseExecutioner.getActivities()));
    }

    public CaseModelDetailsJaxBean getCasemodel() {
        return casemodel;
    }

    public void setCasemodel(CaseModelDetailsJaxBean casemodel) {
        this.casemodel = casemodel;
    }

    public List<DataObjectJaxBean> getDataobjects() {
        return dataobjects;
    }

    public void setDataobjects(List<DataObjectJaxBean> dataobjects) {
        this.dataobjects = dataobjects;
    }

    public ActivitiesByStatesJaxBean getActivities() {
        return activities;
    }

    public void setActivities(ActivitiesByStatesJaxBean activities) {
        this.activities = activities;
    }
}
