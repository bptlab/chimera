package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;


import de.uni_potsdam.hpi.bpt.bp2014.config.Config;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

public class RestTest {
    String serverURL = de.uni_potsdam.hpi.bpt.bp2014.config.Config.jcomparserServerUrl;

    public void setUpAndNode() {

    }

    @Test
    public void testGetScenarios() {
       String url = serverURL + "jcomparser/scenarios/";
    }

    @Test
    public void testGetScenarioImage() {
        String scenarioID = "000000";
        String url = serverURL + "jcomparser/launch/" + scenarioID + "/image/";
    }

    @Test
    public void testPost() {
       String scenarioID = "000000";
       String url = serverURL + "jcomparser/launch/" + scenarioID + "/";
    }
}