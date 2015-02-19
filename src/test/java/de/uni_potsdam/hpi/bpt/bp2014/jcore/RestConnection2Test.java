package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import javax.ws.rs.core.Application;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class RestConnection2Test extends JerseyTest {

    /* #############################################################################
     *
     * TEST SETUP
     *
     * #############################################################################
     */
    
    @Before
    public void setUpDatabase() {
        DbObject dbObject = new DbObject();
        dbObject.executeUpdateStatement("DROP DATABASE JEngineV2");
        dbObject.executeUpdateStatement("CREATE DATABASE JEngineV2");
        Document sqlFile = getDocumentFromSQLFile(new File("src/main/resources/JEngineV2.sql"));

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
    public void testGetAllScenarios(){
        final Response test = target("/interface/v1/en/scenario/0/").request().get();
        assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));
    }

    @Test
    public void testGetScenarioInstances(){
        final Response test = target("/interface/v1/en/scenario/1/instance/0/").request().get();
        assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));
    }

    @Test
    public void testGetAllActivitiesForScenarioInstance(){
        final Response test = target("/interface/v1/en/scenario/0/").request().get();
        assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));
    }

    @Test
    public void testGetAllDataobjectForScenarioInstance(){
        final Response test = target("/interface/v1/en/scenario/0/").request().get();
        assertEquals("{\"ids\":[1,2,3,100,101,103,105,111,113,114,115,116,117,118,134]}", test.readEntity(String.class));
    }

    /* #############################################################################
     *
     * HTTP POST REQUEST
     *
     * #############################################################################
     */

    @Test
    public void testPostActivityStatusUpdate(){
        //final Response test = target("/interface/v1/en/scenario/1/instance/0/activityinstance/319/?status=enabled").request().post();
        //assertEquals("true", test.readEntity(String.class));
    }

    @Test
    public void testPostNewInstanceForScenario(){
        //final Response test = target("/interface/v1/en/scenario/1/").request().post();
        //assertEquals("123", test.readEntity(String.class));
    }

    private Document getDocumentFromSQLFile(final File sql) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sql);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
