package de.hpi.bpt.chimera.jcomparser.jaxb;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bpmn:scriptTask")
@XmlAccessorType(XmlAccessType.NONE)
public class ScriptTask extends AbstractDataControlNode {

    @XmlAttribute(name = "griffin:scripttaskjar")
    String scriptTaskJar = "";

    @XmlAttribute(name = "griffin:scripttaskclasspath")
    String scriptTaskClassPath = "";

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNode(this.getName(), "ScriptTask", this.getFragmentId(), this.getId());
        connector.insertScriptTask(this.databaseId, scriptTaskJar, scriptTaskClassPath);
        return this.databaseId;
    }

}
