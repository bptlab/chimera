package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;


import org.junit.Test;

/**
 * http://www.jayway.com/2010/12/27/rest-assured-or-how-to-easily-test-rest-services-in-java/
 */
public class AcceptanceTest {
    String Server_URL = "http://172.16.64.113:8080/";

    public void setUpAndNode() {

    }

    @Test
    public void testSomething() {
        //pull xml from processeditor
        //given().auth().basic("username", "password").expect().statusCode(200).when().get("/secured/hello");

        //check if xml is like expected
        //expect().body(hasXPath("/greeting/firstName[text()='John']")).with().parameters("firstName", "John", "lastName", "Doe").post("/greetXML");

        //TODO: call jcomparser execution
        //check if database contains correct entries
    }

}