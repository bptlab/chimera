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
    private int dataClassID;
    /**
     *
     */
    private String dataClassName;
    /**
     *
     */
    private String processeditorServerUrl;
    /**
     *
     */
    private List<DataAttribute> dataAttributes;
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
        processeditorServerUrl = serverURL;
    }

    /**
     * This constructor is only used for testcases as a connection to the server is not needed therefore
     */
    public DataClass() {
    }
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {
        this.dataClassXML = element;
        setDataClassName();
        generateDataAttributeList();
    }


    private void setDataClassName() {

        XPath xPath = XPathFactory.newInstance().newXPath();
        String xPathQuery = "/model/@name";
        try {
            this.dataClassName = xPath
                    .compile(xPathQuery)
                    .evaluate(this.dataClassXML);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
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
    private void generateDataAttributeList() {
        try {
            //look for all fragments in the scenarioXML and save their node
            XPath xPath = XPathFactory.newInstance().newXPath();
            //TODO fix it for dataAttributes
            String xPathQuery =
                    "/model/nodes/node[property[@name = '#type' and " +
                            "@value = 'net.frapu.code.visualization.pcm.PCMFragmentNode']]";
            NodeList dataAttributesNodes = (NodeList) xPath
                    .compile(xPathQuery)
                    .evaluate(this.dataClassXML, XPathConstants.NODESET);
            this.dataAttributes = new LinkedList<DataAttribute>();

            for (int i = 0; i < dataAttributesNodes.getLength(); i++) {
                // get the ID of the current node
                //TODO fix it for dataAttributes
                xPathQuery = "property[@name = 'fragment mid']/@value";
                String currentNodeID = xPath
                        .compile(xPathQuery)
                        .evaluate(dataAttributesNodes.item(i));
                this.dataAttributes.add(createAndInitializeDataAttribute(currentNodeID));
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private DataAttribute createAndInitializeDataAttribute(String dataAttributeID) {
        Retrieval retrieval = new Retrieval();
        String dataAttributeXML = retrieval.getHTMLwithAuth(
                this.processeditorServerUrl,
                this.processeditorServerUrl +
                        "models/" + dataAttributeID + ".pm");
        DataAttribute dataAttribute = new DataAttribute(processeditorServerUrl);
        dataAttribute.initializeInstanceFromXML(stringToDocument(dataAttributeXML));
        return dataAttribute;
    }

    private Document stringToDocument(final String xml) {
        try {
            DocumentBuilder db = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
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

    public Node getDataClassXML() {
        return dataClassXML;
    }

}
