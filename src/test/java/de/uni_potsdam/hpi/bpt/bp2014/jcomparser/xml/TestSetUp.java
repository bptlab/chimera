package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.ibatis.common.jdbc.ScriptRunner;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.easymock.IAnswer;
import org.junit.AfterClass;
import org.powermock.api.easymock.PowerMock;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;



/**
 * This class provides methods for both creating scenarios by mocking necessary components and for setting the database
 * to an appropriate state after executing the tests.
 */
public class TestSetUp {
    // We need the name of all methods which communicate to the server.
    /**
     * This Method fetches The Version from the PE-Server.
     */
    public static final String FETCH_VERSION_METHOD
            = "fetchVersionXML";
    /**
     * This creates and initializes a fragment.
     */
    public static final String CREATE_FRAGMENT_METHOD
            = "createAndInitializeFragment";
    /**
     * This checks the XML for the URI of the domainModel and sets it.
     * It is found in the ScenarioXML.
     */
    public static final String FETCH_DOMAIN_MODEL_METHOD =
            "fetchDomainModelXML";
    /**
     * This method creates the the domainModel from the given domainModelXML.
     */
    public static final String SET_DOMAIN_MODEL_METHOD =
            "createAndInitializeDomainModel";

    /**
     * The file which contains the sql-statement for emptying the database.
     */
    public static final String TRUNCATE_TABLES_FILE = "src/test/resources/truncate_all_tables.sql";
    /**
     * The sql-file for setting up the database.
     */
    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2_schema.sql";

    /**
     * Initialize a fragment by configuring the mock.
     * @param versionLocation Location of the XML-file that contains the versions of the fragment
     * @return A mocked Fragment
     * @throws Exception java.lang.Exception
     */
    public static Fragment initializeFragment(final String versionLocation) throws Exception {
        final Fragment fragment = PowerMock.createPartialMock(Fragment.class,
                FETCH_VERSION_METHOD);
        PowerMock.expectPrivate(fragment, FETCH_VERSION_METHOD)
                .andAnswer(new IAnswer<org.w3c.dom.Node>() {
                    @Override
                    public org.w3c.dom.Node answer() throws Throwable {
                        return getDocumentFromXmlFile(
                                new File(versionLocation))
                                .getDocumentElement();
                    }
                });
        PowerMock.replay(fragment);
        return fragment;
    }

    /**
     * Initialize a domainModel by configuring the mock.
     * @param versionLocation Location of the XML-file that contains the versions of the domainModel
     * @return A mockedDomainmodel
     * @throws Exception java.lang.Exception
     */
    public static DomainModel initializeDomainModel(final String versionLocation) throws Exception {
        final DomainModel domainModel = PowerMock.createPartialMock(DomainModel.class,
                FETCH_VERSION_METHOD);
        PowerMock.expectPrivate(domainModel, FETCH_VERSION_METHOD).
                andAnswer(new IAnswer<org.w3c.dom.Node>() {
                    @Override
                    public org.w3c.dom.Node answer() throws Throwable {
                        return getDocumentFromXmlFile(
                                new File(versionLocation))
                                .getDocumentElement();
                    }
                });
        PowerMock.replay(domainModel);
        return domainModel;
    }

    /**
     * Initialize a scenario by configuring the mock.
     * @param versionLocation Location of the XML-file that contains the versions of the fragment
     * @param fragments List of fragments the scenario consists of
     * @param domainModel The domainModel of the scenario
     * @return A mocked Scenario
     * @throws Exception java.lang.Exception
     */
    public static Scenario initializeCompleteScenario(final String versionLocation, final List<Fragment> fragments, final DomainModel domainModel) throws Exception {
        final Scenario scenario = PowerMock.createPartialMock(Scenario.class,
                FETCH_VERSION_METHOD,
                CREATE_FRAGMENT_METHOD,
                SET_DOMAIN_MODEL_METHOD,
                FETCH_DOMAIN_MODEL_METHOD);
        PowerMock.expectPrivate(scenario, FETCH_DOMAIN_MODEL_METHOD).andReturn(null);
        PowerMock.expectPrivate(scenario, SET_DOMAIN_MODEL_METHOD)
                .andAnswer(new IAnswer<DomainModel>() {
                    @Override
                    public DomainModel answer() throws Throwable {
                        return domainModel;
                    }
                });
        PowerMock.expectPrivate(scenario, FETCH_VERSION_METHOD)
                .andAnswer(new IAnswer<org.w3c.dom.Node>() {
                    @Override
                    public org.w3c.dom.Node answer() throws Throwable {
                        return getDocumentFromXmlFile(
                                new File(versionLocation))
                                .getDocumentElement();
                    }
                });
        for (final Fragment fragment : fragments) {
            PowerMock.expectPrivate(scenario, CREATE_FRAGMENT_METHOD, Long.toString(fragment.getFragmentID()))
                    .andAnswer(new IAnswer<Fragment>() {
                        @Override
                        public Fragment answer() throws Throwable {
                            return fragment;
                        }
                    });
            if (fragments.size() == 1) {
                PowerMock.replay(scenario, fragment, Fragment.class, domainModel);
            }
        }
        if (fragments.size() > 1) {
            PowerMock.replayAll(scenario, domainModel);
        }
        //PowerMock.replay(scenario, fragments, Fragment.class);
        return scenario;
    }
    /**
     * Casts a XML from its String Representation to a w3c Document.
     *
     * @param xml The String representation of the XML.
     * @return The from String created Document.
     */
    public static Document getDocumentFromXmlFile(final File xml) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Truncate all tables of the dataBase (default entry in table 'emailconfiguration' remains.
     * @throws Exception java.lang.Exception
     */
    public static void emptyDatabase() throws Exception {
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(TRUNCATE_TABLES_FILE));
    }
    /**
     * Refill database from DEVELOPMENT_SQL_SEED_FILE after clearing it.
     * @throws IOException java.io.Exception
     * @throws SQLException java.sql.Exception
     */
    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }
    /**
     * Drops and recreates the database.
     */
    private static void clearDatabase() {
        java.sql.Connection conn = Connection.getInstance().connect();
        Statement stmt = null;
        if (conn == null) {
            return;
        }
        try {
            //Execute a querystmt = conn.createStatement();
            stmt = conn.createStatement();
            stmt.execute("DROP DATABASE JEngineV2");
            stmt.execute("CREATE DATABASE JEngineV2");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
