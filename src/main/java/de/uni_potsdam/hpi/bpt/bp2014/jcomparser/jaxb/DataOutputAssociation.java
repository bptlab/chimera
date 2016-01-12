package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import javax.xml.bind.annotation.*;
import java.util.Map;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataOutputAssociation")
@XmlAccessorType(XmlAccessType.NONE)
public class DataOutputAssociation extends Edge {
    @XmlAttribute
    private String id;
    @XmlElement(name = "bpmn:targetRef")
    private String targetRef;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public int save(Map<String, Integer> nodeToDatabaseId) {
        int targetDatabaseId = nodeToDatabaseId.get(targetRef);
        Connector connector = new Connector();
        connector.insertDataFlowIntoDatabase(targetDatabaseId, setId, false);
        return 0;
    }

}
