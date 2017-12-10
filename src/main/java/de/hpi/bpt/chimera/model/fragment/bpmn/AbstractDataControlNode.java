package de.hpi.bpt.chimera.model.fragment.bpmn;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.JsonPathMapper;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;

@Entity
public abstract class AbstractDataControlNode extends AbstractControlNode implements JsonPathMapper {
	private String name = "";

	@OneToOne(cascade = CascadeType.ALL)
	private DataStateCondition preCondition;
	@OneToOne(cascade = CascadeType.ALL)
	private DataStateCondition postCondition;
	@ElementCollection
	private Map<AtomicDataStateCondition, Map<DataAttribute, String>> jsonPathMapping;

	public AbstractDataControlNode() {
		this.preCondition = new DataStateCondition();
		this.postCondition = new DataStateCondition();
	}

	// GETTER & SETTER
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataStateCondition getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(DataStateCondition preCondition) {
		this.preCondition = preCondition;
	}

	public DataStateCondition getPostCondition() {
		return postCondition;
	}

	public void setPostCondition(DataStateCondition postCondition) {
		this.postCondition = postCondition;
	}

	@Override
	public Map<AtomicDataStateCondition, Map<DataAttribute, String>> getJsonPathMapping() {
		return null;
		// return jsonPathMapping;
	}

	@Override
	public void setJsonPathMapping(Map<AtomicDataStateCondition, Map<DataAttribute, String>> jsonPathMapping) {
		// this.jsonPathMapping = jsonPathMapping;
	}

}
