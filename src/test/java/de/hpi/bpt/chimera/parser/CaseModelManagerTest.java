package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

public class CaseModelManagerTest {
	final String filepath = "src/test/resources/parser/JsonString";
	String JsonString;

	@Before
	public void setup() {
		JsonString = CaseModelTestHelper.getJsonString(filepath);
	}


	@Test
	public void parseCaseModel() {
		// First of all we write some bad code for parsing and saving, a
		// CaseModel without using the CaseModelManager. So we get a CaseModel
		// into the Database which is not cached by the CaseModelManager.
		// You should not use this methods for parsing and saving a CaseModel,
		// use CaseModelManager.parseCaseModel(JsonString) instead
		CaseModel cm2;
		cm2 = CaseModelParser.parseCaseModel(JsonString);
		cm2.saveCaseModelToDB();

		// Use the CaseModelManager to load a Model which isn't in RAM
		// the CaseModel manager now should get initialized when its first
		// Operation is called. During initialization alle the the CaseModels
		// from the database shoud be loaded.
		CaseModel cmLoaded3;
		cmLoaded3 = CaseModelManager.getCaseModel(cm2.getId());
		// Test whether the CaseModel saved earlier was loaded correctly.
		assertEquals("The id wasn't saved correctly.", cm2.getId(), cmLoaded3.getId());
		assertEquals("The name wasn't saved correcty.", cm2.getName(), cmLoaded3.getName());
		assertEquals("The DataModel VersionNumber wasn't saved correcty.", cm2.getDataModel().getVersionNumber(), cmLoaded3.getDataModel().getVersionNumber());
		assertEquals("The First Fragments name wasn't saved correcty.", cm2.getFragments().get(0).getName(), cmLoaded3.getFragments().get(0).getName());
		assertEquals("The First DataModelClass' name wasn't  saved correcty.", cm2.getDataModel().getDataModelClasses().get(0).getName(), cmLoaded3.getDataModel().getDataModelClasses().get(0).getName());
		assertEquals("The OLC wasn't saved correctly", ((DataClass) (cm2.getDataModel().getDataModelClasses().get(0))).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName(), ((DataClass) (cmLoaded3.getDataModel().getDataModelClasses()).get(0)).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName());


		// Now also test the other features of the CaseModelManager

		// Parse a CaseModel from a JsonString and give it to the CaseModel
		// manager
		CaseModel cm1 = CaseModelManager.parseCaseModel(JsonString);

		// Load a CaseModel which should be hold by the CaseModelManager
		CaseModel cmLoaded;
		cmLoaded = CaseModelManager.getCaseModel(cm1.getId());
		assertEquals("The id wasn't saved correctly.", cm1.getId(), cmLoaded.getId());
		assertEquals("The name wasn't saved correcty.", cm1.getName(), cmLoaded.getName());
		assertEquals("The DataModel VersionNumber wasn't saved correcty.", cm1.getDataModel().getVersionNumber(), cmLoaded.getDataModel().getVersionNumber());
		assertEquals("The First Fragments name wasn't saved correcty.", cm1.getFragments().get(0).getName(), cmLoaded.getFragments().get(0).getName());
		assertEquals("The First DataModelClass' name wasn't  saved correcty.", cm1.getDataModel().getDataModelClasses().get(0).getName(), cmLoaded.getDataModel().getDataModelClasses().get(0).getName());
		assertEquals("The OLC wasn't saved correctly", ((DataClass) (cm1.getDataModel().getDataModelClasses().get(0))).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName(), ((DataClass) (cmLoaded.getDataModel().getDataModelClasses()).get(0)).getObjectLifecycle().getObjectLifecycleStates().get(0).getSuccessors().get(0).getName());
	}
}
