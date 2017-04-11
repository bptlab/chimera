package de.hpi.bpt.chimera.jcore;

//import com.ibatis.common.jdbc.ScriptRunner;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.hpi.bpt.chimera.AbstractDatabaseDependentTest;
import de.hpi.bpt.chimera.ScenarioTestHelper;

import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;



/**
 * This class extends JerseyTest, because it's need the Rest Interface to check the Webservice Task functions.
 * The GET, PUT and POST Requests are send to the Rest Interface.
 */
/*
 * In addition the test checks whether an Webservice Task with multiple data
 * objects runs automatically. The Webservice Task should execute without the
 * users decision which data object shall be used by the Webservice Task.
 */
public class WebServiceAcceptanceTest  {

    String json;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @After
    public void resetDatabase() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Before
    public void setUp() throws IOException {
        String path ="src/test/resources/json/jsonPathExample.json";
        json = FileUtils.readFileToString(new File(path));
    }

    @Test
    public void testWebServiceIntegrationGet() throws IOException {
        stubFor(get(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)));

        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(
                "src/test/resources/Scenarios/JsonPathWebserviceScenarioGet.json");
        DataManager manager = instance.getDataManager();
        Collection<DataAttributeInstance> dataAttributes =
                manager.getDataAttributeInstances();
        assertDataAttributeInstancesEmpty(dataAttributes);
        // Terminating the manual task starts the automatic task
        ScenarioTestHelper.executeActivityByName("ManualTask", instance);
        dataAttributes = manager.getDataAttributeInstances();
        assertDataAttributeInstanceHasValue(dataAttributes);
    }

    @Test
    public void testWebServiceIntegrationPost() throws IOException {
        stubFor(post(urlEqualTo("/my/resource"))
                .withHeader("Accept", matching("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)));

        ScenarioInstance instance = ScenarioTestHelper.createScenarioInstance(
                "src/test/resources/Scenarios/JsonPathWebserviceScenarioPost.json");
        DataManager manager = instance.getDataManager();
        Collection<DataAttributeInstance> dataAttributes =
                manager.getDataAttributeInstances();
        assertDataAttributeInstancesEmpty(dataAttributes);
        ScenarioTestHelper.executeActivityByName("ManualTask", instance);
        dataAttributes = manager.getDataAttributeInstances();
        assertDataAttributeInstanceHasValue(dataAttributes);
    }

    private void assertDataAttributeInstancesEmpty(Collection<DataAttributeInstance> dataAttributes) {
        // Before executing the webservice task none of the data attributes has a value
        for (DataAttributeInstance dataAttribute : dataAttributes) {
            assertEquals("",  dataAttribute.getValue());
        }
    }

    private void assertDataAttributeInstanceHasValue(Collection<DataAttributeInstance> dataAttributes) {
		long amountOfDataAttributesWithValues = dataAttributes.stream().filter(x -> "foo".equals(x.getValue())).count();
        assertEquals(1L, amountOfDataAttributesWithValues);
    }
}
