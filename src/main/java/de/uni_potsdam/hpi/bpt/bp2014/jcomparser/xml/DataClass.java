package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Retrieval;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ihdefix on 04.03.2015.
 */
public class DataClass implements IDeserialisable, IPersistable {
    /**
     *
     */
    private long dataClassModelID;
    /**
     *
     */
    private int dataClassID;
    /**
     *
     */
    private String dataClassName;
    /**
     *
     */
    private boolean rootNode = false;
    /**
     *
     */
    private List<DataAttribute> dataAttributes = new LinkedList<>();
    /**
     *
     */
    private org.w3c.dom.Node dataClassXML;
    /**
     *
     * @param element The XML Node which will be used for deserialisation
     */
    /**
     * Sets the processeditorServerUrl which is needed for connecting to the server
     * in order to get the XML-files for the fragments.
     */
    public DataClass(String serverURL) {

    }

    /**
     * This constructor is only used for testcases as a connection to the server is not needed therefore
     */
    public DataClass() {
    }
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {
        this.dataClassXML = element;
        NodeList properties = element.getChildNodes();
        for(int i = 0; i < properties.getLength(); i++){
            if(properties.item(i).getNodeName().equals("property")){
                org.w3c.dom.Node property = properties.item(i);
                initializeField(property);
            }
        }
    }

    private void initializeField(final org.w3c.dom.Node property) {

        NamedNodeMap attributes = property.getAttributes();
        String name = attributes.getNamedItem("name").getTextContent();
        String value = attributes.getNamedItem("value").getTextContent();

        switch (name) {
            case "text":
                dataClassName = value;
                break;
            case "#attributes":
                generateDataAttributeList(value);
                break;
            case "#id":
                dataClassModelID = Long.parseLong(value);
                break;
            case "stereotype":
                rootNode = value.equals("root_instance");
                break;
            default:
                // Property will not be handled
                break;
        }
    }

    @Override
    public int save() {
        Connector conn = new Connector();
        this.dataClassID = conn.insertDataClassIntoDatabase(
                this.dataClassName);
        saveDataAttributes();

        return dataClassID;
    }

    private void generateDataAttributeList(String value) {
        String[] attributes = value.split(" ;");
        for(String attribute : attributes){
            if(!attribute.isEmpty()){
                DataAttribute newDataAttribute = new DataAttribute(attribute.replaceAll("[^a-zA-Z]", ""));
                dataAttributes.add(newDataAttribute);
            }
        }
    }

    private void saveDataAttributes() {
        for (DataAttribute dataAttribute : dataAttributes) {
            dataAttribute.setDataClassID(dataClassID);
            dataAttribute.save();
        }
    }

    public int getDataClassID() {
        return dataClassID;
    }

    public String getDataClassName() {
        return dataClassName;
    }

    public List<DataAttribute> getDataAttributes() {
        return dataAttributes;
    }

    public boolean isRootNode(){ return rootNode;}

    public Node getDataClassXML() {
        return dataClassXML;
    }

    public long getDataClassModelID() {
        return dataClassModelID;
    }

}
