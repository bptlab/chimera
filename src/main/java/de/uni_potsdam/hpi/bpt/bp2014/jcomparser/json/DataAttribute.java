package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

/**
 * This class represents a DataAttribute.
 */
public class DataAttribute implements IPersistable {
	/**
	 * This is the databaseID of the dataClass belonging to this attribute.
	 */
	private int dataClassID = -1;
	/**
	 * This is the name of the attribute.
	 */
	private String dataAttributeName;
	/**
	 * This is the type of the attribute.
	 */
	private String dataAttributeType;
	/**
	 * This is the databaseID of the attribute.
	 */
	private int dataAttributeID;

	/**
	 * Constructor which sets the Name of the DataAttribute.
	 *
	 * @param name This is the name of the DataAttribute.
	 */
	public DataAttribute(String name) {
		this.dataAttributeName = name;
		this.dataAttributeType = "";
	}

	/**
	 * Constructor which sets the Name and Type of the DataAttribute.
	 *
	 * @param name This is the name of the DataAttribute.
	 * @param type This is the dataype of the DataAttribute.
	 */
	public DataAttribute(String name, String type) {
		this.dataAttributeName = name;
		this.dataAttributeType = type;
	}

	/**
	 * This constructor is only used for testCases
	 * therefore, a connection to the server is not needed.
	 */
	public DataAttribute() {
	}

	/**
	 * This method sets the databaseID of the dataClass corresponding to the dataAttribute.
	 *
	 * @param id This is the databaseID of the dataClass
	 */
	public void setDataClassID(final int id) {
		this.dataClassID = id;
	}

	@Override public int save() {
		Connector conn = new Connector();
		this.dataAttributeID = conn.insertDataAttributeIntoDatabase(
				this.dataAttributeName, this.dataClassID,
						this.dataAttributeType);
		return dataAttributeID;
	}

	public int getDataClassID() {
		return dataClassID;
	}

	public int getDataAttributeID() {
		return dataAttributeID;
	}

	public String getDataAttributeType() {
		return dataAttributeType;
	}

	public String getDataAttributeName() {
		return dataAttributeName;
	}
}
