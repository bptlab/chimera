package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;

public class ConditionSetTranslation extends AbstractTranslation {

	private final Transition conditionSetTransition;

	public ConditionSetTranslation(TranslationContext translationContext, ConditionSet preConditionSet,
			ConditionSet postConditionSet, String name) {
		super(translationContext, name);

		final String prefixString = this.context.getPrefixString();

		conditionSetTransition = addTransition(prefixString);

		for (AtomicDataStateCondition atomicDataStateCondition : preConditionSet.getConditions()) {
			Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
			conditionSetTransition.addInputPlace(placeForDataState);
		}
		for (AtomicDataStateCondition atomicDataStateCondition : postConditionSet.getConditions()) {
			Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
			conditionSetTransition.addOutputPlace(placeForDataState);
		}
	}

	private Place getPlaceForDataState(AtomicDataStateCondition atomicDataStateCondition) {
		DataClassTranslation dataClassTranslation = this.context.getCaseModelTranslation()
				.getDataClassTranslationsByName().get(atomicDataStateCondition.getDataClassName());
		Place place = dataClassTranslation.getOlcStatePlacesByName()
				.get(atomicDataStateCondition.getObjectLifecycleState().getName());
		return place;
	}

	public Transition getConditionSetTransition() {
		return conditionSetTransition;
	}

}
