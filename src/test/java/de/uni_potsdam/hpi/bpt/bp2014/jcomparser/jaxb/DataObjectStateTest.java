package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.DatabaseFragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Node;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DataObjectStateTest {
    @Test
    public void testDataObjectStates() {
        File file = new File("src/test/resources/fragments/DataObjectStatesFragment.xml");
        try {
            String xml = FileUtils.readFileToString(file);
            DatabaseFragment frag = new DatabaseFragment();
            String fragmentName = "someName";
            int versionNumber  = 1;
            int fragmentId = 1;
            frag.initializeFromXml(xml, versionNumber, fragmentName, fragmentId);
            List<Node> nodes = new ArrayList<>(frag.getControlNodes().values());
            List<Node> dataNodes = new ArrayList<>();
            List<String> states = new ArrayList<>();
            for (Node node : nodes) {
                if (node.isDataNode()) {
                    dataNodes.add(node);
                    states.add(node.getState());
                }
            }
            assertEquals(2, dataNodes.size());
            assertEquals(dataNodes.get(0).getText(), dataNodes.get(1).getText());
            assert(states.contains("received"));
            assert(states.contains("reviewed"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
