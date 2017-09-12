package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.*;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

public class CaseModelSavingTest {
	String jsonString = "";
	final String fileName = "JsonString";

	@Before
	public void getJsonString() {
		try {
			// String file = getClass().getResource(fileName).getFile();
			String file = "src/test/resources/parser/JsonStringCaseModelWithDataNodes";
			FileInputStream inputStream = new FileInputStream(file);
			jsonString = IOUtils.toString(inputStream);
			inputStream.close();
		} catch (Exception e) {
			assertEquals("Error", 1, e);
		}
	}

	@Test
	public void parseCaseModel() {


		CaseModel cm1;
		cm1 = CaseModelParser.parseCaseModel(jsonString);
		assertEquals("59b51bda8eea331ea4b0440b", cm1.getId());
		cm1.saveCaseModelToDB();

		CaseModel cm2;
		cm2 = CaseModelParser.parseCaseModel(jsonString);
		cm2.setId("59b51bda8eea331ea4b0440c");
		assertEquals("59b51bda8eea331ea4b0440c", cm2.getId());
		cm2.saveCaseModelToDB();

		CaseModel cmLoaded;
		cmLoaded = DomainModelPersistenceManager.loadCaseModel("59b51bda8eea331ea4b0440b");
		assertEquals("The id wasn't saved correctly.", cm1.getId(), cmLoaded.getId());
		assertEquals("The name wasn't saved correcty.", cm1.getName(), cmLoaded.getName());
		assertEquals("The DataModel VersionNumber wasn't saved correcty.", cm1.getDataModel().getVersionNumber(), cmLoaded.getDataModel().getVersionNumber());
		assertEquals("The First Fragments name wasn't saved correcty.", cm1.getFragments().get(0).getName(), cmLoaded.getFragments().get(0).getName());
		assertEquals("The First DataModelClass' name wasn't saved correcty.", cm1.getDataModel().getDataModelClasses().get(0).getName(), cmLoaded.getDataModel().getDataModelClasses().get(0).getName());
		assertEquals("The OLC wasn't saved correctly", ((DataClass) (cm1.getDataModel().getDataModelClasses().get(0))).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName(), ((DataClass) (cmLoaded.getDataModel().getDataModelClasses()).get(0)).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName());


		CaseModel cm3;
		cm3 = CaseModelParser.parseCaseModel(jsonString);
		assertEquals("59b51bda8eea331ea4b0440b", cm1.getId());
		cm3.setName(cm1.getName() + "_Version2");
		cm3.saveCaseModelToDB();

		CaseModel cmLoaded2;
		cmLoaded2 = DomainModelPersistenceManager.loadCaseModel("59b51bda8eea331ea4b0440b");
		assertEquals("The id wasn't saved correctly.", cm3.getId(), cmLoaded2.getId());
		assertEquals("The name wasn't saved correcty.", cm3.getName(), cmLoaded2.getName());
		assertEquals("The DataModel VersionNumber wasn't saved correcty.", cm3.getDataModel().getVersionNumber(), cmLoaded2.getDataModel().getVersionNumber());
		assertEquals("The First Fragments name wasn't saved correcty.", cm3.getFragments().get(0).getName(), cmLoaded2.getFragments().get(0).getName());
		assertEquals("The First DataModelClass' name wasn't  saved correcty.", cm3.getDataModel().getDataModelClasses().get(0).getName(), cmLoaded2.getDataModel().getDataModelClasses().get(0).getName());
		assertEquals("The OLC wasn't saved correctly", ((DataClass) (cm3.getDataModel().getDataModelClasses().get(0))).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName(), ((DataClass) (cmLoaded2.getDataModel().getDataModelClasses()).get(0)).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName());
	}
}
