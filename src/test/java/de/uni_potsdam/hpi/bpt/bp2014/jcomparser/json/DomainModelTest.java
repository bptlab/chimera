package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataClass;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataObject;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class DomainModelTest {

    @After
    public void teardown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testSave() throws IOException {
        String path = "src/test/resources/exampleDomainModel.json";
        assertEquals(0, getNumberOfDataclasses());
        String domainModelJson = FileUtils.readFileToString(new File(path));
        DomainModel model = new DomainModel(domainModelJson);
        List<DataClass> dataclasses = model.getDataClasses();
        model.save();
        assertEquals(2, getNumberOfDataclasses());
    }

    private int getNumberOfDataclasses() {
        String sql = "SELECT COUNT(*) as num_dataclasses from dataclass;";
        return new DbObject().executeStatementReturnsInt(sql, "num_dataclasses");
    }
}