package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to read in Activity from BPMN standard.
 * Example String
 * <bpmn:task id="Task_0c9phqs" name="Drink coffee">
 *      <bpmn:incoming>SequenceFlow_0ct04t8</bpmn:incoming>
 *      <bpmn:outgoing>SequenceFlow_1gyvb8d</bpmn:outgoing>
 * </bpmn:task>
 */
@XmlRootElement(name = "bpmn:task")
@XmlAccessorType(XmlAccessType.NONE)
public class Task extends AbstractTask {
    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "Activity", this.getFragmentId(), this.getId());
        return this.databaseId;
    }
}
