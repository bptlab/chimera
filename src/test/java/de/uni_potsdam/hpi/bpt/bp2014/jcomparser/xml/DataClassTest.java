package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import com.ibatis.common.jdbc.ScriptRunner;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractTest;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.ws.rs.client.WebTarget;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;


public class DataClassTest extends AbstractTest {
    private Document document = new DocumentImpl(null);
    private Element rootDataClass;
    private Element normalDataClass;
    private static final String DEVELOPMENT_SQL_SEED_FILE = "src/main/resources/JEngineV2_schema.sql";

    /**
     * Sets up the seed file for the test database.
     */
    static {
        TEST_SQL_SEED_FILE = "src/test/resources/JEngineV2_AcceptanceTests.sql";
    }
    /**
     * The base url of the jcore rest interface.
     * Allows us to send requests to the {@link de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface}.
     */
    private WebTarget base;

    @AfterClass
    public static void resetDatabase() throws IOException, SQLException {
        clearDatabase();
        ScriptRunner runner = new ScriptRunner(Connection.getInstance().connect(), false, false);
        runner.runScript(new FileReader(DEVELOPMENT_SQL_SEED_FILE));
    }

    @Before
    public void setupRootDataClass(){
        rootDataClass = document.createElement("node");
        rootDataClass.appendChild(createProperty("text", "Reise"));
        rootDataClass.appendChild(createProperty("#attributes", "{0}+Beginn ;{1}+Ende ;{2}+Gesamtkosten ;"));
        rootDataClass.appendChild(createProperty("#id", "801101005"));
        rootDataClass.appendChild(createProperty("#type", "net.frapu.code.visualization.domainModel.DomainClass"));
        rootDataClass.appendChild(createProperty("stereotype", "root_instance"));
    }

    @Before
    public void setupNormalDataClass(){
        normalDataClass = document.createElement("node");
        normalDataClass.appendChild(createProperty("text", "Flug"));
        normalDataClass.appendChild(createProperty("#attributes", "{0}+Abflugsdatum ;{1}+Ankunftsdatum ;{2}+StartFlughafen ;{3}+EndFlughafen ;"));
        normalDataClass.appendChild(createProperty("#id", "679826034"));
        normalDataClass.appendChild(createProperty("#type", "net.frapu.code.visualization.domainModel.DomainClass"));
        normalDataClass.appendChild(createProperty("stereotype", ""));
    }

    private Element createProperty(String name, String value) {
        if (null == document) {
            return null;
        }
        Element property = document.createElement("property");
        property.setAttribute("name", name);
        property.setAttribute("value", value);
        return property;
    }

    @Test
    public void testRootDataClass(){
        DataClass dClass = new DataClass();
        dClass.initializeInstanceFromXML(rootDataClass);
        Assert.assertTrue(dClass.isRootNode());
        Assert.assertEquals("ID has not been set correctly", 801101005L, dClass.getDataClassModelID());
        Assert.assertEquals("Name has not been set correctly", "Reise", dClass.getDataClassName());
        Assert.assertEquals("Attributes have not been set correctly", 3, dClass.getDataAttributes().size());
        String[] attribute = {"Beginn","Ende","Gesamtkosten"};
        for(int i = 0; i < dClass.getDataAttributes().size(); i++){
            Assert.assertEquals("Attribute" + i + "has not been set correctly", attribute[i], dClass.getDataAttributes().get(i).getDataAttributeName());
        }
    }

    @Test
    public void testNormalDataClass(){
        DataClass dClass = new DataClass();
        dClass.initializeInstanceFromXML(normalDataClass);
        Assert.assertFalse(dClass.isRootNode());
        Assert.assertEquals("ID has not been set correctly", 679826034L, dClass.getDataClassModelID());
        Assert.assertEquals("Name has not been set correctly", "Flug", dClass.getDataClassName());
        Assert.assertEquals("Attributes have not been set correctly", 4, dClass.getDataAttributes().size());
        String[] attribute = {"Abflugsdatum","Ankunftsdatum","StartFlughafen", "EndFlughafen"};
        for(int i = 0; i < dClass.getDataAttributes().size(); i++){
            Assert.assertEquals("Attribute" + i + "has not been set correctly", attribute[i], dClass.getDataAttributes().get(i).getDataAttributeName());
        }
    }
}
