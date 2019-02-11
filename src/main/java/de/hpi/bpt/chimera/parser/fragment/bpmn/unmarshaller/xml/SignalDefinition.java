package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <bpmn:signalEventDefinition signalRef=\"Signal_3gms59a\" />\n
 */
@XmlRootElement(name = "bpmn:signalEventDefinition")
@XmlAccessorType(XmlAccessType.NONE)
public class SignalDefinition {
    // TODO: may not be needed
    @XmlElement(name = "bmpn:signalRef")
    private String signalId;

    public String getSignalId() {
        return signalId;
    }
}
