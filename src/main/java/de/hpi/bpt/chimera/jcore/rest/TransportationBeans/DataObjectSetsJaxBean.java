package de.hpi.bpt.chimera.jcore.rest.TransportationBeans;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 *
 */
@XmlRootElement
public class DataObjectSetsJaxBean {
	/**
	 * Map from name of data class to a list of possible states.w
	 */
	@XmlAnyAttribute
	private Map<String, List<String>> dataObjects;

	Map<String, List<String>> getDataObjects() {
		return this.dataObjects;
	}

	public void setDataObjects(Map<String, List<String>> dataObjects) {
		this.dataObjects = dataObjects;
	}
}
