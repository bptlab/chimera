package de.hpi.bpt.chimera.jcomparser.jaxb;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bpmn:intermediateThrowEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class IntermediateThrowEvent extends AbstractDataControlNode {

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNode(
                this.getName(), "IntermediateThrowEvent", this.fragmentId, this.getId());

        return databaseId;
    }
}
