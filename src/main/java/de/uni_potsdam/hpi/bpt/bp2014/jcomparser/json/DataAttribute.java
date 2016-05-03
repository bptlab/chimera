package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.IPersistable;

/**
 * This class represents a DataAttribute.
 */
public class DataAttribute implements IPersistable {
    private final String editorId;

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
	 * Constructor which sets the Name and Type of the DataAttribute.
	 *
	 * @param name This is the name of the DataAttribute.
	 * @param type This is the dataype of the DataAttribute.
	 */
	public DataAttribute(String name, String type, String editorId) {
		this.dataAttributeName = name;
		this.dataAttributeType = type;
        this.editorId = editorId;
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

    public String getEditorId() {
        return "";
    }
}
