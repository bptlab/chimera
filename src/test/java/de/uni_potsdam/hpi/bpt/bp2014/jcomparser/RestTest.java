package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.config.Config;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Node;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.ws.rs.core.Application;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;


public class RestTest extends AbstractTest {
    String serverURL = de.uni_potsdam.hpi.bpt.bp2014.config.Config.jcomparserServerUrl;

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2.sql";
    static {
        TEST_SQL_SEED_FILE = "src/main/resources/JEngineV2RESTTest.sql";
    }

    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(TEST_SQL_SEED_FILE));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jcore.RestConnection.class);

    }

    /* #############################################################################
     *
     * HTTP GET REQUEST
     *
     * #############################################################################
     */


    @Test
    public void testGetScenarios() {
       String url = serverURL + "jcomparser/scenarios/";
    }

    @Test
    public void testGetScenarioImage() {
       // String scenarioID = "000000";
       // String url = serverURL + "jcomparser/launch/" + scenarioID + "/image/";
    }

    /* #############################################################################
      *
      * HTTP POST REQUEST
      *
      * #############################################################################
      */
    
    @Test
    public void testPost() {
      // String scenarioID = "000000";
       //String url = serverURL + "jcomparser/launch/" + scenarioID + "/";
    }
}