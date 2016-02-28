package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <bpmn:timerEventDefinition>
 *     <bpmn:timeDuration xsi:type=\"bpmn:tFormalExpression\">PT1M30S
 *     </bpmn:timeDuration>
 * </bpmn:timerEventDefinition>
 */
@XmlRootElement(name =  "bpmn:timerEventDefinition")
@XmlAccessorType(XmlAccessType.NONE)
public class TimerDefinition {
    @XmlElement(name = "bpmn:timeDuration")
    private String timerDuration;

    public String getTimerDuration() {
        return timerDuration;
    }
}
