package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;

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
		DataClassTranslation dataClassTranslation = this.context.getCaseModelTranslation()
				.getDataClassTranslationsByName().get(atomicDataStateCondition.getDataClassName());
		Place place = dataClassTranslation.getOlcStatePlacesByName()
				.get(atomicDataStateCondition.getObjectLifecycleState().getName());
		return place;
	}
}
