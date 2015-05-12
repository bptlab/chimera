package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.settings.Settings;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

public class RestTest extends AbstractTest {
    String serverURL = Settings.jcomparserServerUrl;

    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2.sql";
    static {
        String TEST_SQL_SEED_FILE = "src/main/resources/JEngineV2RESTTest.sql";
    }

    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface.class);

    }

    /* #############################################################################
     *
     * HTTP GET REQUEST
     *
     * #############################################################################
     */


    @Test
    public void testGetScenarios() {
        final Response test = target("jcomparser/scenarios/").request().get();
        //assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));
    }

    @Test
    public void testGetScenarioImage() {
        String scenarioID = "000000";
    	final Response test = target("/jcomparser/launch/" + scenarioID + "/image/").request().get();
        //assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));
    }

    /* #############################################################################
      *
      * HTTP POST REQUEST
      *
      * #############################################################################
      */
    
    @Test
    public void testPostLaunchImport() {
         String scenarioID = "000000";
         final Response test = target("/jcomparser/launch/" + scenarioID + "/").request().get();
        //assertEquals("{true}", test.readEntity(String.class));
    }
}
