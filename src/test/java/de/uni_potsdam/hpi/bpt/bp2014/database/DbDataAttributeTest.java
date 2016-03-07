package de.uni_potsdam.hpi.bpt.bp2014.database;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import de.uni_potsdam.hpi.bpt.bp2014.AbstractDatabaseDependentTest;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataAttribute;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.DataClass;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 * Created by Maarten on 09.02.2016.
 */
public class DbDataAttributeTest extends AbstractDatabaseDependentTest {
	private Document document = new DocumentImpl(null);
	private String dataClass;

	@Before
	public void setupDataClass(){
		dataClass = new JSONObject()
				.put("name", "Reise")
				.put("_id", "801101005")
				.put("attributes", new JSONArray()
								.put(new JSONObject()
										.put("name", "Beginn")
										.put("datatype", "String"))
								.put(new JSONObject()
										.put("name", "Ende")
										.put("datatype", "String"))
								.put(new JSONObject()
										.put("name", "Gesamtkosten")
										.put("datatype", "Float"))
				).toString();
	}

	@Test
	public void testAttributeSaving() {
		DataClass dClass = new DataClass();
		dClass.initializeInstanceFromJson(dataClass);
		dClass.save();
		DataAttribute attribute = dClass.getDataAttributes().get(0);
		DbDataAttributeInstance instance  = new DbDataAttributeInstance();
		String dbType = instance.getType(attribute.getDataAttributeID());
		String dbName = instance.getName(attribute.getDataAttributeID());
		assertEquals("The attributeName has not been saved correctly", "Beginn", dbName);
		assertEquals("The attributeType has not been saved correctly", "String", dbType);

	}
}
