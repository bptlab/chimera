package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public abstract class AbstractDataStateConditionTranslation extends AbstractTranslation {

	final Place initialPlace;
	final Place finalPlace;

	public AbstractDataStateConditionTranslation(TranslationContext translationContext, String name, Place initialPlace,
			Place finalPlace) {
		super(translationContext, "dc_" + name);
		this.initialPlace = initialPlace;
		this.finalPlace = finalPlace;
	}

	protected Place getPlaceForDataState(AtomicDataStateCondition atomicDataStateCondition) {
		return getPlaceForDataState(atomicDataStateCondition.getDataClass(),
				atomicDataStateCondition.getObjectLifecycleState());
	}

	protected Place getPlaceForDataState(DataClass dataClass, ObjectLifecycleState olcState) {
		DataClassTranslation dataClassTranslation = this.context.getCaseModelTranslation()
				.getDataClassTranslationsByName().get(dataClass.getName());
		Place place = dataClassTranslation.getOlcStatePlacesByName().get(olcState.getName());
		return place;
	}
}
