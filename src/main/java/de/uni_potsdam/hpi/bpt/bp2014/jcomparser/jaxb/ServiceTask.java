package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to read in a Service Task from BPMN standard.
 * Example String
 * <bpmn:serviceTask id="ServiceTask_165z0yh" name="Execute service">
 *      <bpmn:incoming>SequenceFlow_0ct04t8</bpmn:incoming>
 *      <bpmn:outgoing>SequenceFlow_1gyvb8d</bpmn:outgoing>
 * </bpmn:serviceTask>
 */
@XmlRootElement(name = "bpmn:serviceTask")
@XmlAccessorType(XmlAccessType.NONE)
public class ServiceTask extends Task{
    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "WebServiceTask", this.getFragmentId(), this.getId());
        return this.databaseId;
    }
}
