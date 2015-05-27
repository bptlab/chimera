package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.w3c.dom.Node;


public interface IDeserialisable {

    /**
     * A Method to deserialize a model element from XML.
     *
     * @param element The XML Node which will be used for deserialisation
     */
    void initializeInstanceFromXML(Node element);
}
