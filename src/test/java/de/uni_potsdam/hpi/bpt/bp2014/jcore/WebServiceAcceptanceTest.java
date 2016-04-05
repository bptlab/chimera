package de.uni_potsdam.hpi.bpt.bp2014.jcore;

//import com.ibatis.common.jdbc.ScriptRunner;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.ScenarioTestHelper;
import de.uni_potsdam.hpi.bpt.bp2014.ScriptRunner;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataAttribute;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Application;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;



/**
 * This class extends JerseyTest, because it's need the Rest Interface to check the Webservice Task functions.
 * The GET, PUT and POST Requests are send to the Rest Interface.
 */
public class WebServiceAcceptanceTest  {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);


    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    /**
     * T
     * @throws IOException
     */
    @Test
    public void testWebserviceIntegration() throws IOException {
        String path = "src/test/resources/json/jsonPathExample.json";
        String json = FileUtils.readFileToString(new File(path));

        stubFor(get(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)));


        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(
                "src/test/resources/Scenarios/JsonPathWebserviceScenario.json");
        Collection<DataAttributeInstance> dataAttributes =
                instance.getDataAttributeInstances().values();

        // Before executing the webservice task none of the data attributes has a value
        for (DataAttributeInstance dataAttribute : dataAttributes) {
            assertEquals("",  dataAttribute.getValue());
        }

        // Terminating the manual task starts the automatic task
        ScenarioTestHelper.terminateActivityInstanceByName("ManualTask", instance);
        long setDataattributes = dataAttributes.stream().filter(x -> "foo".equals(x.getValue())).count();
        assertEquals(1L, setDataattributes);
    }
}
