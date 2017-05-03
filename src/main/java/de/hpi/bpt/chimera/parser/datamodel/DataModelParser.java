package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.Class;
import de.hpi.bpt.chimera.model.datamodel.DataModel;

public class DataModelParser {

	private DataModelParser() {
	}

	public static DataModel parseDataModel(final JSONObject datamodelJson) {
		DataModel dataModel = new DataModel();

		int versionNumber = datamodelJson.getInt("revision");
		dataModel.setVersionNumber(versionNumber);

		List<Class> classes = getClasses(datamodelJson.getJSONArray("dataclasses"));
		dataModel.setClasses(classes);

		return dataModel;
	}

	private static List<Class> getClasses(final JSONArray classJsonArray) {
		int arraySize = classJsonArray.length();
		List<Class> classes = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject classJson = classJsonArray.getJSONObject(i);
			Class currentClass = ClassParser.parseClass(classJson);
			classes.add(currentClass);
		}

		return classes;
	}
}
