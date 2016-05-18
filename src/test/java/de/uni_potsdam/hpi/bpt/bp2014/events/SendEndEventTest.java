package de.uni_potsdam.hpi.bpt.bp2014.events;

import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class SendEndEventTest {
    private static final String URL_PROP = "unicorn.url";
    private static final String PATH_PROP = "unicorn.path.deploy";
    private static final String URL_OLD = PropertyLoader.getProperty(URL_PROP);
    private static final String PATH_OLD = PropertyLoader.getProperty(PATH_PROP);
    private static final String URL_NEW = "http://localhost:8080";
    private static final String PATH_NEW = "Unicorn";

    @BeforeClass
    public void setup() {
        PropertyLoader.setProperty(URL_PROP, URL_NEW);
        PropertyLoader.setProperty(PATH_PROP, PATH_NEW);
    }

    @AfterClass
    public void restoreProperties() {
        PropertyLoader.setProperty(URL_PROP, URL_OLD);
        PropertyLoader.setProperty(PATH_PROP, PATH_OLD);
    }


    @Test
    public void testSendEndEventParsing() {

    }

    @Test
    public void testSendEndEventSaving() {

    }

    @Test
    public void testSendEndEventExecution() {

    }
}
