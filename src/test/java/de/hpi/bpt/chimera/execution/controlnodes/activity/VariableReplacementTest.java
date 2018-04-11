package de.hpi.bpt.chimera.execution.controlnodes.activity;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.controlnodes.AbstractDataControlNodeInstance;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;

public class VariableReplacementTest {
	private final String filepath = "src/test/resources/execution/VariableReplacementTest";
	private CaseModel cm;
	private CaseExecutioner ex;
	private DataManager dm;
	private Method method;
	private AbstractDataControlNodeInstance taskInstance;

	@Before
	public void setup() throws Exception {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		ex = new CaseExecutioner(cm, cm.getName());
		dm = ex.getDataManager();
		ex.startCase();
		List<DataObject> selectedDataObjects = new ArrayList<>();
		AtomicDataStateCondition DC2 = CaseModelTestHelper.createDataStateCondition(cm, "D4t4Class2", "final");
		DataObject DO2 = dm.createDataObject(DC2);
		List<DataAttributeInstance> DAI2 = DO2.getDataAttributeInstances();
		for (DataAttributeInstance dai : DAI2) {
			if ("country".equals(dai.getDataAttribute().getName())) {
				dai.setValue("DE");
			}
		}
		selectedDataObjects.add(DO2);
		
		AtomicDataStateCondition DC1 = CaseModelTestHelper.createDataStateCondition(cm, "Person", "init");
		DataObject DO1 = dm.createDataObject(DC1);
		List<DataAttributeInstance> DAI1 = DO1.getDataAttributeInstances();
		for (DataAttributeInstance dai : DAI1) {
			String attributeName = dai.getDataAttribute().getName();
			switch (attributeName) {
			case "name":
				dai.setValue("Chimera");
				break;
			case "age":
				dai.setValue(3);
				break;
			case "height":
				dai.setValue(0.323d);
				break;
			case "married":
				dai.setValue(true);
				break;
			}
		}
		selectedDataObjects.add(DO1);
		
		taskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(ex, "call web service");
		taskInstance.setSelectedDataObjects(selectedDataObjects);
		method = AbstractDataControlNodeInstance.class.getDeclaredMethod("replaceVariableExpressions", String.class);
		method.setAccessible(true);
	}

	@Test
	public void testNoVariableExpressionContained() throws Exception {
		String ts = "I do not contain a variable expression";
		assertEquals("Strings without variable expression should not be left unchanged", ts, method.invoke(taskInstance, ts));
	}
	
	@Test
	public void testNoSuchDataObject() throws Exception {
		String ts = "#ThereIsNoSuchDataClass";
		String tsResult = "<not found>";
		assertEquals("", tsResult, method.invoke(taskInstance, ts));
	}
	
	@Test
	public void testNoSuchAttribute() throws Exception {
		List<DataObject> selectedDataObjects = new ArrayList<>();
		AtomicDataStateCondition DC1 = CaseModelTestHelper.createDataStateCondition(cm, "Person", "init");
		DataObject DO1 = dm.createDataObject(DC1);
		selectedDataObjects.add(DO1);
		taskInstance.setSelectedDataObjects(selectedDataObjects);
		String ts = "#Person.thereIsNoSuchAttribute";
		String tsResult = "<not found>";
		assertEquals("", tsResult, method.invoke(taskInstance, ts));
	}
	
	@Test
	public void testAttributeIsNull() throws Exception {
		List<DataObject> selectedDataObjects = new ArrayList<>();
		AtomicDataStateCondition DC1 = CaseModelTestHelper.createDataStateCondition(cm, "Person", "init");
		DataObject DO1 = dm.createDataObject(DC1);
		selectedDataObjects.add(DO1);
		taskInstance.setSelectedDataObjects(selectedDataObjects);
		String ts = "#Person.thisIsNull";
		String tsResult = "<value is 'null'>";
		assertEquals("", tsResult, method.invoke(taskInstance, ts));
	}
	
	@Test
	public void testVariableReplacement() throws Exception {
		String ts = "https://reisewarnung.net/api/country=#D4t4Class2.country";
		String tsResult = "https://reisewarnung.net/api/country=DE";
		assertEquals("", tsResult, method.invoke(taskInstance, ts));
	}
	
	@Test
	public void testMultipleExpressions() throws Exception {
		String ts = "Hello my name is #Person.name, I am #Person.age years old and #Person.height cm high.";
		String tsResult = "Hello my name is Chimera, I am 3 years old and 0.323 cm high.";
		assertEquals("", tsResult, method.invoke(taskInstance, ts));
	}
}
