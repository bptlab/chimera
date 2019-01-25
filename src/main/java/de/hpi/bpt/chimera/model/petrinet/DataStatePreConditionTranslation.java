package de.hpi.bpt.chimera.model.petrinet;

import java.util.Set;
import java.util.stream.Collectors;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;

public class DataStatePreConditionTranslation extends AbstractDataStateConditionTranslation {

	final DataStateCondition preCondition;
	final DataStateCondition postCondition;
	Set<DataClass> writtenDataClasses;

	public DataStatePreConditionTranslation(TranslationContext translationContext, DataStateCondition preCondition,
			DataStateCondition postCondition, String name, Place initialPlace, Place finalPlace) {
		super(translationContext, name, initialPlace, finalPlace);

		this.preCondition = preCondition;
		this.postCondition = postCondition;

		final String prefixString = this.context.getPrefixString();

		if (preCondition.getConditionSets().isEmpty()) {
			addTransition(prefixString + "cs_1", initialPlace, finalPlace);
		} else {
			writtenDataClasses = postCondition.getConditionSets().stream()
					.flatMap(postConditionSet -> postConditionSet.getConditions().stream())
					.map(AtomicDataStateCondition::getDataClass).collect(Collectors.toSet());
			int conditionSetId = 1;
			for (ConditionSet preConditionSet : preCondition.getConditionSets()) {
				Transition conditionSetTransition = translatePreConditionSet(preConditionSet, conditionSetId);
				conditionSetId++;
			}
		}
	}

	private Transition translatePreConditionSet(ConditionSet preConditionSet, int conditionSetId) {
		Transition conditionSetTransition = addTransition(
				this.context.getPrefixString() + "cs_" + Integer.toString(conditionSetId));

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

		return conditionSetTransition;
	}
}
