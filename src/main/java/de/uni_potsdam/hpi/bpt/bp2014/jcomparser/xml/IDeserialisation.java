package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface IDeserialisation {

    public void initializeInstanceFromXML(Node element);
}
