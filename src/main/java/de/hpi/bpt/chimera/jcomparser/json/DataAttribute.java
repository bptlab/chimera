package de.hpi.bpt.chimera.jcomparser.json;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcomparser.saving.IPersistable;

/**
 * This class represents a DataAttribute.
 */
public class DataAttribute implements IPersistable {
	private final String editorId;

	/**
	 * This is the databaseID of the dataClass belonging to this attribute.
	 */
	private int dataClassId = -1;
	/**
	 * This is the name of the attribute.
	 */
	private String name;
	/**
	 * This is the type of the attribute.
	 */
	private String type;
	/**
	 * This is the databaseID of the attribute.
	 */
	private int id;

	/**
	 * Constructor which sets the Name and Type of the DataAttribute.
	 *
	 * @param name This is the name of the DataAttribute.
	 * @param type This is the dataype of the DataAttribute.
	 */
	public DataAttribute(String name, String type, String editorId) {
		this.name = name;
		this.type = type;
		this.editorId = editorId;
	}

	@Override
	public int save() {
		Connector conn = new Connector();
		this.id = conn.insertDataAttribute(this.name, this.dataClassId, this.type);
		return id;
	}

	public int getDataClassId() {
		return dataClassId;
	}

	/**
	 * This method sets the databaseID of the dataClass corresponding to the dataAttribute.
	 *
	 * @param id This is the databaseID of the dataClass
	 */
	public void setDataClassId(final int id) {
		this.dataClassId = id;
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getEditorId() {
		return this.editorId;
	}
}
