package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.w3c.dom.*;
import org.w3c.dom.Node;

import java.util.Map;

/**
 * Created by Ihdefix on 04.03.2015.
 */
public class Aggregation implements IDeserialisable, IPersistable {
    private DataClass source;
    private DataClass target;
    private int multiplicity;
    @Override
    public void initializeInstanceFromXML(final org.w3c.dom.Node element) {

    }

    @Override
    public int save() {
        return 0;
    }

    public void setDataClasses(Map<Long, DataClass> dataClasses) {

    }
}
