package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.w3c.dom.*;
import org.w3c.dom.Node;

import java.util.Map;

public class Aggregation implements IDeserialisable, IPersistable {
    private DataClass source;
    private DataClass target;
    private int multiplicity;
    private Map<Long, DataClass> dataClasses;
    private org.w3c.dom.Node aggregationXML;

    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {
        this.aggregationXML = element;
        NodeList properties = element.getChildNodes();
        for (int i = 0; i < properties.getLength(); i++) {
            if (properties.item(i).getNodeName().equals("property")) {
                org.w3c.dom.Node property = properties.item(i);
                initializeField(property);
            }
        }
    }

    private void initializeField(Node property) {

        NamedNodeMap attributes = property.getAttributes();
        String name = attributes.getNamedItem("name").getTextContent();
        String value = attributes.getNamedItem("value").getTextContent();

        switch (name) {
            case "#sourceNode":
                setSourceNode(value);
                break;
            case "#targetNode":
                setTargetNode(value);
                break;
            case "sourceMultiplicity":
                setMultiplicity(value);
                break;
            default:
                // Property will not be handled
                break;
        }
    }

    private void setMultiplicity(String value) {
        String[] multiplicity = value.split("\\.\\.");
        if (multiplicity.length != 0) {
            if (multiplicity[multiplicity.length - 1].equals("*")) {
                this.multiplicity = Integer.MAX_VALUE;
            } else {
                this.multiplicity = Integer.parseInt(multiplicity[multiplicity.length - 1]);
            }
        }
    }

    private void setTargetNode(String value) {
        if (value != null && this.dataClasses != null) {
            long targetNodeModelID = Long.parseLong(value);
            this.target = this.dataClasses.get(targetNodeModelID);
        }
    }

    private void setSourceNode(String value) {
        if (value != null && this.dataClasses != null) {

            long sourceNodeModelID = Long.parseLong(value);
            this.source = this.dataClasses.get(sourceNodeModelID);
        }
    }

    @Override
    public int save() {
        Connector conn = new Connector();
        conn.insertAggregationIntoDatabase(
                this.source.getDataClassID(),
                this.target.getDataClassID(),
                this.multiplicity);
        return 1;
    }

    public void setDataClasses(Map<Long, DataClass> dataClasses) {
        this.dataClasses = dataClasses;
    }

    public Map<Long, DataClass> getDataClasses() {
        return dataClasses;
    }

    public DataClass getSource() {
        return source;
    }

    public DataClass getTarget() {
        return target;
    }

    public int getMultiplicity() {
        return multiplicity;
    }

}
