package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


/**
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DomainModel.class})
public class DomainModelTest  {
    /**
     * This Method fetches the version from the PE-Server.
     */
    private static final String FETCH_VERSION_METHOD
            = "fetchVersionXML";
    /**
     * This is the domainModel used for testing.
     */
    private DomainModel domainModel;

    /**
     * This mocks the domainModel needed for the tests.
     */
    @Before
    public void setupDomainModel(){
        try {
            domainModel = PowerMock.createPartialMock(DomainModel.class,
                    FETCH_VERSION_METHOD);
            org.w3c.dom.Node domainModelNode = getDocumentFromXmlFile(new File("src/test/resources/Domain_Reise.xml"));
            PowerMock.expectPrivate(domainModel, FETCH_VERSION_METHOD).
                    andAnswer(new IAnswer<org.w3c.dom.Element>() {
                        @Override
                        public org.w3c.dom.Element answer() throws Throwable {
                            return getDocumentFromXmlFile(new File("src/test/resources/Version.xml")).getDocumentElement();
                        }
                    });
            PowerMock.replay(domainModel);
            domainModel.initializeInstanceFromXML(domainModelNode);
            PowerMock.verify(domainModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method checks if the deserialization sets the correct domainModelID.
     */
    @Test
    public void testSimpleDomainModelDeserialization(){
        Assert.assertEquals("The DomainModelID has not been set or is wrong", 269479299, domainModel.getDomainModelModelID());
    }

    /**
     * This checks if the dataClasses have been set.
     */
    @Test
    public void testDataClassesSetCorrectly(){
        Assert.assertNotNull("The first DataClass has not been set correctly", domainModel.getDataClasses().get(801101005L));
        Assert.assertNotNull("The second DataClass has not been set correctly", domainModel.getDataClasses().get(679826034L));
        Assert.assertNotNull("The third DataClass has not been set correctly", domainModel.getDataClasses().get(940990347L));
        Assert.assertNotNull("The fourth DataClass has not been set correctly", domainModel.getDataClasses().get(1175017344L));

    }

    /**
     * This method checks if the aggregations have been set.
     */
    @Test
    public void testAggregationSetCorrectly() {
        Assert.assertEquals("The aggregation has not been set correctly", 3, domainModel.getAggregations().size());
        Assert.assertNotNull("The sourceNode has not been set correctly", domainModel.getAggregations().get(0).getDataClasses().get(801101005L));
        for (int i = 0; i < domainModel.getAggregations().size(); i++) {
            Assert.assertEquals("The multiplicity has not been set correctly", Integer.MAX_VALUE, domainModel.getAggregations().get(i).getMultiplicity());
        }
    }

    /**
     * This method checks if the mocking of a domainModel was correct.
     */
    @Test
    public void testSetupDomainModel(){
        try {
            DomainModel testDomainModel = PowerMock.createPartialMock(DomainModel.class,
                    FETCH_VERSION_METHOD);
            org.w3c.dom.Node testDomainModelNode = getDocumentFromXmlFile(new File("src/test/resources/Domain_Reise.xml"));
            PowerMock.expectPrivate(testDomainModel, FETCH_VERSION_METHOD).
                    andAnswer(new IAnswer<org.w3c.dom.Element>() {
                        @Override
                        public org.w3c.dom.Element answer() throws Throwable {
                            return getDocumentFromXmlFile(new File("src/test/resources/Version.xml")).getDocumentElement();
                        }
                    });
            PowerMock.replay(testDomainModel);
            testDomainModel.initializeInstanceFromXML(testDomainModelNode);
            PowerMock.verify(testDomainModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method checks if the version has been set correctly.
     */
    @Test
    public void testVersionSetCorrectly(){
        Assert.assertEquals("The version has not been set correctly", 0, domainModel.getVersionNumber());
    }
    /**
     * Casts a XML from its String representation to a w3c Document.
     *
     * @param xml The String representation of the XML.
     * @return The document created from String.
     */
    private Document getDocumentFromXmlFile(final File xml) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
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
