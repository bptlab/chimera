package de.hpi.bpt.chimera.jcomparser.jaxb;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EndEvent extends AbstractDataControlNode {

    @XmlElement(name = "bpmn:messageEventDefinition")
    private MessageDefinition message;

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "EndEvent", this.getFragmentId(), this.getId());

        if (message != null) {
            connector.saveSendEvent(databaseId);
        }

        return this.databaseId;
    }
}
