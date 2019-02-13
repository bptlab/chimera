package de.hpi.bpt.chimera.parser.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.validation.NameValidation;

public class DataModelParser {
	private static final Logger log = Logger.getLogger((DataModelParser.class).getName());

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

			List<DataClass> dataModelClasses = getDataModelClasses(datamodelJson.getJSONArray("dataclasses"));
			dataModel.setDataModelClasses(dataModelClasses);

			String caseClassName = datamodelJson.getString("caseclass");
			Optional<DataClass> caseClass = dataModelClasses.stream().filter(dc -> dc.getName().equals(caseClassName))
					.findFirst();
			if (caseClass.isPresent()) {
				dataModel.setCaseClass(caseClass.get());
			} else {
				throw new JSONException("Referenced case class does not exist");
			}

		} catch (JSONException e) {
			log.error(e);
			throw new JSONException("Invalid DataModel->" + e.getMessage());
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
	private static List<DataClass> getDataModelClasses(final JSONArray classJsonArray) {
		int arraySize = classJsonArray.length();
		List<DataClass> dataModelClasses = new ArrayList<>();

		for (int i = 0; i < arraySize; i++) {
			JSONObject classJson = classJsonArray.getJSONObject(i);

			// TODO: rework parsing of event class
			try {
				if (classJson.getBoolean("is_event")) {
					DataClass eventClass = DataModelClassParser.parseDataClass(classJson);
					eventClass.setEvent(true);
					dataModelClasses.add(eventClass);
				} else {
					dataModelClasses.add(DataModelClassParser.parseDataClass(classJson));
				}
			} catch (JSONException e) {
				log.error(e);
				throw new JSONException("Invalid DataModelClass->" + e.getMessage());
			}
		}

		NameValidation.validateNameFrequency(dataModelClasses);

		return dataModelClasses;
	}
}
