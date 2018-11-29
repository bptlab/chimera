package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;

public class DataStatePreConditionTranslation extends AbstractDataStateConditionTranslation {

	public DataStatePreConditionTranslation(TranslationContext translationContext, DataStateCondition preCondition,
			String name, Place initialPlace, Place finalPlace) {
		super(translationContext, name, initialPlace, finalPlace);

		final String prefixString = this.context.getPrefixString();

		if (preCondition.getConditionSets().isEmpty()) {
			addTransition(prefixString + "cs_1", initialPlace, finalPlace);
		} else {
			int conditionId = 1;
			for (ConditionSet preConditionSet : preCondition.getConditionSets()) {

				Transition conditionSetTransition = addTransition(prefixString + "cs_" + Integer.toString(conditionId));

				for (AtomicDataStateCondition atomicDataStateCondition : preConditionSet.getConditions()) {
					Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
					// read
					conditionSetTransition.addInputPlace(placeForDataState);
					// write back
					conditionSetTransition.addOutputPlace(placeForDataState);
				}

				conditionSetTransition.addInputPlace(initialPlace);
				conditionSetTransition.addOutputPlace(finalPlace);
				conditionId++;
			}
		}
	}
}
