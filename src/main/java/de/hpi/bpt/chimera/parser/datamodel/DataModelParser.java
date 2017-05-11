package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataModelClass;
import de.hpi.bpt.chimera.model.datamodel.DataModel;

public class DataModelParser {

	private DataModelParser() {
	}

	public static DataModel parseDataModel(final JSONObject datamodelJson) {
		DataModel dataModel = new DataModel();

		int versionNumber = datamodelJson.getInt("revision");
		dataModel.setVersionNumber(versionNumber);

		List<DataModelClass> dataModelClasses = getDataModelClasses(datamodelJson.getJSONArray("dataclasses"));
		dataModel.setDataModelClasses(dataModelClasses);

		return dataModel;
	}

	/*
	 * creates List of DataModelClasses with DataModelClassParser
	 */
	private static List<DataModelClass> getDataModelClasses(final JSONArray classJsonArray) {
		int arraySize = classJsonArray.length();
		List<DataModelClass> dataModelClasses = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject classJson = classJsonArray.getJSONObject(i);
			DataModelClass currentClass = DataModelClassParser.parseDataModelClass(classJson);
			dataModelClasses.add(currentClass);
		}

		return dataModelClasses;
	}
}
