package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.validation;

import de.uni_potsdam.hpi.bpt.bp2014.FragmentTestHelper;
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

    /**
     * Create a graph for "src/test/resources/fragments/SmallFragment.xml"
     * @return the graph for the SmallFragment.
     */
    private Map<String, Set<String>> getComplexGraph() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("start", new HashSet<>(Collections.singletonList("startToAndSplit")));
        graph.put("startToAndSplit", new HashSet<>(Collections.singletonList("andSplit")));
        graph.put("andSplit", new HashSet<>(Arrays.asList("andSplitToA", "andSplitToXorSplit")));
        graph.put("andSplitToA", new HashSet<>(Collections.singletonList("A")));
        graph.put("A", new HashSet<>(Collections.singletonList("AToAndJoin")));
        graph.put("AToAndJoin", new HashSet<>(Collections.singletonList("andJoin")));
        graph.put("andSplitToXorSplit", new HashSet<>(Collections.singletonList("xorSplit")));
        graph.put("xorSplit", new HashSet<>(Arrays.asList("xorSplitToB", "xorSplitToC")));
        graph.put("xorSplitToB", new HashSet<>(Collections.singletonList("B")));
        graph.put("B", new HashSet<>(Collections.singletonList("BToXorJoin")));
        graph.put("BToXorJoin", new HashSet<>(Collections.singletonList("xorJoin")));
        graph.put("xorSplitToC", new HashSet<>(Collections.singletonList("C")));
        graph.put("C", new HashSet<>(Collections.singletonList("CToXorJoin")));
        graph.put("CToXorJoin", new HashSet<>(Collections.singletonList("xorJoin")));
        graph.put("xorJoin", new HashSet<>(Collections.singletonList("xorJoinToAndJoin")));
        graph.put("xorJoinToAndJoin", new HashSet<>(Collections.singletonList("andJoin")));
        graph.put("andJoin", new HashSet<>(Collections.singletonList("andJoinToEnd")));
        graph.put("andJoinToEnd", new HashSet<>(Collections.singletonList("end")));
        return graph;
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidateStructuralSoundness() {
        String path = "src/test/resources/fragments/FragmentNotSound.xml";
        try {
            Fragment fragment = FragmentTestHelper.createFragment(path);
            SoundnessValidator.validateStructuralSoundness(fragment);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Couldn't load the test fragment.");
        }
    }

    @Test
    public void testValidateStructuralSoundness2() throws Exception {
        String path = "src/test/resources/fragments/SoundFragment.xml";
        String path2 = "src/test/resources/fragments/SmallFragment.xml";
        try {
            Fragment fragment = FragmentTestHelper.createFragment(path);
            SoundnessValidator.validateStructuralSoundness(fragment);
            Fragment fragment2 = FragmentTestHelper.createFragment(path2);
            SoundnessValidator.validateStructuralSoundness(fragment2);
            assert true;
        } catch (IOException e) {
            e.printStackTrace();
            fail("Couldn't load the test fragment.");
        }
    }

    @Test
    public void testBuildGraphFromFragment() throws Exception {
        String path = "src/test/resources/fragments/SmallFragment.xml";
        try {
            Fragment fragment = FragmentTestHelper.createFragment(path);
            assertEquals("Couldn't build the graph correctly", getComplexGraph(), SoundnessValidator.buildGraphFromFragment(fragment));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Couldn't load the test fragment.");
        }
    }

    @Test
    public void testBuildReverseGraph() throws Exception {
        assertEquals("The reverse graph wasn't build correctly", getReverseGraph(), SoundnessValidator.buildReverseGraph(getSampleGraph()));
        assertEquals("The reverse graph wasn't build correctly", getSampleGraph(),
                SoundnessValidator.buildReverseGraph(SoundnessValidator.buildReverseGraph(getSampleGraph())));
    }

    @Test
    public void testCheckOnlyOneEnd() throws Exception {
        Set<String> connectedNodes = nodes;
        connectedNodes.remove("C"); // C is not connected and thus not part of the graph
        assertFalse("Couldn't identify end-nodes correctly", SoundnessValidator.checkOnlyOneEnd(connectedNodes, getSampleGraph()));
        assertTrue("Couldn't identify end-nodes correctly", SoundnessValidator.checkOnlyOneEnd(connectedNodes, getReverseGraph()));
    }

    @Test
    public void testGetLastNode() throws Exception {
        assertEquals("Couldn't identify last node correctly", "A", SoundnessValidator.getLastNode(nodes, getReverseGraph()));
    }

    @Test
    public void testGetReachableNodes() throws Exception {
        Set<String> expected = nodes;
        expected.remove("C"); // C is not connected and thus not reachable
        assertEquals("Reachable nodes weren't identified correctly",
                expected, SoundnessValidator.getReachableNodes("A", getSampleGraph()));
    }

}