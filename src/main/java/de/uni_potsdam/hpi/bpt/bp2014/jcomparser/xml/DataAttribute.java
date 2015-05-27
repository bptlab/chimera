package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.*;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
     * Constructor which sets the attributeName and type of the DataAttribute.
     *
     * @param attribute This is the name of the dataAttribute.
     */
    public DataAttribute(String attribute) {
        dataAttributeName = attribute;
        dataAttributeType = "";
    }

    /**
     * This constructor is only used for testCases as a connection to the server is not needed therefore
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

    @Override
    public int save() {
        Connector conn = new Connector();
        this.dataAttributeID = conn.insertDataAttributeIntoDatabase(
                this.dataAttributeName,
                this.dataClassID,
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
