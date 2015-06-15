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

public class DataClass implements IDeserialisable, IPersistable {
    /**
     * This is the modelID of the dataClass.
     */
    private long dataClassModelID;
    /**
     * This is the databaseID of the dataClass.
     */
    private int dataClassID;
    /**
     * This is the name of the dataClass.
     */
    private String dataClassName;
    /**
     * This boolean is used to identify the root element
     * of the domainModel which is used to get the caseDataObject.
     */
    private boolean isRootNode = false;
    /**
     * This is a list containing all dataAttributes belonging to this dataClass.
     */
    private List<DataAttribute> dataAttributes = new LinkedList<>();
    /**
     * This contains the XML representation of the dataClass.
     */
    private org.w3c.dom.Node dataClassXML;

    /**
     * Sets the processeditorServerUrl which is needed for connecting to the server
     * in order to get the XML-files for the fragments.
     */
    public DataClass(String serverURL) {

    }

    /**
     * The standard constructor.
     */
    public DataClass() {
    }

    /**
     * This initializes the dataClass from the XML.
     *
     * @param element The XML Node which will be used for deserialisation
     */
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {
        this.dataClassXML = element;
        NodeList properties = element.getChildNodes();
        for (int i = 0; i < properties.getLength(); i++) {
            if (properties.item(i).getNodeName().equals("property")) {
                org.w3c.dom.Node property = properties.item(i);
                initializeField(property);
            }
        }
    }

    /**
     * This sets the corresponding attributes of the dataClass class or
     * calls corresponding method based on the property found in the XML.
     *
     * @param property
     */
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
                isRootNode = value.equals("root_instance");
                break;
            default:
                // Property will not be handled
                break;
        }
    }

    /**
     * This method saves the dataClass to the database.
     *
     * @return the databaseID of the dataClass.
     */
    @Override
    public int save() {
        Connector conn = new Connector();
        int root = this.isRootNode ? 1 : 0;
        this.dataClassID = conn.insertDataClassIntoDatabase(
                this.dataClassName,
                root);
        saveDataAttributes();

        return dataClassID;
    }

    /**
     * This method gets all the dataAttributes from the XML.
     * DataAttributes can only be alphanumerical.
     *
     * @param value This String contains all dataAttributes from the XML, separated by ";".
     */
    private void generateDataAttributeList(String value) {
        String[] attributes = value.split(" ;");
        for (String attribute : attributes) {
            if (!attribute.isEmpty()) {
                /*
                    DataAttributes are saved in the following form:
                    "{[number]}+[nameOfTheAttribute]".
                    This regex removes all the stuff not needed.
                 */
                DataAttribute newDataAttribute = new DataAttribute(attribute.replaceAll("\\{[0-9]*\\}|[^a-zA-Z0-9]", ""));
                dataAttributes.add(newDataAttribute);
            }
        }
    }

    /**
     * This method iterates through all dataAttributes and sets
     * the dataClass for them as well as calling the save method.
     */
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

    public boolean isRootNode() {
        return isRootNode;
    }

    public Node getDataClassXML() {
        return dataClassXML;
    }

    public long getDataClassModelID() {
        return dataClassModelID;
    }

}
