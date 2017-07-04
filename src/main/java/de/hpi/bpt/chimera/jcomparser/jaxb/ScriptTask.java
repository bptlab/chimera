package de.hpi.bpt.chimera.jcomparser.jaxb;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to read in a Script Task from BPMN standard.
 *
 * Example String
 * <bpmn:scriptTask id="ScriptTask_1wx3xx0" name="SimpleScriptTask\"
 *      griffin:scripttaskjar="SimpleScriptTask.jar"
 *      griffin:scripttaskclasspath="de.hpi.bpt.reschedule.InformParticipants"
 *      griffin:scripttaskfile="a.java">
 * <bpmn:incoming>SequenceFlow_1t2gh1s</bpmn:incoming>
 * <bpmn:outgoing>SequenceFlow_0s4ofgo</bpmn:outgoing>
 * </bpmn:scriptTask>
 */
@XmlRootElement(name = "bpmn:scriptTask")
@XmlAccessorType(XmlAccessType.NONE)
public class ScriptTask extends AbstractDataControlNode {

    @XmlAttribute(name = "griffin:scripttaskjar")
    String scriptTaskJar = "";

    @XmlAttribute(name = "griffin:scripttaskclasspath")
    String scriptTaskClassPath = "";

    /**
     * Save the script task in the database
     *
     * @return an int containing the id of the database entry
     */
    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNode(this.getName(), "ScriptTask", this.getFragmentId(), this.getId());
        connector.insertScriptTask(this.databaseId, scriptTaskJar, scriptTaskClassPath);
        return this.databaseId;
    }

}
