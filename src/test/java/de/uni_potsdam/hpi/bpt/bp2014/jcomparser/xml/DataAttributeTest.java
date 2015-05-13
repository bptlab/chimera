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

/**
 *
 */
public class DataAttributeTest extends AbstractTest {
    private Document document = new DocumentImpl(null);
    private Element dataClass;

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
    public void setupDataClass(){
        dataClass = document.createElement("node");
        dataClass.appendChild(createProperty("text", "Reise"));
        dataClass.appendChild(createProperty("#attributes", "{0}+Beginn ;{1}+Ende ;{2}+Gesamtkosten ;"));
        dataClass.appendChild(createProperty("#id", "801101005"));
        dataClass.appendChild(createProperty("#type", "net.frapu.code.visualization.domainModel.DomainClass"));
        dataClass.appendChild(createProperty("stereotype", "root_instance"));
    }

    /**
     *
     * @param name
     * @param value
     * @return
     */
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
    public void testDataAttributeSetMethod(){
        DataClass dClass = new DataClass();
        dClass.initializeInstanceFromXML(dataClass);
        for(DataAttribute attr : dClass.getDataAttributes()){
            attr.setDataClassID(42);
        }
        for(int i = 0; i < dClass.getDataAttributes().size(); i++){
            Assert.assertEquals("DataClassID has not been set correctly", 42, dClass.getDataAttributes().get(i).getDataClassID());
        }
    }

    @Test
    public void testDataAttribute(){
        DataAttribute dataAttribute = new DataAttribute("state");
        Assert.assertEquals("AttributeName has been set correctly", "state", dataAttribute.getDataAttributeName());
        Assert.assertEquals("AttributeType has not been set correctly", "", dataAttribute.getDataAttributeType());
    }
}
