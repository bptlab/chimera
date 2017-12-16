package de.hpi.bpt.chimera.model.fragment.bpmn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.DataAttributeJsonPath;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;

@Entity
public abstract class AbstractDataControlNode extends AbstractControlNode {
	private String name = "";

	@OneToOne(cascade = CascadeType.ALL)
	private DataStateCondition preCondition;
	@OneToOne(cascade = CascadeType.ALL)
	private DataStateCondition postCondition;

	@Transient
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

	public boolean hasPreCondition() {
		return !preCondition.isEmpty();
	}

	public DataStateCondition getPostCondition() {
		return postCondition;
	}

	public void setPostCondition(DataStateCondition postCondition) {
		this.postCondition = postCondition;
	}

	public boolean hasPostCondition() {
		return !postCondition.isEmpty();
	}

	/**
	 * 
	 * @return true if there is maximal one ConditionSet
	 */
	public boolean hasUniquePostCondition() {
		return postCondition.getConditionSets().size() <= 1;
	}


	public Map<AtomicDataStateCondition, Map<DataAttribute, String>> getJsonPathMapping() {
		if (jsonPathMapping == null) {
			parseJsonPathMapping();
		}
		return jsonPathMapping;
	}

	private void parseJsonPathMapping() {
		jsonPathMapping = new HashMap<>();
		for (AtomicDataStateCondition condition : postCondition.getAtomicDataStateConditions()) {
			if (!(condition instanceof DataNode)) {
				continue;
			}
			DataNode dataNode = (DataNode) condition;
			List<DataAttributeJsonPath> dataAttributeJsonPaths = dataNode.getDataAttributeJsonPaths();
			if (!dataAttributeJsonPaths.isEmpty()) {
				Map<DataAttribute, String> dataAttributeToJsonPath = new HashMap<>();
				for (DataAttributeJsonPath dataAttributeJsonPath : dataAttributeJsonPaths) {
					dataAttributeToJsonPath.put(dataAttributeJsonPath.getDataAttribute(), dataAttributeJsonPath.getJsonPath());
				}
				jsonPathMapping.put(condition, dataAttributeToJsonPath);
			}
		}
	}
}
