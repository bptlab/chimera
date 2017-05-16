package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataModelClass;
import de.hpi.bpt.chimera.model.datamodel.DataModel;

public class DataModelParser {

	private DataModelParser() {
	}

	/**
	 * Parse DataModel out of datamodelJson.
	 * 
	 * @param datamodelJson
	 * @return DataModel
	 */
	public static DataModel parseDataModel(final JSONObject datamodelJson) {
		DataModel dataModel = new DataModel();
		try {
			int versionNumber = datamodelJson.getInt("revision");
			dataModel.setVersionNumber(versionNumber);

			List<DataModelClass> dataModelClasses = getDataModelClasses(datamodelJson.getJSONArray("dataclasses"));
			dataModel.setDataModelClasses(dataModelClasses);
		} catch (JSONException e) {
			throw e;
		}
		return dataModel;
	}

	/**
	 * Create List of DataClasses and EventClasses. Uses DataModelClassParser.
	 * Therefore check whether DataModelClass is DataClass or EventClass.
	 * 
	 * @param classJsonArray
	 * @return List of DataModelClasses
	 */
	private static List<DataModelClass> getDataModelClasses(final JSONArray classJsonArray) {
		int arraySize = classJsonArray.length();
		List<DataModelClass> dataModelClasses = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject classJson = classJsonArray.getJSONObject(i);

			try {
				if (classJson.getBoolean("is_event"))
					dataModelClasses.add(DataModelClassParser.parseEventClass(classJson));
				else
					dataModelClasses.add(DataModelClassParser.parseDataClass(classJson));
			} catch (JSONException e) {
				throw e;
			}
		}

		return dataModelClasses;
	}
}
