package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 *
 */
public class SoundnessValidatorTest {

    private Set<String> nodes = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E", "F"));

    /**
     * Create a graph containing a cycle, multiple sinks and a detached node.
     * A → B → D → F
     *      ↖←↙ ↘
     *  C         E
     * @return a sample graph for this test.
     */
    private Map<String, Set<String>> getSampleGraph() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("A", new HashSet<>(Collections.singletonList("B")));
        graph.put("B", new HashSet<>(Collections.singletonList("D")));
        graph.put("D", new HashSet<>(Arrays.asList("B", "E", "F")));
        return graph;
    }

    /**
     * Create the reverse graph for {@link #getSampleGraph()}
     * @return a sample reverse graph.
     */
    private Map<String, Set<String>> getReverseGraph() {
        Map<String, Set<String>> reverseGraph = new HashMap<>();
        reverseGraph.put("F", new HashSet<>(Collections.singletonList("D")));
        reverseGraph.put("E", new HashSet<>(Collections.singletonList("D")));
        reverseGraph.put("D", new HashSet<>(Collections.singletonList("B")));
        reverseGraph.put("B", new HashSet<>(Arrays.asList("A", "D")));
        return reverseGraph;
    }

    private Fragment getFragmentNotSound() {
        //Since we need fragments in various tests, we should extract something like "createFragmentFromFile()"
        String path = "src/test/resources/fragments/FragmentNotSound.xml";
        File file = new File(path);
        try {
            String xml = FileUtils.readFileToString(file);
            int versionNumber = 0;
            String fragmentName = "aDummyName";
            String fragmentId = "aDummyId";
            return new Fragment(xml, versionNumber, fragmentName, fragmentId);

        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Fragment getSoundFragment() {
        //Since we need fragments in various tests, we should extract something like "createFragmentFromFile()"
        String path = "src/test/resources/fragments/SoundFragment.xml";
        File file = new File(path);
        try {
            String xml = FileUtils.readFileToString(file);
            int versionNumber = 0;
            String fragmentName = "aDummyName";
            String fragmentId = "aDummyId";
            return new Fragment(xml, versionNumber, fragmentName, fragmentId);

        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidateStructuralSoundness() throws Exception {
        SoundnessValidator.validateStructuralSoundness(getFragmentNotSound());
    }

    @Test
    public void testValidateStructuralSoundness2() throws Exception {
        SoundnessValidator.validateStructuralSoundness(getSoundFragment());
        assert true;
    }

    @Test
    public void testGetLastNode() throws Exception {
        assertEquals("Couldn't identify last node correctly", "A", SoundnessValidator.getLastNode(nodes, getReverseGraph()));
    }

    @Ignore @Test
    public void testBuildGraphFromFragment() throws Exception {
        //maybe we can use an invalid bpmn-diagram to simplify this test
        //assertEquals("Couldn't build the graph correctly", getSampleGraph(), SoundnessValidator.buildGraphFromFragment(fragment));
    }

    @Test
    public void testBuildReverseGraph() throws Exception {
        assertEquals("The reverse graph wan't build correctly", getReverseGraph(), SoundnessValidator.buildReverseGraph(getSampleGraph()));
    }

    @Test
    public void testCheckOnlyOneEnd() throws Exception {
        assertFalse("Couldn't identify end-nodes correctly", SoundnessValidator.checkOnlyOneEnd(nodes, getSampleGraph()));
        assertTrue("Couldn't identify end-nodes correctly", SoundnessValidator.checkOnlyOneEnd(nodes, getReverseGraph()));
    }

    @Test
    public void testGetReachableNodes() throws Exception {
        Set<String> expected = nodes;
        expected.remove("C"); // C is not connected and thus not reachable
        assertEquals("Reachable nodes weren't identified correctly",
                expected, SoundnessValidator.getReachableNodes("A", getSampleGraph()));
    }

}