package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb.DataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Fragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ExecutionService;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.MockProvider;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

/**
 *
 */
public class ScenarioDataTest {
    private static final Logger LOGGER = Logger.getLogger(ScenarioDataTest.class);

    @After
    public void tearDown() throws IOException, SQLException {
        AbstractDatabaseDependentTest.resetDatabase();
    }

    @Test
    public void testSave() throws Exception {
        try {
            String path = "src/test/resources/Scenarios/IOSetScenario.json";
            String jsonString = FileUtils.readFileToString(new File(path));
            ScenarioData data = new ScenarioData(jsonString);
            int scenarioId = data.save();
            Assert.fail();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail();
        }
    }

    @Test
    public void testAssociateDataNodesWithDataClasses() {
        ScenarioData scenarioData = new ScenarioData();
        DomainModel domainModel = EasyMock.createNiceMock(DomainModel.class);
        scenarioData.associateDataNodesWithDataClasses(createMockFragments(), domainModel);
        Assert.fail();
    }

    @Test
    public void testAssociateStatesWithDataClasses() throws JAXBException {
        ScenarioData scenarioData = new ScenarioData();
        List<Fragment> fragments = createMockFragments();
        Map<String, DataClass> nameToDataclass = mockNameToDataClass();
        scenarioData.associateStatesWithDataClasses(
                fragments, nameToDataclass);
        nameToDataclass.values().forEach(EasyMock::verify);
    }

    private Map<String, DataClass> mockNameToDataClass() {
        Map<String, DataClass> nameToDataClass = new HashMap<>();
        DataClass customer = EasyMock.createNiceMock(DataClass.class);
        @SuppressWarnings("unchecked")
        List<String> customerStates = EasyMock.createNiceMock(ArrayList.class);
        expect(customerStates.add("received")).andReturn(true);
        expect(customerStates.add("reviewed")).andReturn(true);
        replay(customerStates);
        expect(customer.getStates()).andReturn(customerStates).anyTimes();
        replay(customer);
        nameToDataClass.put("Customer", customer);

        DataClass contract = EasyMock.createNiceMock(DataClass.class);
        @SuppressWarnings("unchecked")
        List<String> contractStates = EasyMock.createNiceMock(ArrayList.class);
        expect(contractStates.add("initial")).andReturn(true);
        replay(contractStates);
        expect(contract.getStates()).andReturn(contractStates);
        replay(contract);
        nameToDataClass.put("Contract", contract);
        return nameToDataClass;
    }

    private List<Fragment> createMockFragments() {
        List<DataNode> dataNodes = MockProvider.mockDataNodes(
                Arrays.asList("Customer", "Customer"), Arrays.asList("received", "reviewed"));
        List<DataNode> dataNodes2 = MockProvider.mockDataNodes(
                Collections.singletonList("Contract"), Collections.singletonList("initial"));
        Fragment fragment = EasyMock.createMock(Fragment.class);
        Fragment fragment2 = EasyMock.createMock(Fragment.class);
        expect(fragment.getDataNodes()).andReturn(dataNodes);
        expect(fragment2.getDataNodes()).andReturn(dataNodes2);
        replay(fragment);
        replay(fragment2);
        return Arrays.asList(fragment, fragment2);
    }


    @Test
    public void testGenerateFragmentList() {

        Assert.fail();
    }
}