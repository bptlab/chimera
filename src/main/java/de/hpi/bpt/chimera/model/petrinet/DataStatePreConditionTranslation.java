package de.hpi.bpt.chimera.model.petrinet;

import java.util.Set;
import java.util.stream.Collectors;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;

public class DataStatePreConditionTranslation extends AbstractDataStateConditionTranslation {

	public DataStatePreConditionTranslation(TranslationContext translationContext, DataStateCondition preCondition,
			DataStateCondition postCondition, String name, Place initialPlace, Place finalPlace) {
		super(translationContext, name, initialPlace, finalPlace);

		final String prefixString = this.context.getPrefixString();

		if (preCondition.getConditionSets().isEmpty()) {
			addTransition(prefixString + "cs_1", initialPlace, finalPlace);
		} else {
			int conditionId = 1;
			for (ConditionSet preConditionSet : preCondition.getConditionSets()) {
				Set<DataClass> writtenDataClasses = postCondition.getConditionSets().stream()
						.flatMap(postConditionSet -> postConditionSet.getConditions().stream())
						.map(AtomicDataStateCondition::getDataClass).collect(Collectors.toSet());

				Transition conditionSetTransition = addTransition(prefixString + "cs_" + Integer.toString(conditionId));

				for (AtomicDataStateCondition atomicDataStateCondition : preConditionSet.getConditions()) {
					Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
					// read
					conditionSetTransition.addInputPlace(placeForDataState);
					// write back
					conditionSetTransition.addOutputPlace(placeForDataState);

					// consume mutex if data object will be modified
					if (writtenDataClasses.contains(atomicDataStateCondition.getDataClass())) {
						Place placeForSemaphore = getPlaceForDataSemaphore(atomicDataStateCondition.getDataClass());
						conditionSetTransition.addInputPlace(placeForSemaphore);
					}
				}

				conditionSetTransition.addInputPlace(initialPlace);
				conditionSetTransition.addOutputPlace(finalPlace);
				conditionId++;
			}
		}
	}
}
