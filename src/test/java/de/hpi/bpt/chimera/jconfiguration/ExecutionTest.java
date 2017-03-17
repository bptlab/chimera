package de.hpi.bpt.chimera.jconfiguration;

import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
//import com.ibatis.common.jdbc.ScriptRunner;
import de.hpi.bpt.chimera.util.ScriptRunner;
import de.hpi.bpt.chimera.database.ConnectionWrapper;
import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.Scenario;
import de.hpi.bpt.chimera.jcore.ScenarioFactory;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;


public class ExecutionTest {
    //private static final String DEVELOPMENT_SQL_SEED_FILE = "src/test/resources/JEngineV2_AcceptanceTests.sql";
    @Test
    public void testDeletion() throws IOException, SQLException {
        String insertScenarios = "INSERT INTO `scenario` (`id`, `name`, `deleted`, `modelversion`, `datamodelversion`) VALUES " +
                "(4, 'Testszenario', 0, 0, 0), " +
                "(5, 'Testszenario', 1, 0, 0), " +
                "(6, 'Testszenario', 0, 0, 0);";
        String insertScenarioInstances ="INSERT INTO `scenarioinstance` (`id`, `terminated`, `scenario_id`) VALUES " +
                "(1, 1, 4), " +
                "(2, 0, 4), " +
                "(3, 1, 5), " +
                "(4, 1, 6), " +
                "(5, 1, 6);";
        ScriptRunner runner = new ScriptRunner(ConnectionWrapper.getInstance().connect(), false, false);
        runner.runScript(new StringReader(insertScenarios));
        runner.runScript(new StringReader(insertScenarioInstances));
        
        Scenario sc4 = ScenarioFactory.createScenarioFromDatabase(4);
        Scenario sc5 = ScenarioFactory.createScenarioFromDatabase(5);
        Scenario sc6 = ScenarioFactory.createScenarioFromDatabase(6);
        sc4.delete();
        sc5.delete();
        sc6.delete();
        DbObject dbObject = new DbObject();
        String select = "SELECT deleted FROM scenario WHERE id = 4";
        List<Integer> deleted = dbObject.executeStatementReturnsListInt(select, "deleted");
        Assert.assertEquals("Scenario not deleted", 1, deleted.get(0).intValue());
        select = "SELECT deleted FROM scenario WHERE id = 5";
        deleted = dbObject.executeStatementReturnsListInt(select, "deleted");
        Assert.assertEquals("Scenario not deleted", 1, deleted.get(0).intValue());
        select = "SELECT deleted FROM scenario WHERE id = 6";
        deleted = dbObject.executeStatementReturnsListInt(select, "deleted");
        Assert.assertEquals("Scenario not deleted", 1, deleted.get(0).intValue());
    }

}
