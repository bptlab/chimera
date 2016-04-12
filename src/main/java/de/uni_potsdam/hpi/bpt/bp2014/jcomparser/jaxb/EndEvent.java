package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EndEvent extends AbstractDataControlNode {


    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "EndEvent", this.getFragmentId(), this.getId());

        connector.insertEventIntoDatabase("EndEvent", "None",
                this.fragmentId, this.getId(), this.databaseId);

        return this.databaseId;
    }
}
