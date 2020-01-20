package de.hpi.bpt.chimera.model.fragment.bpmn.activity;

import javax.persistence.Entity;

@Entity
public class ResourceTask extends AbstractActivity {

    private String host;
    private String ID;
    private String problemDefinition;
    private String optimizationMethod;
    private String contentType;

    /**
     * ResourceTasks are executed automatically
     * @return true
     */
    @Override
    public boolean isAutomatic() {
        return true;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getID() {
        return ID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProblemDefinition() {
        return problemDefinition;
    }

    public void setProblemDefinition(String problemDefinition) {
        this.problemDefinition = problemDefinition;
    }

    public String getOptimizationMethod() {
        return optimizationMethod;
    }

    public void setOptimizationMethod(String optimizationMethod) {
        this.optimizationMethod = optimizationMethod;
    }

}
