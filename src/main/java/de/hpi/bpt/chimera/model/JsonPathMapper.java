package de.hpi.bpt.chimera.model;

import java.util.Map;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

/**
 * A mapper from an DataStateCondition to a mapping from a DataAttribute of the
 * DataClass of the DataStateCondition and the Json String used for writing the
 * value of the DataAttribute.
 */
public interface JsonPathMapper {
	public Map<AtomicDataStateCondition, Map<DataAttribute, String>> getJsonPathMapping();

	public void setJsonPathMapping(Map<AtomicDataStateCondition, Map<DataAttribute, String>> jsonPathMapping);
}
