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
public class DataAttribute implements IDeserialisable, IPersistable {
    private int dataClassID = -1;
    private String dataAttributeValue;
    private String dataAttributeName;
    private String dataAttributeType;
    private int dataAttributeID;
    private String processeditorServerUrl;
    private org.w3c.dom.Node dataAttributeXML;

    public DataAttribute(String serverURL) {
        processeditorServerUrl = serverURL;
    }

    /**
     * This constructor is only used for testcases as a connection to the server is not needed therefore
     */
    public DataAttribute() {
    }
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {

        this.dataAttributeXML = element;
        setDataAttributeName();
        setDataAttributeValue();
        setDataAttributeType();
    }

    private void setDataAttributeValue() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@value";
        try {
            this.dataAttributeValue = xPath
                    .compile(xPathQuery)
                    .evaluate(this.dataAttributeXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private void setDataAttributeType() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@type";
        try {
            this.dataAttributeType = xPath
                    .compile(xPathQuery)
                    .evaluate(this.dataAttributeXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private void setDataAttributeName() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@name";
        try {
            this.dataAttributeName = xPath
                    .compile(xPathQuery)
                    .evaluate(this.dataAttributeXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
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
                this.dataAttributeValue,
                this.dataAttributeType);
        return dataAttributeID;
    }
    public int getDataClassID() {
        return dataClassID;
    }

    public String getDataAttributeValue() {
        return dataAttributeValue;
    }

    public Node getDataAttributeXML() {
        return dataAttributeXML;
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
