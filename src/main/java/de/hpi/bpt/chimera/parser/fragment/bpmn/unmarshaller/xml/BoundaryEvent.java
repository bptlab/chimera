package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * .
 */
@XmlRootElement(name = "bpmn:boundaryEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class BoundaryEvent extends AbstractDataControlNode {
	/**
	 * Stores the Id of the activity (or subprocess) the Event is attached to.
	 */
	@XmlAttribute(name = "attachedToRef")
	private String attachedToRef;

	@XmlAttribute(name = "griffin:eventquery")
	private String eventQuery;


	/**
	 * @param savedControlNodes Map from the Id of the control nodes to the database Id, which
	 *                          is needed to reference the activity the event is attached to.
	 * @return
	 */
	public void saveConnectionToActivity(Map<String, Integer> savedControlNodes) {
		Connector connector = new Connector();
		int activityDatabaseId = savedControlNodes.get(this.getAttachedToRef());
		connector.insertBoundaryEvent("BoundaryEvent", this.getEventQuery(), this.getFragmentId(), this.getId(), this.getDatabaseId(), activityDatabaseId);
	}

	public String getAttachedToRef() {
		return attachedToRef;
	}

	public void setAttachedToRef(String attachedToRef) {
		this.attachedToRef = attachedToRef;
	}

	@Override
	public int save() {
		Connector connector = new Connector();
		this.databaseId = connector.insertControlNode(this.getName(), "BoundaryEvent", this.getFragmentId(), this.getId());
		return this.getDatabaseId();
	}

	public String getEventQuery() {
		return this.eventQuery;
	}

	public void setEventQuery(String eventQuery) {
		this.eventQuery = eventQuery;
	}

	@Override
	public List<String> getIncoming() {
		List<String> incoming = new ArrayList<>();
		incoming.add(attachedToRef);
		return incoming;
	}
}
