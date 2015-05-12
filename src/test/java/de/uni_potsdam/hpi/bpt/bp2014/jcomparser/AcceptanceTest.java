package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import org.junit.Test;

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