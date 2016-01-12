package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataInputAssociation;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataOutputAssociation;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.SequenceFlow;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.DataClass;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.InputSet;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Node;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.OutputSet;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for maintaining the correct order to insert elements
 * from the fragment into the database. This is important because some control
 * structures operate on the database Ids of elements which have to be inserted before
 * them.
 */
public class FragmentInserter {

    /**
     * Method which saves a fragment to the
     * @return return database Id of the inserted fragment
     */
    public int save(String fragmentXml, int versionNumber, String fragmentName,
                    int scenarioId, int fragmentId, Map<Long, DataClass> dataClasses) {
        Connector conn = new Connector();

        Fragment fragment = buildFragment(fragmentXml);
        int databaseId = conn.insertFragmentIntoDatabase(fragmentName,
                scenarioId, fragmentId, versionNumber);


        Map<String, Integer> nodeToDatabaseId = saveNodesToDatabase(databaseId, fragment);
        // Save input and output set prior to edges because set database Id of edges
        // is set in this process
        saveSets(fragment);

        saveEdges(fragment, nodeToDatabaseId);

        return databaseId;

    }

    private Map<String, Integer> saveNodesToDatabase(int fragmentId, Fragment fragment) {
        Map<String, Integer> nodeToDatabaseId = new HashMap<>();
        for (Node node : fragment.getNodes()) {
            node.setFragmentId(fragmentId);
            int databaseId = node.save();
            nodeToDatabaseId.put(node.getId(), databaseId);
        }

        return nodeToDatabaseId;
    }

    private void saveEdges(Fragment fragment, Map<String, Integer> nodeToDatabaseId) {
        for (SequenceFlow flow : fragment.getSequenceFlow()) {
            flow.save(nodeToDatabaseId);
        }
        for (DataInputAssociation asso : fragment.getDataInputAssociations()) {
            asso.save(nodeToDatabaseId);
        }
        for (DataOutputAssociation asso : fragment.getDataOutputAssociations()) {
            asso.save(nodeToDatabaseId);
        }
    }

    /**
     * Saves the input and output sets to the database.
     */
    private void saveSets(Fragment fragment) {
        for (InputSet set : fragment.getInputSets()) {
            set.save();
        }
        for (OutputSet set : fragment.getOutputSets()) {
            set.save();
        }
    }

    Fragment buildFragment(String fragmentXml) {

        Document doc = getXmlDocFromString(fragmentXml);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Fragment.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Fragment) jaxbUnmarshaller.unmarshal(doc);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Document getXmlDocFromString(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }


}
