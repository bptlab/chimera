package de.hpi.bpt.chimera.jcomparser.json;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.database.DbObject;

/**
 *
 */
public class DomainModelTest extends AbstractDatabaseDependentTest {

    @Test
    public void testSave() throws IOException {
        String path = "src/test/resources/exampleDomainModel.json";
        assertEquals(0, getNumberOfDataclasses());
        String domainModelJson = FileUtils.readFileToString(new File(path));
        DomainModel model = new DomainModel(domainModelJson);
        model.save();
        assertEquals(2, getNumberOfDataclasses());
    }

    private int getNumberOfDataclasses() {
        String sql = "SELECT COUNT(*) as num_dataclasses from dataclass;";
        return new DbObject().executeStatementReturnsInt(sql, "num_dataclasses");
    }
}