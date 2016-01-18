package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.w3c.dom.*;

/**
 *
 */
public interface IDeserialisableJson {
	/**
	 * A Method to deserialize a model element from JSON.
	 *
	 * @param element The JSON Object which will be used for deserialisation
	 */
	void initializeInstanceFromJson(String element);
}