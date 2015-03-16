package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.*;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Ihdefix on 04.03.2015.
 */
public class DataAttribute implements IPersistable {
    private int dataClassID = -1;
    private String dataAttributeName;
    private String dataAttributeType;
    private int dataAttributeID;

    public DataAttribute(String attribute) {
        dataAttributeName = attribute;
        dataAttributeType = "";
    }

    /**
     * This constructor is only used for testcases as a connection to the server is not needed therefore
     */
    public DataAttribute() {
    }

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
