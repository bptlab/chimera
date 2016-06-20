package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bpmn:intermediateThrowEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class IntermediateThrowEvent extends AbstractDataControlNode {

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "IntermediateThrowEvent", this.fragmentId, this.getId());

        return databaseId;
    }
}
