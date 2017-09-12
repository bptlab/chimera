package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

public class CaseModelManagerTest {
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

		// Parse a CaseModel from jsonString and give it to the CaseModel
		// manager
		CaseModel cm1 = CaseModelManager.parseCaseModel(jsonString);
		assertEquals("591330db1ed1325048306e40", cm1.getId());

		// Parse another CaseModel from jsonString, but this time save it to
		// database directly
		CaseModel cm2;
		cm2 = CaseModelParser.parseCaseModel(jsonString);
		cm2.setId("591330db1ed1325048306e41");
		assertEquals("591330db1ed1325048306e41", cm2.getId());
		cm2.saveCaseModelToDB();

		// get back the first Model from the CaseModel manager
		CaseModel cmLoaded;
		cmLoaded = CaseModelManager.getCaseModel("591330db1ed1325048306e40");
		assertEquals("The id wasn't saved correctly.", cm1.getId(), cmLoaded.getId());
		assertEquals("The name wasn't saved correcty.", cm1.getName(), cmLoaded.getName());
		assertEquals("The DataModel VersionNumber wasn't saved correcty.", cm1.getDataModel().getVersionNumber(), cmLoaded.getDataModel().getVersionNumber());
		assertEquals("The First Fragments name wasn't saved correcty.", cm1.getFragments().get(0).getName(), cmLoaded.getFragments().get(0).getName());
		assertEquals("The First DataModelClass' name wasn't  saved correcty.", cm1.getDataModel().getDataModelClasses().get(0).getName(), cmLoaded.getDataModel().getDataModelClasses().get(0).getName());
		assertEquals("The OLC wasn't saved correctly", ((DataClass) (cm1.getDataModel().getDataModelClasses().get(0))).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName(), ((DataClass) (cmLoaded.getDataModel().getDataModelClasses()).get(0)).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName());

		// generate a second Version from the first CaseModel and give it to the
		// CaseModel manager
		CaseModel cm3;
		cm3 = CaseModelManager.parseCaseModel(jsonString);
		assertEquals("591330db1ed1325048306e40", cm1.getId());
		cm3.setName(cm1.getName() + "_Version2");

		// Load the first Model from the CaseModel manager again,
		// but this time we should get the newer Version (cm3)
		CaseModel cmLoaded2;
		cmLoaded2 = CaseModelManager.getCaseModel("591330db1ed1325048306e40");
		assertEquals("The id wasn't saved correctly.", cm3.getId(), cmLoaded2.getId());
		assertEquals("The name wasn't saved correcty.", cm3.getName(), cmLoaded2.getName());
		assertEquals("The DataModel VersionNumber wasn't saved correcty.", cm3.getDataModel().getVersionNumber(), cmLoaded2.getDataModel().getVersionNumber());
		assertEquals("The First Fragments name wasn't saved correcty.", cm3.getFragments().get(0).getName(), cmLoaded2.getFragments().get(0).getName());
		assertEquals("The First DataModelClass' name wasn't  saved correcty.", cm3.getDataModel().getDataModelClasses().get(0).getName(), cmLoaded2.getDataModel().getDataModelClasses().get(0).getName());
		assertEquals("The OLC wasn't saved correctly", ((DataClass) (cm3.getDataModel().getDataModelClasses().get(0))).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName(), ((DataClass) (cmLoaded2.getDataModel().getDataModelClasses()).get(0)).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName());

		// Use the CaseModel Manager to load a Model which isn't in RAM
		// the CaseModel manager now should load the CaseModel from the database
		CaseModel cmLoaded3;
		cmLoaded3 = CaseModelManager.getCaseModel("591330db1ed1325048306e41");
		assertEquals("The id wasn't saved correctly.", cm2.getId(), cmLoaded3.getId());
		assertEquals("The name wasn't saved correcty.", cm2.getName(), cmLoaded3.getName());
		assertEquals("The DataModel VersionNumber wasn't saved correcty.", cm2.getDataModel().getVersionNumber(), cmLoaded3.getDataModel().getVersionNumber());
		assertEquals("The First Fragments name wasn't saved correcty.", cm2.getFragments().get(0).getName(), cmLoaded3.getFragments().get(0).getName());
		assertEquals("The First DataModelClass' name wasn't  saved correcty.", cm2.getDataModel().getDataModelClasses().get(0).getName(), cmLoaded3.getDataModel().getDataModelClasses().get(0).getName());
		assertEquals("The OLC wasn't saved correctly", ((DataClass) (cm2.getDataModel().getDataModelClasses().get(0))).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName(), ((DataClass) (cmLoaded3.getDataModel().getDataModelClasses()).get(0)).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName());


		/*
		 * for (Map.Entry<String, String> entry :
		 * CaseModelManager.getAllCaseModelNameDetails("").entrySet()) {
		 * System.out.println("Name:" + entry.getValue() + "Id:" +
		 * entry.getKey() + "\n"); }
		 */
	}
}
