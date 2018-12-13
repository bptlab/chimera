package de.hpi.bpt.chimera.rest.beans.casemodel;

import de.hpi.bpt.chimera.model.fragment.Fragment;

public class FragmentJaxBean {
    private String name;
    private String bpmn;

    public FragmentJaxBean(Fragment fragment) {
        setName(fragment.getName());
        setBpmn(fragment.getContentXML());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBpmn() {
        return bpmn;
    }

    public void setBpmn(String bpmn) {
        this.bpmn = bpmn;
    }
}
