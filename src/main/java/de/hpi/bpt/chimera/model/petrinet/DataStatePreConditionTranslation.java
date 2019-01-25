package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;

public class DataStatePreConditionTranslation extends AbstractDataStateConditionTranslation {

	final DataStateCondition preCondition;
	final Place initialPlace;
	final Place finalPlace;

	public DataStatePreConditionTranslation(TranslationContext translationContext, DataStateCondition preCondition,
			String name, Place initialPlace, Place finalPlace) {
		super(translationContext, name, initialPlace, finalPlace);

		this.preCondition = preCondition;

		this.initialPlace = initialPlace;
		this.finalPlace = finalPlace;

		if (preCondition.getConditionSets().isEmpty()) {
			addTransition("cs_1", initialPlace, finalPlace);
		} else {
			int conditionSetId = 1;
			for (ConditionSet preConditionSet : preCondition.getConditionSets()) {
				Transition conditionSetTransition = translatePreConditionSet(preConditionSet, conditionSetId);
				conditionSetId++;
			}
		}
	}

	private Transition translatePreConditionSet(ConditionSet preConditionSet, int conditionSetId) {
		Transition conditionSetTransition = addTransition("cs_" + Integer.toString(conditionSetId));

		for (AtomicDataStateCondition atomicDataStateCondition : preConditionSet.getConditions()) {
			Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
			// read and write back
			conditionSetTransition.addInputPlace(placeForDataState);
			conditionSetTransition.addOutputPlace(placeForDataState);
		}

		conditionSetTransition.addInputPlace(initialPlace);
		conditionSetTransition.addOutputPlace(finalPlace);

		return conditionSetTransition;
	}
}
