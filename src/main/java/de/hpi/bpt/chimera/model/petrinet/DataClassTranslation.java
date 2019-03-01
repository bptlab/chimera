package de.hpi.bpt.chimera.model.petrinet;

import java.util.HashMap;
import java.util.Map;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public class DataClassTranslation extends AbstractTranslation {

	private final Map<String, Place> olcStatePlacesByName = new HashMap<>();

	public DataClassTranslation(TranslationContext translationContext, DataClass dataClass) {
		super(translationContext, dataClass.getName());

		// Every state gets one place
		for (ObjectLifecycleState olcState : dataClass.getObjectLifecycle().getObjectLifecycleStates()) {
			Place olcStatePlace = addPlace(dataClass.getName() + "[" + olcState.getName() + "]");
			olcStatePlace.setSignificant(true);
			olcStatePlacesByName.put(olcState.getName(), olcStatePlace);
		}
	}

	public Map<String, Place> getOlcStatePlacesByName() {
		return olcStatePlacesByName;
	}
}
