package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.*;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.model.CaseModel;
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

		DomainModelPersistenceManager.saveCaseModel(cm);
		// CaseModel cm2;
		// cm2 =
		// DomainModelPersistenceManager.loadCaseModel("591330db1ed1325048306e40");
		// assertEquals("The id hasn't parsed and saved correctly.",
		// "591330db1ed1325048306e40", cm2.getId());
		// assertEquals(\"The name hasn't parsed and saved correcty.\",
		// \"CaseModelJPAtest\", cm2.getName());
		// assertEquals(\"The DataModel VersionNumber hasn't parsed and saved
		// correcty.\", 4, cm2.getDataModel().getVersionNumber());
		// assertEquals(\"The First Fragments name hasn't parsed and saved
		// correcty.\", \"First Fragment\",
		// cm2.getFragments().get(0).getName());
		// assertEquals(\"The First DataModelClass' name hasn't parsed and saved
		// correcty.\", \"DataClass1\",
		// cm2.getDataModel().getDataModelClasses().get(0).getName());
	}
}
