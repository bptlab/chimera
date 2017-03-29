package de.hpi.bpt.chimera.jcomparser.saving;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.jcomparser.jaxb.AbstractDataControlNode;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 *
 */
public class FragmentTest {
    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testGetAllActivities() throws JAXBException, IOException {
        File file = new File("src/test/resources/fragments/FragmentTest.xml");
        String fragmentXml = FileUtils.readFileToString(file);
        Fragment fragment = new Fragment(fragmentXml, 1, "aTestFragment");
        List<AbstractDataControlNode> activities = fragment.getAllActivities();
        assertEquals(2, activities.size());
        List<String> activityNames = activities.stream()
                .map(AbstractDataControlNode::getName)
                .collect(Collectors.toList());
        assertTrue(activityNames.contains("aNormalTask"));
        assertTrue(activityNames.contains("aWebServiceTask"));
        // ReceiveTasks are events
    }
}