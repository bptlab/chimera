package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.*;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

public class CaseModelParserTest {
	String jsonString = "";
	final String fileName = "JsonString";

	@Before
	public void getJsonString() {
		try {
			String file = getClass().getResource(fileName).getFile();
			FileInputStream inputStream = new FileInputStream(file);
			jsonString = IOUtils.toString(inputStream);
			inputStream.close();
		} catch (Exception e) {
			assertEquals("Error", 1, e);
		}
	}

	@Test
	public void parseCaseModel() {


		CaseModel cm;
		cm = CaseModelParser.parseCaseModel(jsonString);
		assertEquals("591330db1ed1325048306e40", cm.getId());
		cm.saveCaseModel();

		CaseModel cmb;
		cmb = CaseModelParser.parseCaseModel(jsonString);
		assertEquals("591330db1ed1325048306e40", cmb.getId());
		cmb.saveCaseModel();

		CaseModel cm2;
		cm2 =DomainModelPersistenceManager.loadCaseModel("591330db1ed1325048306e40");
		assertEquals("The id hasn't saved correctly.", cm.getId(), cm2.getId());
		assertEquals("The name hasn't saved correcty.", cm.getName(), cm2.getName());
		assertEquals("The DataModel VersionNumber hasn't saved correcty.", cm.getDataModel().getVersionNumber(), cm2.getDataModel().getVersionNumber());
		assertEquals("The First Fragments name hasn't saved correcty.", cm.getFragments().get(0).getName(), cm2.getFragments().get(0).getName());
		assertEquals("The First DataModelClass' name hasn't  saved correcty.", cm.getDataModel().getDataModelClasses().get(0).getName(), cm2.getDataModel().getDataModelClasses().get(0).getName());
		assertEquals("The OLC hasn't saved correctly", ((DataClass) (cm.getDataModel().getDataModelClasses().get(0))).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName(), ((DataClass) (cm2.getDataModel().getDataModelClasses()).get(0)).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName());
	}
}
