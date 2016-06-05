package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

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
    public void testSave() {

        Assert.fail();
    }

    @Test
    public void testGetAllActivities() {
        Assert.fail();
    }
}