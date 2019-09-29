package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import javax.persistence.Entity;

@Entity
public class ResourceTask extends AbstractActivity {
    @Override
    public boolean isAutomatic() {
        return true;
    }
}
