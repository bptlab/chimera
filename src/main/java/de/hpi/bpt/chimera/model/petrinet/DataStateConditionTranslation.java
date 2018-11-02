package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;

public class DataStateConditionTranslation extends AbstractTranslation {

	public DataStateConditionTranslation(TranslationContext translationContext, DataStateCondition dataStateCondition,
			String name, Place initialPlace, Place finalPlace) {
		super(translationContext, "dc_" + name);

		final String prefixString = this.context.getPrefixString();

		if (dataStateCondition.getConditionSets().isEmpty()) {
			addTransition(prefixString + "cs_1", initialPlace, finalPlace);
		} else {
			int conditionId = 1;
			for (ConditionSet conditionSet : dataStateCondition.getConditionSets()) {
				ConditionSetTranslation conditionSetTranslation = new ConditionSetTranslation(this.context,
						conditionSet, "cs_" + Integer.toString(conditionId));
				Transition conditionSetTransition = conditionSetTranslation.getConditionSetTransition();
				conditionSetTransition.addInputPlace(initialPlace);
				conditionSetTransition.addOutputPlace(finalPlace);
				conditionId++;
			}
		}
	}
}
