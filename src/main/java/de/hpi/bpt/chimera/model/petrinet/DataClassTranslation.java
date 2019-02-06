package de.hpi.bpt.chimera.model.petrinet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.hpi.bpt.chimera.execution.exception.IllegalStateNameException;
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

		// Data objects are singletons
		// There has to be an "initial" state with 1 initial token
		final String initialStateName = "init";
		Optional<ObjectLifecycleState> initialState = dataClass.getObjectLifecycle().getObjectLifecycleStates().stream()
				.filter(olcState -> olcState.getName().equals(initialStateName)).findFirst();
		if (initialState.isPresent()) {
			olcStatePlacesByName.get(initialStateName).setNumTokens(1);
		} else {
			throw new IllegalStateNameException(initialStateName);
		}
	}

	public Map<String, Place> getOlcStatePlacesByName() {
		return olcStatePlacesByName;
	}
}