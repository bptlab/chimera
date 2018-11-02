package de.hpi.bpt.chimera.model.petrinet;

import java.util.HashMap;
import java.util.Map;

import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public class DataClassTranslation extends AbstractTranslation {

	private final Map<String, Place> olcStatePlacesByName = new HashMap<>();
	// private final Place semaphor;

	public DataClassTranslation(TranslationContext translationContext, DataClass dataClass) {
		super(translationContext, dataClass.getName());

		final String prefixString = this.context.getPrefixString();

		for (ObjectLifecycleState olcState : dataClass.getObjectLifecycle().getObjectLifecycleStates()) {
			olcStatePlacesByName.put(olcState.getName(), addPlace(prefixString + olcState.getName()));
		}
		// semaphor = addPlace(prefixString);
	}

	public Map<String, Place> getOlcStatePlacesByName() {
		return olcStatePlacesByName;
	}

//	public Place getSemaphor() {
//		return semaphor;
//	}
}
